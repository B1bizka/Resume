package org.libin.Transactions;

import org.libin.BankAccounts.IBankAccount;
import org.libin.BankSystems.CentralBank;
import org.libin.UserInterfaces.Console;

import java.util.Scanner;

public class ParserDeposit implements IParser {

    private IParser NextParser = null;
    private Scanner scan = Console.scan;
    @Override
    public ITransaction DefineTransaction(TransactionType type, IBankAccount currentBankAccount, CentralBank currentCentralBank) {
        if (type == TransactionType.DEPOSIT){
            System.out.println("Input money amount");
            float Money = scan.nextFloat();
            int i =currentBankAccount.getHistory().size();
            return new TransactionDeposit(currentBankAccount, Money, i);
        }
        else {
            if(NextParser == null){
                NextParser = new ParserTransfer();
            }
            return NextParser.DefineTransaction(type, currentBankAccount, currentCentralBank);
        }
    }
}
