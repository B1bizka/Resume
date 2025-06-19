package org.libin.BankSystems;

/**
 * pair structure to save deposit percentage info
 */
public class DepositPercentage {
    private float key;
    private float value;

    public float getKey() {
        return key;
    }
    public float getValue(){
        return value;
    }
    public void setKey(float newKey){
        key = newKey;
    }
    public void setValue(float newValue){value = newValue;}
}
