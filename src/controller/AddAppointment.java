package controller;

import DAO.AppointmentDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Appointment;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * This class is the controller for the add-appointment.fxml view.
 *
 * End-user will be able to input data into text fields and choice boxes in order to add a new appointment.
 */
public class AddAppointment implements Initializable {

    // Form fields
    @FXML
    private DatePicker startDatePicker;
    @FXML
    private ChoiceBox startTimeCombo;
    @FXML
    private ChoiceBox endTimeCombo;
    @FXML
    private DatePicker endDatePicker;
    @FXML
    private TextField apptID;
    @FXML
    private ComboBox contactCombo;
    @FXML
    private TextField typeTextfield;
    @FXML
    private TextField titleTextfield;
    @FXML
    private TextField descTextfield;
    @FXML
    private TextField locationTextfield;
    @FXML
    private TextField custIdTextfield;
    @FXML
    private TextField userIdTextfield;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Add appointment is initialized!");

        // generates unique appointment id for new appointment
        try {
            apptID.setText(String.valueOf(AppointmentDAO.allAppointments().size()+1));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * This will add the data from form fields as a new appointment to database.
     *
     @param actionEvent Save button is clicked.
     */
    public void onSaveAppt(ActionEvent actionEvent) {

        // end-user form fields
        String addStartDate = startDatePicker.getValue().format(DateTimeFormatter.ofPattern("dd MMMM yyyy zzzz"));
        String addStartTime = (String) startTimeCombo.getValue().toString();
        String addEndDate = endDatePicker.getValue().format(DateTimeFormatter.ofPattern("dd MMMM yyyy zzzz"));
        String addEndTime = (String) endTimeCombo.getValue().toString();
        //int apptId = Integer.parseInt(apptID.getText());
        String addContact = contactCombo.getValue().toString();
        String addType = typeTextfield.getText();
        String addTitle = titleTextfield.getText();
        String addDescription = descTextfield.getText();
        String addLocation = locationTextfield.getText();
        int addCustID = Integer.parseInt(custIdTextfield.getText());
        int addUserID = Integer.parseInt(userIdTextfield.getText());

        // Input Validation Messages
        try {
            if (addStartDate.isEmpty() || addEndDate.isEmpty() ||addStartTime.isEmpty() || addEndTime.isEmpty() ||
                    addContact.isEmpty() || addType.isEmpty() || addTitle.isEmpty() || addDescription.isEmpty() ||
                    addLocation.isEmpty()) {
                Alert emptyField = new Alert(Alert.AlertType.ERROR, "One or more fields are empty. Please enter a" +
                        " value in each field.");
                Optional<ButtonType> results = emptyField.showAndWait();
                if (results.isPresent() && results.get() == ButtonType.OK)
                    return;
            }
        } catch (Exception e) {
            return;
        }

        try {
            Integer.parseInt(String.valueOf(addCustID));
        } catch (NumberFormatException e) {
            Alert invalidDataType = new Alert(Alert.AlertType.ERROR, "Customer ID should be an integer.");
            Optional<ButtonType> results = invalidDataType.showAndWait();
            if (results.isPresent() && results.get() == ButtonType.OK)
                return;
        }

        try {
            Integer.parseInt(String.valueOf(addUserID));
        } catch (NumberFormatException e) {
            Alert invalidDataType = new Alert(Alert.AlertType.ERROR, "User ID should be an integer.");
            Optional<ButtonType> results = invalidDataType.showAndWait();
            if (results.isPresent() && results.get() == ButtonType.OK)
                return;
        }

         Alert addAppointmentConfirm = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to add this " +
                 "appointment?");
         Optional<ButtonType> result = addAppointmentConfirm.showAndWait();
         if(result.isPresent() && result.get() == ButtonType.YES) {


         }

    }

    /**
     * This navigates the user back to the Main Menu.
     * @param actionEvent Cancel button is clicked.
     * */
    public void onCancel(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load((Objects.requireNonNull(getClass().getResource("/view/main-menu.fxml"))));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1108, 620);
        stage.setScene(scene);
        stage.show();
        stage.centerOnScreen();
        stage.setResizable(false);
    }


}