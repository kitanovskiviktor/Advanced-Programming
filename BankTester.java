package com.example.np_lab2_zad1;


import java.util.*;
import java.util.stream.Collectors;
import java.util.ArrayList;

class Account {

    String name;
    long ID;
    double balance;

    public Account(String name, double balance) {
        this.name = name;
        this.balance = balance;
        this.ID = new Random().nextInt(999999 - 111111 + 1) + 111111;
    }

    public double getBalance() {
        return balance;
    }

    public String getName() {
        return name;
    }

    public long getId(){
        return ID;
    }

    public void setBalance(double balance){
        this.balance = balance;
    }

    @Override
    public String toString(){
        String sb = "";
        sb += "\n";
        sb += "Name: " + this.name + "\n";
        sb += "Balance: " + this.balance;
        return sb;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Account)) return false;
        Account account = (Account) obj;
        return ID == account.ID && Double.compare(account.balance, balance) == 0 && name.equals(account.name);
    }
}

class Transaction {

    long fromID;
    long toID;
    String description;
    double amount;


    public Transaction() {

    }

    public Transaction(long fromID, long toID, String description, double amount){
        this.fromID = fromID;
        this.toID = toID;
        this.description = description;
        this.amount = amount;
    }

    public double getAmount(){
        return this.amount;
    }

    public long getFromId() {
        return this.fromID;
    }

    public long getToId() {
        return this.toID;
    }

    public String getDescription() {
        return this.description;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Transaction)) return false;
        Transaction transaction = (Transaction) obj;
        return fromID == transaction.fromID && toID == transaction.toID &&
                Double.compare(transaction.amount, amount) == 0 && description.equals(transaction.description);
    }
}

class FlatAmountProvisionTransaction extends Transaction {

    private double flatAmountProvision;

    public FlatAmountProvisionTransaction(long fromId, long toId,double amount, double flatProvision){
        this.fromID = fromId;
        this.toID = toId;
        this.amount = amount;
        this.description = "FlatAmount";
        this.flatAmountProvision = flatProvision;
    }

    public double getFlatAmount() {
        return flatAmountProvision;
    }

    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj)) return false;
        if (!(obj instanceof FlatAmountProvisionTransaction)) return false;
        FlatAmountProvisionTransaction that = (FlatAmountProvisionTransaction) obj;
        return Double.compare(that.flatAmountProvision, flatAmountProvision) == 0;
    }
}

class FlatPercentProvisionTransaction extends Transaction {

    private int flatPercentProvision;

    public FlatPercentProvisionTransaction (long fromId, long toId, double amount, int centsPerDolar){
        this.fromID = fromId;
        this.toID = toId;
        this.amount = amount;
        this.description = "FlatPercent";
        this.flatPercentProvision = centsPerDolar;
    }

    public int getPercent() {
        return this.flatPercentProvision;
    }

    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj)) return false;
        if (!(obj instanceof FlatPercentProvisionTransaction)) return false;
        FlatPercentProvisionTransaction that = (FlatPercentProvisionTransaction) obj;
        return flatPercentProvision == that.flatPercentProvision;
    }
}

class Bank {
    private Account [] accounts;

    private ArrayList<Transaction> transactions;
    private String name;
    private int totalSumOfTransaction;
    private int totalSumOfProvision;

    public Bank(String name, Account accounts[]) {
        this.name = name;
        this.accounts = new Account[accounts.length];
        System.arraycopy(accounts, 0, this.accounts, 0, accounts.length);
        this.transactions = new ArrayList<>();
    }

    public boolean makeTransaction(Transaction t) {
        long senderID = t.fromID;
        long receiverID = t.toID;
        double provision = 0.0;

        Account senderAccount = null;
        Account receiverAccount = null;

        for(Account account : this.accounts){
            if(account.ID == senderID){
                senderAccount = account;
            }
            if(account.ID == receiverID){
                receiverAccount = account;
            }
        }


        if(senderAccount == null || receiverAccount == null){
            return false;
        }

        if(senderAccount.balance < t.amount) {
            return false;
        }

        if (t instanceof FlatAmountProvisionTransaction) {
            FlatAmountProvisionTransaction flatAmountTransaction = (FlatAmountProvisionTransaction) t;
            provision += flatAmountTransaction.getFlatAmount();
        } else if (t instanceof FlatPercentProvisionTransaction) {
            FlatPercentProvisionTransaction flatPercentTransaction = (FlatPercentProvisionTransaction) t;
            provision = (flatPercentTransaction.getPercent() / 100.0) * t.getAmount();
        }

        for(Account account : this.accounts){
            if(senderAccount.equals(account)){
                account.balance -= t.amount + provision;
            }
            if(receiverAccount.equals(account)){
                account.balance += t.amount;
            }
        }

        transactions.add(t);

        return true;
    }


