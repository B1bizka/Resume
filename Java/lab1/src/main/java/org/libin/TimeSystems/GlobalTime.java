package org.libin.TimeSystems;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * global class that keeps local time for all parts of the application
 */

public  class GlobalTime {
    public static Calendar calendar = new GregorianCalendar();
    private static Date currentDate = calendar.getTime();
    public static SimpleDateFormat readableDate = new SimpleDateFormat("dd MMMM yyyy");

    public static Date DepositExpiration(int days){
        Date tmp;
        calendar.add(Calendar.DATE, days);
        tmp = calendar.getTime();
        calendar.add(Calendar.DATE, -days);
        return tmp;
    }
    public static Date now(){
        return currentDate;
    }
    public static void changeDate(Date date){
        currentDate = date;
    }
}
