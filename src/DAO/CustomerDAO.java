package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointment;
import model.Customer;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

/**
 * This class contains the database queries for customers.
 */
public class CustomerDAO extends Customer {

    /**
     * This is constructor represents the customer.
     *
     * @param customerID The customer ID.
     * @param customer_name The customer name.
     */
    public CustomerDAO(int customerID,String customer_name) {
        super(customerID,customer_name);
    }

    /**
     * This method queries all customers.
     *
     * @throws SQLException The exception to throw if there is an error with database connection or with the query.
     * @return The list of all customers.
     */
    public static ObservableList<Customer> allCustomers() throws SQLException {
        ObservableList<Customer> listOfCustomers = FXCollections.observableArrayList();
        String customerQuery = "SELECT Customer_ID, Customer_Name, Address, Postal_Code, Phone, countries.Country, " +
                "first_level_divisions.Country_ID, customers.Division_ID, Division, Type FROM customers INNER JOIN " +
                "first_level_divisions ON customers.Division_ID= first_level_divisions.Division_ID INNER JOIN " +
                "countries ON first_level_divisions.Country_ID= countries.Country_ID ORDER BY Customer_ID ASC;";

        PreparedStatement ps = JDBC.connection.prepareStatement(customerQuery);
        ResultSet rs = ps.executeQuery();

        while(rs.next()){
            int customerId = rs.getInt("Customer_ID");
            String customerName = rs.getString("Customer_Name");
            String customerAddress = rs.getString("Address");
            String customerPhone = rs.getString("Phone");
            String customerCountry = rs.getString("Country");
            int countryId = rs.getInt("Country_ID");
            int divisionId = rs.getInt("Division_ID");
            String division = rs.getString("Division");
            String customerPostal = rs.getString("Postal_Code");
            String customerType = rs.getString("Type");
            Customer customer = new Customer(customerId,customerName,customerAddress,customerPhone, customerCountry,
                    countryId, divisionId,division,customerPostal,customerType);
            listOfCustomers.add(customer);
        }
        return listOfCustomers;
    }

    /**
     * This method queries corporate accounts.
     *
     * @return The list of all customers.
     * @throws SQLException The exception to throw if there is an error with database connection or with the query.
     */
    public static ObservableList<Customer> corporateAccounts() throws SQLException {
        ObservableList<Customer> listOfAccounts = FXCollections.observableArrayList();
        String accountQuery = "SELECT Customer_ID, Customer_Name, Address, Postal_Code, Phone, countries.Country, " +
                "first_level_divisions.Country_ID, customers.Division_ID, " +
                "Division FROM customers INNER JOIN first_level_divisions ON customers.Division_ID=" +
                "first_level_divisions.Division_ID INNER JOIN countries ON first_level_divisions.Country_ID=" +
                "countries.Country_ID WHERE Type = 'Corporate Account' ORDER BY Customer_ID ASC;";

        PreparedStatement ps = JDBC.connection.prepareStatement(accountQuery);
        ResultSet rs = ps.executeQuery();

        while(rs.next()){
            int customerId = rs.getInt("Customer_ID");
            String customerName = rs.getString("Customer_Name");
            String customerAddress = rs.getString("Address");
            String customerPhone = rs.getString("Phone");
            String customerCountry = rs.getString("Country");
            int countryId = rs.getInt("Country_ID");
            int divisionId = rs.getInt("Division_ID");
            String division = rs.getString("Division");
            String customerPostal = rs.getString("Postal_Code");
            Customer.CorporateAccount customer = new Customer.CorporateAccount(customerId,customerName,customerAddress,customerPhone, customerCountry,
                    countryId, divisionId,division,customerPostal);
            listOfAccounts.add(customer);
        }
        return listOfAccounts;
    }

