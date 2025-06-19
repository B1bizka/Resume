package org.libin.UserInterfaces;

import org.libin.BankSystems.Bank;
import org.libin.BankSystems.CentralBank;
import org.libin.BankSystems.DepositPercentage;

import java.util.Scanner;

public class MenuCentralBank {

    public MenuCentralBank(CentralBank centralBank){
        currentCentralBank = centralBank;
    }

    private final Scanner scan = Console.scan;
    private final CentralBank currentCentralBank;

    public void startCentralBank () {
        boolean flag = true;
        do {
            displayOptions();
            System.out.println("select option using numbers above");
            String optionNumber;
            do {
                optionNumber = scan.nextLine();
                if (optionNumber.isEmpty()) {
                    System.out.println("Please, try again");
                } else if (optionNumber.equals("1") || optionNumber.equals("2")) {
                    break;
                } else System.out.println("Wrong input - please, try again");
            } while (true);

            switch (optionNumber) {
                case "1":
                    createBank();
                    break;
                case "2":
                    flag = false;
                    System.out.println("Successful exit");
                    break;
            }
        }while (flag);
    }

    private void createBank(){
        String newName;
        float debit;
        float creditLimit;
        float credit;
        float sus;
        Bank currentBank;
        System.out.println("Wright down new bank name");
        do{
            newName = scan.nextLine();
            if(newName.isEmpty())System.out.println("Please, try again");
        }while (newName.isEmpty());

        System.out.println("Wright down new bank debit account percentage");
        debit = scan.nextFloat();

        System.out.println("Wright down new bank credit account limit");
        creditLimit = scan.nextFloat();

        System.out.println("Wright down new bank credit account commission");
        credit = scan.nextFloat();

        System.out.println("Wright down new bank credit limit for suspicious users");
        sus = scan.nextFloat();

        currentCentralBank.addBank(newName,debit,creditLimit, credit,sus);
        currentBank = currentCentralBank.BANK_LIST.getLast();


        System.out.println("Wright down new bank deposit account percentages");
        depositAccountPercentages(currentBank);

    }
    private void displayOptions(){
        System.out.println("Welcome to Central Bank Menu System");
        System.out.println("Choose options:");
        System.out.println("..................");
        System.out.println("1- Create new bank");
        System.out.println("2- Exit");
        System.out.println("..................");
    }
    private void depositAccountPercentages(Bank currentBank){
        boolean flag1 = true;
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
                    flag1 = false;
                    System.out.println("Successful exit");
                    break;
                default:
                    System.out.println("Wrong input, try again");
                    break;
            }

        }while (flag1);
    }
    private void inputPercentage(DepositPercentage percentage){
        System.out.println("Wright down upper limit");
        percentage.setKey(scan.nextFloat());
        System.out.println("Wright down percentage");
        percentage.setValue(scan.nextFloat());
        scan.nextLine();
    }
}
