package org.libin.Transactions;

import org.libin.BankAccounts.IBankAccount;
import org.libin.BankSystems.CentralBank;

public class ParserTransfer implements IParser{
    private IParser NextParser = null;
    @Override
    public ITransaction DefineTransaction(TransactionType type, IBankAccount currentBankAccount, CentralBank currentCentralBank) {
        if (type == TransactionType.TRANSFER){
            int i =currentBankAccount.getHistory().size();
            return new TransactionTransfer(currentBankAccount, currentCentralBank,i);
        }
        else {
            if(NextParser == null){
                NextParser = new ParserWithdraw();
            }
            return NextParser.DefineTransaction(type,currentBankAccount, currentCentralBank);
        }
    }
}

