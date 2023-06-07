package controller;

import DAO.AppointmentDAO;
import DAO.ContactDAO;
import DAO.CustomerDAO;
import DAO.UserDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
import model.Appointment;
import model.Customer;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.*;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * This class is the controller for add-appointment.fxml. The end-user can add a new appointment by inputting data into
 * text fields, combo boxes, and date pickers. The appointment ID is auto-incremented from the database and disabled.
 * The end-user will be able to save the data for a new appointment by clicking the save button at the bottom of the
 * screen or cancel if the end-user no longer wants to add an appointment.
 */
public class AddAppointment implements Initializable {

    // observable lists for combo boxes
    ObservableList<ContactDAO> contactList = ContactDAO.allContacts();
    ObservableList<UserDAO> users = UserDAO.allUsers();
    ObservableList<Customer> customers = CustomerDAO.allCustomers();
    Appointment newAppointment = new Appointment();
    @FXML
    private ComboBox<String> locationCombo;
    @FXML
    private ComboBox<String> typeCombo;
    // Form fields
    @FXML
    private DatePicker startDatePicker;
    @FXML
    private ComboBox<LocalTime> startCombo;
    @FXML
    private ComboBox<LocalTime> endCombo;
    @FXML
    private DatePicker endDatePicker;
    @FXML
    private ComboBox<ContactDAO> contactCombo;
    @FXML
    private TextField typeTextfield;
    @FXML
    private TextField titleTextfield;
    @FXML
    private TextField descTextfield;
    @FXML
    private TextField locationTextfield;
    @FXML
    private ComboBox<Customer> custIDCombo;
    @FXML
    private ComboBox<UserDAO> userCombo;

    public AddAppointment() throws SQLException {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Add appointment is initialized!");

        // combo box for time selection
        LocalTime start = LocalTime.of(4, 0);
        LocalTime end = LocalTime.of(23, 0);

        while (start.isBefore(end.plusSeconds(1))) {
            startCombo.getItems().add(start);
            endCombo.getItems().add(start);
            start = start.plusMinutes(15);
        }

        startCombo.setPromptText("Select Start Time");
        endCombo.setPromptText("Select End Time");

        // contact combo box
        contactCombo.setItems(contactList);
        contactCombo.getSelectionModel().clearSelection();
        contactCombo.setPromptText("Select Contact");

        // user combo box
        userCombo.setItems(users);
        userCombo.setPromptText("Select User ID");

        // customer combo box
        custIDCombo.setItems(customers);
        custIDCombo.setPromptText("Select Customer ID");

        // appointment type combo box
        ObservableList<String> types = FXCollections.observableArrayList(
                "Custom Cake Design",
                "Wedding Cake Consultation",
                "Corporate Event Consultation",
                "Cake Tasting"
        );
        typeCombo.setItems(types);
        typeCombo.setPromptText("Select Type");

        // location combo box
        ObservableList<String> locations = FXCollections.observableArrayList(
                "New York City", "San Francisco"
        );
        locationCombo.setItems(locations);
        locationCombo.setPromptText("Selection Location");
    }

