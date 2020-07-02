package sample.Java.Util;

import javafx.scene.control.DatePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class TimeUtils {
    private static final String PATTERN = "yyyy-MM-dd";
    public static String convertTimeToString(double seconds){
        long numberOfMinutes = (long) (((seconds % 86400 ) % 3600 ) / 60);
        long numberOfSeconds = (long) (((seconds % 86400 ) % 3600 ) % 60);
        return String.valueOf(numberOfMinutes).concat(":").concat(String.valueOf(numberOfSeconds));
    }

    public static Date convertStringToDate(String dateString) throws ParseException {
        return new SimpleDateFormat(PATTERN).parse(dateString);
    }
    public static LocalDate convertToLocalDateViaInstant(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    public static String convertDateTimePickerToString(DatePicker datePicker){
        String date = datePicker.getValue().format(DateTimeFormatter.ofPattern(PATTERN));
        return date;
    }
}
