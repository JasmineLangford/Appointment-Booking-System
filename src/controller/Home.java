package controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

// TODO: rewrite Javadocs

/**
 * This class is the controller for home.fxml.
 * End-user can also navigate to the reports screen.
 */
public class Home implements Initializable {
    @FXML
    public Label currentDate;

    /**
     * This method takes the end-user to the main menu from another screen.
     *
     * @param actionEvent When button is clicked.
     * @throws IOException The exception to throw if I/O error occurs.
     */
    public static void toHomeScreen(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load((Objects.requireNonNull(Home.class.getResource("/view/home.fxml"))));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1108, 620);
        scene.setFill(Color.TRANSPARENT);
        root.setStyle("-fx-background-radius: 30px 30px 30px 30px;");
        stage.setScene(scene);
        stage.show();
        stage.centerOnScreen();
        stage.setResizable(false);
    }

    /**
     * This method launches the home screen.
     */

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Main Menu initialized!");

        LocalDate current = LocalDate.now();
        DateTimeFormatter formatCurrentDate = DateTimeFormatter.ofPattern("MMMM d, yyyy");
        String formattedDate = current.format(formatCurrentDate);
        currentDate.setText("Today," + " " + formattedDate);
    }

    /**
     * This method takes the end-user to view reports on the Reports screen.
     *
     * @param actionEvent Reports button is clicked (located on the right panel).
     * @throws IOException The exception to throw if I/O error occurs.
     */
    public void toReports(MouseEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load((Objects.requireNonNull(getClass().getResource("/view/reports.fxml"))));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1108, 620);
        stage.setScene(scene);
        stage.show();
        stage.centerOnScreen();
        stage.setResizable(false);
    }

    /**
     * This method closes the application and an alert will ask the end-user to confirm close.
     */
    public void closeApp() {
        exitApp();
    }

    public void exitApp() {
        Alert closeConfirm = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to close the program?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = closeConfirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.YES) {
            Platform.exit();
            System.out.println("Program Closed");
        }
    }

    public void toAppointments(MouseEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load((Objects.requireNonNull(getClass().getResource("/view/appointments.fxml"))));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1108, 620);
        stage.setScene(scene);
        stage.show();
        stage.centerOnScreen();
        stage.setResizable(false);
    }

    public void toCustomers(MouseEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load((Objects.requireNonNull(getClass().getResource("/view/customers.fxml"))));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1108, 620);
        stage.setScene(scene);
        stage.show();
        stage.centerOnScreen();
        stage.setResizable(false);
    }
}