package controller;

import DAO.AppointmentDAO;
import DAO.CustomerDAO;
import javafx.collections.FXCollections;
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

import javax.swing.*;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.WeekFields;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * This class contains a tableview for appointments and customers.
 * Users are able to navigate to other screens by buttons to add, modify and delete appointments and customers.
 * Users can also navigate to a report view.
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
    private TableColumn<Appointment, String > mainApptID;
    @FXML
    private TableColumn<Appointment, String> mainApptTitle;
    @FXML
    private TableColumn<Appointment, String>  mainApptDesc;
    @FXML
    private TableColumn<Appointment, String>  mainApptLocation;
    @FXML
    private TableColumn<Appointment, String> mainApptContact;
    @FXML
    private TableColumn<Appointment, String>  mainApptType;
    @FXML
    private TableColumn<Appointment, String> mainApptStart;
    @FXML
    private TableColumn<Appointment, String> mainApptEnd;
    @FXML
    private TableColumn<Appointment, String> mainApptCust;
    @FXML
    private TableColumn<Appointment, String>  mainApptUser;

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


    public void initialize() throws SQLException{
        System.out.println("Main Menu initialized!");

        // Appointment table columns
        mainApptID.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        mainApptTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        mainApptDesc.setCellValueFactory(new PropertyValueFactory<>("description"));
        mainApptLocation.setCellValueFactory(new PropertyValueFactory<>("location"));
        mainApptContact.setCellValueFactory(new PropertyValueFactory<>("contactID"));
        mainApptType.setCellValueFactory(new PropertyValueFactory<>("type"));
        mainApptStart.setCellValueFactory(new PropertyValueFactory<>("start"));
        mainApptEnd.setCellValueFactory(new PropertyValueFactory<>("end"));
        mainApptCust.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        mainApptUser.setCellValueFactory(new PropertyValueFactory<>("userID"));

        // All appointments appear on Appointment table on screen load
        ObservableList<Appointment> allAppointments = AppointmentDAO.allAppointments();
        mainApptTable.setItems(allAppointments);

        // Customer table columns
        customerIdCol.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        customerNameCol.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        customerAddressCol.setCellValueFactory(new PropertyValueFactory<>("customerAddress"));
        customerPhoneCol.setCellValueFactory(new PropertyValueFactory<>("customerPhone"));
        customerCountryCol.setCellValueFactory(new PropertyValueFactory<>("customerCountry"));
        customerStateCol.setCellValueFactory(new PropertyValueFactory<>("Division"));
        customerPostalCol.setCellValueFactory(new PropertyValueFactory<>("customerPostal"));

        // All customers appear on Customer table on screen load
        ObservableList<Customer> allcustomers = CustomerDAO.allCustomers();
        mainCustomerTable.setItems(allcustomers);
    }

    // Show all appointments if All Appointments radio button selected
    /*public void changeToAllAppts() {
        try{
            ObservableList<Appointment> allAppointments = AppointmentDAO.allAppointments();
            for(Appointment ignored : allAppointments){
                mainApptTable.setItems(allAppointments);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }*/

    // Filter appointments by current month or week
    public void filterAppts(ActionEvent event) throws SQLException {
        if(viewAllAppts.isSelected()){
            ObservableList<Appointment> allAppointments = AppointmentDAO.allAppointments();
            for(Appointment ignored : allAppointments){
                mainApptTable.setItems(allAppointments);

        //}else if(viewByMonth.isSelected()){

        //} else if (viewByWeek.isSelected()){
            }
        }
    }

    /**
     * This will take the user to the Add Appointment screen where they can add a new appointment.
     * @param actionEvent Add button is clicked under the Appointments tableview.
     * */
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
     * This will take the user to Modify Appointment screen where they can modify existing appointments.
     * @param actionEvent Modify button is clicked under the Appointments tableview.
     * */
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
     * This will take the user to the Add Customer screen where they can add a new customer.
     * @param actionEvent Add button is clicked under the Customers tableview.
     * */
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
     * This will take the user to Modify Customer screen where they can modify existing customers.
     * @param actionEvent Modify button is clicked under the Customer tableview.
     * */

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
     * This will delete appointments from the Appointments tableview.
     * Appointments that involve customers cannot be deleted.
     * The customers associated with that appointment will need to be deleted prior to deleting the appointment.
     * @param actionEvent delete button below Appointment tableview clicked.
     */
    public void deleteApt(ActionEvent actionEvent) {
    }

    /**
     * This will delete a customer from the Customer tableview.
     * @param actionEvent delete button below Customer tableview is clicked.
     */
    public void deleteCustomer(ActionEvent actionEvent) {

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