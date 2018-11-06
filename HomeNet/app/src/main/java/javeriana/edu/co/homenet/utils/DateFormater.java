package javeriana.edu.co.homenet.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public final class DateFormater {
    public static Date stringToDate(String s){
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        try {
            return format.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
