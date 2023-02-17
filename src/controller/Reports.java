package controller;

import DAO.AppointmentDAO;
import DAO.ContactDAO;
import DAO.ReportDAO;
import javafx.beans.Observable;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
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
import javafx.util.Callback;
import model.Appointment;
import model.Contact;
import model.Customer;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class Reports implements Initializable {

    // table of appointments by contact
    @FXML
    private TableView<Appointment> byContactView;
    @FXML
    private TableColumn<Object, Object> apptIdCol;
    @FXML
    private TableColumn<Object, Object> apptTitleCol;
    @FXML
    private TableColumn<Object, Object> apptDescCol;
    @FXML
    private TableColumn<Object, Object> apptTypeCol;
    @FXML
    private TableColumn<Object, Object> apptStartCol;
    @FXML
    private TableColumn<Object, Object> apptEndCol;
    @FXML
    private TableColumn<Object, Object> custIdCol;

    // combo box of contacts to be filtered
    @FXML
    private ComboBox<ContactDAO> contactCombo;
    ObservableList<ContactDAO> contacts = ContactDAO.allContacts();

    // table of current month's appointments by type
    @FXML
    private TableView<Appointment> byTypeMonthView;
    @FXML
    private TableColumn<Object, Object> apptMonthCol;
    @FXML
    private TableColumn<Object, Object> typeByMonthCol;
    @FXML
    private TableColumn<Object, Object> totalTypeApptCol;

    // table of customer appointments by state or province
    @FXML
    private TableView<Customer> byStateProvView;
    @FXML
    private TableColumn<Object, Object> divisionCol;
    @FXML
    private TableColumn<Object, Object> totalCustomersByDivision;

    public Reports() throws SQLException {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Reports initialized.");

        // report by contact table
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

        // report by customer state or province

        //totalCustomersByDivision.setCellValueFactory(new PropertyValueFactory<>("totalCustomers"));

        divisionCol.setCellValueFactory(new PropertyValueFactory<>("division"));
        totalCustomersByDivision.setCellValueFactory(new PropertyValueFactory<>("COUNT(first_level_divisions.Division)"));

        try{
            ObservableList<Customer> reportByDivision = ReportDAO.customersByDivision();
            byStateProvView.setItems(reportByDivision);
            byStateProvView.setSelectionModel(null);
        }catch (SQLException e) {
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
