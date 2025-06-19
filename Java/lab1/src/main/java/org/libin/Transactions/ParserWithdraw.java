package org.libin.Transactions;

import org.libin.BankAccounts.IBankAccount;
import org.libin.BankSystems.CentralBank;
import org.libin.UserInterfaces.Console;

import java.util.Scanner;

public class ParserWithdraw implements IParser{
    private IParser NextParser = null;
    private Scanner scan = Console.scan;
    @Override
    public ITransaction DefineTransaction(TransactionType type, IBankAccount currentBankAccount, CentralBank currentCentralBank) {
        if (type == TransactionType.WITHDRAWAL){
            System.out.println("Input money amount");
            float Money = scan.nextFloat();
            int i =currentBankAccount.getHistory().size();
            return new TransactionWithdraw(currentBankAccount,Money, i, currentCentralBank.BANK_LIST.get(currentBankAccount.getBankId()));
        }
        else {
            if(NextParser == null){
                NextParser = new ParserUnknown();
            }
            return NextParser.DefineTransaction(type,currentBankAccount, currentCentralBank);
        }
    }
}
