package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointment;
import java.sql.*;
import java.time.format.DateTimeFormatter;

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
            Timestamp start = rs.getTimestamp("Start");
            Timestamp end = rs.getTimestamp("End");
            int customerID = rs.getInt("Customer_ID");
            int userID = rs.getInt("User_ID");

            Appointment appointment = new Appointment (appointmentID,title,description,location,contactID,type,start,end,customerID,userID);
            listOfAppointments.add(appointment);
        }
        return listOfAppointments;
    }

    /**
     * Method to query current month appointments
     */
    public static ObservableList<Appointment> currentMonth() throws SQLException{

        ObservableList<Appointment> monthOfAppointments = FXCollections.observableArrayList();
        String monthApptQuery = "SELECT * FROM appointments WHERE month(Start) = month(curdate())";
        PreparedStatement ps = JDBC.connection.prepareStatement(monthApptQuery);
        ResultSet rs = ps.executeQuery();

        while(rs.next()){
            int appointmentID = rs.getInt("Appointment_ID");
            String title = rs.getString("Title");
            String description = rs.getString("Description");
            String location = rs.getString("Location");
            int contactID = rs.getInt("Contact_ID");
            String type = rs.getString("Type");
            Timestamp start = rs.getTimestamp("Start");
            Timestamp end = rs.getTimestamp("End");
            int customerID = rs.getInt("Customer_ID");
            int userID = rs.getInt("User_ID");

            Appointment appointment = new Appointment (appointmentID,title,description,location,contactID,type,start,end,customerID,userID);
            monthOfAppointments.add(appointment);
        }
        return monthOfAppointments;
    }

    /**
     * Method to query current week appointments
     */
    public static ObservableList<Appointment> currentWeek() throws SQLException{

        ObservableList<Appointment> weekOfAppointments = FXCollections.observableArrayList();
        String weekApptQuery = "SELECT * FROM appointments WHERE yearweek(Start) = yearweek(NOW())";
        PreparedStatement ps = JDBC.connection.prepareStatement(weekApptQuery);
        ResultSet rs = ps.executeQuery();

        while(rs.next()){
            int appointmentID = rs.getInt("Appointment_ID");
            String title = rs.getString("Title");
            String description = rs.getString("Description");
            String location = rs.getString("Location");
            int contactID = rs.getInt("Contact_ID");
            String type = rs.getString("Type");
            Timestamp start = rs.getTimestamp("Start");
            Timestamp end = rs.getTimestamp("End");
            int customerID = rs.getInt("Customer_ID");
            int userID = rs.getInt("User_ID");

            Appointment appointment = new Appointment (appointmentID,title,description,location,contactID,type,start,end,customerID,userID);
            weekOfAppointments.add(appointment);
        }
        return weekOfAppointments;
    }

    /**
     * Method to insert new appointment in appointments table in database.
     */
    /* static void addAppt(Appointment addAppointment) throws SQLException {
        try {
        String addApptQuery = "INSERT INTO appointments Appointment_ID, Title, Description, Location, Type, Start, End, Create_Date, " +
                "Created_By, Last_Update, Last_Updated_By, Customer_ID, User_ID, Contact_ID VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement ps = JDBC.connection.prepareStatement(addApptQuery);
        ps.setInt(1, addAppointment.getAppointmentID());
        ps.setString(2, addAppointment.getTitle());
        ps.setString(3, addAppointment.getDescription());
        ps.setString(4, addAppointment.getLocation());
        ps.setString(5, addAppointment.getType());
        ps.setTimestamp(6, MainMenu.localDT (addAppointment.getStart()));
        ps.setTimestamp(7, MainMenu.localDT (addAppointment.getEnd()));
        ps.setString(6,);


        } catch (){

        }
    }*/

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
