package training.supportbank;

public class Transactions {
    private String date;
    private String sender;
    private String recipient;
    private float moneyTransferred;
    private String description;

    public Transactions(String date, String sender, String recipient, float moneyTransferred, String description){
        this.date = date;
        this.sender = sender;
        this.recipient = recipient;
        this.moneyTransferred = moneyTransferred;
        this.description = description;
    }

    public String getSender(){
        return sender;
    }

    public String getRecipient(){
        return recipient;
    }

    public float getMoneyTransferred(){
        return moneyTransferred;
    }


    public void displayTransaction(){
        System.out.println("On " + date + ", " + sender + " owes " + recipient + " " + Float.toString(moneyTransferred) + " Pounds. Reason: " + description);
    }


}
