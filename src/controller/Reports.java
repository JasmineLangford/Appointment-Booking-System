package controller;

import DAO.AppointmentDAO;
import DAO.ContactDAO;
import DAO.ReportDAO;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Appointment;
import model.Customer;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/***
 * This class is the controller for reports.fxml.
 *
 * <b>Required Report #1</b> The table displayed on the second row and to the right contains three columns for month,
 * type and total type. This report is being queried from the database and shows the month of the customer appointment,
 * where the months are in ascending order. Then, the type of the customer appointment with the sum of the type of
 * appointments for that particular month.
 *
 * <b>Required Report #2</b> The table displayed on the first row contains seven columns for appointment ID, title,
 * description, type, start date/time, end date/time, and customer ID. This report is being queried from the database
 * with the purpose of filtering appointments by their associated contact. Contacts can be selected from a combo box
 * above the report table.
 *
 * <b>Required Report #3</b> - Report of choice<b/> The table displayed on the second row and to the left contains two
 * columns for state/province and total customers. This report is being queried from the database with the purpose of
 * showing the total customers in each state/province. This report would be beneficial to see if the company needed to
 * understand staffing needs for each office.
 *
 * The end-user is able to navigate back to the main menu or logout with the use of buttons in the lower right-hand
 * corner of the screen.
 */

public class Reports implements Initializable {
    // appointments by contact table
    @FXML
    private TableView<Appointment> byContactView;
    @FXML
    private TableColumn<Appointment, Integer> apptIdCol;
    @FXML
    private TableColumn<Appointment, String> apptTitleCol;
    @FXML
    private TableColumn<Appointment, String> apptDescCol;
    @FXML
    private TableColumn<Appointment, String> apptTypeCol;
    @FXML
    private TableColumn<Appointment, String> apptStartCol;
    @FXML
    private TableColumn<Appointment, String> apptEndCol;
    @FXML
    private TableColumn<Appointment, Integer> custIdCol;

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
        apptIdCol.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        apptTitleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        apptDescCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        apptTypeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        apptStartCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getStart().format(formatter)));
        apptEndCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getEnd().format(formatter)));
        custIdCol.setCellValueFactory(new PropertyValueFactory<>("customerID"));

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
     * This method navigates the user back to the Main Menu.
     *
     * @param actionEvent Back to Main button is clicked.
     * @throws IOException The exception to throw if I/O error occurs.
     */
    public void toMainMenu(ActionEvent actionEvent) throws IOException {
        Appointments.backToAppointments(actionEvent);
    }

    /**
     * This method closes the application and an alert will ask the end-user to confirm close.
     */
    public void toCloseApplication () {
        Alert closeConfirm = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to close the program?",
                ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = closeConfirm.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.YES) {
            Platform.exit();
            System.out.println("Program Closed");
        }
    }
}
