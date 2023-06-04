package controller;

import DAO.AppointmentDAO;
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
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.Appointment;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

public class Appointments implements Initializable {
    public DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public ToggleGroup apptViewToggle;
    public RadioButton viewByMonth;
    public RadioButton viewByWeek;
    public RadioButton viewAllAppts;
    public Label currentDate;
    @FXML
    private TableView<Appointment> mainApptTable;
    @FXML
    private TableColumn<Appointment, String> apptIDCol;
    @FXML
    private TableColumn<Appointment, String> apptTitleCol;
    @FXML
    private TableColumn<Appointment, String> apptDescCol;
    @FXML
    private TableColumn<Appointment, String> apptLocationCol;
    @FXML
    private TableColumn<Appointment, String> apptContactCol;
    @FXML
    private TableColumn<Appointment, String> apptTypeCol;
    @FXML
    private TableColumn<Appointment, String> apptStartCol;
    @FXML
    private TableColumn<Appointment, String> apptEndCol;
    @FXML
    private TableColumn<Appointment, String> apptCustCol;
    @FXML
    private TableColumn<Appointment, String> apptUserCol;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // appointment table columns
        apptIDCol.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        apptTitleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        apptDescCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        apptLocationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        apptContactCol.setCellValueFactory(new PropertyValueFactory<>("contactID"));
        apptTypeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        apptCustCol.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        apptUserCol.setCellValueFactory(new PropertyValueFactory<>("userID"));

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
}


    // load appointment tableview
    public void loadApptTable() throws SQLException {
        ObservableList<Appointment> allAppointments = AppointmentDAO.allAppointments();
        mainApptTable.setItems(allAppointments);
        mainApptTable.getSelectionModel().clearSelection();
    }

    // show all appointments if All Appointments radio button selected - default selection
    public void changeToAllAppts() throws SQLException {
        ObservableList<Appointment> allAppointments = AppointmentDAO.allAppointments();
        for (Appointment ignored : allAppointments) {
            mainApptTable.setItems(allAppointments);
            mainApptTable.setPlaceholder(new Label("No appointments available"));

        }
    }

    // show appointments for current month if radio button selected
    public void changeToMonth() throws SQLException {
        ObservableList<Appointment> currentMonth = AppointmentDAO.currentMonth();
        for (Appointment ignored : currentMonth) {
            mainApptTable.setItems(currentMonth);
            mainApptTable.setPlaceholder(new Label("No appointments for this month"));
        }
    }

    // show appointments for current week if radio button selected
    public void changeToWeek() throws SQLException {
        ObservableList<Appointment> currentWeek = AppointmentDAO.currentWeek();
        for (Appointment ignored : currentWeek) {
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
        if (mainApptTable.getSelectionModel().isEmpty()) {
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
            if (result.isPresent() && result.get() == ButtonType.OK)
                mainApptTable.getSelectionModel().clearSelection();
        } else {
            Alert deleteApptConfirm = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this " +
                    "appointment?", ButtonType.YES, ButtonType.NO);
            Optional<ButtonType> result = deleteApptConfirm.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.YES) {
                try {
                    Appointment selectedAppt = mainApptTable.getSelectionModel().getSelectedItem();
                    AppointmentDAO.deleteAppt(selectedAppt);
                    loadApptTable();

                    Alert apptInfo = new Alert(Alert.AlertType.INFORMATION, "You have deleted the following appointment: " +
                            '\n' + '\n' + "Appointment ID: " + selectedAppt.getAppointmentID() + '\n' + "Type: " +
                            selectedAppt.getType(), ButtonType.OK);
                    apptInfo.showAndWait();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            mainApptTable.getSelectionModel().clearSelection();
        }
    }

    public void toAppointments(MouseEvent mouseEvent) {
    }
}
