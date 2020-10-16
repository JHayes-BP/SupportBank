package training.supportbank;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import com.opencsv.CSVReaderHeaderAware;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class Main {

    public static void main(String args[]) throws FileNotFoundException, IOException {

        List<Transactions> allTransactions = addTransactions(); //creates list of all transactions
        List<Accounts> allAccounts = createAccountList(allTransactions); //creates accounts and adds them to list
        calculateAccountBalances(allAccounts, allTransactions); //passes in transaction list and account list, and generates balances for each account
        Accounts accountName = getAccountNameInput(allAccounts); // asks for input on account name

        if (accountName == null){
            for (int i = 0; i < allAccounts.size(); i ++){ //prints all transactions for account per account passed in
                printAllTransactionsForAccount(allTransactions, allAccounts.get(i));
            }
        }else{
            printAllTransactionsForAccount(allTransactions,accountName);
        }
        //printAllTransactionsForAccount(allTransactions,account);

    }  

    private static final Logger LOGGER = LogManager.getLogger();

    public static Accounts validateAccount(String accountName, List<Accounts> allAccounts){
        Accounts blankAccount = null;
        for (int i = 0; i < allAccounts.size(); i++){
            if (allAccounts.get(i).getAccountName().equals(accountName)){
                blankAccount = allAccounts.get(i);
                break;
            }
        }
        return blankAccount;
    }


    public static void calculateAccountBalances(List<Accounts> allAccounts,List<Transactions> allTransactions){
        for (int i = 0; i < allAccounts.size(); i++){ // for every account name find transactions involving that account and use it to calculate balance
            String name = allAccounts.get(i).getAccountName();
            float balance = 0f;
            for( int x = 0; x < allTransactions.size(); x++){ //passes in current account name, through all transactions
                if (allTransactions.get(x).getRecipient().equals(name)){ //adds to balance when owed money
                    balance += allTransactions.get(x).getMoneyTransferred();
                }
                if (allTransactions.get(x).getSender().equals(name)){ //deducts from balance when owing money
                    balance -= allTransactions.get(x).getMoneyTransferred();
                }
            }
            allAccounts.get(i).setAccountBalance(balance); //takes total balance of account and sets it to the account balance tied to account
        }
    }

    // public static void printAllTransactions(List<Transactions> allTransactions){ //just prints out the list of transactions
    //     for(int i = 0; i < allTransactions.size(); i++){
    //         allTransactions.get(i).displayTransaction();
    //     }
    // }

    public static Accounts getAccountNameInput(List<Accounts> allAccounts){//gets account name input, need to do input exception handling, and account validation
        Scanner sc = new Scanner(System.in);
        boolean typed = false;
        Accounts accountToSearch = null;
        while (!typed){
            try{
                System.out.print("Please type in a specific Account or All: ");
                String input = sc.nextLine();
                if (input.equals("all")){
                    break;
                }
                accountToSearch = validateAccount(input, allAccounts);
                typed = true;
            }catch(Exception e){
                System.out.print("Not a valid Account Name. Please try again! ");
            }
        }
        sc.close();
        return accountToSearch;
    }

    public static List<Accounts> createAccountList(List<Transactions> allTransactions){ //searches through transactions and creates accounts for all names mentioned
        List<String> allAccountNames = new ArrayList<>(); //empty string list for used account names
        List<Accounts> allAccounts = new ArrayList<>(); //empty list for whole accounts
        for (int i = 0;i < allTransactions.size(); i++){ //goes through transaction list and searches for account names
            String sender = allTransactions.get(i).getSender();
            if(!allAccountNames.contains(sender)){ //if account name is "new", creates new account and adds it to account list
                allAccountNames.add(sender); //adds name to names list
                allAccounts.add(new Accounts(sender)); //creates new account under unique name
                LOGGER.debug("Created account with name: " + sender);
            }
        }
        return allAccounts;
    }



    public static void printAllTransactionsForAccount(List<Transactions> allTransactions, Accounts account){ //prints out list of transactions for a specific account, 
        for (int i = 0; i < allTransactions.size(); i++){
            String recipient = allTransactions.get(i).getRecipient();
            String sender = allTransactions.get(i).getSender();
            if (recipient.equals(account.getAccountName()) || sender.equals(account.getAccountName())){
                allTransactions.get(i).displayTransaction();
            }
        }
        System.out.println(account.getAccountName() + " has an account balance of " + Float.toString(account.getAccountBalance()) + "\n");

    }

    public static List<Transactions> addTransactions() throws IOException{ //adds all transactions to the transaction class and then adds them to a list of all transaction
        List<Transactions> transactionsList = new ArrayList<>();
        List<String> strFiles = new ArrayList<>();
        strFiles.add("C:\\Users\\9558bw\\OneDrive - BP\\Documents\\SupportBank\\Transactions2014.csv");
        strFiles.add("C:\\Users\\9558bw\\OneDrive - BP\\Documents\\SupportBank\\DodgyTransactions2015.csv");
        for (String file : strFiles){
            CSVReaderHeaderAware reader = new CSVReaderHeaderAware(new FileReader(file));
            while(true){
                Map <String,String> values = reader.readMap();
                if(values == null){
                    break;
                }
                try{
                    transactionsList.add(new Transactions(values.get("Date"),values.get("From"),values.get("To"),Float.parseFloat(values.get("Amount")),values.get("Narrative")));
                    LOGGER.debug("created account " + transactionsList.get(transactionsList.size()-1));
                }catch(NumberFormatException e){
                    LOGGER.debug("Transaction creation failed  Reason: " + e);
                    System.out.println("Transaction creation failed, skipping account and storing transaction details in log file.");
                    LOGGER.debug ("Transaction: " + values);
                }
                
            }
        }
        return transactionsList;
    }

    

    //need to read elements from file 
    //need to find participants in transaction
    //need to find individual transaction money needed to be given/taken from the bank per transaction per person
    //need to find total transaction money for all transactions combined per person, that is to be given or taken from the bank
    //for each individual transaction, display the reason (example: pokemon cards)


}
