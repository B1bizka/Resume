package org.libin.UserInterfaces;

import org.libin.BankAccounts.IBankAccount;
import org.libin.BankSystems.Bank;
import org.libin.BankSystems.CentralBank;
import org.libin.ClientSystems.Client;
import org.libin.Transactions.IParser;
import org.libin.Transactions.ITransaction;
import org.libin.Transactions.ParserDeposit;
import org.libin.Transactions.TransactionType;

import java.util.LinkedList;
import java.util.Scanner;

public class MenuTransactions {
    private Scanner scan = Console.scan;
    private IBankAccount currentAccount;
    private IParser parser = new ParserDeposit();
    private ITransaction tmpTransaction;

    public void startTransactionMenu(Bank currentBank, Client currentClient, CentralBank centralBank, int id){
        String option;
        currentAccount = currentBank.getBankAccountBase().get(currentClient.getClientId()).get(id);
        boolean exit = true;
        do {
            printTransactionMenu();
            option = scan.nextLine();
            switch (option){
                case "1":
                    tmpTransaction = parser.DefineTransaction(TransactionType.WITHDRAWAL,currentAccount,centralBank);
                    if(tmpTransaction.isCompleted()){
                        currentAccount.addTransaction(tmpTransaction);
                    }
                    break;
                case "2":
                    tmpTransaction = parser.DefineTransaction(TransactionType.DEPOSIT,currentAccount,centralBank);
                    if(tmpTransaction.isCompleted()){
                        currentAccount.addTransaction(tmpTransaction);
                    }
                    break;
                case "3":
                    tmpTransaction = parser.DefineTransaction(TransactionType.TRANSFER,currentAccount,centralBank);
                    if(tmpTransaction.isCompleted()){
                        currentAccount.addTransaction(tmpTransaction);
                    }
                    break;
                case "4":
                    showHistory(currentAccount);
                    break;
                case "5":
                    System.out.println("Your balance is " + currentAccount.getBalance());
                    break;
                case "6":
                    exit = false;
                    break;
                default: System.out.println("Try again");

            }
        }while (exit);

    }
    private void printTransactionMenu(){
        System.out.println("Choose options below");
        System.out.println("............................");
        System.out.println("1- Withdraw money");
        System.out.println("2- Deposit money");
        System.out.println("3- Transfer to another bank");
        System.out.println("4- History");
        System.out.println("5- Balance");
        System.out.println("6- Exit");
        System.out.println("............................");
    }
    private void showHistory(IBankAccount currentAccount){
        LinkedList<ITransaction > history = currentAccount.getHistory();
        for (ITransaction el : history){
            System.out.println("Operation - "+ el.getName() + " sum =" +el.showBalance() );
        }
    }
}

