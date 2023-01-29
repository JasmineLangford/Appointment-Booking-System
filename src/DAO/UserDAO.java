package DAO;

import model.Logger;
import model.User;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.format.DateTimeFormatter;

/**
 * This class queries the database for valid users to be able to log into the application from the login screen.
 *
 * Logger is used to track user activity and records login attempts.
 */

public class UserDAO {

    private static User userLogin;

    // checks to see if end-user is valid based on username/password stored in database
    public static Boolean validateUser(String username, String password) {
        try {
            String loginQuery = "SELECT * FROM users WHERE User_Name='" + username + "' AND password='" + password + "'";
            Statement statement = JDBC.connection.createStatement();
            ResultSet rs = statement.executeQuery(loginQuery);

            if(rs.next()) {
                userLogin = new User();
                userLogin.setUsername(rs.getString("User_Name"));

                // logs end-user login attempt as successful
                Logger.trackerLog(username,DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"),true);
                return true;
            } else {
                // logs end-user login attempt as failed
                Logger.trackerLog(username, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"),false);
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Login Error: " + e.getMessage());
            return false;
        }
    }

    public static User getUserLogin() {
        return userLogin;
    }

    public static void setUserLogin(User userLogin) {
        UserDAO.userLogin = userLogin;
    }
}
