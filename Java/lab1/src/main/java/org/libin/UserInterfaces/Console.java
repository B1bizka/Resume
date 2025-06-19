package org.libin.UserInterfaces;

import org.libin.BankSystems.CentralBank;

import java.util.Scanner;

/**
 * main console which creates others
 * central bank menu can be accessed only once
 * other menus can be accessed multiple times
 */
public class Console {

    public static Scanner scan = new Scanner(System.in);
    private final CentralBank currentCentralBank = new CentralBank();
    private final MenuUser menuUser = new MenuUser(currentCentralBank);
    private final MenuBank menuBank = new MenuBank(currentCentralBank);
    private final MenuTime menuTime = new MenuTime(currentCentralBank);
    private final MenuCentralBank menuCentralBank = new MenuCentralBank(currentCentralBank);

    public void startConsole(){
        menuCentralBank.startCentralBank();
        boolean exit = false;
        String option;
        do{
            printConsoleMenu();
            option = scan.nextLine();
            switch (option){
                case "1":
                    menuUser.startUserInterface();
                    break;
                case "2":
                    menuBank.startMenuBank();
                    break;
                case "3":
                    menuTime.startMenuTime();
                    break;
                case "4":
                    exit = true;
                    break;
                default:
                    System.out.println("Wrong input");
                    break;
            }

        }while (!exit);

    }
    private void printConsoleMenu() {
        System.out.println("Choose options below");
        System.out.println("............................");
        System.out.println("1- User menu");
        System.out.println("2- Bank menu");
        System.out.println("3- Time machine");
        System.out.println("4- Close app");
        System.out.println("............................");
    }
}

