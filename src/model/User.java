package model;

/**
 * Model class for user.
 */
public class User {

    public User() {

    }
    // override format for combo box selection display
    @Override
    public String toString(){
        return (getUserID() + " " + getUsername());
    }

    private String username;
    private String password;
    private int userID;

    public User (int userID, String userName, String password){
        this.userID = userID;
        this.username = userName;
        this.password = password;

    }
    /**
     * @return the username
     */
    public String getUsername(){
        return username;
    }

    /**
     * @param username to set
     */
    public void setUsername(String username){
        this.username = username;
    }

    /**
     * @return the password
     */
    public String getPassword(){
        return password;
    }

    /**
     * @param password to set
     */
    public void setPassword(String password){
        this.password = password;
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
