package controller;

import DAO.CustomerDAO;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Customer;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * This class is the controller for add-customer.fxml view.
 * User will be able to input data in text fields and choice boxes in order to add a new customer.
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
    private ComboBox countryCombo;
    @FXML
    private ComboBox firstLevelCombo;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Add customer is initialized!");

        // generates unique customer id for new appointment
        try {
            customerID.setText(String.valueOf(CustomerDAO.allCustomers().size()+1));
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    /**
     * This will save data in the text fields and add the new customer to the Customer tableview on the Main Menu.
     @param actionEvent Save button is clicked.
     */
    public void onSaveCustomer(ActionEvent actionEvent) {

        /*int addCustomerID = Integer.parseInt(customerID.getText());
        String addCustomerName = custNameTextfield.getText();
        String addAddress = addressTextfield.getText();
        String addPhoneNumber = phoneNumberTextfield.getText();
        String addPostalCode =  postalCodeTextfield.getText();
        String addCountry = countryCombo.getValue();
        String addFirstLevel = firstLevelCombo.getValue();

        try {
            if (addCustomerName.isEmpty() || addAddress.isEmpty() ||addPhoneNumber.isEmpty() || addPostalCode.isEmpty()
                    || addCountry.isEmpty() || addFirstLevel.isEmpty()) {
                Alert emptyField = new Alert(Alert.AlertType.ERROR, "One or more fields are empty. Please enter a" +
                        " value in each field.");
                emptyField.showAndWait();
            }
        } catch (Exception e) {
            return;
        }*/

    }

    /**
     * This navigates the user back to the Main Menu.
     * @param actionEvent Cancel button is clicked.
     * */
    public void onCancel(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load((Objects.requireNonNull(getClass().getResource("/view/main-menu.fxml"))));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1108, 620);
        stage.setScene(scene);
        stage.show();
        stage.centerOnScreen();
        stage.setResizable(false);
    }


}
