package javeriana.edu.co.homenet.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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
    public static int getHourOftheDay(){
        Date date = new Date();
        Calendar calendar = GregorianCalendar.getInstance();    // gets the current month
        calendar.setTime(date);   // assigns calendar to given date
        return calendar.get(Calendar.HOUR_OF_DAY); // 24 hour format
    }
}