    public double totalTransfers() {
        double sum = 0;
        for(Transaction transaction : this.transactions){
            sum += transaction.amount;
        }
        return sum;
    }

    public double totalProvision(){
        double totalProvision = 0.0;

        for (Transaction transaction : this.transactions) {
            if (transaction instanceof FlatAmountProvisionTransaction) {
                FlatAmountProvisionTransaction flatAmountTransaction = (FlatAmountProvisionTransaction) transaction;
                totalProvision += flatAmountTransaction.getFlatAmount();
            } else if (transaction instanceof FlatPercentProvisionTransaction) {
                FlatPercentProvisionTransaction flatPercentTransaction = (FlatPercentProvisionTransaction) transaction;
                double provision = (flatPercentTransaction.getPercent() / 100.0) * transaction.getAmount();
                totalProvision += provision;
            }
        }

        return totalProvision;
    }

    @Override
    public String toString() {
        String sb = "";
        sb += "Name: " + this.name + "\n";
        for(int i = 0; i<accounts.length; i++) {
            sb += accounts[i].toString();
        }
        return sb;
    }

    public Account [] getAccounts(){
        return accounts;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Bank)) return false;
        Bank bank = (Bank) obj;
        return name.equals(bank.name) && Arrays.equals(accounts, bank.accounts) && transactions.equals(bank.transactions);
    }


}

