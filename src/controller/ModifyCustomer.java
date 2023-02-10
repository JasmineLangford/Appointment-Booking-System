package controller;

import DAO.CountryDAO;
import DAO.FirstLevelDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Customer;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

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
    private ComboBox<Customer> countryCombo;
    @FXML
    private ComboBox firstLevelCombo;

    Customer setCustomer = new Customer(1,"Jennifer","132 Sunny Road",
            "123-456-7895","US",1,1,"Alabama","32832");

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Modify Customer initialized.");

    }

    /**
     * This populates the data from the selected customer tableview from Main Menu.
     */
    public void sendCustomer(Customer customer) throws SQLException {
        setCustomer = customer;
        customerIdField.setText(String.valueOf(setCustomer.getCustomerId()));
        customerNameText.setText(setCustomer.getCustomerName());
        customerAddressText.setText(setCustomer.getCustomerAddress());
        customerPhoneText.setText(setCustomer.getCustomerPhone());
        customerPostalText.setText(setCustomer.getCustomerPostal());

    }
    /**
     * Modified fields will be saved to the Customer tableview on the Main Menu.
     @param actionEvent Save button is clicked.
     */
    public void onSave(ActionEvent actionEvent) {
    }

    /**
     * This navigates the user back to the Main Menu.
     * @param actionEvent Cancel button is clicked.
     * */
    public void onCancel(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(AddAppointment.class.getResource("/view/main-menu.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load(), 1108, 620);
        stage.setScene(scene);
        stage.show();
        stage.centerOnScreen();
        stage.setResizable(false);
    }

}
