package controller;

import DAO.AppointmentDAO;
import DAO.CustomerDAO;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
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
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import model.Appointment;
import model.Customer;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * This class is the controller for main-menu.fxml. This provides tableviews for appointments and customers, which are
 * queried from the database. The appointment table provides radio buttons to view all appointments, current week, and
 * current month. The end-user is able to navigate to other screens with the use of buttons. The end-user
 * is able to add, modify and delete both appointments and customers.
 *
 * End-user can also navigate to the reports screen.
 */
public class MainMenu implements Initializable {

    // radio buttons for appointment View
    public ToggleGroup apptViewToggle;
    public RadioButton viewByMonth;
    public RadioButton viewByWeek;
    public RadioButton viewAllAppts;

    // appointment tableview
    @FXML
    private TableView<Appointment> mainApptTable;
    @FXML
    private TableColumn<Appointment, String > apptIDCol;
    @FXML
    private TableColumn<Appointment, String> apptTitleCol;
    @FXML
    private TableColumn<Appointment, String>  apptDescCol;
    @FXML
    private TableColumn<Appointment, String>  apptLocationCol;
    @FXML
    private TableColumn<Appointment, String> apptContactCol;
    @FXML
    private TableColumn<Appointment, String>  apptTypeCol;
    @FXML
    private TableColumn<Appointment, String> apptStartCol;
    @FXML
    private TableColumn<Appointment, String> apptEndCol;
    @FXML
    private TableColumn<Appointment, String> apptCustCol;
    @FXML
    private TableColumn<Appointment, String>  apptUserCol;
    public DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // customer tableview
    @FXML
    private TableView<Customer> mainCustomerTable;
    @FXML
    private TableColumn<Customer, String> customerIdCol;
    @FXML
    private TableColumn<Customer, String> customerNameCol;
    @FXML
    private TableColumn<Customer, String> customerAddressCol;
    @FXML
    private TableColumn<Customer, String> customerPhoneCol;
    @FXML
    private TableColumn<Customer, String>  customerCountryCol;
    @FXML
    private TableColumn<Customer, String>  customerStateCol;
    @FXML
    private TableColumn<Customer, String> customerPostalCol;

    /**
     * This method populates the customer and appointment tables on screen load.
     *
     * <b>LAMBDA REQUIREMENT #1</b> - This single parameter lambda expression sets up columns for start and end dates as
     * a string property in order to be easily formatted.
     */

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Main Menu initialized!");

        // appointment table columns
        apptIDCol.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        apptTitleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        apptDescCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        apptLocationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        apptContactCol.setCellValueFactory(new PropertyValueFactory<>("contactID"));
        apptTypeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        apptCustCol.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        apptUserCol.setCellValueFactory(new PropertyValueFactory<>("userID"));

