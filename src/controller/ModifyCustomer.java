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
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Customer;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * This class is the controller for modify-customer.fxml view.
 * User selects a row from the Customer tableview from the Main Menu and info populates to this screen.
 * User can change any modifiable fields.
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
    private ComboBox countryModCombo;
    @FXML
    private ComboBox firstLevelModCombo;

    // list of countries
    ObservableList<CountryDAO> countries = CountryDAO.allCountries();

    // list of divisions
    ObservableList<FirstLevelDAO> divisions = FirstLevelDAO.allFirstLevelDivision();

    Customer modCustomer = new Customer();

    public ModifyCustomer() throws SQLException {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Modify Customer initialized.");

        countryModCombo.setItems(countries);
        firstLevelModCombo.setItems(divisions);
        firstLevelModCombo.setVisibleRowCount(5);
    }

    /**
     * This populates the data from the selected customer tableview from Main Menu.
     */
    public void sendCustomer(Customer customer) {
        modCustomer = customer;

        customerIdField.setText(String.valueOf(modCustomer.getCustomerId()));
        customerNameText.setText(modCustomer.getCustomerName());
        customerAddressText.setText(modCustomer.getCustomerAddress());
        customerPhoneText.setText(modCustomer.getCustomerPhone());
        countryModCombo.setValue(modCustomer.getCustomerCountry());
        firstLevelModCombo.setValue(modCustomer.getDivision());
        customerPostalText.setText(modCustomer.getCustomerPostal());

    }
    /**
     * Modified fields will be saved to the Customer tableview on the Main Menu.
     @param actionEvent Save button is clicked.
     */
    public void onSaveModCustomer(ActionEvent actionEvent) {

        int modCustomerID = Integer.parseInt(customerIdField.getText());
        String modCustomerName = customerNameText.getText();
        String modAddress = customerAddressText.getText();
        String modPhone = customerPhoneText.getText();
        String modPostal = customerPostalText.getText();
        String modCountry = countryModCombo.getValue().toString();
        int modFirstLevel = Integer.parseInt(firstLevelModCombo.getValue().toString());

        if (modCustomerName.isEmpty() || modAddress.isEmpty() || modPhone.isEmpty() || modCountry.isEmpty() || modPostal.isEmpty()) {

            Alert emptyField = new Alert(Alert.AlertType.ERROR, "One or more fields are empty. Please enter a " +
                    "value in each field.");
                    emptyField.showAndWait();
            }

        try{
            modCustomer.setCustomerId(modCustomerID);
            modCustomer.setCustomerName(modCustomerName);
            modCustomer.setCustomerAddress(modAddress);
            modCustomer.setCustomerPhone(modPhone);
            modCustomer.setCustomerPostal(modPostal);
            modCustomer.setCustomerCountry(modCountry);
            modCustomer.setDivisionId(modFirstLevel);

            Alert modCustomer = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to modify this " +
                    "customer?", ButtonType.YES, ButtonType.NO);
            Optional<ButtonType> result = modCustomer.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.YES){

                CustomerDAO.modifyCustomer(modCustomerID,modCustomerName,modAddress,modPhone,modPostal,modFirstLevel);
                toMainMenu(actionEvent);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }


    /**
     * This navigates the user back to the Main Menu.
     * @param actionEvent Cancel button is clicked.
     * */
    public void toMainMenu(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(AddAppointment.class.getResource("/view/main-menu.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load(), 1108, 620);
        stage.setScene(scene);
        stage.show();
        stage.centerOnScreen();
        stage.setResizable(false);
    }

    /**
     * This filters the divisions based on the end-user updating the country.
     */
    public void onModCountry() throws SQLException {

        // unselects the previous division
        firstLevelModCombo.getSelectionModel().clearAndSelect(0);

        // filters first-level division combo box
        Customer modSelection = (Customer) countryModCombo.getSelectionModel().getSelectedItem();
        firstLevelModCombo.setItems(FirstLevelDAO.allFirstLevelDivision().stream()
                .filter(firstLevel -> firstLevel.getCountryId() == modSelection.getCountryId())
                .collect(Collectors.toCollection(FXCollections::observableArrayList)));

        firstLevelModCombo.setVisibleRowCount(5);
    }
}
