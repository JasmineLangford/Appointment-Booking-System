package model;

import java.sql.Timestamp;
import java.time.LocalDateTime;
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
     * Converts local date and time to UTC date and time.
     *
     * @param startDateTime The start date and time to be converted.
     * @return The converted date and time.
     */
    public static Timestamp toUTCStartDT(LocalDateTime startDateTime) {
        return Timestamp.valueOf(startDateTime.atZone(userTimeZone).withZoneSameInstant(zoneUTC).toLocalDateTime());
    }

    /**
     * Converts local date and time to UTC date and time.
     *
     * @param endDateTime The end date and time to be converted.
     * @return The converted date and time.
     */
    public static Timestamp toUTCEndDT(LocalDateTime endDateTime) {
        return Timestamp.valueOf(endDateTime.atZone(userTimeZone).withZoneSameInstant(zoneUTC).toLocalDateTime());
    }
}
