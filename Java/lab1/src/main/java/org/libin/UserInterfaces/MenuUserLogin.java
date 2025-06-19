package org.libin.UserInterfaces;

import org.libin.BankSystems.Bank;
import org.libin.BankSystems.CentralBank;
import org.libin.ClientSystems.Client;

import java.util.Scanner;

public class MenuUserLogin {
    private final Scanner scan = Console.scan;
    private final MenuBankAccount menuBankAccount = new MenuBankAccount();
    private Bank bank;

    public void logIn(Bank currentBank, CentralBank currentCentralBank){
        bank = currentBank;
        String userOption;
        String userInput;
        String userName;
        String userSurname;
        String userPassword;
        Client currentClient = null;
        boolean check = false;
        boolean check1 = !currentBank.getClientsBase().isEmpty();
        if (check1) {
            do {
                System.out.println("Enter your name");
                do {
                    userName = scan.nextLine();
                    if (userName.isEmpty()) {
                        System.out.println("Please, try again");
                    }
                } while (userName.isEmpty());

                userInput = userName;

                System.out.println("Enter your Surname");
                do {
                    userSurname = scan.nextLine();
                    if (userSurname.isEmpty()) {
                        System.out.println("Please, try again");
                    }
                } while (userSurname.isEmpty());

                for (Client i : currentBank.getClientsBase()) {
                    if (i.getName().equals(userName) && i.getSurname().equals(userSurname)) {
                        currentClient = i;
                        check = true;
                        break;
                    }
                }

                if (!check) {
                    System.out.println("There is no such user in base - try again");
                    System.out.println("Type YES to continue, type NO to exit");
                    do {
                        userInput = scan.nextLine();
                        if (!userInput.equals("NO") && !userInput.equals("YES")) {
                            System.out.println("Please, try again");
                        } else {
                            break;
                        }
                    } while (true);
                }
            } while (userInput.equals("YES"));
            if (!userInput.equals("NO")) {

                System.out.println("Enter your password (8 digits)");
                do {
                    userPassword = scan.nextLine();
                    if (userPassword.isEmpty()) {
                        System.out.println("Please, try again");
                    }
                    if (!currentClient.getPassword().equals(userPassword)) {
                        System.out.println("Please, try again");
                    }
                } while (!currentClient.getPassword().equals(userPassword));

                do {
                    displayOptions();
                    do {
                        userOption = scan.nextLine();
                        switch (userOption) {
                            case "1":
                                showInfo(currentClient);
                                check1 = false;
                                break;
                            case "2":
                                editPassport(currentClient);
                                check1 = false;
                                break;
                            case "3":
                                editAddress(currentClient);
                                check1 = false;
                                break;
                            case "4":
                                editPassword(currentClient);
                                check1 = false;
                                break;
                            case "5":
                                check1 = false;
                                break;
                            case "6":
                                menuBankAccount.startClientBankAccountService(currentBank,currentClient,currentCentralBank);
                                check1 = false;
                                break;
                            case "7":
                                check1 = false;
                                check = false;
                                break;
                            default:
                                System.out.println("Wrong input");
                        }
                    } while (check1);

                } while (check);


            }
        }else System.out.println("There is no registered users in this bank, try creating account first ");
    }
    private void displayOptions(){
        System.out.println("Choose option");
        System.out.println(".............................");
        System.out.println("1- show full user information");
        System.out.println("2- Edit passport");
        System.out.println("3- Edit address");
        System.out.println("4- Edit password");
        System.out.println("5- Check notifications");
        System.out.println("6- Bank Accounts");
        System.out.println("7- Exit");
        System.out.println(".............................");
    }
    private void showInfo(Client currentClient){
        System.out.println(".............................");
        System.out.println("User Name - " + currentClient.getName());
        System.out.println("User Surname - " + currentClient.getSurname());
        System.out.println("User password - " + currentClient.getPassword());
        System.out.println("User passport - " + currentClient.getPassportInfo());
        System.out.println("User address - " + currentClient.getAddress());
        System.out.println("Notifications - " + ((currentClient.getIsNotifications())? "On": "Off"));
        System.out.println(".............................");
        System.out.println("Press any key");
        scan.nextLine();

    }
    private void editPassword(Client currentClient){
        String tmp;
        System.out.println("Input new password");
        do{
            tmp = scan.nextLine();
            if(tmp.length() == 8){
                break;
            }else System.out.println("Try again");
        }while (true);
        currentClient.setPassword(tmp);
    }
    private void editPassport(Client currentClient){
        String tmp;
        System.out.println("Input new passport");
        do{
            tmp = scan.nextLine();
            if(!tmp.isEmpty()){
                break;
            }else System.out.println("Try again");
        }while (true);
        currentClient.setPassportInfo(tmp);
        bank.notifyBankAccounts(currentClient);
    }
    private void editAddress(Client currentClient){
        String tmp;
        System.out.println("Input new address");
        do{
            tmp = scan.nextLine();
            if(!tmp.isEmpty()){
                break;
            }else System.out.println("Try again");
        }while (true);
        currentClient.setAddress(tmp);
        bank.notifyBankAccounts(currentClient);
    }
}

