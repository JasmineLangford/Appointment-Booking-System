package model;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.ZonedDateTime;

/**
 * This class is used to track user activity by recording all user log-in attempts.
 */
public class Logger {
    private static final String FILENAME = "login_activity.txt";

    public Logger() {}

    public static void trackLogin (String username, boolean success, String login_attempt) {
        try {
            FileWriter fw = new FileWriter(FILENAME,true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter(bw);
            {
                pw.println("User: " + username + "Login Attempt" + login_attempt + ZonedDateTime.now() +
                        (success ? "Successful" : "Failed"));
            }
        }catch (IOException e) {
            System.out.println("Log Error: " + e.getMessage());
        }
    }
}
