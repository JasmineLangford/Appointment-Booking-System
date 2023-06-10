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
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.Customer;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * This class is the controller for add-appointment.fxml.
 * The end-user can add a new customer by inputting data into text fields and combo boxes.
 * The customer ID is auto-incremented from the database and disabled on the form.
 * The end-user will be able to save the data for a new customer by clicking the save button at the bottom of the
 * screen or cancel if the end-user no longer wants to add a customer.
 */
public class AddCustomer implements Initializable {

    @FXML
    private TextField loyaltyPoints;
    @FXML
    private TextField customerType;
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
    ObservableList<CountryDAO> countries = CountryDAO.allCountries();
    ObservableList<FirstLevelDAO> divisions = FirstLevelDAO.allFirstLevelDivision();
    Customer.RegularCustomer newCustomer = new Customer.RegularCustomer();

    public AddCustomer() throws SQLException {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Add customer is initialized!");

        // Set combo box selection of countries
        countryCombo.setItems(countries);
        countryCombo.setPromptText("Select Country");

        // Set combo box selection of states/provinces
        firstLevelCombo.setItems(divisions);
        firstLevelCombo.setPromptText("Select State");
        firstLevelCombo.setVisibleRowCount(5);
    }

    /**
     * This method filters the combo box containing the state/provinces depending on the country chosen.
     *
     * @throws SQLException The exception to throw if there are errors querying the divisions for the combo box.
     */
    public void countrySelected() throws SQLException {
        Customer selection = countryCombo.getSelectionModel().getSelectedItem();

        firstLevelCombo.setItems(FirstLevelDAO.allFirstLevelDivision().stream()
                .filter(firstLevel -> firstLevel.getCountryId() == selection.getCountryId())
                .collect(Collectors.toCollection(FXCollections::observableArrayList)));

        firstLevelCombo.getSelectionModel().selectFirst();
        firstLevelCombo.setVisibleRowCount(5);
    }

    /**
     * This method will save the new customer in the database.
     *
     * @param actionEvent The save button is clicked.
     */
    public void onSaveCustomer(ActionEvent actionEvent) {

        // end-user form fields
        String addCustomerName = custNameTextfield.getText();
        String addAddress = addressTextfield.getText();
        String addPhoneNumber = phoneNumberTextfield.getText();
        String addPostalCode =  postalCodeTextfield.getText();
        String addCountry = countryCombo.getValue().toString();
        String addType = customerType.getText();
        int addLoyaltyPoints = Integer.parseInt(loyaltyPoints.getText());

        // input validation: empty field(s) found
        if (addCustomerName.isEmpty() || addAddress.isEmpty() ||addPhoneNumber.isEmpty() || addPostalCode.isEmpty() ||
        addLoyaltyPoints == 0) {
            Alert emptyField = new Alert(Alert.AlertType.ERROR, "One or more fields are empty. Please enter a" +
                    " value in each field.");
            Optional<ButtonType> result = emptyField.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK){
                return;
            }
        }

        try{
            int addFirstLevel = firstLevelCombo.getSelectionModel().getSelectedItem().getDivisionId();

            // setting new customer
            newCustomer.setCustomerName(addCustomerName);
            newCustomer.setCustomerAddress(addAddress);
            newCustomer.setCustomerPhone(addPhoneNumber);
            newCustomer.setCustomerPostal(addPostalCode);
            newCustomer.setCustomerCountry(addCountry);
            newCustomer.setDivisionId(addFirstLevel);
            newCustomer.setCustomerType(addType);
            newCustomer.setLoyaltyPoints(addLoyaltyPoints);

            CustomerDAO.addCustomer(addCustomerName,addAddress,addPhoneNumber,addPostalCode,addFirstLevel, addType,
                    addLoyaltyPoints);
            Alert addCustomer = new Alert(Alert.AlertType.CONFIRMATION, "The customer was successfully added.",
                    ButtonType.OK);
            Optional<ButtonType> result = addCustomer.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK){
                toCustomers(actionEvent);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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
    /**
     * This method navigates the end-user back to the Main Menu.
     *
     * @param actionEvent The cancel button is clicked.
     * @throws IOException The exception to throw if I/O error occurs.
     */
    public void toAppointments(ActionEvent actionEvent) throws IOException {
        toCustomers(actionEvent);
    }
}
