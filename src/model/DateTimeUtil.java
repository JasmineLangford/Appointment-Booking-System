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

    /**
     * Converts UTC date and time to end-user local date and time
     *
     * @param timestamp UTC timestamp
     * @return timestamp conversion
     */
    public static LocalDateTime toLocalDT(Timestamp timestamp) {
        ZoneId zoneUTC = ZoneId.of("UTC");
        ZoneId userTimeZone = ZoneId.systemDefault();
        return timestamp.toLocalDateTime().atZone(zoneUTC).withZoneSameInstant(userTimeZone).toLocalDateTime();
    }

    /**
     * Converts to UTC data and time
     *
     * @param dateTime local date and time to convert
     * @return converted time
     */
    public static Timestamp localToUTC(LocalDateTime dateTime) {
        ZoneId zoneUTC = ZoneId.of("UTC");
        ZoneId localTimeZone = ZoneId.systemDefault();
        return Timestamp.valueOf(dateTime.atZone(localTimeZone).withZoneSameInstant(zoneUTC).toLocalDateTime());
    }

    public static Timestamp toUTCStartDT(LocalDate addStartDate, LocalTime addStartTime) {
        ZoneId zoneUTC = ZoneId.of("UTC");
        ZoneId localTimeZone = ZoneId.systemDefault();
        LocalDateTime localDateLocalTime = LocalDateTime.of(addStartDate,addStartTime);
        return Timestamp.valueOf(localDateLocalTime.atZone(localTimeZone).withZoneSameInstant(zoneUTC).toLocalDateTime());
    }

    public static Timestamp toUTCEndDT(LocalDate addEndDate, LocalTime addEndTime) {
        ZoneId zoneUTC = ZoneId.of("UTC");
        ZoneId localTimeZone = ZoneId.systemDefault();
        LocalDateTime localDateLocalTime = LocalDateTime.of(addEndDate,addEndTime);
        return Timestamp.valueOf(localDateLocalTime.atZone(localTimeZone).withZoneSameInstant(zoneUTC).toLocalDateTime());
    }
}
