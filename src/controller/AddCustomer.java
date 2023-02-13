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
import javafx.stage.Stage;
import model.Customer;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * This class is the controller for adding a new customer.
 *
 * End-user will be able to input data in text fields and combo boxes.
 */
public class AddCustomer implements Initializable {

    // form fields
    @FXML
    private TextField customerID;
    @FXML
    private TextField custNameTextfield;
    @FXML
    private TextField addressTextfield;
    @FXML
    private TextField phoneNumberTextfield;
    @FXML
    private TextField postalCodeTextfield;
    @FXML
    private ComboBox<CountryDAO> countryCombo;
    @FXML
    private ComboBox<FirstLevelDAO> firstLevelCombo;

    // list of countries
    ObservableList<CountryDAO> countries = CountryDAO.allCountries();

    Customer newCustomer = new Customer();

    public AddCustomer() throws SQLException {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Add customer is initialized!");

        // generates unique customer id for new customer
        try {
            customerID.setText(String.valueOf(CustomerDAO.allCustomers().size()+1));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // setting combo box selection of countries
        countryCombo.setItems(countries);
        countryCombo.setPromptText("Select country.");
    }

    /**
     * <b>LAMBDA #1 - Filters first level division combo box selection based on end-user's selection of country.
     * This will provide the end-user with specific divisions to choose from rather than a long list.<b>
     */
    public void countrySelected() throws SQLException {
        Customer selection = countryCombo.getSelectionModel().getSelectedItem();
        firstLevelCombo.setItems(FirstLevelDAO.allFirstLevelDivision().stream()
                .filter(firstLevel -> firstLevel.getCountryId() == selection.getCountryId())
                .collect(Collectors.toCollection(FXCollections::observableArrayList)));

        firstLevelCombo.setVisibleRowCount(3);

        // changes prompt based on country selected
        if(selection.getCountryId() == 1){
            firstLevelCombo.setPromptText("Select state.");
        } else if (selection.getCountryId() == 2) {
            firstLevelCombo.setPromptText("Select region.");
        } else if (selection.getCountryId() == 3) {
            firstLevelCombo.setPromptText("Select province.");
        }
    }

    /**
     * This will save data entered in form fields and add the new customer to the database.
     *
     * @param actionEvent saved button clicked
     */
    public void onSaveCustomer(ActionEvent actionEvent) {

        // end-user form fields
        int addCustomerID = Integer.parseInt(customerID.getText());
        String addCustomerName = custNameTextfield.getText();
        String addAddress = addressTextfield.getText();
        String addPhoneNumber = phoneNumberTextfield.getText();
        String addPostalCode =  postalCodeTextfield.getText();
        String addCountry = countryCombo.getValue().toString();
        int addFirstLevel = firstLevelCombo.getSelectionModel().getSelectedItem().getDivisionId();

        // input validation messages
        if (addCustomerName.isEmpty() || addAddress.isEmpty() ||addPhoneNumber.isEmpty() || addPostalCode.isEmpty() ||
                addCountry.isEmpty() || firstLevelCombo.getSelectionModel().isEmpty()) {
            Alert emptyField = new Alert(Alert.AlertType.ERROR, "One or more fields are empty. Please enter a" +
                        " value in each field.");
            emptyField.showAndWait();
        }

       try{
           newCustomer.setCustomerId(addCustomerID);
           newCustomer.setCustomerName(addCustomerName);
           newCustomer.setCustomerAddress(addAddress);
           newCustomer.setCustomerPhone(addPhoneNumber);
           newCustomer.setCustomerPostal(addPostalCode);
           newCustomer.setCustomerCountry(addCountry);
           newCustomer.setDivisionId(addFirstLevel);

        Alert addCustomer = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to add this new " +
                "customer?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = addCustomer.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.YES){
            CustomerDAO.addCustomer(addCustomerID,addCustomerName,addAddress,addPhoneNumber,addPostalCode,
                    addFirstLevel);
            MainMenu.toMainMenu(actionEvent);
        }
       }catch (IOException e){
           e.printStackTrace();
       }
    }

    /**
     * This navigates the end-user back to the Main Menu.
     *
     * @param actionEvent cancel button clicked
     */
    public void toMainMenu(ActionEvent actionEvent) throws IOException {
        MainMenu.toMainMenu(actionEvent);
    }

}
