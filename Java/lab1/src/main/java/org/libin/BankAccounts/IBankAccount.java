package org.libin.BankAccounts;

import org.libin.ClientSystems.Client;
import org.libin.Transactions.ITransaction;

import java.util.Date;
import java.util.LinkedList;

/**
 * Bank Account Interface
 */
public interface IBankAccount {
    /**
     * Deposit money from Bank account
     * @param money float
     */
    public void depositMoney(float money);

    /**
     * Withdraw money from Bank account
     * @param money float
     */
    public void withdrawMoney(float money);

    /**
     * get bank account balance
     * @return
     */
    public float getBalance();

    /**
     * get bank account name
     * @return
     */
    public String getName();

    /**
     * get list of transactions with this bank account
     * @return
     */
    public LinkedList<ITransaction> getHistory();

    /**
     * add new transaction after it completion in the transaction list
     * @param transaction
     */
    public void addTransaction(ITransaction transaction);

    /**
     * get account type
     * @return
     */
    public AccountType getType();

    /**
     * check client status
     * @param client
     */
    public void isSuspicious(Client client);

    /**
     * creation time
     * @return
     */
    public Date getCreationTime();

    /**
     * id
     * @return
     */
    public int getBankId();

    /**
     * balance, which was on bank account upon creation
     * @return
     */
    public float getStartingBalance();
}
