package com.school.final_project;

import java.util.ArrayList;

public class Child extends User {
    private String childId;
    private String name;
    private String username;
    private String parentId;
    private double balance;
    private ArrayList<Transaction> transactionHistory;

    public Child(String name, String username, String parentId, String childID) {
        this.name = name;
        this.username = username;
        this.parentId = parentId;
        this.balance = 0.0;
        this.childId = childID;
    }

    public double getBalance() {
        return balance;
    }

    public void addBalance(double amount) {
        balance += amount;
    }

    public void subtractBalance(double amount) {
        balance -= amount;
    }

    public ArrayList<Transaction> getTransactionHistory() {
        return transactionHistory;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getParentId() {
        return parentId;
    }

    public void setName(String newName) {
        this.name = newName;
    }

    public void setUsername(String newUsername) {
        this.username = newUsername;
    }

    public void setParentId(String newParentId) {
        this.parentId = newParentId;
    }

    public void setBalance(double newBalance) {
        this.balance = newBalance;
    }

    public void setTransactionHistory(ArrayList<Transaction> newTransactionHistory) {
        this.transactionHistory = newTransactionHistory;
    }
}
