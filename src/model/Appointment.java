package model;

import java.time.LocalDateTime;

/**
 * This class is the model class for appointments.
 */
public class Appointment {

    private int appointmentID;
    private String title;
    private String description;
    private String location;
    private String type;
    private LocalDateTime start;
    private LocalDateTime end;
    private int customerID;
    private int userID;
    private int contactID;
    private String contactName;
    private int totalType;
    private String startString;

    /**
     * This constructor represents all appointment data fields.
     *
     * @param appointmentID Auto-incremented ID from the database.
     * @param contactID The contact ID.
     * @param customerID The customer ID.
     * @param description The description.
     * @param end The end date/time.
     * @param location The location.
     * @param start The start date/time.
     * @param title The title.
     * @param type The type.
     * @param userID The user ID.
     */
    public Appointment(int appointmentID, String title, String description, String location, int contactID, String type, LocalDateTime start, LocalDateTime end, int customerID, int userID) {
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

    // Null appointment
    public Appointment() {
    }

    /**
     * This is the constructor representing the report for appointments by contact.
     *
     * @param type The type.
     * @param title The title.
     * @param start The start date/time.
     * @param end The end date/time
     * @param description The description.
     * @param customerID The customer ID.
     * @param appointmentID The appointment ID.
     */
    public Appointment(int appointmentID, String title, String description, String location, String type,
                       LocalDateTime start, LocalDateTime end, int customerID) {
        this.appointmentID = appointmentID;
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.start = start;
        this.end = end;
        this.customerID = customerID;
    }

    /**
     * This is the constructor representing the report for appointments by month and type.
     *
     * @param type The type.
     * @param startString The start date/time in string format.
     * @param totalType The sum of appointment types.
     */
    public Appointment(String startString, String type, int totalType) {
        this.startString = startString;
        this.type = type;
        this.totalType = totalType;
    }

    /**
     * This is the constructor representing contact data used for the combo box.
     * @param contactName The contact name.
     * @param contactID The contact ID.
     * */
    public Appointment(int contactID, String contactName){
        this.contactID = contactID;
        this.contactName =  contactName;
    }

    /**
     * Gets the appointment ID.
     *
     * @return An integer representing the appointment ID.
     */
    public int getAppointmentID(){
        return appointmentID;
    }

    /**
     * Sets the appointment ID.
     *
     * @param appointmentID An integer containing the appointment ID.
     */
    public void setAppointmentID(int appointmentID){
        this.appointmentID = appointmentID;
    }

    /**
     * Gets the title.
     *
     * @return A string representing the title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title.
     *
     * @param title A string containing the title.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets the description.
     *
     * @return A string representing the description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description.
     *
     * @param description A string containing the description.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the location.
     *
     * @return A string representing the location.
     */
    public String getLocation() {
        return location;
    }

    /**
     * Sets the location.
     *
     * @param location A string containing the location.
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Gets the contact ID.
     *
     * @return An integer representing the contact ID.
     */
    public int getContactID() {
        return contactID;
    }

    /**
     * Sets the contact ID.
     *
     * @param contactID An integer containing the contact ID.
     */
    public void setContactID(int contactID) {
        this.contactID = contactID;
    }

    /**
     * Gets the contact name.
     *
     * @return A string representing the contact name.
     */
    public String getContactName() {
        return contactName;
    }

    /**
     * Sets the contact name.
     *
     * @param contactName A string containing the contact name.
     */
    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

   /**
    * Gets the type.
    *
    * @return A string representing the type.
    */
    public String getType() {
        return type;
    }

    /**
     * Sets the type.
     *
     * @param type A string containing the type.
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Gets the start.
     *
     * @return The LocalDateTime representing start date and time.
     */
    public LocalDateTime getStart() {
        return start;
    }

    /**
     * Sets the start.
     *
     * @param start The LocalDateTime containing the date and time.
     */
    public void setStart(LocalDateTime start) {
        this.start = start;

    }

    /**
     * Gets the end.
     *
     * @return The LocalDateTime representing end date and time.
     */
    public LocalDateTime getEnd() {
        return end;
    }

    /**
     * Sets the end.
     *
     * @param end The LocalDateTime containing end date and time.
     */
    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    /**
     * Gets the customer ID.
     *
     * @return The integer representing the customer ID.
     */
    public int getCustomerID() {
        return customerID;
    }

    /**
     * Sets the customer ID.
     *
     * @param customerID The integer containing the customer ID.
     */
    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    /**
     * Gets the user ID.
     *
     * @return The integer representing the user ID.
     */
    public int getUserID() {
        return userID;
    }

    /**
     * Sets the user ID.
     *
     * @param userID The integer containing the user ID.
     */
    public void setUserID(int userID) {
        this.userID = userID;
    }

    /**
     * Gets the total type.
     *
     * @return The integer representing total type.
     */
    public int getTotalType() {
        return totalType;
    }

    /**
     * Sets the total type.
     *
     * @param totalType The integer containing total type.
     */
    public void setTotalType(int totalType) {
        this.totalType = totalType;
    }

    /**
     * Gets the start.
     *
     * @return The string representing the start date and time.
     */
    public String getStartString() {
        return startString;
    }

    /**
     * Sets the start.
     *
     * @param startString The string containing the start date and time.
     */
    public void setStartString(String startString) {
        this.startString = startString;
    }

}
