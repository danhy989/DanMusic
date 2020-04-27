package sample.Java.Util;

import java.util.concurrent.TimeUnit;

public class TimeUtils {
    public static String convertTimeToString(double seconds){
        long numberOfMinutes = (long) (((seconds % 86400 ) % 3600 ) / 60);
        long numberOfSeconds = (long) (((seconds % 86400 ) % 3600 ) % 60);
        System.out.println(numberOfMinutes+"\t"+numberOfSeconds);
        return String.valueOf(numberOfMinutes).concat(":").concat(String.valueOf(numberOfSeconds));
    }
}
