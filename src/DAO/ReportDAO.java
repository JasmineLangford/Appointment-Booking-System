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
import java.time.format.DateTimeFormatter;

public class ReportDAO {
    /**
     * <b>Report #1 - Total number of customer appointments by type and month</b>
     */
    public static ObservableList<Appointment> appointmentsByMonthType() throws SQLException{

        ObservableList<Appointment> apptByMonthType = FXCollections.observableArrayList();
        String byMonthQuery = "SELECT Type,count(Type) AS Total_Type FROM appointments GROUP BY Type;";
        PreparedStatement monthPS = JDBC.connection.prepareStatement(byMonthQuery);
        ResultSet monthResult = monthPS.executeQuery();

        String byTypeQuery= "SELECT monthname(Start) AS Month FROM appointments ORDER BY month(Start) asc";
        PreparedStatement typePS = JDBC.connection.prepareStatement(byTypeQuery);
        ResultSet typeResult = typePS.executeQuery();

        while(monthResult.next()){
            LocalDateTime start = DateTimeUtil.toLocalDT(monthResult.getTimestamp("Start"));
            String type = typeResult.getString("Type");
            int totalType = typeResult.getInt("Total_Type");
            apptByMonthType.add(new Appointment(start,type,totalType));

        }
        while(typeResult.next()){
            LocalDateTime start = DateTimeUtil.toLocalDT(monthResult.getTimestamp("Start"));
            String type = typeResult.getString("Type");
            int totalType = typeResult.getInt("Total_Type");
            apptByMonthType.add(new Appointment(start,type,totalType));
        }
        return apptByMonthType;
    }

    /**
     * <b>Report #2 - Schedule for each contact in the organization</b>
     *
     * @return all appointments associated with the selected contact
     */
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

    /**
     * <b>Report #3 - Total number of appointments for each division</b>
     */
    public static ObservableList<Customer> customersByDivision() throws SQLException{

        ObservableList<Customer> customersByDivision = FXCollections.observableArrayList();
        String byDivision = "SELECT first_level_divisions.Country_ID, customers.Division_ID, Division AS State_Province, COUNT(first_level_divisions.Division_ID) AS Total_Customers FROM customers INNER JOIN first_level_divisions ON customers.Division_ID = " +
                "first_level_divisions.Division_ID INNER JOIN countries ON first_level_divisions.Country_ID = " +
                "countries.Country_ID GROUP BY Division_ID";
        PreparedStatement ps = JDBC.connection.prepareStatement(byDivision);
        ResultSet rs = ps.executeQuery();

        while(rs.next()){
            int country_ID = rs.getInt("Country_ID");
            String division =  rs.getString("State_Province");
            int division_ID = rs.getInt("Division_ID");
            int totalCustomers = rs.getInt("Total_Customers");

            Customer customer = new Customer(country_ID,division,division_ID,totalCustomers);
            customersByDivision.add(customer);
        }
        return customersByDivision;
    }

}
