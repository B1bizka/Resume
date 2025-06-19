package org.libin.UserInterfaces;

import org.libin.BankAccounts.IBankAccount;
import org.libin.BankSystems.Bank;
import org.libin.BankSystems.CentralBank;
import org.libin.ClientSystems.Client;

import java.util.Scanner;

public class MenuBankAccount {
    private final Scanner scan = Console.scan;
    private final MenuCreateBankAccount menuCreateBankAccount = new MenuCreateBankAccount();
    private final MenuTransactions menuTransactions = new MenuTransactions();

    public void startClientBankAccountService(Bank currentBank, Client currentClient, CentralBank currentCentralBank) {
        String option;
        boolean flag = true;
        do {
            printAccountMenu();
            option = scan.nextLine();

            switch (option) {
                case "1":
                    showUserBankAccounts(currentBank, currentClient);
                    break;
                case "2":
                    menuCreateBankAccount.createUserBankAccount(currentBank, currentClient);
                    break;
                case "3":
                    chooseCurrentAccount(currentBank,currentCentralBank,currentClient);
                    break;
                case "4":
                    System.out.println("Exiting bank account menu");
                    flag = false;
                    break;
                default:
                    System.out.println("Try again");
                    break;
            }
        } while (flag);
    }

    private void printAccountMenu() {
        System.out.println("Welcome to bank Account menu");
        System.out.println("Choose options below");
        System.out.println("............................");
        System.out.println("1- See your bank accounts");
        System.out.println("2- Create new account");
        System.out.println("3- Operate with your accounts");
        System.out.println("4- Exit");
        System.out.println("............................");
    }

    private void showUserBankAccounts(Bank currentBank, Client currentClient) {
        for (IBankAccount el : currentBank.getBankAccountBase().get(currentClient.getClientId())) {
            System.out.println(el.getName());
            System.out.println();
        }
    }

    private void chooseCurrentAccount(Bank currentBank,CentralBank currentCentralBank, Client currentClient){
        String option;
        int id;
        boolean flag = true;
        do {
            System.out.println("Choose options below");
            System.out.println("1- Input bank account id");
            System.out.println("2- Exit");
            option = scan.nextLine();
            switch (option){
                case "1":
                    System.out.println("Input account id");
                    id = scan.nextInt();
                    menuTransactions.startTransactionMenu(currentBank, currentClient, currentCentralBank, id);
                    flag = false;
                    break;
                case "2":
                    System.out.println("Exiting");
                    flag = false;
                    break;
                default:
                    System.out.println("Try again");
            }
        }while (flag);
    }
}
