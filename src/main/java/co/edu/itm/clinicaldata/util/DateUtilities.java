package co.edu.itm.clinicaldata.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class DateUtilities {

    private static final String DATE_FORMAT = "dd-MM-yyyy HH:mm:ss";

    public static String timestampToString(Timestamp timestamp) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        String string  = dateFormat.format(timestamp);
        return string;
    }

    public static Timestamp getTimestamp(){
        return new Timestamp(System.currentTimeMillis());
    }
}