    /**
     * This method inserts a new customer in customers table in the database.
     *
     * @param addAddress The address to add.
     * @param addCustomerName The customer name to add.
     * @param addFirstLevel The state/province to add.
     * @param addPhoneNumber The phone number to add.
     * @param addPostalCode The postal code to add.
     */
    public static void addCustomer(String addCustomerName, String addAddress, String addPhoneNumber,
                                   String addPostalCode, int addFirstLevel, String addType, int addLoyaltyPoints){

        String phoneNumber = addPhoneNumber.replaceFirst("(\\d{3})(\\d{3})(\\d+)", "$1-$2-$3");

        try {
            String addCustomerQuery = "INSERT INTO customers (Customer_Name,Address,Postal_Code,Phone," +
                    "Create_Date,Created_By,Last_Update,Last_Updated_By,Division_ID,Type,Loyalty_Points) " +
                    "VALUES (?,?,?,?,?,?,?,?,?,?,?)";
            PreparedStatement ps = JDBC.connection.prepareStatement(addCustomerQuery);
            ps.setString(1, addCustomerName);
            ps.setString(2, addAddress);
            ps.setString(3, addPostalCode);
            ps.setString(4, phoneNumber);
            ps.setString(5, null);
            ps.setString(6, null);
            ps.setString(7, null);
            ps.setString(8, null);
            ps.setInt(9, addFirstLevel);
            ps.setString(10,addType);
            ps.setInt(11,addLoyaltyPoints);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method inserts a new customer in customers table in the database.
     *
     * @param addAddress The address to add.
     * @param addFirstLevel The state/province to add.
     * @param addPhoneNumber The phone number to add.
     * @param addPostalCode The postal code to add.
     */
    public static void addCorporateAccount(String addCompanyName, String addAddress, String addPhoneNumber,
                                   String addPostalCode, int addFirstLevel, String addType){

        String phoneNumber = addPhoneNumber.replaceFirst("(\\d{3})(\\d{3})(\\d+)", "$1-$2-$3");

        try {
            String addCustomerQuery = "INSERT INTO customers (Customer_Name,Address,Postal_Code,Phone," +
                    "Create_Date,Created_By,Last_Update,Last_Updated_By,Division_ID,Type) " +
                    "VALUES (?,?,?,?,?,?,?,?,?,?)";
            PreparedStatement ps = JDBC.connection.prepareStatement(addCustomerQuery);
            ps.setString(1, addCompanyName);
            ps.setString(2, addAddress);
            ps.setString(3, addPostalCode);
            ps.setString(4, phoneNumber);
            ps.setString(5, null);
            ps.setString(6, null);
            ps.setString(7, null);
            ps.setString(8, null);
            ps.setInt(9, addFirstLevel);
            ps.setString(10,addType);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * This method modifies data that is part of an existing customer.
     *
     * @param modCustomerID The customer ID to reference.
     * @param modAddress The address to modify.
     * @param modCustomerName The customer name to modify.
     * @param modFirstLevel The customer state/province to modify.
     * @param modPhone The phone number to modify.
     * @param modPostal The postal code to modify.
     */
    public static void modifyCustomer(int modCustomerID, String modCustomerName, String modAddress, String modPhone,
                                      String modPostal, int modFirstLevel) {

        String phoneNumber = modPhone.replaceFirst("(\\d{3})(\\d{3})(\\d+)", "$1-$2-$3");

        try{
            String modCustomerQuery = "UPDATE customers SET Customer_ID = ?, Customer_Name = ?, Address = ?, " +
                    "Postal_Code = ?, Phone = ?, Create_Date = ?,Created_By = ?,Last_Update = ?,Last_Updated_By = ?," +
                    "Division_ID = ? WHERE Customer_ID = ?";
            PreparedStatement ps = JDBC.connection.prepareStatement(modCustomerQuery);
            ps.setInt(1, modCustomerID);
            ps.setString(2, modCustomerName);
            ps.setString(3, modAddress);
            ps.setString(4, modPostal);
            ps.setString(5, phoneNumber);
            ps.setString(6, null);
            ps.setString(7, null);
            ps.setString(8, null);
            ps.setString(9, null);
            ps.setInt(10, modFirstLevel);
            ps.setInt(11,modCustomerID);

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method deletes a selected customer from the customer table in the database.
     *
     * @throws SQLException The exception to throw if there are errors with database connection or the query.
     */
    public static void deleteCustomer (Customer customer) throws SQLException {

        String deleteCustomerQuery = "DELETE FROM customers WHERE Customer_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(deleteCustomerQuery);
        ps.setInt(1, customer.getCustomerId());
        ps.executeUpdate();
    }

    /**
     * The method to query appointments if the there is a matching customer ID.
     *
     * @param customerId The customer ID to look up if there is an associated appointment.
     * @throws SQLException The exception to throw if there are errors with database connection or the query.
     * @return The associated appointments with this customer.
     */
    public static ObservableList<Appointment> deleteAssociated(int customerId) throws SQLException {
        ObservableList<Appointment> associatedAppts = FXCollections.observableArrayList();
        String associatedApptQuery = "SELECT * FROM appointments WHERE EXISTS (SELECT Customer_ID FROM customers " +
                "WHERE appointments.Customer_ID = customers.Customer_ID AND Customer_ID = ?)";
        PreparedStatement ps = JDBC.connection.prepareStatement(associatedApptQuery);
        ps.setInt(1, customerId);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
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

            Appointment deleteAppt = new Appointment(appointmentID, title, description, location, contactID, type,
                    start, end, customerID, userID);
            associatedAppts.add(deleteAppt);
        }
        return associatedAppts;
    }
}