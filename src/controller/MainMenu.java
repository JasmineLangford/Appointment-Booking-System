package controller;

import DAO.AppointmentDAO;
import DAO.CustomerDAO;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Appointment;
import model.Customer;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Optional;

/**
 * This class contains a tableview for appointments and customers queried from the database.
 *
 * End-user is able to navigate to other screens by buttons to add, modify and delete appointments and customers.
 *
 * End-user can also navigate to a report view.
 */
public class MainMenu {

    // Radio Buttons for Appointment View
    public ToggleGroup apptViewToggle;
    public RadioButton viewByMonth;
    public RadioButton viewByWeek;
    public RadioButton viewAllAppts;

    // Appointment Table
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

    // Customers Table
    @FXML
    private TableView<Customer> mainCustomerTable;
    @FXML
    private TableColumn<Appointment, String> customerIdCol;
    @FXML
    private TableColumn<Appointment, String> customerNameCol;
    @FXML
    private TableColumn<Appointment, String> customerAddressCol;
    @FXML
    private TableColumn<Appointment, String> customerPhoneCol;
    @FXML
    private TableColumn<Appointment, String>  customerCountryCol;
    @FXML
    private TableColumn<Appointment, String>  customerStateCol;
    @FXML
    private TableColumn<Appointment, String> customerPostalCol;


    /**
     * This initializes the Main Menu and populates the tableviews.
     */
    public void initialize() throws SQLException{
        System.out.println("Main Menu initialized!");

        // Appointment table columns
        apptIDCol.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        apptTitleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        apptDescCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        apptLocationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        apptContactCol.setCellValueFactory(new PropertyValueFactory<>("contactID"));
        apptTypeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        apptStartCol.setCellValueFactory(new PropertyValueFactory<>("start"));
        apptEndCol.setCellValueFactory(new PropertyValueFactory<>("end"));
        apptCustCol.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        apptUserCol.setCellValueFactory(new PropertyValueFactory<>("userID"));

        loadApptTable();

        // Customer table columns
        customerIdCol.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        customerNameCol.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        customerAddressCol.setCellValueFactory(new PropertyValueFactory<>("customerAddress"));
        customerPhoneCol.setCellValueFactory(new PropertyValueFactory<>("customerPhone"));
        customerCountryCol.setCellValueFactory(new PropertyValueFactory<>("customerCountry"));
        customerStateCol.setCellValueFactory(new PropertyValueFactory<>("Division"));
        customerPostalCol.setCellValueFactory(new PropertyValueFactory<>("customerPostal"));

        loadCustomerTable();

    }

    // refresh tables
    public void loadApptTable() throws SQLException {
        ObservableList<Appointment> allAppointments = AppointmentDAO.allAppointments();
        mainApptTable.setItems(allAppointments);

    }

    public void loadCustomerTable() throws SQLException {
        ObservableList<Customer> allCustomers = CustomerDAO.allCustomers();
        mainCustomerTable.setItems(allCustomers);
    }

    // Show all appointments if All Appointments radio button selected
    public void changeToAllAppts() {
        try{
            ObservableList<Appointment> allAppointments = AppointmentDAO.allAppointments();
            for(Appointment ignored : allAppointments){
                mainApptTable.setItems(allAppointments);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void changeToMonth() {
    }

    public void changeToWeek() {
}

    // Filter appointments by all, current month, or current week by radio button selection
/*        if (viewByWeek.isSelected()) {
            LocalDateTime startWeek = LocalDate.now().with(WeekFields.ISO.getFirstDayOfWeek()).atStartOfDay();
            LocalDateTime endWeek = startWeek.plusWeeks(1);
            mainApptTable.setItems(AppointmentDAO.allAppointments().stream()
                    .filter(a -> a.getStart().isAfter(startWeek) && a.getStart().isBefore(endWeek))
                    .collect(Collectors.toCollection(FXCollections::observableArrayList)));
        } else if (viewByMonth.isSelected()) {
            mainApptTable.setItems(AppointmentDAO.allAppointments().stream()
                    .filter(a -> a.getStart().getMonth() == LocalDate.now().getMonth())
                    .collect(Collectors.toCollection(FXCollections::observableArrayList)));
        } else {
            viewAllAppts.isSelected();
            ObservableList<Appointment> allAppointments = AppointmentDAO.allAppointments();
            for (Appointment ignored : allAppointments) {
                mainApptTable.setItems(allAppointments);
            }
        }
    }*/

    /**
     * This will take the end-user to the Add Appointment screen where they can add a new appointment.
     *
     * @param actionEvent Add button is clicked under the Appointments tableview.
     */
    public void addAppt(ActionEvent actionEvent) throws IOException {
        System.out.println("Add Appointment initialized.");

        Parent root = FXMLLoader.load((Objects.requireNonNull(getClass().getResource("/view/add-appointment.fxml"))));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 560, 625);
        stage.setScene(scene);
        stage.show();
        stage.centerOnScreen();
        stage.setResizable(false);
    }

    /**
     * This will take the end-user to Modify Appointment screen where they can modify existing appointments.
     *
     * @param actionEvent Modify button is clicked under the Appointments tableview.
     */
    public void updateAppt(ActionEvent actionEvent) throws IOException {
        System.out.println("Modify Appointment initialized.");

        Parent root = FXMLLoader.load((Objects.requireNonNull(getClass().getResource("/view/modify-appointment.fxml"))));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 560, 625);
        stage.setScene(scene);
        stage.show();
        stage.centerOnScreen();
        stage.setResizable(false);
    }

    /**
     * This will delete appointments from appointments table in the database.
     *
     * Will update appointment table on main menu after deletion.
     */
    public void deleteApptRow() throws SQLException {
        if (mainApptTable.getSelectionModel().isEmpty()) {
            Alert deleteAppt = new Alert(Alert.AlertType.WARNING, "Please select an appointment to be deleted."
                    + "");
            Optional<ButtonType> result = deleteAppt.showAndWait();
            if(result.isPresent() && result.get() == ButtonType.OK)
                return;
        }

        Alert deleteApptConfirm = new Alert(Alert.AlertType.CONFIRMATION,"Are you sure you want to delete this " +
                "appointment?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = deleteApptConfirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.YES) {
            Appointment selectedAppt = mainApptTable.getSelectionModel().getSelectedItem();
            AppointmentDAO.deleteAppt(selectedAppt);

            loadApptTable();
        }
        mainApptTable.getSelectionModel().clearSelection();
    }

    /**
     * This will take the end-user to the Add Customer screen where they can add a new customer.
     *
     * @param actionEvent Add button is clicked under the Customers tableview.
     */
    public void addCustomer(ActionEvent actionEvent) throws IOException {
        System.out.println("Add Customer initialized.");

        Parent root = FXMLLoader.load((Objects.requireNonNull(getClass().getResource("/view/add-customer.fxml"))));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 500, 475);
        stage.setScene(scene);
        stage.show();
        stage.centerOnScreen();
        stage.setResizable(false);
    }

