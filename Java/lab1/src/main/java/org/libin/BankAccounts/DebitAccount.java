package org.libin.BankAccounts;

import org.libin.ClientSystems.Client;
import org.libin.TimeSystems.GlobalTime;
import org.libin.Transactions.ITransaction;

import java.util.Date;
import java.util.LinkedList;

public class DebitAccount implements IBankAccount{
    public DebitAccount(String name, int userId, int accountId, Client client, int bank){
        BankId= bank;
        Name = name;
        UserId = userId;
        AccountId = accountId;
        Balance = 0;
        StartingBalance = 0;
        CreationTime = GlobalTime.now();
        Type = AccountType.DEBITACCOUNT;
        isSuspicious(client);
    }
    private float Balance;
    private float StartingBalance;
    private int BankId;
    private String Name;
    private int UserId;
    private int AccountId;
    private Date CreationTime;
    private boolean isSuspiciousStatus;
    private AccountType Type;
    private LinkedList<ITransaction> TRANSACTION_BASE = new LinkedList<>();
    /**
     * @see IBankAccount#depositMoney(float)
     * @param money float
     */
    @Override
    public void depositMoney( float money) {
        Balance+=money;
    }

    /**
     * @see IBankAccount#withdrawMoney(float)
     * @param money float
     */
    @Override
    public void withdrawMoney(float money) {Balance-= money;}
    /**
     * @see IBankAccount#getBalance()
     * @return
     */
    @Override
    public float getBalance() {
        return Balance;
    }
    /**
     * @see IBankAccount#getName()
     * @return
     */

    @Override
    public String getName() {
        return Name;
    }
    /**
     * @see  IBankAccount#getType()
     * @return
     */
    public AccountType getType() { return Type;}
    /**
     * @see IBankAccount#addTransaction(ITransaction)
     * @param transaction
     */
    public void addTransaction(ITransaction transaction){
        TRANSACTION_BASE.add(transaction);

    }
    /**
     * @see IBankAccount#getHistory()
     * @return
     */
    public LinkedList<ITransaction > getHistory(){
        return TRANSACTION_BASE;
    }
    /**
     * @see IBankAccount#isSuspicious(Client)
     * @param client
     * checks client's passport and address
     */

    public void isSuspicious(Client client){
        isSuspiciousStatus = client.getPassportInfo() == null || client.getAddress() == null;
    }
    /**
     * @see IBankAccount#getCreationTime()
     * @return
     */

    @Override
    public Date getCreationTime() {
        return null;
    }
    /**
     * @see #getBankId()
     * @return
     */


    @Override
    public int getBankId() {
        return BankId;
    }
    /**
     * @see #getStartingBalance()
     * @return
     */
    @Override
    public float getStartingBalance() {
        return StartingBalance;
    }
}

