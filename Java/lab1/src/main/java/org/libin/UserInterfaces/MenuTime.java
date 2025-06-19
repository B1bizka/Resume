package org.libin.UserInterfaces;

import org.libin.BankSystems.CentralBank;
import org.libin.Notifications.ClockObserver;
import org.libin.TimeSystems.TimeMachine;

import java.util.Scanner;

public class MenuTime {
    public MenuTime(CentralBank centralBank){
        clockObserver = new ClockObserver(centralBank);
    }
    private Scanner scan = Console.scan;
    private TimeMachine timeMachine = new TimeMachine();
    private ClockObserver clockObserver;

    public void startMenuTime(){
        boolean exit = true;
        String option;
        do{
            printMenuTime();
            option = scan.nextLine();
            switch (option){
                case "1":
                    changingTime(timeMachine);
                    clockObserver.checkTime();
                    break;
                case "2":
                    exit = false;
                    break;
                default:
                    System.out.println("Wrong input");
                    break;
            }

        }while (exit);



    }
    private void printMenuTime(){
        System.out.println("Choose option number");
        System.out.println("......................................");
        System.out.println("1- Change time");
        System.out.println("2- Exit");
        System.out.println("......................................");
    }
    private void changingTime(TimeMachine timeMachine){
        int days;
        int years;
        System.out.println("Wright down, how much days u want to skip");
        days = scan.nextInt();
        System.out.println("Wright down, how much years u want to skip");
        years = scan.nextInt();
        scan.nextLine();
        timeMachine.addTime(days,years);
    }
}

