package controller;

import DAO.AppointmentDAO;
import DAO.ContactDAO;
import DAO.FirstLevelDAO;
import DAO.ReportDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Appointment;
import model.Contact;
import model.Customer;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class Reports implements Initializable {

    // table of appointments by contact
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
    private TableColumn<Appointment, LocalDateTime> apptStartCol;
    @FXML
    private TableColumn<Appointment, LocalDateTime> apptEndCol;
    @FXML
    private TableColumn<Appointment, Integer> custIdCol;

    // combo box of contacts to be filtered
    @FXML
    private ComboBox<ContactDAO> contactCombo;
    ObservableList<ContactDAO> contacts = ContactDAO.allContacts();

    // table of current month's appointments by type
    @FXML
    private TableView<Appointment> byTypeMonthView;
    @FXML
    private TableColumn<Appointment, String> apptMonthCol;
    @FXML
    private TableColumn<Appointment, String > typeByMonthCol;
    @FXML
    private TableColumn<Appointment, Integer> totalTypeCol;

    // table of customer appointments by state or province
    @FXML
    private TableView<Customer> byStateProvView;
    @FXML
    private TableColumn<Customer, String> divisionCol;
    @FXML
    private TableColumn<Customer, Integer> totalCustomersByDivision;

    public Reports() throws SQLException {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Reports initialized.");

        // tableview set up - report by contact
        apptIdCol.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        apptTitleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        apptDescCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        apptTypeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        apptStartCol.setCellValueFactory(new PropertyValueFactory<>("start"));
        apptEndCol.setCellValueFactory(new PropertyValueFactory<>("end"));
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
        contactCombo.setPromptText("Select contact.");

        // tableview set up - report by customer state or province
        divisionCol.setCellValueFactory(new PropertyValueFactory<>("division"));
        totalCustomersByDivision.setCellValueFactory(new PropertyValueFactory<>("totalCustomers"));

        try{
            ObservableList<Customer> reportByDivision = ReportDAO.customersByDivision();
            byStateProvView.setItems(reportByDivision);
            byStateProvView.setSelectionModel(null);
        }catch (SQLException e) {
            e.printStackTrace();
        }

        // tableview set up - report by month and type
        apptMonthCol.setCellValueFactory(new PropertyValueFactory<>("start"));
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
         * Method to filter contact appointments based on end-user selection from contact combo box
         */
        public void selectedContact() {
            Contact contactSelected = contactCombo.getSelectionModel().getSelectedItem();
            try {
                byContactView.setItems(AppointmentDAO.allAppointments().stream()
                        .filter(contactAppts -> contactAppts.getContactID() == contactSelected.getContactId())
                        .collect(Collectors.toCollection(FXCollections::observableArrayList))
                );
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }


         /*try{
             LocalDate currentDate = LocalDate.now();
             Month currentMonth = currentDate.getMonth();
             ObservableList<Appointment> monthReportByType = AppointmentDAO.currentMonth();
             byTypeMonthView.setItems(monthReportByType);
             byTypeMonthView.setSelectionModel(null);
             byTypeMonthView.setPlaceholder(new Label("There are no appointments for the current month of " +
                     currentMonth));
         } catch (SQLException e) {
             e.printStackTrace();
         }

        apptMonthCol.setCellValueFactory(new PropertyValueFactory<>("start"));
        typeByMonthCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        //totalTypeApptCol.setCellValueFactory(new PropertyValueFactory<>("start"));*/

    /**
     * This navigates the user back to the Main Menu.
     *
     * @param actionEvent Back to Main button is clicked.
     * */
    public void toMainMenu(ActionEvent actionEvent) throws IOException {
       MainMenu.toMainMenu(actionEvent);
    }

}
