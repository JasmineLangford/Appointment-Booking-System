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

    public static Timestamp toUTCStartDT(LocalDate addStartDate, LocalTime addStartTime) {
        LocalDateTime localDateLocalTime = LocalDateTime.of(addStartDate,addStartTime);
        return Timestamp.valueOf(localDateLocalTime.atZone(userTimeZone).withZoneSameInstant(zoneUTC).toLocalDateTime());
    }

    public static Timestamp toUTCEndDT(LocalDate addEndDate, LocalTime addEndTime) {
        LocalDateTime localDateLocalTime = LocalDateTime.of(addEndDate,addEndTime);
        return Timestamp.valueOf(localDateLocalTime.atZone(userTimeZone).withZoneSameInstant(zoneUTC).toLocalDateTime());
    }

    public static Timestamp toLocalStartDT(LocalTime modStartDate, LocalTime modStartTime) {
        LocalDateTime localDateLocalTime = LocalDateTime.of(LocalDate.from(modStartDate),modStartTime);
        return Timestamp.valueOf(localDateLocalTime.atZone(userTimeZone).toLocalDateTime());
    }

    public static Timestamp toLocalEndDT(LocalTime modEndDate, LocalTime modEndTime) {
        LocalDateTime localDateLocalTime = LocalDateTime.of(LocalDate.from(modEndDate),modEndTime);
        return Timestamp.valueOf(localDateLocalTime.atZone(userTimeZone).toLocalDateTime());
    }


}