public class BankTester {

    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        String test_type = jin.nextLine();
        switch (test_type) {
            case "typical_usage":
                testTypicalUsage(jin);
                break;
            case "equals":
                testEquals();
                break;
        }
        jin.close();
    }

    private static double parseAmount (String amount){
        return Double.parseDouble(amount.replace("$",""));
    }

    private static void testEquals() {
        Account a1 = new Account("Andrej", 20.0);
        Account a2 = new Account("Andrej", 20.0);
        Account a3 = new Account("Andrej", 30.0);
        Account a4 = new Account("Gajduk", 20.0);
        List<Account> all = Arrays.asList(a1, a2, a3, a4);
        if (!(a1.equals(a1)&&!a1.equals(a2)&&!a2.equals(a1)&&!a3.equals(a1)
                && !a4.equals(a1)
                && !a1.equals(null))) {
            System.out.println("Your account equals method does not work properly.");
            return;
        }
        Set<Long> ids = all.stream().map(Account::getId).collect(Collectors.toSet());
        if (ids.size() != all.size()) {
            System.out.println("Different accounts have the same IDS. This is not allowed");
            return;
        }
        FlatAmountProvisionTransaction fa1 = new FlatAmountProvisionTransaction(10, 20, 20.0, 10.0);
        FlatAmountProvisionTransaction fa2 = new FlatAmountProvisionTransaction(20, 20, 20.0, 10.0);
        FlatAmountProvisionTransaction fa3 = new FlatAmountProvisionTransaction(20, 10, 20.0, 10.0);
        FlatAmountProvisionTransaction fa4 = new FlatAmountProvisionTransaction(10, 20, 50.0, 50.0);
        FlatAmountProvisionTransaction fa5 = new FlatAmountProvisionTransaction(30, 40, 20.0, 10.0);
        FlatPercentProvisionTransaction fp1 = new FlatPercentProvisionTransaction(10, 20, 20.0, 10);
        FlatPercentProvisionTransaction fp2 = new FlatPercentProvisionTransaction(10, 20, 20.0, 10);
        FlatPercentProvisionTransaction fp3 = new FlatPercentProvisionTransaction(10, 10, 20.0, 10);
        FlatPercentProvisionTransaction fp4 = new FlatPercentProvisionTransaction(10, 20, 50.0, 10);
        FlatPercentProvisionTransaction fp5 = new FlatPercentProvisionTransaction(10, 20, 20.0, 30);
        FlatPercentProvisionTransaction fp6 = new FlatPercentProvisionTransaction(30, 40, 20.0, 10);
        if (fa1.equals(fa1) &&
                !fa2.equals(null) &&
                fa2.equals(fa1) &&
                fa1.equals(fa2) &&
                fa1.equals(fa3) &&
                !fa1.equals(fa4) &&
                !fa1.equals(fa5) &&
                !fa1.equals(fp1) &&
                fp1.equals(fp1) &&
                !fp2.equals(null) &&
                fp2.equals(fp1) &&
                fp1.equals(fp2) &&
                fp1.equals(fp3) &&
                !fp1.equals(fp4) &&
                !fp1.equals(fp5) &&
                !fp1.equals(fp6)) {
            System.out.println("Your transactions equals methods do not work properly.");
            return;
        }
        Account accounts[] = new Account[]{a1, a2, a3, a4};
        Account accounts1[] = new Account[]{a2, a1, a3, a4};
        Account accounts2[] = new Account[]{a1, a2, a3};
        Account accounts3[] = new Account[]{a1, a2, a3, a4};

        Bank b1 = new Bank("Test", accounts);
        Bank b2 = new Bank("Test", accounts1);
        Bank b3 = new Bank("Test", accounts2);
        Bank b4 = new Bank("Sample", accounts);
        Bank b5 = new Bank("Test", accounts3);

        if (!(b1.equals(b1) &&
                !b1.equals(null) &&
                !b1.equals(b2) &&
                !b2.equals(b1) &&
                !b1.equals(b3) &&
                !b3.equals(b1) &&
                !b1.equals(b4) &&
                b1.equals(b5))) {
            System.out.println("Your bank equals method do not work properly.");
            return;
        }
        accounts[2] = a1;
        if (!b1.equals(b5)) {
            System.out.println("Your bank equals method do not work properly.");
            return;
        }
        long from_id = a2.getId();
        long to_id = a3.getId();
        Transaction t = new FlatAmountProvisionTransaction(from_id, to_id, 3.0, 3.0);
        b1.makeTransaction(t);
        if (b1.equals(b5)) {
            System.out.println("Your bank equals method do not work properly.");
            return;
        }
        b5.makeTransaction(t);
        if (!b1.equals(b5)) {
            System.out.println("Your bank equals method do not work properly.");
            return;
        }
        System.out.println("All your equals methods work properly.");
    }

    private static void testTypicalUsage(Scanner jin) {
        String bank_name = jin.nextLine();
        int num_accounts = jin.nextInt();
        jin.nextLine();
        Account accounts[] = new Account[num_accounts];
        for (int i = 0; i < num_accounts; ++i)
            accounts[i] = new Account(jin.nextLine(),  parseAmount(jin.nextLine()));
        Bank bank = new Bank(bank_name, accounts);
        while (true) {
            String line = jin.nextLine();
            switch (line) {
                case "stop":
                    return;
                case "transaction":
                    String descrption = jin.nextLine();
                    double amount = parseAmount(jin.nextLine());
                    double parameter = parseAmount(jin.nextLine());
                    int from_idx = jin.nextInt();
                    int to_idx = jin.nextInt();
                    jin.nextLine();
                    Transaction t = getTransaction(descrption, from_idx, to_idx, amount, parameter, bank);
                    System.out.println("Transaction amount: " + String.format("%.2f$",t.getAmount()));
                    System.out.println("Transaction description: " + t.getDescription());
                    System.out.println("Transaction successful? " + bank.makeTransaction(t));
                    break;
                case "print":
                    System.out.println(bank.toString());
                    System.out.println("Total provisions: " + String.format("%.2f$",bank.totalProvision()));
                    System.out.println("Total transfers: " + String.format("%.2f$",bank.totalTransfers()));
                    System.out.println();
                    break;
            }
        }
    }

    private static Transaction getTransaction(String description, int from_idx, int to_idx, double amount, double o, Bank bank) {
        switch (description) {
            case "FlatAmount":
                return new FlatAmountProvisionTransaction(bank.getAccounts()[from_idx].getId(),
                        bank.getAccounts()[to_idx].getId(), amount, o);
            case "FlatPercent":
                return new FlatPercentProvisionTransaction(bank.getAccounts()[from_idx].getId(),
                        bank.getAccounts()[to_idx].getId(), amount, (int) o);
        }
        return null;
    }


}
