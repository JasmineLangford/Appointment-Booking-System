package model;

import java.sql.Timestamp;

/**
 * This class is the model class for appointments.
 */
public class Appointment {

    private int appointmentID;
    private String title;
    private String description;
    private String location;
    private String type;
    private Timestamp start;
    private Timestamp end;
    private Timestamp createdDate;
    private String createdBy;
    private Timestamp lastUpdated;
    private String lastUpdatedBy;
    private int customerID;
    private int userID;
    private int contactID;


    /**
     * Constructor
     */

    public Appointment(int appointmentID, String title, String description, String location, int contactID, String type, Timestamp start, Timestamp end, int customerID, int userID) {
        this.appointmentID = appointmentID;
        this.title = title;
        this.description = description;
        this.location = location;
        this.contactID = contactID;
        this.type = type;
        this.start = start;
        this.end = end;
        this.customerID = customerID;
        this.userID = userID;
    }

    public Appointment (Appointment addScreen){

        this.appointmentID = addScreen.appointmentID;
        this.title = addScreen.title;
        this.description = addScreen.description;
        this.location = addScreen.location;
        this.type = addScreen.type;
        this.start = addScreen.start;
        this.end = addScreen.end;
        this.createdDate = addScreen.createdDate;
        this.createdBy = addScreen.createdBy;
        this.lastUpdated = addScreen.lastUpdated;
        this.lastUpdatedBy = addScreen.lastUpdatedBy;
        this.customerID = addScreen.customerID;
        this.userID = addScreen.userID;
        this.contactID = addScreen.contactID;
    }

    // Null appointment
    public Appointment() {
    }

    /**
     * @return the Appointment ID
     */
    public int getAppointmentID(){
        return appointmentID;
    }

    /**
     * @param appointmentID to set
     */
    public void setAppointmentID(int appointmentID){
        this.appointmentID = appointmentID;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the location
     */
    public String getLocation() {
        return location;
    }

    /**
     * @param location to set
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * @return the contact ID
     */
    public int getContactID() {
        return contactID;
    }

    /**
     * @param contactID to set
     */
    public void setContactID(int contactID) {
        this.contactID = contactID;
    }

   /**
    * @return the type
    */
    public String getType() {
        return type;
    }

    /**
     * @param type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return local start date and time
     */
    public Timestamp getStart() {
        return start;
    }

    /**
     * @param start date and time to set
     */
    public void setStart(Timestamp start) {
        this.start = start;

    }

    /**
     * @return local end date and time
     */
    public Timestamp getEnd() {
        return end;
    }

    /**
     * @param end date and time to set
     */
    public void setEnd(Timestamp end) {
        this.end = end;
    }

    /**
     * @return the customer ID
     */
    public int getCustomerID() {
        return customerID;
    }

    /**
     * @param customerID to set
     */
    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    /**
     * @return user ID
     */
    public int getUserID() {
        return userID;
    }

    /**
     * @param userID to set
     */
    public void setUserID(int userID) {
        this.userID = userID;
    }
}
