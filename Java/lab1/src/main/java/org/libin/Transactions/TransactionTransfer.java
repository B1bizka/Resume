package org.libin.Transactions;

import org.libin.BankAccounts.IBankAccount;
import org.libin.BankSystems.Bank;
import org.libin.BankSystems.CentralBank;
import org.libin.TimeSystems.GlobalTime;

import java.util.Scanner;

public class TransactionTransfer implements ITransaction {
    TransactionTransfer(IBankAccount currentBankAccount, CentralBank currentCentralBank, int id){
        bankAccount = currentBankAccount;
        centralBank = currentCentralBank;
        Name =  "Transfer Transaction" + "id =" + id + " creation time " + GlobalTime.readableDate.format(GlobalTime.now());
        Id = id;
        this.execute();
    }
    ITransaction transferWithdraw;
    public Scanner scan = new Scanner(System.in);
    public boolean isCompiled = false;
    IBankAccount bankAccount;
    boolean IsReversed = false;
    Bank transactionBank = null;
    String Name;
    int transactionAccount;
    int accountId;
    int Id;
    float transactionSum;
    CentralBank centralBank;
    @Override
    public int execute() {
        boolean exit = true;
        String option;
        System.out.println("............................");
        System.out.println("Choose bank to transfer");
        centralBank.BANK_LIST.forEach(s -> System.out.println(s.getBankName()));
        System.out.println("............................");
        do {
            option = scan.nextLine();
            for (Bank el : centralBank.BANK_LIST) {
                if (el.getBankName().equals(option)) {
                    transactionBank = el;
                    exit = false;
                    break;
                }
            }
            System.out.println("Wrong input");
        }while(exit);
        System.out.println("Input user id (not yours)");
        transactionAccount = scan.nextInt();
        System.out.println("Input bank account id");
        accountId = scan.nextInt();
        System.out.println("Input amount of money");
        transactionSum = scan.nextFloat();
        int i =1;
        /**
         * transfer uses transaction withdraw but don't add it to the history of transactions therefore
         * u can't reverse this withdraw without transfer operation
         */
        transferWithdraw = new TransactionWithdraw(bankAccount, transactionSum, i, centralBank.BANK_LIST.get(bankAccount.getBankId()));
        if(transferWithdraw.isCompleted()){
            transactionBank.getBankAccountBase().get(transactionAccount).get(accountId).depositMoney(transactionSum);
            isCompiled = true;
            return 0;
        }else {
            System.out.println("Transfer canceled - check your account balance");
            return 1;
        }
    }

    @Override
    public void reverse() {
        if(!IsReversed){
            transferWithdraw.reverse();
            transactionBank.getBankAccountBase().get(transactionAccount).get(accountId).withdrawMoney(transactionSum);
            IsReversed = true;
        }
        else {
            System.out.println("Transaction is already reversed");
        }
    }

    @Override
    public String getName() {
        return Name;
    }

    @Override
    public float showBalance() {
        return transactionSum;

    }

    @Override
    public int getId() {
        return Id;
    }

    @Override
    public boolean isCompleted() {
        return isCompiled;
    }
}
