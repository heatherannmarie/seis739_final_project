package com.school.final_project;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Child {
    private String childId;
    private String name;
    private String username;
    private String parentId;
    private Parent parent;
    private double balance;
    private ArrayList<Transaction> transactionHistory;

    public Child(String name, String username, String parentId, String childId, Parent parent) {
        this.name = name;
        this.username = username;
        this.parentId = parentId;
        this.balance = 0.0;
        this.childId = childId;
        this.transactionHistory = new ArrayList<>();
        this.parent = parent;
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

    public String getChildId() {
        return this.childId;
    }

    public void setChildID(String newChildId) {
        this.childId = newChildId;
    }

    public List<Chore> viewAvailableChores() {
        return this.parent.getChores().stream()
                .filter(Chore::isAvailable)
                .collect(Collectors.toList());
    }

    public ArrayList<StoreItem> viewStoreItems() {
        return this.parent.getStoreInventory();
    }

    public void selectChore(String choreId) {
        Chore chore = this.parent.getChores().stream()
                .filter(c -> c.getChoreId().equals(choreId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Chore not found"));

        if (!chore.isAvailable()) {
            throw new RuntimeException("Chore is not available");
        }

        chore.setAssignedChildId(this.childId);
    }

    public Transaction markChoreComplete(String choreId) {
        Chore chore = this.parent.getChores().stream()
                .filter(c -> c.getChoreId().equals(choreId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Chore not found"));

        if (!chore.getAssignedChildId().equals(this.childId)) {
            throw new RuntimeException("This chore is not assigned to you");
        }

        this.parent.payChildForChore(this, chore);
        return this.transactionHistory.get(this.transactionHistory.size() - 1);
    }

    public Transaction purchaseItem(String itemId) {
        StoreItem item = this.parent.getStoreInventory().stream()
                .filter(i -> i.getItemID().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Item not found"));

        if (!item.isAvailable()) {
            throw new RuntimeException("Item is out of stock");
        }

        if (this.balance < item.getItemPrice()) {
            throw new RuntimeException("Insufficient balance");
        }

        item.purchase();
        this.balance -= item.getItemPrice();

        Transaction transaction = new Transaction(
                TransactionType.PURCHASE,
                (float) item.getItemPrice(),
                this.childId,
                "Purchased: " + item.getItemName(),
                java.time.LocalDateTime.now());

        this.transactionHistory.add(transaction);
        this.parent.getTransactions().add(transaction);

        return transaction;
    }
}
