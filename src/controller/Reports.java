package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import java.io.IOException;

public class Reports {

    // Schedules by Contact Table
    @FXML
    private TableView apptReportTable;
    @FXML
    private TableColumn apptIdCol;
    @FXML
    private TableColumn apptTitleCol;
    @FXML
    private TableColumn apptDescCol;
    @FXML
    private TableColumn apptTypeCol;
    @FXML
    private TableColumn apptStartCol;
    @FXML
    private TableColumn apptEndCol;
    @FXML
    private TableColumn custIdCol;

    // Appointment Types by Month Table
    @FXML
    private TableView apptByMonth;
    @FXML
    private TableColumn apptMonthCol;
    @FXML
    private TableColumn typeByMonthCol;
    @FXML
    private TableColumn totalTypeApptCol;

    // Customer Appointments by State Province Table
    @FXML
    private TableView apptByState;

    @FXML
    private TableColumn stateCol;
    @FXML
    private TableColumn totalStateApptCol;


    /**
     * This navigates the user back to the Main Menu.
     * @param actionEvent Back to Main button is clicked.
     * */
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
