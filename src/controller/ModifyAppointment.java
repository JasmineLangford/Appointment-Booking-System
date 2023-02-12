package controller;

import DAO.AppointmentDAO;
import DAO.ContactDAO;
import DAO.CustomerDAO;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Appointment;
import model.Contact;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * This class is the controller for modifying an appointment.
 *
 * End-user can modify all fields except appointment ID.
 */
public class ModifyAppointment implements Initializable {

    // form fields
    @FXML
    private TextField apptId;
    @FXML
    private DatePicker startDatePicker;
    @FXML
    private ComboBox<LocalTime> startCombo;
    @FXML
    private DatePicker endDatePicker;
    @FXML
    private ComboBox<LocalTime> endCombo;
    @FXML
    private TextField locationText;
    @FXML
    private TextField typeText;
    @FXML
    private TextField titleText;
    @FXML
    private TextField descText;
    @FXML
    private TextField customerId;
    @FXML
    private TextField userId;
    @FXML
    private ComboBox<ContactDAO> contactCombo;

    ObservableList<ContactDAO> contacts = ContactDAO.allContacts();

    Appointment modAppointment = new Appointment();

    public ModifyAppointment() throws SQLException {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Modify Appointment initialized.");

        contactCombo.setItems(contacts);
    }

    /**
     * This populates the date from the selected appointment tableview from Main Menu.
     */
    public void sendAppointment(Appointment appointment) {
        modAppointment = appointment;

        apptId.setText(String.valueOf(modAppointment.getAppointmentID()));
        startDatePicker.setValue(modAppointment.getStart().toLocalDate());
        startCombo.setValue(modAppointment.getStart().toLocalTime());
        endDatePicker.setValue(modAppointment.getEnd().toLocalDate());
        endCombo.setValue(modAppointment.getEnd().toLocalTime());
        //contactCombo.setValue(modAppointment.getContactName());
        locationText.setText(modAppointment.getLocation());
        typeText.setText(modAppointment.getType());
        titleText.setText(modAppointment.getTitle());
        descText.setText(modAppointment.getDescription());
        customerId.setText(String.valueOf(modAppointment.getCustomerID()));
        userId.setText(String.valueOf(modAppointment.getUserID()));
    }

    /**
     * Modified fields will be updated in the database and reflect on appointment table on main menu.
     *
     * @param actionEvent Save button is clicked.
     */
    public void onSaveModAppointment(ActionEvent actionEvent) {
        LocalDate modStartDate = startDatePicker.getValue();
        LocalTime modStartTime = startCombo.getValue();
        LocalDate modEndDate = endDatePicker.getValue();
        LocalTime modEndTime = endCombo.getValue();
        int modApptId = Integer.parseInt(apptId.getText());
        int modContact = contactCombo.getSelectionModel().getSelectedItem().getContactId();
        String modType = typeText.getText();
        String modTitle = titleText.getText();
        String modDescription = descText.getText();
        String modLocation = locationText.getText();
        int modCustomerID = Integer.parseInt(customerId.getText());
        int modUserID = Integer.parseInt(userId.getText());

        try {
            startDatePicker.getValue();
            endDatePicker.getValue();
        } catch (Exception e) {
            Alert emptyPicker = new Alert(Alert.AlertType.ERROR, "Please select a start date.");
            emptyPicker.showAndWait();
        }

        if (startCombo.getSelectionModel().isEmpty() || endCombo.getSelectionModel().isEmpty() ||
                contactCombo.getSelectionModel().isEmpty() || modType.isEmpty() || modTitle.isEmpty() ||
                modDescription.isEmpty() || modLocation.isEmpty() || customerId.getText().isEmpty() ||
                userId.getText().isEmpty()) {

            Alert emptyPicker = new Alert(Alert.AlertType.ERROR, "One or more fields are empty. Please enter a" +
                    " value in each field.");
            emptyPicker.showAndWait();
        }

        try {
            modAppointment.setAppointmentID(modApptId);
            modAppointment.setStart(LocalDateTime.from(modStartDate));
            modAppointment.setStart(LocalDateTime.from(modStartTime));
            modAppointment.setEnd(LocalDateTime.from(modEndDate));
            modAppointment.setEnd(LocalDateTime.from(modEndTime));
            modAppointment.setContactID(modContact);
            modAppointment.setType(modType);
            modAppointment.setTitle(modTitle);
            modAppointment.setDescription(modDescription);
            modAppointment.setLocation(modLocation);
            modAppointment.setCustomerID(modCustomerID);
            modAppointment.setUserID(modUserID);

            Alert modAppointment = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to modify this " +
                    "appointment?", ButtonType.YES, ButtonType.NO);
            Optional<ButtonType> result = modAppointment.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.YES) {

                //AppointmentDAO.modifyAppointment(modApptId,modStartDate,modStartTime,modEndDate,modEndTime,modContact,
                // modType,modTitle,modDescription,modLocation,modCustomerID,modUserID);
                toMainMenu(actionEvent);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This navigates the end-user back to the Main Menu.
     *
     * @param actionEvent Cancel button is clicked.
     */
    public void toMainMenu(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(AddAppointment.class.getResource("/view/main-menu.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load(), 1108, 620);
        stage.setScene(scene);
        stage.show();
        stage.centerOnScreen();
        stage.setResizable(false);
    }
}
