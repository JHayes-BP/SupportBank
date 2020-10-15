package training.supportbank;


public class Accounts {

    private String accountName;
    private float totalBalance;


    public Accounts(String accountName){
        this.accountName = accountName;
        totalBalance = 0f;
    }

    public String getAccountName(){
        return accountName;
    }

    public float getAccountBalance(){
        return totalBalance;
    }

    public void setAccountBalance(float num){
        totalBalance = num;
    }
}
