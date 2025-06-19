package org.libin.Transactions;

import org.libin.BankAccounts.AccountType;
import org.libin.BankAccounts.IBankAccount;
import org.libin.TimeSystems.GlobalTime;


public class TransactionDeposit implements ITransaction {
    TransactionDeposit(IBankAccount currentBankAccount, float money, int id){
        bankAccount = currentBankAccount;
        Money = money;
        Id =id;
        Name = "Deposit Transaction " + "id = " + id + " creation time " + GlobalTime.readableDate.format(GlobalTime.now());
        this.execute();
    }
    IBankAccount bankAccount;
    public boolean isCompiled;
    String Name;
    float Money;
    boolean IsReversed = false;
    int Id;
    @Override
    public int execute() {
        if(!IsReversed) {
            if (bankAccount.getType() == AccountType.DEPOSITACCOUNT) {
                bankAccount.depositMoney(Money);
                isCompiled = true;
                return 0;
            } else if (bankAccount.getType() == AccountType.DEPOSITACCOUNT) {
                bankAccount.depositMoney(Money);
                isCompiled = true;
                return 0;
            } else {
                bankAccount.depositMoney(Money);
                isCompiled = true;
                return 0;
            }
        }else {
            System.out.println("Transaction is reversed");
            return 0;
        }
    }

    @Override
    public void reverse() {
        if(!IsReversed){
            bankAccount.withdrawMoney(Money);
            IsReversed = true;
        }
        else {
            System.out.println("Transaction is already reversed");
        }
    }

    @Override
    public String getName() {
        if(IsReversed){
            return Name + " transaction is reversed ";
        }else {
            return Name;
        }
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

