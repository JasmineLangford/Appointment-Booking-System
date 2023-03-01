package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointment;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This Class for contact query
 *
 * Extends from Appointment model class
 */
public class ContactDAO extends Appointment {


    // override data type for combo box selection display
    @Override
    public String toString(){
        return (getContactID() + " " + getContactName());
    }

    /**
     * Constructor
     */
    public ContactDAO (int contactId, String contactName){
        super(contactId, contactName);
    }

    /**
     * Method to query all contact selections for combo box
     *
     * @return list of contacts
     */
    public static ObservableList<ContactDAO> allContacts() throws SQLException {
        ObservableList<ContactDAO> contactList = FXCollections.observableArrayList();
        String contactQuery = "SELECT Contact_ID, Contact_Name FROM contacts";
        PreparedStatement ps = JDBC.connection.prepareStatement(contactQuery);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            int contactId = rs.getInt("Contact_ID");
            String contactName = rs.getString("Contact_Name");

            ContactDAO contacts = new ContactDAO(contactId, contactName);
            contactList.add(contacts);
        }
        return contactList;
    }
}