    /**
     * This will take the end-user to Modify Customer screen where they can modify existing customers.
     *
     * @param actionEvent Modify button is clicked under the Customer tableview.
     */

    public void updateCustomer(ActionEvent actionEvent) throws IOException {
        System.out.println("Modify Customer initialized.");

        Parent root = FXMLLoader.load((Objects.requireNonNull(getClass().getResource("/view/modify-customer.fxml"))));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 500, 475);
        stage.setScene(scene);
        stage.show();
        stage.centerOnScreen();
        stage.setResizable(false);
    }


    /**
     * This will delete a customer from the customers table in the database.
     *
     * All customer's associated appointments will need to be deleted prior to deleting the customer.
     */
    public void deleteCustomerRow() throws SQLException {
        if (mainCustomerTable.getSelectionModel().isEmpty()) {
            Alert deleteCust = new Alert(Alert.AlertType.WARNING, "Please select a customer to be deleted.");
            Optional<ButtonType> result = deleteCust.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK)
                return;
        }

        /*Customer deleteAssociatedAppts = mainCustomerTable.getSelectionModel().getSelectedItem();

        if (deleteAssociatedAppts.get size() > 0){

            Alert associatedAppt = new Alert(Alert.AlertType.WARNING, "All associated appointments will be deleted"
            + "along with this customer. Are you sure you want to delete the customer?", ButtonType.YES, ButtonType.NO);
            Optional<ButtonType> results = associatedAppt.showAndWait();
            if (results.isPresent() && results.get() == ButtonType.YES ) {

            CustomerDAO.deleteCustomer(deleteAssociatedAppts);

            loadApptTable();
            loadCustomerTable();
            }
        }
        /*mainCustomerTable.getSelectionModel().clearSelection();

        Alert deleteCustConfirm = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this " +
                "customer?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = deleteCustConfirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.YES) {
            Customer selectedCustomer = mainCustomerTable.getSelectionModel().getSelectedItem();
            CustomerDAO.deleteCustomer(selectedCustomer);

            loadCustomerTable();
        }
        mainCustomerTable.getSelectionModel().clearSelection();*/
    }

    /**
     * This takes the user to view reports on the Reports screen.
     * @param actionEvent Reports button is clicked (located on the right panel).
     */
    public void toReports(ActionEvent actionEvent) throws IOException {
        System.out.println("Reports initialized.");

        Parent root = FXMLLoader.load((Objects.requireNonNull(getClass().getResource("/view/reports.fxml"))));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1108, 620);
        stage.setScene(scene);
        stage.show();
        stage.centerOnScreen();
        stage.setResizable(false);
    }

    /**
     * This closes the application.
     * An alert will ask the user to confirm close.
     * @param actionEvent Logout button is clicked (located on the right panel).
     */
    public void toClose(ActionEvent actionEvent) {
        Alert closeConfirm = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to close the program?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = closeConfirm.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.YES) {
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.close();
            System.out.println("Program Closed");
        }
    }



}