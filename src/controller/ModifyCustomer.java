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
 * This class is the controller for modify-customer.fxml. The user is able to modify text fields and combo boxes.
 * The customer ID was auto-incremented from the database when the customer was added and is disabled. The user will
 * be able to save the changes to this customer by clicking the update button at the bottom of the screen or cancel
 * changes if the user no longer wants to modify the customer details.
 */
public class ModifyCustomer implements Initializable {

    ObservableList<CountryDAO> countries = CountryDAO.allCountries();
    ObservableList<FirstLevelDAO> divisions = FirstLevelDAO.allFirstLevelDivision();
    Customer.RegularCustomer modCustomer = new Customer.RegularCustomer();
    @FXML
    private TextField customerType;
    @FXML
    private TextField loyaltyPointsText;
    @FXML
    private TextField customerIdField;
    @FXML
    private TextField customerNameText;
    @FXML
    private TextField customerAddressText;
    @FXML
    private TextField customerPhoneText;
    @FXML
    private TextField customerPostalText;
    @FXML
    private ComboBox<CountryDAO> countryModCombo;
    @FXML
    private ComboBox<FirstLevelDAO> firstLevelModCombo;

    public ModifyCustomer() throws SQLException {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Modify Customer initialized.");

        // Combo boxes
        countryModCombo.setItems(countries);
        firstLevelModCombo.setItems(divisions);
    }

    /**
     * This method populates the selected customer information to the modify customer screen.
     *
     * @param customer The customer represented from the customer model.
     * @throws SQLException The exception to throw if there are errors querying the countries or divisions for the
     * combo boxes.
     */
    public void sendCustomer(Customer.RegularCustomer customer) throws SQLException {
        modCustomer = customer;

        for (CountryDAO c : countryModCombo.getItems()) {
            if (c.getCountryId() == modCustomer.getCountryId()) {
                countryModCombo.getSelectionModel().select(c);

                Customer modSelection = countryModCombo.getSelectionModel().getSelectedItem();
                firstLevelModCombo.setItems(FirstLevelDAO.allFirstLevelDivision().stream().filter(firstLevel -> firstLevel.getCountryId() == modSelection.getCountryId()).collect(Collectors.toCollection(FXCollections::observableArrayList)));

                firstLevelModCombo.setVisibleRowCount(5);
                break;
            }
        }
        for (FirstLevelDAO f : firstLevelModCombo.getItems()) {
            if (f.getDivisionId() == modCustomer.getDivisionId()) {
                firstLevelModCombo.getSelectionModel().select(f);
                break;
            }
        }
        customerIdField.setText(String.valueOf(modCustomer.getCustomerId()));
        customerNameText.setText(modCustomer.getCustomerName());
        customerAddressText.setText(modCustomer.getCustomerAddress());
        customerPhoneText.setText(modCustomer.getCustomerPhone().replaceAll("\\D", ""));
        customerPostalText.setText(modCustomer.getCustomerPostal());
        customerType.setText((modCustomer.getCustomerType()));
        loyaltyPointsText.setText(String.valueOf(modCustomer.getLoyaltyPoints()));
    }

    /**
     * This method filters the divisions based on the user updating the country.
     *
     * @throws SQLException The exception to throw if there are errors querying the divisions for the combo box.
     */
    public void onModCountry() throws SQLException {
        Customer modSelection = countryModCombo.getSelectionModel().getSelectedItem();

        firstLevelModCombo.setItems(FirstLevelDAO.allFirstLevelDivision().stream().filter(firstLevel -> firstLevel.getCountryId() == modSelection.getCountryId()).collect(Collectors.toCollection(FXCollections::observableArrayList)));

        firstLevelModCombo.getSelectionModel().selectFirst();
        firstLevelModCombo.setVisibleRowCount(5);
    }

    /**
     * This method will save any changes to customer information.
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
        String modType = customerType.getText();
        int modLoyaltyPoints = Integer.parseInt(loyaltyPointsText.getText());

        // Input validation messages
        if (modCustomerName.isEmpty() || modAddress.isEmpty() || modPhone.isEmpty() || modCountry.isEmpty() || modPostal.isEmpty()) {

            Alert emptyField = new Alert(Alert.AlertType.ERROR, "One or more text fields are empty. Please enter a " + "value in each field.");
            emptyField.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            Optional<ButtonType> result = emptyField.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) return;
        }

        try {
            int modDivision = firstLevelModCombo.getSelectionModel().getSelectedItem().getDivisionId();

            modCustomer.setCustomerId(modCustomerID);
            modCustomer.setCustomerName(modCustomerName);
            modCustomer.setCustomerAddress(modAddress);
            modCustomer.setCustomerPostal(modPostal);
            modCustomer.setCustomerPhone(modPhone);
            modCustomer.setCustomerCountry(modCountry);
            modCustomer.setDivisionId(modDivision);
            modCustomer.setLoyaltyPoints(modLoyaltyPoints);

            Alert modCustomer = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to modify this " + "customer?", ButtonType.YES, ButtonType.NO);
            Optional<ButtonType> result = modCustomer.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.YES) {
                CustomerDAO.modifyCustomer(modCustomerID, modCustomerName, modAddress, modPostal, modPhone, modDivision, modType, modLoyaltyPoints);
                toCustomers(actionEvent);
            }
        } catch (IOException e) {
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

    /**
     * This method takes the user to the customers screen.
     *
     * @param actionEvent Customers is clicked (located on the left panel).
     * @throws IOException The exception to throw if I/O error occurs.
     */
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
