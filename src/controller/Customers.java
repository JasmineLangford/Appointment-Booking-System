package controller;

import DAO.CustomerDAO;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.Appointment;
import model.Customer;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

public class Customers implements Initializable {

    public RadioButton viewRegularCustomer;
    public ToggleGroup customerTypeToggle;
    public RadioButton viewCorpAcct;
    @FXML
    private TextField searchCustomer;
    @FXML
    private TableView<Customer> mainCustomerTable;
    @FXML
    private TableColumn<Customer, String> customerNameCol;
    @FXML
    private TableColumn<Customer, String> customerAddressCol;
    @FXML
    private TableColumn<Customer, String> customerPhoneCol;
    @FXML
    private TableColumn<Customer, String> customerCountryCol;
    @FXML
    private TableColumn<Customer, String> customerStateCol;
    @FXML
    private TableColumn<Customer, String> customerPostalCol;

    private FilteredList<Customer> filteredCustomers;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        customerNameCol.setText("Customer Name");
        searchCustomer.setPromptText("Search by Customer Name");

        customerNameCol.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        customerAddressCol.setCellValueFactory(new PropertyValueFactory<>("customerAddress"));
        customerPhoneCol.setCellValueFactory(new PropertyValueFactory<>("customerPhone"));
        customerCountryCol.setCellValueFactory(new PropertyValueFactory<>("customerCountry"));
        customerStateCol.setCellValueFactory(new PropertyValueFactory<>("division"));
        customerPostalCol.setCellValueFactory(new PropertyValueFactory<>("customerPostal"));

        try {
            loadCustomerTable();
        } catch (
                SQLException e) {
            e.printStackTrace();
        }

        // Search customers
        try {
            filteredCustomers = new FilteredList<>(CustomerDAO.allCustomers());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        customerTypeToggle.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == viewRegularCustomer) {
                try {
                    changeToCustomer();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else if (newValue == viewCorpAcct) {
                try {
                    changeToCorp();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    public void applySearch(){
        searchCustomer.textProperty().addListener((observable, oldValue, newValue) ->
                filteredCustomers.setPredicate(customer -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }

                    String searchKeyword = newValue.toLowerCase();

                    if (customer.getCustomerName().toLowerCase().contains(searchKeyword)) {
                        return true;
                    } else return Integer.toString(customer.getCustomerId()).contains(searchKeyword);
                })
        );

        SortedList<Customer> sortedParts = new SortedList<>(filteredCustomers);
        sortedParts.comparatorProperty().bind(mainCustomerTable.comparatorProperty());
        mainCustomerTable.setItems(sortedParts);

        // Message displayed on customer tableview if there are no matching items.
        mainCustomerTable.setPlaceholder(new Label("No matches"));
    }
    // load customer tableview
    public void loadCustomerTable() throws SQLException {
        ObservableList<Customer> allCustomers = CustomerDAO.allCustomers();
        mainCustomerTable.setItems(allCustomers);
        mainCustomerTable.getSelectionModel().clearSelection();
    }

    /**
     * This method will take the end-user to Modify Customer screen where they can modify existing customers. The
     * data from the selected row will auto-populate to the modify form.
     *
     * @param actionEvent Modify button is clicked under the customer tableview.
     * @throws IOException The exception to throw if I/O error occurs.
     */
    public void updateCustomer(ActionEvent actionEvent) throws IOException, SQLException {

        if (mainCustomerTable.getSelectionModel().isEmpty()) {
            Alert modCustomerSelect = new Alert(Alert.AlertType.WARNING, "Please select a customer to be modified.");
            Optional<ButtonType> results = modCustomerSelect.showAndWait();
            if (results.isPresent() && results.get() == ButtonType.OK)
                modCustomerSelect.setOnCloseRequest(Event::consume);
            return;
        }

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/modify-customer.fxml"));
        loader.load();

        ModifyCustomer MainMenu = loader.getController();
        MainMenu.sendCustomer(mainCustomerTable.getSelectionModel().getSelectedItem());

        Parent scene = loader.getRoot();
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(new Scene(scene));
        stage.show();
        stage.centerOnScreen();
        stage.setResizable(false);
    }

    /**
     * This method will delete a customer from the customers table in the database. If a customer has an associated
     * appointment, the appointment will also be deleted along with the customer if the end-user proceeds with the
     * deletion. A message will state the customer name that was deleted.
     */
    public void deleteCustomerRow() throws SQLException {
        if (mainCustomerTable.getSelectionModel().isEmpty()) {
            Alert deleteCust = new Alert(Alert.AlertType.WARNING, "Please select a customer to be deleted.");
            Optional<ButtonType> result = deleteCust.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK)
                deleteCust.setOnCloseRequest(Event::consume);
            return;
        }

        Customer deleteAssociatedAppts = mainCustomerTable.getSelectionModel().getSelectedItem();
        ObservableList<Appointment> associatedAppts = CustomerDAO.deleteAssociated(deleteAssociatedAppts.getCustomerId());

        try {
            if (associatedAppts.size() > 0) {
                Alert associatedAppt = new Alert(Alert.AlertType.WARNING, "All associated appointments will be " +
                        "deleted along with this customer. Are you sure you want to delete this customer?", ButtonType.YES, ButtonType.NO);
                associatedAppt.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                associatedAppt.setTitle(" ");
                associatedAppt.setHeaderText("Associated Appointment Found!");
                Optional<ButtonType> results = associatedAppt.showAndWait();
                if (results.isPresent() && results.get() == ButtonType.YES) {
                    CustomerDAO.deleteCustomer(deleteAssociatedAppts);
                    //loadApptTable();
                    loadCustomerTable();
                    mainCustomerTable.getSelectionModel().clearSelection();
                }
                if (results.isPresent() && results.get() == ButtonType.NO) {
                    associatedAppt.setOnCloseRequest(Event::consume);
                    mainCustomerTable.getSelectionModel().clearSelection();
                }

            } else {
                Alert deleteCustConfirm = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete "
                        + "this customer?", ButtonType.YES, ButtonType.NO);
                Optional<ButtonType> result = deleteCustConfirm.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.YES) {
                    Customer selectedCustomer = mainCustomerTable.getSelectionModel().getSelectedItem();
                    CustomerDAO.deleteCustomer(selectedCustomer);
                    loadCustomerTable();
                    mainCustomerTable.getSelectionModel().clearSelection();

                    Alert apptInfo = new Alert(Alert.AlertType.INFORMATION, "You have deleted the following " +
                            "customer: " + selectedCustomer.getCustomerName(), ButtonType.OK);
                    apptInfo.showAndWait();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method will take the user to the Add Customer screen where a new customer can be added.
     *
     * @param actionEvent Add button is clicked under the customers' tableview.
     * @throws IOException The exception to throw if I/O error occurs.
     */
    public void addCustomer(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load((Objects.requireNonNull(getClass().getResource("/view/add-customer.fxml"))));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 500, 475);
        stage.setScene(scene);
        stage.show();
        stage.centerOnScreen();
        stage.setResizable(false);
    }

    /**
     * This method will take the end-user to the Add Customer screen where a new customer can be added.
     *
     * @param actionEvent Add button is clicked under the customers' tableview.
     * @throws IOException The exception to throw if I/O error occurs.
     */
    public void addCorpAcct(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load((Objects.requireNonNull(getClass().getResource("/view/add-corp-account.fxml"))));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 500, 475);
        stage.setScene(scene);
        stage.show();
        stage.centerOnScreen();
        stage.setResizable(false);
    }

    public void toReports(MouseEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load((Objects.requireNonNull(getClass().getResource("/view/reports.fxml"))));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1108, 538);
        scene.setFill(Color.TRANSPARENT);
        root.setStyle("-fx-background-radius: 30px 30px 30px 30px;");
        stage.setScene(scene);
        stage.show();
        stage.centerOnScreen();
        stage.setResizable(false);
    }

