package play;

import play.templates.JavaExtensions;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CustomExtensions extends JavaExtensions {
    public static String asDate(LocalDateTime time) {
        return time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    public static String asWeekday(LocalDateTime time) {
        return time.format(DateTimeFormatter.ofPattern("EEEE"));
    }
}
