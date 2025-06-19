package org.libin.BankSystems;

import java.util.LinkedList;

public class CentralBank {
    public LinkedList<Bank> BANK_LIST = new LinkedList<>();
    public void addBank(String bankName,float debit, float creditBankLimit, float credit, float susLimit) {

        Bank newBank = new Bank(bankName, debit, creditBankLimit, credit, susLimit, BANK_LIST.size());
        BANK_LIST.add(newBank);
    }
    public void updateCentralBank(){
        System.out.println("Central Bank notification - new month has come ");
        System.out.println("Its time to notify banks");
        notifyBanks();
    }

    /**
     * notify all banks in system
     */
    public void notifyBanks(){
        for ( Bank bank : BANK_LIST){
            bank.updateBank();
        }
    }
}