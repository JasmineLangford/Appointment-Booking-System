package DAO;

import model.User;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.ZonedDateTime;

/**
 * This class queries the database for valid users to be able to log into the application from the login screen.
 */

public class UserDAO {

    private static User currentUser;
    private static final String FILENAME = "login_activity.txt";


    public static void logger (String username, boolean success) {
        try {
            FileWriter fw = new FileWriter(FILENAME,true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter(bw);
            {
            pw.println(ZonedDateTime.now() + "User: " + username + "Login_Attempt: " +(success ? "Successful" :
                    "Failed"));
            }
        }catch (IOException e) {
            System.out.println("Log Error: " + e.getMessage());
            }
        }

    public static Boolean validateUser(String username, String password) {
        try {
            String loginQuery = "SELECT * FROM users WHERE User_Name='" + username + "' AND password='" + password + "'";
            Statement statement = JDBC.connection.createStatement();
            ResultSet rs = statement.executeQuery(loginQuery);

            if(rs.next()) {
                currentUser = new User();
                currentUser.setUsername(rs.getString("User_Name"));
                statement.close();

                logger(username, true);
                return true;
            } else {
                logger(username, false);
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
