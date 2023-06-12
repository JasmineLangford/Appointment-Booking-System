package controller;

import DAO.CountryDAO;
import DAO.CustomerDAO;
import DAO.FirstLevelDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.Customer;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * This class is the controller for modify-customer.fxml. The end-user is able to modify text fields and combo boxes.
 * The customer ID was auto-incremented from the database when the customer was added and is disabled. The end-user will
 * be able to save the changes to this customer by clicking the save button at the bottom of the screen or cancel
 * changes if the end-user no longer wants to modify the customer details.
 */
public class ModifyCustomer implements Initializable {
    // form fields to be pre-populated
    @FXML
    private TextField customerIdField;
    @FXML
    private TextField customerNameText;
    @FXML
    private  TextField customerAddressText;
    @FXML
    private  TextField customerPhoneText;
    @FXML
    private  TextField customerPostalText;
    @FXML
    private ComboBox<CountryDAO> countryModCombo;
    @FXML
    private ComboBox<FirstLevelDAO> firstLevelModCombo;

    // observable lists for combo boxes
    ObservableList<CountryDAO> countries = CountryDAO.allCountries();
    ObservableList<FirstLevelDAO> divisions = FirstLevelDAO.allFirstLevelDivision();

    Customer modCustomer = new Customer();

    public ModifyCustomer() throws SQLException {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Modify Customer initialized.");

        // country combo box
        countryModCombo.setItems(countries);

        // state/province combo box
        firstLevelModCombo.setItems(divisions);
    }

    /**
     * This method populates the selected customer row of data from Main Menu.
     *
     * @param customer The customer represented from the customer model.
     * @throws SQLException The exception to throw if there are errors querying the countries or divisions for the
     * combo boxes.
     */
    public void sendCustomer(Customer customer) throws SQLException {
        modCustomer = customer;

        for (CountryDAO c : countryModCombo.getItems()) {
            if(c.getCountryId() == modCustomer.getCountryId()){
                countryModCombo.getSelectionModel().select(c);

                Customer modSelection = countryModCombo.getSelectionModel().getSelectedItem();
                firstLevelModCombo.setItems(FirstLevelDAO.allFirstLevelDivision().stream()
                        .filter(firstLevel -> firstLevel.getCountryId() == modSelection.getCountryId())
                        .collect(Collectors.toCollection(FXCollections::observableArrayList)));

                firstLevelModCombo.setVisibleRowCount(5);
                break;
            }
        }
        for (FirstLevelDAO f : firstLevelModCombo.getItems()) {
            if(f.getDivisionId() == modCustomer.getDivisionId()) {
                firstLevelModCombo.getSelectionModel().select(f);
                break;
            }
        }
        customerIdField.setText(String.valueOf(modCustomer.getCustomerId()));

        customerNameText.setText(modCustomer.getCustomerName());
        customerAddressText.setText(modCustomer.getCustomerAddress());
        customerPhoneText.setText(modCustomer.getCustomerPhone().replaceAll("\\D", ""));
        customerPostalText.setText(modCustomer.getCustomerPostal());
    }

    /**
     * This method filters the divisions based on the end-user updating the country.
     *
     * @throws SQLException The exception to throw if there are errors querying the divisions for the combo box.
     */
    public void onModCountry() throws SQLException {

        // filters first-level division combo box
        Customer modSelection = countryModCombo.getSelectionModel().getSelectedItem();

        firstLevelModCombo.setItems(FirstLevelDAO.allFirstLevelDivision().stream()
                .filter(firstLevel -> firstLevel.getCountryId() == modSelection.getCountryId())
                .collect(Collectors.toCollection(FXCollections::observableArrayList)));

        firstLevelModCombo.getSelectionModel().selectFirst();
        firstLevelModCombo.setVisibleRowCount(5);
    }

    /**
     * This method will save any modified customer data.
     *
     * @param actionEvent The save button is clicked.
     */
    public void onSaveModCustomer(ActionEvent actionEvent) {

        int modCustomerID = Integer.parseInt(customerIdField.getText());
        String modCustomerName = customerNameText.getText();
        String modAddress = customerAddressText.getText();
        String modPhone = customerPhoneText.getText();
        String modPostal = customerPostalText.getText();
        String modCountry = countryModCombo.getValue().toString();

        // input validation message: empty text field(s)
        if (modCustomerName.isEmpty() || modAddress.isEmpty() || modPhone.isEmpty() || modCountry.isEmpty() ||
                modPostal.isEmpty()) {

            Alert emptyField = new Alert(Alert.AlertType.ERROR, "One or more text fields are empty. Please enter a "
                    + "value in each field.");
            emptyField.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            Optional<ButtonType> result = emptyField.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK)
                return;
        }

        try{
            int modDivision = firstLevelModCombo.getSelectionModel().getSelectedItem().getDivisionId();

            modCustomer.setCustomerId(modCustomerID);
            modCustomer.setCustomerName(modCustomerName);
            modCustomer.setCustomerAddress(modAddress);
            modCustomer.setCustomerPostal(modPostal);
            modCustomer.setCustomerPhone(modPhone);
            modCustomer.setCustomerCountry(modCountry);
            modCustomer.setDivisionId(modDivision);

            Alert modCustomer = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to modify this " +
                    "customer?", ButtonType.YES, ButtonType.NO);
            Optional<ButtonType> result = modCustomer.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.YES){
                CustomerDAO.modifyCustomer(modCustomerID,modCustomerName,modAddress,modPostal,modPhone,modDivision);
                toCustomers(actionEvent);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * This method navigates the user back to the Main Menu.
     *
     * @param actionEvent The cancel button is clicked.
     * @throws IOException The exception to throw if end-user cannot return to the Main Menu.
     */
    public void toMainMenu(ActionEvent actionEvent) throws IOException {
        toCustomers(actionEvent);
    }

    public void toCustomers(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load((Objects.requireNonNull(getClass().getResource("/view/customers.fxml"))));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1108, 538);
        scene.setFill(Color.TRANSPARENT);
        root.setStyle("-fx-background-radius: 30px 30px 30px 30px;");
        stage.setScene(scene);
        stage.show();
        stage.centerOnScreen();
        stage.setResizable(false);
    }
}
