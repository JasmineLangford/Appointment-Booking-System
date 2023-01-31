package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointment;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

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
    /*public void addAppt(Appointment appointment) throws SQLException {
        try {
        String addApptQuery = "INSERT INTO appointments (Appointment_ID, Title, Description, Location, Type, Start, End, " +
                "Customer_ID, User_ID, Contact_ID) VALUES (?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement ps = JDBC.connection.prepareStatement(addApptQuery);
        ps.setInt(1, appointment.getAppointmentID());
        ps.setString(2, appointment.getTitle());
        ps.setString(3, appointment.getDescription());
        ps.setString(4, appointment.getLocation());
        ps.setString(5, appointment.getType());
        ps.setTimestamp(6, localDT.appointment.getStart());
        ps.setTimestamp(7, localDT.appointment.getEnd());
        ps.setString(8, appointment.get);
        ps.setString(9,);
        ps.setString(10,);


        } catch (){

        }
    }*/

    /**
     * Method to query for 15 minute appointment alert.
     */

    public static Appointment appointmentAlert() {
        LocalDateTime now = LocalDateTime.now();
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zonedDT = now.atZone(zoneId);
        LocalDateTime localDT = zonedDT.withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime();
        LocalDateTime fifteenMinTime = localDT.plusMinutes(15);

        String userLoggedIn = UserDAO.getUserLogin().getUsername();

        Appointment alertAppointments;

        try {
        String alertFifteenQuery = "SELECT * FROM appointments,users WHERE appointments.Start BETWEEN" + localDT + fifteenMinTime + "AND users.User_Name=" + userLoggedIn;
        PreparedStatement ps = JDBC.connection.prepareStatement(alertFifteenQuery);
        ResultSet rs = ps.executeQuery();

        if(rs.next()) {
            alertAppointments = new Appointment(
            rs.getInt("Appointment_ID"),
            rs.getTimestamp("Start"));

            return alertAppointments;

        }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
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
