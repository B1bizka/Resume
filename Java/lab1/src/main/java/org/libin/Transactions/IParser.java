package org.libin.Transactions;

import org.libin.BankAccounts.IBankAccount;
import org.libin.BankSystems.CentralBank;

/**
 * pattern chain
 * its easy to change transaction type realisation or add new ones
 */

public interface IParser {
    public IParser NextParser = null;
    public ITransaction DefineTransaction(TransactionType type, IBankAccount currentBankAccount, CentralBank currentCentralBank);
}
