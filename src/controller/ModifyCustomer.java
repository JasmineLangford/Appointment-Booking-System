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
import javafx.scene.input.MouseEvent;
import model.Customer;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
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
     * This populates the data from the selected customer tableview from Main Menu.
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
     * This filters the divisions based on the end-user updating the country.
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
        int modFirstLevel = firstLevelModCombo.getSelectionModel().getSelectedIndex();

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
     *
     * @param actionEvent cancel button is clicked
     */
    public void toMainMenu(ActionEvent actionEvent) throws IOException {
       MainMenu.toMainMenu(actionEvent);
    }
}
