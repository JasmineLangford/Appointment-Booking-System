package model;

import java.sql.Timestamp;
import java.time.LocalDateTime;
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

}
