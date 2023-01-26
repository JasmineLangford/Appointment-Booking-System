package model;

import java.io.*;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * This class tracks user activity.
 */
public class Logger {

    private static final String log = "login_activity.txt";

    /**
     * This method keeps record of all login attempts
     *
     * @param username is the username used for the login attempt by the end-user
     * @param success is used based on the end-user's login attempt
     */
    public static void trackerLog (String username, DateTimeFormatter trackerFormat, Boolean success){
        try{
            BufferedWriter bw = new BufferedWriter(new FileWriter(log, true));
            bw.write ("Username: " + username + " " + "Date/Time Stamp: " + trackerFormat.format(ZonedDateTime.now())
                     + " " + "Attempt: " + (success ? "Successful" : "Failed") + "\n");
            bw.flush();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
