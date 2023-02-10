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

    // list of first-level divisions
    ObservableList<FirstLevelDAO> firstLevel = FirstLevelDAO.allFirstLevelDivision();

    public AddCustomer() throws SQLException {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Add customer is initialized!");

        // generates unique customer id for new appointment
        try {
            customerID.setText(String.valueOf(CustomerDAO.allCustomers().size()+1));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // setting combo box selection of countries
        countryCombo.setItems(countries);
        countryCombo.setPromptText("Select a country.");
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
            firstLevelCombo.setPromptText("Select a state");
        } else if (selection.getCountryId() == 2) {
            firstLevelCombo.setPromptText("Select region");
        } else if (selection.getCountryId() == 3) {
            firstLevelCombo.setPromptText("Select province");
        }
    }

    /**
     * This will save data entered in form fields and add the new customer to the database.
     */
    public void onSaveCustomer() throws SQLException {

        int addCustomerID = Integer.parseInt(customerID.getText());
        String addCustomerName = custNameTextfield.getText();
        String addAddress = addressTextfield.getText();
        String addPhoneNumber = phoneNumberTextfield.getText();
        String addPostalCode =  postalCodeTextfield.getText();
        String addCountry = countryCombo.getValue().toString();
        String addFirstLevel = firstLevelCombo.getValue().toString();


        if (addCustomerName.isEmpty() || addAddress.isEmpty() ||addPhoneNumber.isEmpty() || addPostalCode.isEmpty() ||
                addCountry.isEmpty() || addFirstLevel.isEmpty()) {
            Alert emptyField = new Alert(Alert.AlertType.ERROR, "One or more fields are empty. Please enter a" +
                        " value in each field.");
            emptyField.showAndWait();
        }

       try{

        Alert addCustomer = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to add this new customer?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = addCustomer.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.YES){
            CustomerDAO.addCustomer(addCustomerID,addCustomerName,addAddress,addPostalCode,addPhoneNumber, Integer.parseInt(addFirstLevel));
            toMainMenu(new ActionEvent());
        }
       }catch (IOException e){
           e.printStackTrace();
       }
    }

    /**
     * This navigates the user back to the Main Menu.  */
    public void toMainMenu(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load((Objects.requireNonNull(getClass().getResource("/view/main-menu.fxml"))));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1108, 620);
        stage.setScene(scene);
        stage.show();
        stage.centerOnScreen();
        stage.setResizable(false);
    }

}
