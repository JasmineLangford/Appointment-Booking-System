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

public class AddCorpAccount implements Initializable {
    ObservableList<CountryDAO> countries = CountryDAO.allCountries();
    ObservableList<FirstLevelDAO> divisions = FirstLevelDAO.allFirstLevelDivision();
    Customer.CorporateAccount newCorpAccount = new Customer.CorporateAccount();
    @FXML
    private TextField companyNameTextfield;
    @FXML
    private TextField addressTextfield;
    @FXML
    private TextField postalCodeTextfield;
    @FXML
    private ComboBox<FirstLevelDAO> firstLevelCombo;
    @FXML
    private ComboBox<CountryDAO> countryCombo;
    @FXML
    private TextField phoneNumberTextfield;
    @FXML
    private TextField customerType;

    public AddCorpAccount() throws SQLException {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Add corporate account is initialized!");

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
     * This method will save the new corporate account in the database.
     *
     * @param actionEvent The save button is clicked.
     */
    public void onSaveCorpAccount(ActionEvent actionEvent) {

        String addCompanyName = companyNameTextfield.getText();
        String addAddress = addressTextfield.getText();
        String addPhoneNumber = phoneNumberTextfield.getText();
        String addPostalCode = postalCodeTextfield.getText();
        String addCountry = countryCombo.getValue().toString();
        String addType = customerType.getText();

        // Input Validation
        if (addCompanyName.isEmpty() || addAddress.isEmpty() || addPhoneNumber.isEmpty() || addPostalCode.isEmpty()) {
            Alert emptyField = new Alert(Alert.AlertType.ERROR, "One or more fields are empty. Please enter a" +
                    " value in each field.");
            Optional<ButtonType> result = emptyField.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                return;
            }
        }

        try {
            int addFirstLevel = firstLevelCombo.getSelectionModel().getSelectedItem().getDivisionId();

            // setting new customer
            newCorpAccount.setCompany(addCompanyName);
            newCorpAccount.setCustomerName(addCompanyName);
            newCorpAccount.setCustomerAddress(addAddress);
            newCorpAccount.setCustomerPhone(addPhoneNumber);
            newCorpAccount.setCustomerPostal(addPostalCode);
            newCorpAccount.setCustomerCountry(addCountry);
            newCorpAccount.setDivisionId(addFirstLevel);
            newCorpAccount.setCustomerType(addType);

            CustomerDAO.addCorporateAccount(addCompanyName,addCompanyName, addAddress, addPhoneNumber, addPostalCode,
                    addFirstLevel, addType);
            Alert addCorpAccount = new Alert(Alert.AlertType.CONFIRMATION, "The customer was successfully added.",
                    ButtonType.OK);
            Optional<ButtonType> result = addCorpAccount.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                toCustomers(actionEvent);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void toAppointments(ActionEvent actionEvent) throws IOException {
        toCustomers(actionEvent);
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
}
