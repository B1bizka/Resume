package org.libin.BankSystems;

import org.libin.BankAccounts.AccountType;
import org.libin.BankAccounts.IBankAccount;
import org.libin.ClientSystems.Client;

import java.util.LinkedList;
import java.util.Objects;

public class Bank {
    public Bank(String name, float debit, float creditBankLimit, float credit,float susLimit, int id){
        bankId = id;
        bankName = name;
        debitPercentage = debit;
        creditLimit = creditBankLimit;
        creditCommission = credit;
        suspiciousTransactionLimit = susLimit;
    }
    private String bankName;
    private int bankId;
    private float debitPercentage;
    private float creditLimit;
    private float creditCommission;
    private float suspiciousTransactionLimit;

    private LinkedList<DepositPercentage> DEPOSIT_PERCENTAGE= new LinkedList<>();
    private LinkedList<Client> CLIENTS_BASE = new LinkedList<>();
    private LinkedList<LinkedList<IBankAccount>> BANK_ACCOUNT_BASE = new LinkedList<>();

    public void addDepositPercentage(DepositPercentage percentage){
        DEPOSIT_PERCENTAGE.add(percentage);
    }
    public void deleteDepositPercentage(){
        DEPOSIT_PERCENTAGE.removeIf(Objects::nonNull);
    }
    public void addClient(Client client){
        CLIENTS_BASE.add(client);
    }

    public int createClientBankAccount(){
        int id = BANK_ACCOUNT_BASE.size();
        LinkedList<IBankAccount> list = new LinkedList<>();
        BANK_ACCOUNT_BASE.add(list);
        return id;

    }
    public void addClientBankAccount(int id, IBankAccount bankAccount){
        BANK_ACCOUNT_BASE.get(id).add(bankAccount);
    }
    public void changeDebitPercentage(float debit){
        debitPercentage = debit;
    }
    public void changeCreditPercentage(float credit, float limit){
        creditLimit = limit;
        creditCommission = credit;
    }

    /**
     * part of notification services
     * Observer pattern
     * this function notifies clients each full month
     */
    public void updateBank(){
        System.out.println(bankName +" , its time to deposit clients' bank accounts ");
        for( int el=0; el<BANK_ACCOUNT_BASE.size() ; el++){
            for ( IBankAccount bankAccount : BANK_ACCOUNT_BASE.get(el)  ){
                AccountType tmp = bankAccount.getType();
                switch (tmp){
                    case DEBITACCOUNT :
                        CLIENTS_BASE.get(el).clientUpdate(CLIENTS_BASE.get(el).getName() + " , your debit account got monthly percentage");
                        bankAccount.depositMoney(bankAccount.getBalance() * debitPercentage );
                        break;
                    case DEPOSITACCOUNT:
                        float startingBalance = bankAccount.getStartingBalance();
                        float percentage = 0;
                        for ( DepositPercentage i : DEPOSIT_PERCENTAGE){
                            if ( startingBalance >= i.getKey()){
                                percentage = i.getValue();
                            }
                        }
                        CLIENTS_BASE.get(el).clientUpdate(CLIENTS_BASE.get(el).getName() + " , your deposit account got monthly percentage");
                        bankAccount.depositMoney(bankAccount.getBalance() * percentage);
                        break;
                    default:
                        break;

                }
            }
        }
    }

    /**
     * this function notify clients if bank changed percentage conditions and client has such type of bank account
     * @param type
     */
    public void updateBank1(AccountType type){
        for( int el=0; el<BANK_ACCOUNT_BASE.size() ; el++){
            for ( IBankAccount bankAccount : BANK_ACCOUNT_BASE.get(el)  ){
                AccountType tmp = bankAccount.getType();
                switch (tmp){
                    case DEBITACCOUNT :
                        CLIENTS_BASE.get(el).clientUpdate(CLIENTS_BASE.get(el).getName() + " , " + bankName + " has changed debit account conditions");
                        break;
                    case DEPOSITACCOUNT:
                        CLIENTS_BASE.get(el).clientUpdate(CLIENTS_BASE.get(el).getName() + " , " + bankName + " has changed deposit account conditions");
                        break;
                    case CREDITACCOUNT:
                        CLIENTS_BASE.get(el).clientUpdate(CLIENTS_BASE.get(el).getName() + " , " + bankName + " has changed credit account conditions");
                        break;
                    default:
                        break;
                }
            }
        }

    }

    /**
     * funciton to notify all bank accounts of specific client
     * @param client
     */
    public void notifyBankAccounts(Client client){
        for( IBankAccount bankAccount : BANK_ACCOUNT_BASE.get(client.getClientId())){
            bankAccount.isSuspicious(client);
        }
    }



    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public int getBankId() {
        return bankId;
    }

    public void setBankId(int bankId) {
        this.bankId = bankId;
    }

    public float getDebitPercentage() {
        return debitPercentage;
    }

    public void setDebitPercentage(float debitPercentage) {
        this.debitPercentage = debitPercentage;
    }

    public float getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(float creditLimit) {
        this.creditLimit = creditLimit;
    }

    public float getCreditCommission() {
        return creditCommission;
    }

    public void setCreditCommission(float creditCommission) {
        this.creditCommission = creditCommission;
    }

    public float getSuspiciousTransactionLimit() {
        return suspiciousTransactionLimit;
    }

    public void setSuspiciousTransactionLimit(float suspiciousTransactionLimit) {
        this.suspiciousTransactionLimit = suspiciousTransactionLimit;
    }

    public LinkedList<DepositPercentage> getDepositPercentage() {
        return DEPOSIT_PERCENTAGE;
    }

    public void setDepositPercentage(LinkedList<DepositPercentage> depositPercentage) {
        this.DEPOSIT_PERCENTAGE = depositPercentage;
    }

    public LinkedList<Client> getClientsBase() {
        return CLIENTS_BASE;
    }

    public void setClientsBase(LinkedList<Client> clientsBase) {
        this.CLIENTS_BASE = clientsBase;
    }

    public LinkedList<LinkedList<IBankAccount>> getBankAccountBase() {
        return BANK_ACCOUNT_BASE;
    }

    public void setBankAccountBase(LinkedList<LinkedList<IBankAccount>> bankAccountBase) {
        this.BANK_ACCOUNT_BASE = bankAccountBase;
    }

}

