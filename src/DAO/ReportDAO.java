package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointment;
import model.Customer;
import model.DateTimeUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class ReportDAO {

    /*/**
     * <b>Report #1 - Total number of customer appointments by type and month</b>

    public static ObservableList<Appointment> reportByTypeMonth() throws SQLException {
        ObservableList<Customer> reportTypeMonth = FXCollections.observableArrayList();
        String reportQuery = "SELECT Type, Start FROM appointments";*/

    public static ObservableList<Appointment> appointmentsByCustomer() throws SQLException{

        ObservableList<Appointment> apptByCust = FXCollections.observableArrayList();
        String byCustomerQuery = "SELECT Appointment_ID, Title, Type, Description,Start,End,Customer_ID FROM appointments";
        PreparedStatement ps = JDBC.connection.prepareStatement(byCustomerQuery);
        ResultSet rs = ps.executeQuery();

        while(rs.next()){
            int appointmentID = rs.getInt("Appointment_ID");
            String title = rs.getString("Title");
            String description = rs.getString("Description");
            String type = rs.getString("Type");
            LocalDateTime start = DateTimeUtil.toLocalDT(rs.getTimestamp("Start"));
            LocalDateTime end = DateTimeUtil.toLocalDT(rs.getTimestamp("End"));
            int customerID = rs.getInt("Customer_ID");

            Appointment appointment = new Appointment (appointmentID,title,description,type,start,end,customerID);
            apptByCust.add(appointment);
        }
        return apptByCust;
    }
}
