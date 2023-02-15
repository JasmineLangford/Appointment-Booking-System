package model;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;

/**
 * Methods to help convert date and times.
 */
public class DateTimeUtil {

    private static final ZoneId zoneUTC = ZoneId.of("UTC");
    private static final ZoneId zoneEST = ZoneId.of("US/Eastern");
    private static final ZoneId userTimeZone = ZoneId.systemDefault();

    /**
     * Converts UTC date and time to end-user local date and time
     *
     * @param timestamp UTC timestamp
     * @return timestamp conversion
     */
    public static LocalDateTime toLocalDT(Timestamp timestamp) {
        return timestamp.toLocalDateTime().atZone(zoneUTC).withZoneSameInstant(userTimeZone).toLocalDateTime();
    }

    /**
     * Converts to UTC data and time
     *
     * @param dateTime local date and time to convert
     * @return converted time
     */
    public static Timestamp localToUTC(LocalDateTime dateTime) {
        return Timestamp.valueOf(dateTime.atZone(userTimeZone).withZoneSameInstant(zoneUTC).toLocalDateTime());
    }

    /**
     * Converts local date and time to UTC date and time - add appointment form
     *
     * @param addStartDate date picker date to be converted
     * @param addStartTime time combo box selection to be converted
     * @return converted time
     */
    public static Timestamp toUTCStartDT(LocalDate addStartDate, LocalTime addStartTime) {
        LocalDateTime localDateLocalTime = LocalDateTime.of(addStartDate,addStartTime);
        return Timestamp.valueOf(localDateLocalTime.atZone(userTimeZone).withZoneSameInstant(zoneUTC).toLocalDateTime());
    }

    /**
     * Converts local date and time to UTC date and time - add appointment form
     *
     * @param addEndDate date picker date to be converted
     * @param addEndTime time combo box selection to be converted
     * @return converted time
     */
    public static Timestamp toUTCEndDT(LocalDate addEndDate, LocalTime addEndTime) {
        LocalDateTime localDateLocalTime = LocalDateTime.of(addEndDate,addEndTime);
        return Timestamp.valueOf(localDateLocalTime.atZone(userTimeZone).withZoneSameInstant(zoneUTC).toLocalDateTime());
    }

    /**
     * Converts local date and time to UTC date and time - modify appointment form
     *
     * @param modStartDate date picker date to be converted
     * @param modStartTime time combo box selection to be converted
     * @return converted time
     */
    public static Timestamp toLocalStartDT(LocalTime modStartDate, LocalTime modStartTime) {
        LocalDateTime localDateLocalTime = LocalDateTime.of(LocalDate.from(modStartDate),modStartTime);
        return Timestamp.valueOf(localDateLocalTime.atZone(userTimeZone).toLocalDateTime());
    }


    /**
     * Converts local date and time to UTC date and time - modify appointment form
     *
     * @param modEndDate date picker date to be converted
     * @param modEndTime time combo box selection to be converted
     * @return converted time
     */
    public static Timestamp toLocalEndDT(LocalTime modEndDate, LocalTime modEndTime) {
        LocalDateTime localDateLocalTime = LocalDateTime.of(LocalDate.from(modEndDate),modEndTime);
        return Timestamp.valueOf(localDateLocalTime.atZone(userTimeZone).toLocalDateTime());
    }

    /**
     * The conversion for business hours to be referenced for adding and modifying an appointment.
     * @return
     */
    public static LocalTime toBusinessHours(LocalDateTime dateTime) {
        Timestamp estTS = Timestamp.valueOf(dateTime.atZone(userTimeZone).withZoneSameInstant(zoneEST).toLocalDateTime());
        return estTS.toLocalDateTime().toLocalTime();


        //return Timestamp.valueOf(dateTime.atZone(userTimeZone).withZoneSameInstant(zoneEST).toLocalDateTime());
    }

}
