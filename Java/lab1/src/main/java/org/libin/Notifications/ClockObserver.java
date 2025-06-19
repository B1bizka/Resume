package org.libin.Notifications;

import org.libin.BankSystems.CentralBank;
import org.libin.TimeSystems.GlobalTime;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class ClockObserver {
    private final CentralBank centralBank;
    private Date currentTime;
    public  ClockObserver(CentralBank currentCentralBank){
        centralBank = currentCentralBank;
        currentTime = GlobalTime.now();
    }
    public void notifyCentralBank( CentralBank currentCentralBank){
        currentCentralBank.updateCentralBank();
    }

    /**
     * function that checks time difference and notifies central bank
     * pattern observer
     */
    public void checkTime(){
        YearMonth firstDate = YearMonth.from(convertDateToLocal(currentTime));
        YearMonth secondDate = YearMonth.from(convertDateToLocal(GlobalTime.now()));
        long monthBetween = ChronoUnit.MONTHS.between(firstDate,secondDate);

        if (monthBetween > 0 ){
            while (monthBetween !=0){
                notifyCentralBank(centralBank);
                monthBetween-=1;
            }
            currentTime = GlobalTime.now();
        }
    }
    public LocalDate convertDateToLocal(Date date){
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }
    public Date convertLocalToDate(LocalDate localDate){
        return java.util.Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }
}
