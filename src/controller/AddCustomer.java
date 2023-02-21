package controller;

import DAO.CountryDAO;
import DAO.CustomerDAO;
import DAO.FirstLevelDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
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
 */
public class AddCustomer implements Initializable {

    // form fields
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

    // list of countries for combo box
    ObservableList<CountryDAO> countries = CountryDAO.allCountries();

    Customer newCustomer = new Customer();

    public AddCustomer() throws SQLException {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Add customer is initialized!");

        // setting combo box selection of countries
        countryCombo.setItems(countries);
        countryCombo.getSelectionModel().selectFirst();

        // disabled this combo box until country is selected
        firstLevelCombo.setDisable(true);
    }

    /**
     * <b>LAMBDA REQUIREMENT #2</b> This method uses a lambda expression to filter the state/province selections based
     * on the country selected. The filter takes the predicate of <i>first level</i> and returns
     * true if the country ID from the database matches the country ID from the country selected. The state/province(s)
     * matching the country ID are then collected into an FXCollections instance that will display in the combo box as
     * an observable array list, along with custom prompts for the end-user to select a state/province. The benefit of
     * this lambda expression is that it takes an existing observable list and filtering it.
     *
     * @throws SQLException The exception to throw if there are errors with database access or errors.
     */
    public void countrySelected() throws SQLException {
        // filter based on this selection
        Customer selection = countryCombo.getSelectionModel().getSelectedItem();
        firstLevelCombo.setDisable(false);

        // Lambda: filter state/province and collect only those matching the country ID to be shown
        firstLevelCombo.setItems(FirstLevelDAO.allFirstLevelDivision().stream()
                .filter(firstLevel -> firstLevel.getCountryId() == selection.getCountryId())
                .collect(Collectors.toCollection(FXCollections::observableArrayList)));

        firstLevelCombo.setVisibleRowCount(4);

        // changes prompt based on country selected
        if(selection.getCountryId() == 1){
            firstLevelCombo.setPromptText("Select State");
        } else if (selection.getCountryId() == 2) {
            firstLevelCombo.setPromptText("Select Region");
        } else if (selection.getCountryId() == 3) {
            firstLevelCombo.setPromptText("Select Province");
        }
    }

    /**
     * This method will save the new customer in the database.
     *
     * @param actionEvent Save the inputs for new customer on button click.
     */
    public void onSaveCustomer(ActionEvent actionEvent) {

        // end-user form fields
        String addCustomerName = custNameTextfield.getText();
        String addAddress = addressTextfield.getText();
        String addPhoneNumber = phoneNumberTextfield.getText();
        String addPostalCode =  postalCodeTextfield.getText();
        String addCountry = countryCombo.getValue().toString();


        // input validation: empty field(s) found
        if (addCustomerName.isEmpty() || addAddress.isEmpty() ||addPhoneNumber.isEmpty() || addPostalCode.isEmpty()) {
            Alert emptyField = new Alert(Alert.AlertType.ERROR, "One or more fields are empty. Please enter a" +
                    " value in each field.");
            Optional<ButtonType> result = emptyField.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK){
                return;
            }
        }

        try{
            // user form field
            int addFirstLevel = firstLevelCombo.getSelectionModel().getSelectedItem().getDivisionId();

           // setting new customer
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
            CustomerDAO.addCustomer(addCustomerName,addAddress,addPhoneNumber,addPostalCode,
                    addFirstLevel);
            MainMenu.toMainMenu(actionEvent);
        }
        }catch(NullPointerException e){
           System.out.println("Null selection");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This navigates the end-user back to the Main Menu.
     *
     * @param actionEvent The cancel button is clicked.
     */
    public void toMainMenu(ActionEvent actionEvent) throws IOException {
        MainMenu.toMainMenu(actionEvent);
    }
}
