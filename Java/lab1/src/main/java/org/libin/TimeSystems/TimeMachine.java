package org.libin.TimeSystems;

import java.util.Calendar;

/**
 * function to change global time
 */

public class TimeMachine {
    public void addTime(int days, int years){
        GlobalTime.calendar.add(Calendar.DATE,days);
        GlobalTime.calendar.add(Calendar.YEAR,years);
        GlobalTime.changeDate(GlobalTime.calendar.getTime());
    }
}
