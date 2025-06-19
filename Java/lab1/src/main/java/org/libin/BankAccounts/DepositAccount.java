package org.libin.BankAccounts;

import org.libin.ClientSystems.Client;
import org.libin.Transactions.ITransaction;

import java.util.Date;
import java.util.LinkedList;

import static org.libin.TimeSystems.GlobalTime.DepositExpiration;

/**
 * @see IBankAccount for all def
 *
 */

public class DepositAccount implements IBankAccount{
    public DepositAccount(String name, int userId, int accountId, Client client, int time, int bank, float balance){
        BankId= bank;
        Name = name;
        UserId = userId;
        AccountId = accountId;
        Balance = balance;
        /**
         * starting balance is important for deposit accounts, as its percentage depends on it
         */
        StartingBalance = balance;
        CreationTime =  DepositExpiration(time);
        Type = AccountType.DEPOSITACCOUNT;
        isSuspicious(client);
    }
    private float Balance;
    private float StartingBalance;
    private String Name;
    private int BankId;
    private int UserId;
    private int AccountId;
    private Date CreationTime;
    private boolean isSuspiciousStatus;
    private AccountType Type;
    private LinkedList<ITransaction> TRANSACTION_BASE = new LinkedList<>();
    private int Account_Term;
    @Override
    public void depositMoney(float money){
        Balance+=money;
    }
    @Override
    public void withdrawMoney(float money) {Balance-= money;}
    @Override
    public float getBalance() {
        return Balance;
    }

    @Override
    public String getName() {
        return Name;
    }
    public void addTransaction(ITransaction transaction){
        TRANSACTION_BASE.add(transaction);

    }
    public LinkedList<ITransaction > getHistory(){
        return TRANSACTION_BASE;
    }

    @Override
    public AccountType getType() {
        return Type;
    }

    public void isSuspicious(Client client){
        isSuspiciousStatus = client.getPassportInfo() == null || client.getAddress() == null;
    }

    @Override
    public Date getCreationTime() {
        return CreationTime;
    }

    @Override
    public int getBankId() {
        return BankId;
    }

    @Override
    public float getStartingBalance() {
        return StartingBalance;
    }
}

