package org.libin.UserInterfaces;

import org.libin.BankSystems.Bank;
import org.libin.BankSystems.CentralBank;
import org.libin.ClientSystems.Client;
import org.libin.ClientSystems.ClientBuilder;

import java.util.Scanner;

public class MenuUser {

    public MenuUser(CentralBank centralBank){
        currentCentralBank = centralBank;
    }

    private final CentralBank currentCentralBank;
    private final Scanner scan = Console.scan;
    private final MenuUserLogin menuLogIn = new MenuUserLogin();

    public void startUserInterface (){
        String option;
        Bank currentBank = null;
        boolean interfaceFlag = true;
        do {
            boolean flag = true;
            System.out.println("Hello user, which bank you want to use");
            System.out.println("......................................");
            if (currentCentralBank.BANK_LIST.isEmpty()) {
                System.out.println("Oops, there are no active banks yet");
                System.out.println("We are sorry for inconveniences, press 3");
            } else {
                currentCentralBank.BANK_LIST.forEach(s -> System.out.println(s.getBankName()));
                System.out.println("......................................");
                System.out.println("Choose bank above (input full name) or press 3 for exit");
            }
            do {
                option = scan.nextLine();
                if (option.isEmpty()) {
                    System.out.println("Please, try again");
                } else if(option.equals("3")){
                    flag = false;
                    interfaceFlag = false;
                } else {
                    for (Bank el : currentCentralBank.BANK_LIST) {
                        if (el.getBankName().equals(option)) {
                            flag = false;
                            currentBank = el;
                            break;
                        }
                    }
                    if (flag) {
                        System.out.println("Wrong input, try again");
                    }
                }
            } while (flag);

            if(interfaceFlag) {
                do {
                    displayOptions();
                    flag = true;
                    option = scan.nextLine();
                    switch (option) {
                        case "1":
                            createUserAccount(currentBank);
                            break;
                        case "2":
                            menuLogIn.logIn(currentBank, currentCentralBank);
                            break;
                        case "3":
                            flag = false;
                            break;
                        default:
                            System.out.println("Wrong input, try again");
                            break;
                    }
                } while (flag);
            }
        }while (interfaceFlag);
        System.out.println("Thank you for using our service");
    }
    private void createUserAccount(Bank currentBank){
        String tmp;
        Client newClient;
        ClientBuilder builder = new ClientBuilder();

        System.out.println("Enter your name");
        do{
            tmp = scan.nextLine();
            if(tmp.isEmpty()){System.out.println("Please, try again");}
        }while (tmp.isEmpty());
        builder.setClientName(tmp);

        System.out.println("Enter your surname");
        do{
            tmp = scan.nextLine();
            if(tmp.isEmpty()){System.out.println("Please, try again");}
        }while (tmp.isEmpty());
        builder.setClientSurname(tmp);

        System.out.println("Enter your password (8 digits)");
        do{
            tmp = scan.nextLine();
            if(tmp.isEmpty()){System.out.println("Please, try again");}
            else if (tmp.length() != 8){
                System.out.println("Password must be 8 digits");
            }
        }while ((tmp.length() != 8));
        builder.setClientPassword(tmp);

        System.out.println("Enter your address (to skip press enter, but some transactions require it)");
        tmp = scan.nextLine();
        if(!tmp.isEmpty()){
            builder.setClientAddress(tmp);
        }

        System.out.println("Enter your passport (to skip press enter, but some transactions require it)");
        tmp = scan.nextLine();
        if(!tmp.isEmpty()) {
            builder.setClientPassportInfo(tmp);
        }
        newClient = builder.createClient();
        int userId;
        userId = currentBank.createClientBankAccount();
        newClient.setClientId(userId);
        currentBank.addClient(newClient);
    }
    private void displayOptions(){
        System.out.println("Choose option number");
        System.out.println("......................................");
        System.out.println("1- Create new user account");
        System.out.println("2- Log in");
        System.out.println("3- Exit");
        System.out.println("......................................");
    }
}
