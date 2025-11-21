package com.school.final_project;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Parent {

    private String parentId;
    private String name;
    private ArrayList<Child> children;
    private ArrayList<Chore> chores;
    private ArrayList<StoreItem> storeInventory;
    private ArrayList<Transaction> transactions;

    public Parent(String parentId, String name) {
        this.parentId = parentId;
        this.name = name;
        this.children = new ArrayList<>();
        this.chores = new ArrayList<>();
        this.storeInventory = new ArrayList<>();
        this.transactions = new ArrayList<>();
    }

    public String getParentId() {
        return parentId;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Child> getChildren() {
        return children;
    }

    public ArrayList<Chore> getChores() {
        return chores;
    }

    public ArrayList<StoreItem> getStoreInventory() {
        return storeInventory;
    }

    public ArrayList<Transaction> getTransactions() {
        return transactions;
    }

    public void setParentId(String newParentId) {
        this.parentId = newParentId;
    }

    public void setName(String newName) {
        this.name = newName;
    }

    public void setChildren(ArrayList<Child> newChildren) {
        this.children = newChildren;
    }

    public void setChores(ArrayList<Chore> newChores) {
        this.chores = newChores;
    }

    public void setStoreInventory(ArrayList<StoreItem> newStoreInventory) {
        this.storeInventory = newStoreInventory;
    }

    public void setTransactions(ArrayList<Transaction> newTransactions) {
        this.transactions = newTransactions;
    }

    public void addChore(Chore chore) {
        chores.add(chore);
    }

    public void addStoreItem(StoreItem item) {
        storeInventory.add(item);
    }

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    public Child createChildAccount(String childName, String userName, String childId) {
        if (childName == null || childName.trim().isEmpty()) {
            throw new IllegalArgumentException("Child name cannot be empty");
        }

        Child newChild = new Child(childName, userName, this.parentId, childId, this);
        children.add(newChild);

        // to do: this method needs to return log in credentials of some kind

        return newChild;
    }

    public void payChildForChore(Child child, Chore chore) {

        Transaction transaction = new Transaction(
                TransactionType.ALLOWANCE,
                chore.getChorePrice(),
                child.getChildId(),
                "Completed: " + chore.getChoreName(),
                LocalDateTime.now());

        this.transactions.add(transaction);

        child.getTransactionHistory().add(transaction);

        child.addBalance(chore.getChorePrice());

        chore.setAvailable(false);
    }

    public void assignChoreToChild(Chore chore, String childID) {
        chore.setAssignedChildId(childID);
    }
}
