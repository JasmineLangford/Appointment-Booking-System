package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointment;
import model.DateTimeUtil;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;


/**
 * This class contains the database queries for appointments.
 */
public class AppointmentDAO {
    /**
     * Method to query all appointments from appointments table in database.
     */
    public static ObservableList<Appointment> allAppointments() throws SQLException{

        ObservableList<Appointment> listOfAppointments = FXCollections.observableArrayList();
        String apptQuery = "SELECT * FROM appointments ORDER BY Appointment_ID ASC";
        PreparedStatement ps = JDBC.connection.prepareStatement(apptQuery);
        ResultSet rs = ps.executeQuery();

        while(rs.next()){
            int appointmentID = rs.getInt("Appointment_ID");
            String title = rs.getString("Title");
            String description = rs.getString("Description");
            String location = rs.getString("Location");
            int contactID = rs.getInt("Contact_ID");
            String type = rs.getString("Type");
            LocalDateTime start = DateTimeUtil.toLocalDT(rs.getTimestamp("Start"));
            LocalDateTime end = DateTimeUtil.toLocalDT(rs.getTimestamp("End"));
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
            LocalDateTime start = DateTimeUtil.toLocalDT(rs.getTimestamp("Start"));
            LocalDateTime end = DateTimeUtil.toLocalDT(rs.getTimestamp("End"));
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
            LocalDateTime start = DateTimeUtil.toLocalDT(rs.getTimestamp("Start"));
            LocalDateTime end = DateTimeUtil.toLocalDT(rs.getTimestamp("End"));
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
    public static void addAppointment(LocalDate addStartDate, LocalTime addStartTime, LocalDate addEndDate, LocalTime addEndTime,
                                      int apptId, String addContact, String addType, String addTitle, String addDescription, String
                                              addLocation, int addCustID, int addUserID) throws SQLException {
        try {
        String addApptQuery = "INSERT INTO appointments (Appointment_ID, Title, Description, Location, Type, Start, " +
                "End, Create_Date, Created_By, Last_Update, Last_Updated_By, Customer_ID, User_ID, Contact_ID) VALUES (?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement ps = JDBC.connection.prepareStatement(addApptQuery);
        ps.setInt(1, apptId);
        ps.setString(2, addTitle);
        ps.setString(3, addDescription);
        ps.setString(4, addLocation);
        ps.setString(5, addType);
        ps.setTimestamp(6, DateTimeUtil.toUTCStartDT(addStartDate,addStartTime));
        ps.setTimestamp(7, DateTimeUtil.toUTCEndDT(addEndDate,addEndTime));
        ps.setString(8, null);
        ps.setString(9,null);
        ps.setString(10,null);
        ps.setString(11,null);
        ps.setInt(12,addCustID);
        ps.setInt(13,addUserID);
        ps.setString(14,addContact);

        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * Method to query for 15 minute appointment alert.
     */

    public static Appointment appointmentAlert() {

        // check for appointments in UTC
        /*Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zonedDT = timestamp.toLocalDateTime().atZone(zoneId);
        Timestamp dateTimeUTC = Timestamp.valueOf(zonedDT.withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime());
        LocalDateTime timeUTC = zonedDT.withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime();
        Timestamp plusFifteenUTC = Timestamp.valueOf(timeUTC.plusMinutes(15));

        Appointment alertAppointments;

        try {
            String alertFifteenQuery = "SELECT * FROM appointments WHERE Start >='" + dateTimeUTC + "'AND'" + plusFifteenUTC + "'";
            PreparedStatement ps = JDBC.connection.prepareStatement(alertFifteenQuery);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                alertAppointments = new Appointment(
                        rs.getInt("Appointment_ID"),
                        rs.getTimestamp("Start"));

                return alertAppointments;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }*/
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
