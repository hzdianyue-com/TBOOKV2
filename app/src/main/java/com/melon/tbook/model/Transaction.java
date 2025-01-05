package com.melon.tbook.model;

public class Transaction {
    private int id;
    private double amount;
    private String description;
    private String type; // "income" or "expense"
    private String subAccount;
    private String borrower;

    public Transaction(int id, double amount, String description, String type, String subAccount, String borrower) {
        this.id = id;
        this.amount = amount;
        this.description = description;
        this.type = type;
        this.subAccount = subAccount;
        this.borrower = borrower;
    }

    public Transaction(double amount, String description, String type, String subAccount, String borrower) {
        this.amount = amount;
        this.description = description;
        this.type = type;
        this.subAccount = subAccount;
        this.borrower = borrower;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSubAccount() {
        return subAccount;
    }

    public void setSubAccount(String subAccount) {
        this.subAccount = subAccount;
    }

    public String getBorrower() {
        return borrower;
    }

    public void setBorrower(String borrower) {
        this.borrower = borrower;
    }
}