        // lambda
        apptStartCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getStart().format(formatter)));
        apptEndCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getEnd().format(formatter)));

        try {
            loadApptTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // customer table columns
        customerIdCol.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        customerNameCol.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        customerAddressCol.setCellValueFactory(new PropertyValueFactory<>("customerAddress"));
        customerPhoneCol.setCellValueFactory(new PropertyValueFactory<>("customerPhone"));
        customerCountryCol.setCellValueFactory(new PropertyValueFactory<>("customerCountry"));
        customerStateCol.setCellValueFactory(new PropertyValueFactory<>("division"));
        customerPostalCol.setCellValueFactory(new PropertyValueFactory<>("customerPostal"));

        try {
            loadCustomerTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // load appointment tableview
    public void loadApptTable() throws SQLException {
        ObservableList<Appointment> allAppointments = AppointmentDAO.allAppointments();
        mainApptTable.setItems(allAppointments);
        mainApptTable.getSelectionModel().clearSelection();
    }

    // load customer tableview
    public void loadCustomerTable() throws SQLException {
        ObservableList<Customer> allCustomers = CustomerDAO.allCustomers();
        mainCustomerTable.setItems(allCustomers);
        mainCustomerTable.getSelectionModel().clearSelection();
    }

    // show all appointments if All Appointments radio button selected - default selection
    public void changeToAllAppts() throws SQLException {
            ObservableList<Appointment> allAppointments = AppointmentDAO.allAppointments();
            for(Appointment ignored : allAppointments){
                mainApptTable.setItems(allAppointments);
                mainApptTable.setPlaceholder(new Label("No appointments available"));

            }
    }

    // show appointments for current month if radio button selected
    public void changeToMonth() throws SQLException {
        ObservableList<Appointment> currentMonth = AppointmentDAO.currentMonth();
        for(Appointment ignored : currentMonth){
            mainApptTable.setItems(currentMonth);
            mainApptTable.setPlaceholder(new Label("No appointments for this month"));
        }
    }

    // show appointments for current week if radio button selected
    public void changeToWeek() throws SQLException {
            ObservableList<Appointment> currentWeek = AppointmentDAO.currentWeek();
            for(Appointment ignored : currentWeek){
                mainApptTable.setItems(currentWeek);

        mainApptTable.setPlaceholder(new Label("No appointments for this week"));
        }
    }

    /**
     * This method will take the end-user to the Add Appointment screen where a new appointment can be added.
     *
     * @param actionEvent Add button is clicked under the appointments tableview.
     * @throws IOException The exception to throw if I/O error occurs.
     */
    public void addAppt(ActionEvent actionEvent) throws IOException {

        Parent root = FXMLLoader.load((Objects.requireNonNull(getClass().getResource("/view/add-appointment.fxml"))));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 560, 625);
        stage.setScene(scene);
        stage.show();
        stage.centerOnScreen();
        stage.setResizable(false);
    }

    /**
     * This method will take the end-user to Modify Appointment screen where they can modify existing appointments. The
     * data from the selected row will auto-populate to the modify form.
     *
     * @param actionEvent Modify button is clicked under the appointments tableview.
     * @throws IOException The exception to throw if I/O error occurs.
     */
    public void updateAppt(ActionEvent actionEvent) throws IOException {
        // error message if end-user does not make a selection
        if(mainApptTable.getSelectionModel().isEmpty()){
            Alert modApptSelect = new Alert(Alert.AlertType.WARNING, "Please select an appointment to be modified.");
            Optional<ButtonType> results = modApptSelect.showAndWait();
            if (results.isPresent() && results.get() == ButtonType.OK)
                modApptSelect.setOnCloseRequest(Event::consume);
                return;
        }

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/modify-appointment.fxml"));
        loader.load();

        // send selected data to modify appointment screen
        ModifyAppointment MainMenu = loader.getController();
        MainMenu.sendAppointment(mainApptTable.getSelectionModel().getSelectedItem());

        Parent scene = loader.getRoot();
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(new Scene(scene));

        stage.show();
        stage.centerOnScreen();
        stage.setResizable(false);
    }

    /**
     * This method will delete appointments from appointments table in the database. The deleted row will no longer be
     * displayed on appointment tableview. A message will also appear for deleted appointments stating the cancelled
     * appointment ID and type.
     */
    public void deleteApptRow() {
        if (mainApptTable.getSelectionModel().isEmpty()) {
            Alert deleteAppt = new Alert(Alert.AlertType.WARNING, "Please select an appointment to be deleted.");
            Optional<ButtonType> result = deleteAppt.showAndWait();
            if(result.isPresent() && result.get() == ButtonType.OK)
                mainApptTable.getSelectionModel().clearSelection();
        } else {
            Alert deleteApptConfirm = new Alert(Alert.AlertType.CONFIRMATION,"Are you sure you want to delete this " +
                "appointment?", ButtonType.YES, ButtonType.NO);
            Optional<ButtonType> result = deleteApptConfirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.YES) {
            try {
            Appointment selectedAppt = mainApptTable.getSelectionModel().getSelectedItem();
            AppointmentDAO.deleteAppt(selectedAppt);
            loadApptTable();

            Alert apptInfo = new Alert(Alert.AlertType.INFORMATION,"You have deleted the following appointment: " +
                    '\n' + '\n' + "Appointment ID: " + selectedAppt.getAppointmentID() + '\n' + "Type: " +
                    selectedAppt.getType(), ButtonType.OK);
            apptInfo.showAndWait();
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
            mainApptTable.getSelectionModel().clearSelection();
        }
    }

    /**
     * This method will take the end-user to the Add Customer screen where a new customer can be added.
     *
     * @param actionEvent Add button is clicked under the customers tableview.
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
     * This method will take the end-user to Modify Customer screen where they can modify existing customers. The
     * data from the selected row will auto-populate to the modify form.
     *
     * @param actionEvent Modify button is clicked under the customer tableview.
     * @throws IOException The exception to throw if I/O error occurs.
     */

    public void updateCustomer(ActionEvent actionEvent) throws IOException, SQLException {

        if(mainCustomerTable.getSelectionModel().isEmpty()){
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
            if(associatedAppts.size() > 0){
                Alert associatedAppt = new Alert(Alert.AlertType.WARNING, "All associated appointments will be " +
                    "deleted along with this customer. Are you sure you want to delete this customer?", ButtonType.YES, ButtonType.NO);
                associatedAppt.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                associatedAppt.setTitle(" ");
                associatedAppt.setHeaderText("Associated Appointment Found!");
                Optional<ButtonType> results = associatedAppt.showAndWait();
                    if (results.isPresent() && results.get() == ButtonType.YES ) {
                        CustomerDAO.deleteCustomer(deleteAssociatedAppts);
                        loadApptTable();
                        loadCustomerTable();
                        mainCustomerTable.getSelectionModel().clearSelection();
                    }
                        if (results.isPresent() && results.get() == ButtonType.NO ) {
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

                    Alert apptInfo = new Alert(Alert.AlertType.INFORMATION,"You have deleted the following " +
                            "customer: " + selectedCustomer.getCustomerName(), ButtonType.OK);
                    apptInfo.showAndWait();
                    }
                }
            } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * This method takes the end-user to view reports on the Reports screen.
     *
     * @param actionEvent Reports button is clicked (located on the right panel).
     * @throws IOException The exception to throw if I/O error occurs.
     */
    public void toReports(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load((Objects.requireNonNull(getClass().getResource("/view/reports.fxml"))));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1108, 620);
        stage.setScene(scene);
        stage.show();
        stage.centerOnScreen();
        stage.setResizable(false);
    }

    /**
     * This method takes the end-user to the main menu from another screen.
     *
     * @param actionEvent When button is clicked.
     * @throws IOException The exception to throw if I/O error occurs.
     */
    public static void toMainMenu(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load((Objects.requireNonNull(MainMenu.class.getResource("/view/main-menu.fxml"))));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1108, 620);
        stage.setScene(scene);
        stage.show();
        stage.centerOnScreen();
        stage.setResizable(false);
    }

    /**
     * This method closes the application and an alert will ask the end-user to confirm close.
     */
    public void toClose() {
        Alert closeConfirm = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to close the program?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = closeConfirm.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.YES) {
            Platform.exit();
            System.out.println("Program Closed");
        }
    }
}