package org.libin.UserInterfaces;

import org.libin.BankAccounts.AccountType;
import org.libin.BankSystems.Bank;
import org.libin.BankSystems.CentralBank;
import org.libin.BankSystems.DepositPercentage;


import java.util.Scanner;

public class MenuBank {
    public MenuBank(CentralBank centralBank){
        currentCentralBank = centralBank;

    }
    private final CentralBank currentCentralBank;
    private final Scanner scan = Console.scan;

    public void startMenuBank(){
        Bank currentBank = null;
        boolean exit = true;
        float percentageTmp;
        do {
            boolean flag1 = true;
            String option;

            if (currentCentralBank.BANK_LIST.isEmpty()) {
                System.out.println("Oops, there are no active banks yet, press 3");
            } else {
                System.out.println("......................................");
                currentCentralBank.BANK_LIST.forEach(s -> System.out.println(s.getBankName()));
                System.out.println("......................................");
                System.out.println("Choose bank above (input full name) or press 3 for exit");
            }

            do {
                option = scan.nextLine();
                if (option.isEmpty()) {
                    System.out.println("Please, try again");
                } else if(option.equals("3")){
                    flag1 = false;
                    exit = false;
                } else {
                    for (Bank el : currentCentralBank.BANK_LIST) {
                        if (el.getBankName().equals(option)) {
                            flag1 = false;
                            currentBank = el;
                            break;
                        }
                    }
                    if (flag1) {
                        System.out.println("Wrong input, try again");
                    }
                }

            } while (flag1);
            if (exit) {
                do {
                    printMenuBank();
                    flag1 = true;
                    option = scan.nextLine();
                    switch (option) {
                        case "1":
                            System.out.println("Wright down new bank debit account percentage");
                            percentageTmp = scan.nextFloat();
                            currentBank.changeDebitPercentage(percentageTmp);
                            currentBank.updateBank1(AccountType.DEBITACCOUNT);
                            break;
                        case "2":
                            currentBank.deleteDepositPercentage();
                            depositAccountPercentages(currentBank);
                            currentBank.updateBank1(AccountType.DEPOSITACCOUNT);
                            break;
                        case "3":
                            changeCreditLimit(currentBank);
                            currentBank.updateBank1(AccountType.CREDITACCOUNT);
                            break;
                        case "4":
                            reverseTransaction(currentBank);
                            break;
                        case "5":
                            flag1 = false;
                            break;
                        default:
                            System.out.println("Wrong input");
                            break;
                    }
                } while (flag1);
            }
        }while (exit);
        System.out.println("Thank you for using our service");


    }
    private void printMenuBank(){
        System.out.println("Choose options below");
        System.out.println("............................");
        System.out.println("1- Change debit percentages ");
        System.out.println("2- Change deposit percentage");
        System.out.println("3- Change credit limit");
        System.out.println("4- reverse transaction");
        System.out.println("5- Exit");
        System.out.println("............................");

    }
    private void depositAccountPercentages(Bank currentBank){
        boolean flag3 = true;
        String option;
        scan.nextLine();
        do {
            System.out.println("Choose option");
            System.out.println("1- Add new condition");
            System.out.println("2- Exit");
            option = scan.nextLine();
            switch (option){
                case "1":
                    DepositPercentage percentage = new DepositPercentage();
                    inputPercentage(percentage);
                    currentBank.addDepositPercentage(percentage);
                    break;
                case "2":
                    flag3 = false;
                    System.out.println("Successful exit");
                    break;
                default:
                    System.out.println("Wrong input, try again");
                    break;
            }

        }while (flag3);
    }
    private void inputPercentage(DepositPercentage percentage){
        System.out.println("Wright down upper limit");
        percentage.setKey(scan.nextFloat());
        System.out.println("Wright down percentage");
        percentage.setValue(scan.nextFloat());
        scan.nextLine();
    }
    private void changeCreditLimit(Bank currentBank){
        float creditLimit;
        float credit;
        System.out.println("Wright down new bank credit account limit");
        creditLimit = scan.nextFloat();

        System.out.println("Wright down new bank credit account commission");
        credit = scan.nextFloat();

        currentBank.changeCreditPercentage(credit, creditLimit);
    }
    private void reverseTransaction(Bank currentBank){
        int userId;
        int accountId;
        int transactionId;
        System.out.println("Wright down user id");
        userId = scan.nextInt();
        System.out.println("Wright down account id");
        accountId = scan.nextInt();
        System.out.println("Wright down transaction id");
        transactionId = scan.nextInt();
        currentBank.getBankAccountBase().get(userId).get(accountId).getHistory().get(transactionId).reverse();
        System.out.println("Now transaction is reversed ");
        scan.nextLine();
    }
}