    public void backToAppointments(MouseEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load((Objects.requireNonNull(Appointments.class.getResource("/view/appointments.fxml"))));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1108, 538);
        scene.setFill(Color.TRANSPARENT);
        root.setStyle("-fx-background-radius: 30px 30px 30px 30px;");
        stage.setScene(scene);
        stage.show();
        stage.centerOnScreen();
        stage.setResizable(false);
    }

    public void toClose(MouseEvent mouseEvent) {
        Appointments exitApplication = new Appointments();
        exitApplication.toClose(mouseEvent);
    }

    /**
     * This method filters the appointments to display all appointments.
     *
     * @throws SQLException The exception to throw if there is an issue with the sql query.
     */
    public void changeToCustomer() throws SQLException {
        ObservableList<Customer> regularCustomers = CustomerDAO.allCustomers();
        if (regularCustomers.isEmpty()) {
            mainCustomerTable.setItems(regularCustomers);
            mainCustomerTable.setPlaceholder(new Label("No customers available"));
        } else {
            searchCustomer.setPromptText("Search by Customer Name ");
            customerNameCol.setText("Customer Name");
            //mainCustomerTable.setItems(regularCustomers);
            filteredCustomers = new FilteredList<>(regularCustomers);
            applySearch();
        }
    }

    public void changeToCorp() throws SQLException {
        ObservableList<Customer> corporateAccounts = CustomerDAO.corporateAccounts();
        if (corporateAccounts.isEmpty()) {
            mainCustomerTable.setItems(corporateAccounts);
            mainCustomerTable.setPlaceholder(new Label("No corporate accounts available"));
        } else {
            searchCustomer.setPromptText("Search by Company Name");
            customerNameCol.setText("Company Name");
            //mainCustomerTable.setItems(corporateAccounts);
            filteredCustomers = new FilteredList<>(corporateAccounts);
            applySearch();
        }
    }
}
