package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import model.Appointment;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class AppointmentDAO {
    public static ObservableList<Appointment> getAllAppointments() throws SQLException{
        ObservableList<Appointment> listOfAppointments = FXCollections.observableArrayList();
        String sql = "SELECT * from appointments";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while(rs.next()){
            int appointmentID = rs.getInt("Appointment_ID");
            String title = rs.getString("Title");
            String description = rs.getString("Description");
            String location = rs.getString("Location");
            int contactID = rs.getInt("Contact_ID");
            String type = rs.getString("Type");
            LocalDateTime start = rs.getTimestamp("Start").toLocalDateTime();
            LocalDateTime end = rs.getTimestamp("End").toLocalDateTime();
            int customerID = rs.getInt("Customer_ID");
            int userID = rs.getInt("User_ID");

            Appointment appointment = new Appointment(appointmentID,title,description,location,contactID,type,start,end,customerID,userID);
            listOfAppointments.add(appointment);
        }
        return listOfAppointments;
    }
}
