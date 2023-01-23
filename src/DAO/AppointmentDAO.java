package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointment;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

/**
 * This class contains the database queries for appointments.
 */
public class AppointmentDAO {

    /**
     * Method to query all appointments from appointments table in database.
     */
    public static ObservableList<Appointment> allAppointments() throws SQLException{
        ObservableList<Appointment> listOfAppointments = FXCollections.observableArrayList();
        String apptQuery = "SELECT * FROM appointments";
        PreparedStatement ps = JDBC.connection.prepareStatement(apptQuery);
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

    /**
     * Method to query deletion from appointments table in database.
     */
    public static void deleteAppt(Appointment appointment) throws SQLException {

        String deleteApptQuery = "DELETE FROM appointments WHERE Appointment_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(deleteApptQuery);
        ps.setInt(1, appointment.getAppointmentID());
        ps.executeUpdate();
    }
}
