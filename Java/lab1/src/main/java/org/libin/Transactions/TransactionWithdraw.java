package org.libin.Transactions;

import org.libin.BankAccounts.AccountType;
import org.libin.BankAccounts.IBankAccount;
import org.libin.BankSystems.Bank;
import org.libin.TimeSystems.GlobalTime;


import java.util.Date;
import java.util.Scanner;


public class TransactionWithdraw implements ITransaction{
    TransactionWithdraw(IBankAccount currentBankAccount, float money, int id, Bank bank){
        Money = money;
        bankAccount = currentBankAccount;
        currentBank = bank;
        Name = "Withdrawal Transaction" + "id =" + id + " creation time " + GlobalTime.readableDate.format(GlobalTime.now());
        Id = id;
        currentDate = GlobalTime.now();
        this.execute();
    }
    public Scanner scan = new Scanner(System.in);
    public boolean isCompiled = false;
    IBankAccount bankAccount;
    Bank currentBank;
    String Name;
    float Money;
    boolean IsReversed = false;
    int Id;
    Date currentDate;
    @Override
    public int execute() {
        if(currentBank.getSuspiciousTransactionLimit() <= Money ){
            if(bankAccount.getType() == AccountType.DEBITACCOUNT){
                if(Money>bankAccount.getBalance()){
                    System.out.println("The amount of money is greater than your balance");
                    return 1;
                }else {
                    bankAccount.withdrawMoney(Money);
                    isCompiled = true;
                    return 0;
                }
            }
            else if (bankAccount.getType() == AccountType.DEPOSITACCOUNT){
                if(bankAccount.getCreationTime().after(currentDate)){
                    if(Money>bankAccount.getBalance()){
                        System.out.println("The amount of money is greater than your balance");
                        return 1;
                    }else {
                        bankAccount.withdrawMoney(Money);
                        isCompiled = true;
                        return 0;
                    }
                }
                else {
                    System.out.println("You cant withdraw money yet, try later");
                    return 1;
                }
            }
            else {
                if( (0 - currentBank.getCreditLimit()) <= (bankAccount.getBalance() - ((currentBank.getCreditCommission() + 1) * Money))){
                    bankAccount.withdrawMoney((currentBank.getCreditCommission() + 1) * Money);
                    isCompiled = true;
                    return 0;
                }else {
                    System.out.println("You exceeded your bank credit limit");
                    return 1;
                }
            }
        }
        else {
            System.out.println("You exceeded your limit, add more information into your profile");
            return 1;
        }

    }

    @Override
    public void reverse() {
        if(!IsReversed){
            bankAccount.depositMoney(Money);
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
        return Money;

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

