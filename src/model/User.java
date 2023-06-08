package model;

/**
 * This class represents the user.
 */
public class User {

    public User() {

    }

    private String username;
    private String password;
    private int userID;
    private String userFirstName;
    private String userLastName;

    /**
     * This is the constructor for user credentials for login.
     *
     * @param password The user's password.
     * @param userName The user's username.
     * @param userID The user ID.
     */
    public User (int userID, String userName, String password, String userFirstName, String userLastName){
        this.userID = userID;
        this.username = userName;
        this.password = password;
        this.userFirstName = userFirstName;
        this.userLastName = userLastName;
    }
    public User(String userFirstName, String userLastName) {
        this.userFirstName = userFirstName;
        this.userLastName = userLastName;
    }
    /**
     * Gets the username.
     *
     * @return The string representing the username.
     */
    public String getUsername(){
        return username;
    }

    /**
     * Sets the username.
     *
     * @param username The string containing the username.
     */
    public void setUsername(String username){
        this.username = username;
    }

    /**
     * Gets the password.
     *
     * @return The string representing the password.
     */
    public String getPassword(){
        return password;
    }

    /**
     * Sets the password.
     *
     * @param password The string containing the password.
     */
    public void setPassword(String password){
        this.password = password;
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
     * @param userID The string containing the user ID.
     */
    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getUserFirstName() {
        return userFirstName;
    }

    public void setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
    }

    public String getUserLastName() {
        return userLastName;
    }

    public void setUserLastName(String userLastName) {
        this.userLastName = userLastName;
    }
}
