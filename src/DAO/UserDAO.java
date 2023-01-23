package DAO;

import model.Logger;
import model.User;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * This class queries the database for valid users to be able to log into the application from the login screen.
 *
 * Logger is used to track user activity and records login attempts.
 */

public class UserDAO {

    private static User currentUser;

    // checks to see if end-user is valid based on username/password stored in database
    public static Boolean validateUser(String username, String password) {
        try {
            String loginQuery = "SELECT * FROM users WHERE User_Name='" + username + "' AND password='" + password + "'";
            Statement statement = JDBC.connection.createStatement();
            ResultSet rs = statement.executeQuery(loginQuery);

            if(rs.next()) {
                currentUser = new User();
                currentUser.setUsername(rs.getString("User_Name"));
                statement.close();

                Logger.trackLogin(username, true, "Login Attempt");
                return true;
            } else {
                Logger.trackLogin(username, false, "Login Attempt");
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Login Error: " + e.getMessage());
            return false;
        }
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User currentUser) {
        UserDAO.currentUser = currentUser;
    }
}
