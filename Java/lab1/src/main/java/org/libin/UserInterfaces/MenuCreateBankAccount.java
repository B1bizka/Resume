package org.libin.UserInterfaces;

import org.libin.BankAccounts.CreditAccount;
import org.libin.BankAccounts.DebitAccount;
import org.libin.BankAccounts.DepositAccount;
import org.libin.BankAccounts.IBankAccount;
import org.libin.BankSystems.Bank;
import org.libin.ClientSystems.Client;
import org.libin.TimeSystems.GlobalTime;

import java.util.Scanner;

public class MenuCreateBankAccount {
    private Scanner scan = Console.scan;

    public void createUserBankAccount(Bank currentBank, Client currentClient) {
        String option;
        boolean exit = true;
        do {
            printCreateAccountMenu();
            option = scan.nextLine();

            switch (option) {
                case "1":
                    createCreditAccount(currentBank, currentClient);
                    break;
                case "2":
                    createDebitAccount(currentBank, currentClient);
                    break;
                case "3":
                    createDepositAccount(currentBank, currentClient);
                    break;
                case "4":
                    exit = false;
                    break;
                default:
                    System.out.println("Wrong input");
            }
        } while (exit);

    }

    private void printCreateAccountMenu() {
        System.out.println("Which bank account you want to open");
        System.out.println("Choose options below");
        System.out.println("............................");
        System.out.println("1- Credit account");
        System.out.println("2- Debit account");
        System.out.println("3- Deposit account");
        System.out.println("4- Exit");
        System.out.println("............................");
    }

    private void createDebitAccount(Bank currentBank, Client currentClient) {
        int userId = currentClient.getClientId();
        int accountId = currentBank.getBankAccountBase().get(userId).size();
        IBankAccount debitAccount = new DebitAccount("Debit account, Id - " + accountId + ", " + GlobalTime.readableDate.format(GlobalTime.now()), userId, accountId, currentClient, currentBank.getBankId());
        currentBank.addClientBankAccount(userId, debitAccount);
        System.out.println("You opened bank account");
    }

    private void createCreditAccount(Bank currentBank, Client currentClient) {
        int userId = currentClient.getClientId();
        int accountId = currentBank.getBankAccountBase().get(userId).size();
        IBankAccount creditAccount = new CreditAccount("Credit account, Id - " + accountId + ", " + GlobalTime.readableDate.format(GlobalTime.now()), userId, accountId, currentClient, currentBank.getBankId());
        currentBank.addClientBankAccount(userId, creditAccount);
        System.out.println("You opened bank account");
    }

    private void createDepositAccount(Bank currentBank, Client currentClient) {
        int userId = currentClient.getClientId();
        int accountId = currentBank.getBankAccountBase().get(userId).size();
        System.out.println("duration in days");
        int tmpTime = scan.nextInt();
        System.out.println("Starting balance");
        float tmpBalance = scan.nextFloat();
        scan.nextLine();
        IBankAccount depositAccount = new DepositAccount("Deposit account, Id - " + accountId + ", " + GlobalTime.readableDate.format(GlobalTime.now()), userId, accountId, currentClient, tmpTime, currentBank.getBankId(), tmpBalance);
        currentBank.addClientBankAccount(userId, depositAccount);
        System.out.println("You opened bank account");
    }

}