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
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
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
 * This class is the controller for modify-corp-acct.fxml. The user is able to modify text fields and combo boxes.
 * The customer ID was auto-incremented from the database when the customer was added and is disabled. The user will
 * be able to save the changes to this corporate account by clicking the update button at the bottom of the screen or cancel
 * changes if the user no longer wants to modify the account details.
 */
public class ModifyCorpAcct implements Initializable {

    ObservableList<CountryDAO> countries = CountryDAO.allCountries();
    ObservableList<FirstLevelDAO> divisions = FirstLevelDAO.allFirstLevelDivision();
    Customer.CorporateAccount modCorpAccount = new Customer.CorporateAccount();
    @FXML
    private TextField customerIdField;
    @FXML
    private TextField customerNameText;
    @FXML
    private TextField customerPhoneText;
    @FXML
    private TextField customerAddressText;
    @FXML
    private TextField customerPostalText;
    @FXML
    private ComboBox<CountryDAO> countryModCombo;
    @FXML
    private ComboBox<FirstLevelDAO> firstLevelModCombo;
    @FXML
    private TextField companyName;
    @FXML
    private TextField customerType;

    public ModifyCorpAcct() throws SQLException {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Modify Corporate Account initialized.");

        // Combo boxes
        countryModCombo.setItems(countries);
        firstLevelModCombo.setItems(divisions);

    }

    /**
     * This method populates the selected corporate account information to the modify corporate account screen.
     *
     * @param customer The customer represented from the customer model.
     * @throws SQLException The exception to throw if there are errors querying the countries or divisions for the
     *                      combo boxes.
     */
    public void sendCorpAcct(Customer.CorporateAccount customer) throws SQLException {
        modCorpAccount = customer;

        for (CountryDAO c : countryModCombo.getItems()) {
            if (c.getCountryId() == modCorpAccount.getCountryId()) {
                countryModCombo.getSelectionModel().select(c);

                Customer modSelection = countryModCombo.getSelectionModel().getSelectedItem();
                firstLevelModCombo.setItems(FirstLevelDAO.allFirstLevelDivision().stream().filter(firstLevel -> firstLevel.getCountryId() == modSelection.getCountryId()).collect(Collectors.toCollection(FXCollections::observableArrayList)));

                firstLevelModCombo.setVisibleRowCount(5);
                break;
            }
        }
        for (FirstLevelDAO f : firstLevelModCombo.getItems()) {
            if (f.getDivisionId() == modCorpAccount.getDivisionId()) {
                firstLevelModCombo.getSelectionModel().select(f);
                break;
            }
        }
        companyName.setText(modCorpAccount.getCustomerName());
        customerType.setText(modCorpAccount.getCustomerType());
        customerIdField.setText(String.valueOf(modCorpAccount.getCustomerId()));
        customerNameText.setText(modCorpAccount.getCustomerName());
        customerAddressText.setText(modCorpAccount.getCustomerAddress());
        customerPhoneText.setText(modCorpAccount.getCustomerPhone().replaceAll("\\D", ""));
        customerPostalText.setText(modCorpAccount.getCustomerPostal());
    }

    public void onSaveModCustomer(ActionEvent actionEvent) throws IOException, SQLException {
        int modCustomerID = Integer.parseInt(customerIdField.getText());
        String modCustomerType = customerType.getText();
        String modCompanyName = companyName.getText();
        String modCustomerName = customerNameText.getText();
        String modAddress = customerAddressText.getText();
        String modPhone = customerPhoneText.getText();
        String modPostal = customerPostalText.getText();
        String modCountry = countryModCombo.getValue().toString();

        // Input validation messages
        if (modCustomerName.isEmpty() || modAddress.isEmpty() || modPhone.isEmpty() || modCountry.isEmpty() || modPostal.isEmpty()) {

            Alert emptyField = new Alert(Alert.AlertType.ERROR, "One or more text fields are empty. Please enter a " + "value in each field.");
            emptyField.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            Optional<ButtonType> result = emptyField.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) return;
        }

        int modDivision = firstLevelModCombo.getSelectionModel().getSelectedItem().getDivisionId();

        modCorpAccount.setCompany(modCompanyName);
        modCorpAccount.setCustomerId(modCustomerID);
        modCorpAccount.setCustomerName(modCompanyName);
        modCorpAccount.setCustomerAddress(modAddress);
        modCorpAccount.setCustomerPostal(modPostal);
        modCorpAccount.setCustomerPhone(modPhone);
        modCorpAccount.setCustomerCountry(modCountry);
        modCorpAccount.setCustomerType(modCustomerType);
        modCorpAccount.setDivisionId(modDivision);

        Alert modCustomer = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to modify this " + "account?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = modCustomer.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.YES) {
            CustomerDAO.modifyCorpAcct(modCustomerID, modCompanyName, modCustomerName, modAddress, modPhone, modPostal, modDivision, modCustomerType);
            Customers corpAccountView = new Customers();
            corpAccountView.loadCorpAccountsView(actionEvent);
        }
    }

    /**
     * This method takes the user to the customers screen.
     *
     * @param actionEvent Customers is clicked (located on the left panel).
     * @throws IOException The exception to throw if I/O error occurs.
     */
    private void toCustomers(ActionEvent actionEvent) throws IOException {
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
     * This method takes the user to the customers screen.
     *
     * @param actionEvent Customers is clicked (located on the left panel).
     * @throws IOException The exception to throw if I/O error occurs.
     */
    public void toCustomerScreen(ActionEvent actionEvent) throws IOException {
        toCustomers(actionEvent);
    }
}
