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

public class ModifyCorpAcct implements Initializable {

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

    ObservableList<CountryDAO> countries = CountryDAO.allCountries();
    ObservableList<FirstLevelDAO> divisions = FirstLevelDAO.allFirstLevelDivision();

    Customer.CorporateAccount modCorpAccount = new Customer.CorporateAccount();

    public ModifyCorpAcct() throws SQLException {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Modify Corporate Account initialized.");

        // country combo box
        countryModCombo.setItems(countries);

        // state/province combo box
        firstLevelModCombo.setItems(divisions);

    }
    public void sendCustomer(Customer customer) throws SQLException {
        modCorpAccount = (Customer.CorporateAccount) customer;

        for (CountryDAO c : countryModCombo.getItems()) {
            if(c.getCountryId() == modCorpAccount.getCountryId()){
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
            if(f.getDivisionId() == modCorpAccount.getDivisionId()) {
                firstLevelModCombo.getSelectionModel().select(f);
                break;
            }
        }
        companyName.setText(modCorpAccount.getCompany());
        customerType.setText(modCorpAccount.getCustomerType());
        customerIdField.setText(String.valueOf(modCorpAccount.getCustomerId()));
        customerNameText.setText(modCorpAccount.getCustomerName());
        customerAddressText.setText(modCorpAccount.getCustomerAddress());
        customerPhoneText.setText(modCorpAccount.getCustomerPhone().replaceAll("\\D", ""));
        customerPostalText.setText(modCorpAccount.getCustomerPostal());
    }
    public void onSaveModCustomer(ActionEvent actionEvent) throws IOException {
        int modCustomerID = Integer.parseInt(customerIdField.getText());
        String modCustomerType = customerType.getText();
        String modCompanyName = companyName.getText();
        String modCustomerName = customerNameText.getText();
        String modAddress = customerAddressText.getText();
        String modPhone = customerPhoneText.getText();
        String modPostal = customerPostalText.getText();
        String modCountry = countryModCombo.getValue().toString();

        // input validation message: empty text field(s)
        if (modCustomerName.isEmpty() || modAddress.isEmpty() || modPhone.isEmpty() || modCountry.isEmpty() ||
                modPostal.isEmpty()) {

            Alert emptyField = new Alert(Alert.AlertType.ERROR, "One or more text fields are empty. Please enter a "
                    + "value in each field.");
            emptyField.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            Optional<ButtonType> result = emptyField.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK)
                return;
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

        Alert modCustomer = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to modify this " +
                "account?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = modCustomer.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.YES){
            CustomerDAO.modifyCorpAcct(modCustomerID,modCompanyName,modCustomerName,modAddress,modPhone,modPostal,
                    modDivision,modCustomerType);
            toCustomers(actionEvent);
        }
    }

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

    public void toMainMenu(ActionEvent actionEvent) {
    }

    public void onModCountry() throws SQLException {
        Customer modSelection = countryModCombo.getSelectionModel().getSelectedItem();

        firstLevelModCombo.setItems(FirstLevelDAO.allFirstLevelDivision().stream()
                .filter(firstLevel -> firstLevel.getCountryId() == modSelection.getCountryId())
                .collect(Collectors.toCollection(FXCollections::observableArrayList)));

        firstLevelModCombo.getSelectionModel().selectFirst();
        firstLevelModCombo.setVisibleRowCount(5);
    }

}
