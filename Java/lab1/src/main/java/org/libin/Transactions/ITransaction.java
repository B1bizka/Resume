package org.libin.Transactions;

public interface ITransaction {
    /**
     * execute transaction
     * @return
     */
    public int execute();

    /**
     * reverse transaction
     * can do it only once for a transaction
     */
    public void reverse();
    public String getName();
    public float showBalance();
    public int getId();

    /**
     * transaction was successfully compiled, so unfinished transactions won't be added to the history
     * @return
     */
    public boolean isCompleted();
}
