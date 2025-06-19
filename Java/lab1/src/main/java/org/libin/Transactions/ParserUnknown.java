package org.libin.Transactions;

import org.libin.BankAccounts.IBankAccount;
import org.libin.BankSystems.CentralBank;

/**
 * end of chain
 */
public class ParserUnknown implements IParser{
    @Override
    public ITransaction DefineTransaction(TransactionType type, IBankAccount currentBankAccount, CentralBank currentCentralBank) {
        System.out.println("Wrong transaction type input, try again");
        return null;
    }
}
