package controller;

import DAO.AppointmentDAO;
import DAO.ContactDAO;
import DAO.CustomerDAO;
import DAO.ReportDAO;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import model.Appointment;
import model.Customer;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/***
 * This class is the controller for reports.fxml.
 * The end-user is able to navigate back to the main menu or logout with the use of buttons in the lower right-hand
 * corner of the screen.
 */

public class Reports implements Initializable {

    // appointments by contact table
    @FXML
    private TableView<Appointment> byContactView;
    @FXML
    private TableColumn<Appointment, String> apptLocationCol;
    @FXML
    private TableColumn<Appointment, String> apptTitleCol;
    @FXML
    private TableColumn<Appointment, String> apptDescCol;
    @FXML
    private TableColumn<Appointment, String> apptTypeCol;
    @FXML
    private TableColumn<Appointment, String> apptDateCol;
    @FXML
    private TableColumn<Appointment, String> apptTimeCol;
    @FXML
    private TableColumn<Appointment, String> custIdCol;

    // combo box of contacts to be filtered
    @FXML
    private ComboBox<ContactDAO> contactCombo;

    ObservableList<ContactDAO> contacts = ContactDAO.allContacts();
    public DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // customer appointments by state or province table
    @FXML
    private TableView<Customer> byStateProvView;
    @FXML
    private TableColumn<Customer, String> divisionCol;
    @FXML
    private TableColumn<Customer, Integer> totalCustomersByDivision;

    // appointments by month and type table
    @FXML
    private TableView<Appointment> byTypeMonthView;
    @FXML
    private TableColumn<Appointment, LocalDateTime> apptMonthCol;
    @FXML
    private TableColumn<Appointment, String > typeByMonthCol;
    @FXML
    private TableColumn<Appointment, Integer> totalTypeCol;

    public Reports() throws SQLException {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Reports initialized.");

        // report by contact
        apptLocationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        apptTitleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        apptDescCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        apptTypeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        apptDateCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getStart().format(formatter)));
        apptTimeCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getEnd().format(formatter)));
        custIdCol.setCellValueFactory(cellData -> {
            int customerID = cellData.getValue().getCustomerID();

            try {
                ObservableList<Customer> customers = CustomerDAO.allCustomers();
                String customerName = null;
                for (Customer customer : customers) {
                    if (customer.getCustomerId() == customerID) {
                        customerName = customer.getCustomerName();
                        break;
                    }
                }
                return new SimpleStringProperty(customerName);
            } catch (SQLException e) {
                e.printStackTrace();
                return null;
            }
        });
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMMM d, yyyy");
        apptDateCol.setCellValueFactory(cellData -> {
            String dateTimeValue = cellData.getValue().getStart().format(dateFormatter);
            return new SimpleStringProperty(dateTimeValue);
        });

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("h:mm a");
        apptTimeCol.setCellValueFactory(cellData -> {
            LocalTime startTime = cellData.getValue().getStart().toLocalTime();
            LocalTime endTime = cellData.getValue().getEnd().toLocalTime();
            String timeRange = startTime.format(timeFormatter) + " - " + endTime.format(timeFormatter);
            return new SimpleStringProperty(timeRange);
        });

        try {
            ObservableList<Appointment> reportByContact = ReportDAO.appointmentsByCustomer();
            byContactView.setItems(reportByContact);
            byContactView.setSelectionModel(null);
            byContactView.setPlaceholder(new Label("There are no appointments for this contact."));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // populate existing contacts in combo box
        contactCombo.setItems(contacts);
        contactCombo.setPromptText("Select Contact");

        // report by customer state or province
        divisionCol.setCellValueFactory(new PropertyValueFactory<>("division"));
        totalCustomersByDivision.setCellValueFactory(new PropertyValueFactory<>("totalCustomers"));

        try{
            ObservableList<Customer> reportByDivision = ReportDAO.customersByDivision();
            byStateProvView.setItems(reportByDivision);
            byStateProvView.setSelectionModel(null);
        }catch (SQLException e) {
            e.printStackTrace();
        }

        // report by month and type
        apptMonthCol.setCellValueFactory(new PropertyValueFactory<>("startString"));
        typeByMonthCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        totalTypeCol.setCellValueFactory(new PropertyValueFactory<>("totalType"));

        try{
            ObservableList<Appointment> reportByType = ReportDAO.appointmentsByMonthType();
            byTypeMonthView.setItems(reportByType);
            byTypeMonthView.setSelectionModel(null);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /**
     * This method uses a lambda expression to filter contact appointments based on end-user selection from
     * the contact combo box.
     */
    public void selectedContact() {
        ContactDAO contactSelected = contactCombo.getSelectionModel().getSelectedItem();
        try {
            byContactView.setItems(AppointmentDAO.allAppointments().stream()
                    .filter(contactAppts -> contactAppts.getContactID() == contactSelected.getContactID())
                    .collect(Collectors.toCollection(FXCollections::observableArrayList))
            );
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method closes the application and an alert will ask the end-user to confirm close.
     */
    public void toCloseApplication (MouseEvent mouseEvent) throws IOException {
        Alert closeConfirm = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to close the program?",
                ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = closeConfirm.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.YES) {
            Platform.exit();
            System.out.println("Program Closed");
        } else {
            Appointments toReportScreen = new Appointments();
            toReportScreen.toReports(mouseEvent);
        }
    }

    public void toCustomers(MouseEvent mouseEvent) throws IOException {
        Appointments toCustomerScreen = new Appointments();
        toCustomerScreen.toCustomers(mouseEvent);
    }

    public void toAppointments(MouseEvent mouseEvent) throws IOException {
        Customers toAppointmentScreen = new Customers();
        toAppointmentScreen.backToAppointments(mouseEvent);
    }
}