    /**
     * This method will save data entered in form fields and add the new appointment to the database.
     *
     * @param actionEvent The save button clicked.
     * @throws SQLException The exception to throw if unable to access the database or has errors.
     */
    public void onSaveAppt(ActionEvent actionEvent) throws SQLException {

        // input validation messages: start and end date pickers
        try {
            if (startDatePicker == null || endDatePicker.getValue() == null) {

                Alert noSelection = new Alert(Alert.AlertType.ERROR, "Please select start and end dates.");
                noSelection.setTitle("Appointment Booking System");
                noSelection.setHeaderText("Add Appointment");
                Optional<ButtonType> results = noSelection.showAndWait();
                if (results.isPresent() && results.get() == ButtonType.OK)
                    noSelection.setOnCloseRequest(Event::consume);
                return;
            }
        } catch (NullPointerException e) {
            System.out.println("Caught NullPointerException");
        }

        try {
            if (endDatePicker.getValue().isBefore(startDatePicker.getValue())) {
                Alert invalidDate = new Alert(Alert.AlertType.ERROR, "End date cannot be before start date.");
                invalidDate.setTitle("Appointment Booking System");
                invalidDate.setHeaderText("Add Appointment");
                Optional<ButtonType> results = invalidDate.showAndWait();
                if (results.isPresent() && results.get() == ButtonType.OK)
                    invalidDate.setOnCloseRequest(Event::consume);
                return;
            }
        } catch (Exception DateException) {
            System.out.println("Caught DateException");
        }

        try {
            if (startDatePicker.getValue().isBefore(LocalDate.now())) {
                Alert invalidDate = new Alert(Alert.AlertType.ERROR, "Start date has already passed. Please " +
                        "select another date.");
                invalidDate.setTitle("Appointment Booking System");
                invalidDate.setHeaderText("Add Appointment");
                Optional<ButtonType> results = invalidDate.showAndWait();
                if (results.isPresent() && results.get() == ButtonType.OK)
                    invalidDate.setOnCloseRequest(Event::consume);
                return;
            }
        } catch (Exception DateException) {
            System.out.println("Caught DateException");
        }

        try {
            if (startDatePicker.getValue().getDayOfWeek().equals(DayOfWeek.SATURDAY) ||
                    startDatePicker.getValue().getDayOfWeek().equals(DayOfWeek.SUNDAY) ||
                    endDatePicker.getValue().getDayOfWeek().equals(DayOfWeek.SATURDAY) ||
                    endDatePicker.getValue().getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
                Alert invalidDate = new Alert(Alert.AlertType.ERROR, "Start/End dates must be a weekday " +
                        "(Monday-Friday).");
                invalidDate.setTitle("Appointment Booking System");
                invalidDate.setHeaderText("Add Appointment");
                Optional<ButtonType> results = invalidDate.showAndWait();
                if (results.isPresent() && results.get() == ButtonType.OK)
                    invalidDate.setOnCloseRequest(Event::consume);
                return;
            }
        } catch (Exception DateException) {
            System.out.println("Caught DateException");
        }

        // input validation messages: start and end time combo boxes
        try {
            if (startCombo.getSelectionModel().isEmpty() || endCombo.getSelectionModel().isEmpty()) {

                Alert noSelection = new Alert(Alert.AlertType.ERROR, "Please select start and end times.");
                noSelection.setTitle("Appointment Booking System");
                noSelection.setHeaderText("Add Appointment");
                Optional<ButtonType> results = noSelection.showAndWait();
                if (results.isPresent() && results.get() == ButtonType.OK)
                    noSelection.setOnCloseRequest(Event::consume);
                return;
            }
        } catch (NullPointerException e) {
            System.out.println("Caught NullPointerException");
        }

        LocalDateTime localStart = LocalDateTime.of(startDatePicker.getValue(), startCombo.getValue());
        LocalDateTime localEnd = LocalDateTime.of(endDatePicker.getValue(), endCombo.getValue());
        try {
            if (localEnd.isBefore(localStart)) {
                Alert invalidTime = new Alert(Alert.AlertType.ERROR, "End date/time cannot be before start " +
                        "date/time.");
                invalidTime.setTitle("Appointment Booking System");
                invalidTime.setHeaderText("Add Appointment");
                Optional<ButtonType> results = invalidTime.showAndWait();
                if (results.isPresent() && results.get() == ButtonType.OK)
                    invalidTime.setOnCloseRequest(Event::consume);
                return;
            }
        } catch (Exception TimeException) {
            System.out.println("Caught TimeException");
        }

        try {
            if (startCombo.getValue().equals(endCombo.getValue())) {
                Alert invalidTime = new Alert(Alert.AlertType.ERROR, "End time cannot be the same as start " +
                        "time. Please select another end time.");
                invalidTime.setTitle("Appointment Booking System");
                invalidTime.setHeaderText("Add Appointment");
                invalidTime.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                Optional<ButtonType> results = invalidTime.showAndWait();
                if (results.isPresent() && results.get() == ButtonType.OK)
                    invalidTime.setOnCloseRequest(Event::consume);
                return;
            }
        } catch (Exception TimeException) {
            System.out.println("Caught TimeException");
        }

        // input validation: empty selection for contact combo
        try {
            if (contactCombo.getValue() == null) {

                Alert noSelection = new Alert(Alert.AlertType.ERROR, "Please select a contact.");
                noSelection.setTitle("Appointment Booking System");
                noSelection.setHeaderText("Add Appointment");
                Optional<ButtonType> results = noSelection.showAndWait();
                if (results.isPresent() && results.get() == ButtonType.OK)
                    noSelection.setOnCloseRequest(Event::consume);
                return;
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        // input validation: empty text fields
        try {
            if (contactCombo.getValue() == null || typeCombo.getValue() == null|| titleTextfield.getText().isEmpty() ||
                    descTextfield.getText().isEmpty() || locationCombo.getValue() == null) {
                Alert emptyField = new Alert(Alert.AlertType.ERROR, "One or more fields are empty. Please enter a" +
                        " value in each field.");
                emptyField.setTitle("Appointment Booking System");
                emptyField.setHeaderText("Add Appointment");
                Optional<ButtonType> results = emptyField.showAndWait();
                if (results.isPresent() && results.get() == ButtonType.OK)
                    emptyField.setOnCloseRequest(Event::consume);
                return;
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        // input validation: empty customer ID combo
        try {
            if (custIDCombo.getValue() == null) {

                Alert noSelection = new Alert(Alert.AlertType.ERROR, "Please select a customer ID.");
                noSelection.setTitle("Appointment Booking System");
                noSelection.setHeaderText("Add Appointment");
                Optional<ButtonType> results = noSelection.showAndWait();
                if (results.isPresent() && results.get() == ButtonType.OK)
                    noSelection.setOnCloseRequest(Event::consume);
                return;
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        // input validation: empty user ID combo
        try {
            if (userCombo.getValue() == null) {

                Alert noSelection = new Alert(Alert.AlertType.ERROR, "Please select a user ID.");
                noSelection.setTitle("Appointment Booking System");
                noSelection.setHeaderText("Add Appointment");
                Optional<ButtonType> results = noSelection.showAndWait();
                if (results.isPresent() && results.get() == ButtonType.OK)
                    noSelection.setOnCloseRequest(Event::consume);
                return;
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        LocalDate addStartDate = startDatePicker.getValue();
        LocalTime addStartTime = startCombo.getValue();
        LocalDate addEndDate = endDatePicker.getValue();
        LocalTime addEndTime = endCombo.getValue();
        LocalDateTime startDateTime = LocalDateTime.of(startDatePicker.getValue(), startCombo.getValue());
        LocalDateTime endDateTime = LocalDateTime.of(endDatePicker.getValue(), endCombo.getValue());
        int addContact = contactCombo.getSelectionModel().getSelectedItem().getContactID();
        String addType = typeCombo.getValue();
        String addTitle = titleTextfield.getText();
        String addDescription = descTextfield.getText();
        String addLocation = locationCombo.getValue();
        int addCustID = custIDCombo.getSelectionModel().getSelectedItem().getCustomerId();
        int addUserID = userCombo.getSelectionModel().getSelectedItem().getUserID();

        LocalDateTime dateTimeStart = LocalDateTime.of(addStartDate, addStartTime);
        LocalDateTime dateTimeEnd = LocalDateTime.of(addEndDate, addEndTime);

        ZonedDateTime userStart = ZonedDateTime.of(dateTimeStart, ZoneId.systemDefault());
        ZonedDateTime userEnd = ZonedDateTime.of(dateTimeEnd, ZoneId.systemDefault());
        ZonedDateTime businessStart = ZonedDateTime.of(addStartDate, LocalTime.of(8, 0),
                ZoneId.of("US/Eastern"));
        ZonedDateTime businessEnd = ZonedDateTime.of(addEndDate, LocalTime.of(22, 0),
                ZoneId.of("US/Eastern"));

        // checking for selected appointment dates/times are out of business hours
        if (userStart.isBefore(businessStart) || userStart.isAfter(businessEnd) || userEnd.isBefore(businessStart)
                || userEnd.isAfter(businessEnd)) {
            Alert businessHourConflict = new Alert(Alert.AlertType.ERROR, "Time is outside of normal business " +
                    "hours (8am-10pm EST).");
            businessHourConflict.setTitle("Appointment Booking System");
            businessHourConflict.setHeaderText("Add Appointment");
            Optional<ButtonType> results = businessHourConflict.showAndWait();
            if (results.isPresent() && results.get() == ButtonType.OK)
                businessHourConflict.setOnCloseRequest(Event::consume);
            return;
        }

        // input validation messages for conflicting appointments
        ObservableList<Appointment> getAllAppointments = AppointmentDAO.allAppointments();
        try {
            for (Appointment apptConflicts : getAllAppointments) {

                if (addCustID == apptConflicts.getCustomerID() && (dateTimeStart.isEqual(apptConflicts.getStart()) &&
                        dateTimeEnd.isEqual(apptConflicts.getEnd()))) {

                    Alert apptConflict = new Alert(Alert.AlertType.ERROR, "This customer already has an existing " +
                            "appointment for this date and time.");
                    apptConflict.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                    apptConflict.setTitle("Appointment Booking System");
                    apptConflict.setHeaderText("Add Appointment");
                    Optional<ButtonType> results = apptConflict.showAndWait();
                    if (results.isPresent() && results.get() == ButtonType.OK)
                        apptConflict.setOnCloseRequest(Event::consume);
                    return;
                }

                if (addCustID == apptConflicts.getCustomerID() && ((dateTimeStart.isBefore(apptConflicts.getStart()) ||
                        dateTimeStart.isEqual(apptConflicts.getStart()))) && ((dateTimeEnd.isAfter(apptConflicts.getEnd()) ||
                        dateTimeEnd.isEqual(apptConflicts.getEnd())))) {

                    Alert apptConflict = new Alert(Alert.AlertType.ERROR, "This meeting overlaps with the " +
                            "customer's existing appointment.");
                    apptConflict.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                    apptConflict.setTitle("Appointment Booking System");
                    apptConflict.setHeaderText("Add Appointment");
                    Optional<ButtonType> results = apptConflict.showAndWait();
                    if (results.isPresent() && results.get() == ButtonType.OK)
                        apptConflict.setOnCloseRequest(Event::consume);
                    return;
                }

                if (addCustID == apptConflicts.getCustomerID() && ((dateTimeStart.isAfter(apptConflicts.getStart()) &&
                        dateTimeStart.isBefore(apptConflicts.getEnd())))) {

                    Alert apptConflict = new Alert(Alert.AlertType.ERROR, "Start time overlaps with existing " +
                            "appointment.");
                    apptConflict.setTitle("Appointment Booking System");
                    apptConflict.setHeaderText("Add Appointment");
                    Optional<ButtonType> results = apptConflict.showAndWait();
                    if (results.isPresent() && results.get() == ButtonType.OK)
                        apptConflict.setOnCloseRequest(Event::consume);
                    return;
                }

                if (addCustID == apptConflicts.getCustomerID() && ((dateTimeEnd.isAfter(apptConflicts.getStart()) &&
                        ((dateTimeEnd.isBefore(apptConflicts.getEnd()) || dateTimeEnd.isEqual(apptConflicts.getEnd())))))) {

                    Alert apptConflict = new Alert(Alert.AlertType.ERROR, "End time overlaps with existing " +
                            "appointment.");
                    apptConflict.setTitle("Appointment Booking System");
                    apptConflict.setHeaderText("Add Appointment");
                    Optional<ButtonType> results = apptConflict.showAndWait();
                    if (results.isPresent() && results.get() == ButtonType.OK)
                        apptConflict.setOnCloseRequest(Event::consume);
                    return;
                }
            }
        } catch (Exception AppointmentConflicts) {
            System.out.println("Caught AppointmentConflicts");
        }

        try {
            newAppointment.setStart(startDateTime);
            newAppointment.setEnd(endDateTime);
            newAppointment.setContactID(addContact);
            newAppointment.setType(addType);
            newAppointment.setTitle(addTitle);
            newAppointment.setDescription(addDescription);
            newAppointment.setLocation(addLocation);
            newAppointment.setCustomerID(addCustID);
            newAppointment.setUserID(addUserID);

//            Alert addAppointmentConfirm = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to add " +
//                    "this appointment?", ButtonType.YES, ButtonType.NO);
//            addAppointmentConfirm.setTitle("Appointment Booking System");
//            addAppointmentConfirm.setHeaderText("Add Appointment");
//            Optional<ButtonType> result = addAppointmentConfirm.showAndWait();
//            if (result.isPresent() && result.get() == ButtonType.YES) {

                AppointmentDAO.addAppointment(startDateTime, endDateTime, String.valueOf(addContact),
                        addType, addTitle, addDescription, addLocation, addCustID, addUserID);
            Alert addedSuccessful = new Alert(Alert.AlertType.CONFIRMATION, "The appointment was successfully " +
                    "added.", ButtonType.OK);
            addedSuccessful.setTitle("Appointment Booking System");
            addedSuccessful.setHeaderText("Add Appointment");
            Optional<ButtonType> result = addedSuccessful.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
            toMainMenu(actionEvent);
            }

        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method navigates the user to appointments screen.
     *
     * @param actionEvent Cancel button is clicked.
     * @throws IOException The exception to throw if I/O error occurs.
     */
    public void toMainMenu(ActionEvent actionEvent) throws IOException {
        Appointments.toAppointments(actionEvent);
    }
}