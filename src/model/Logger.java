package model;

import java.io.*;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * This class tracks user activity by recording all user log-in attempts (successful and unsuccessful), date and
 * timestamps to a text file. Each new record is appended to the last record.
 */
public class Logger {

    private static final String log = "login_activity.txt";

    /**
     * This method sets up the user activity tracker.
     *
     * @param username is the username used for the login attempt by the end-user
     * @param trackerFormat is used to format date and time
     * @param success is used based on the end-user's login attempt
     */
    public static void trackerLog (String username, DateTimeFormatter trackerFormat, Boolean success){
        try{
            BufferedWriter bw = new BufferedWriter(new FileWriter(log, true));
            bw.write ("Username: " + username + " " + "Date/Time Stamp: " + trackerFormat.format(ZonedDateTime.now())
                     + " " + "Attempt: " + (success ? "Successful" : "Failed") + "\n");
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
