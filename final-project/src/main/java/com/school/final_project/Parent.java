package com.school.final_project;

import java.util.ArrayList;

public class Parent extends User {

    private String parentId;
    private String name;
    private ArrayList<Child> children;
    private ArrayList<Chore> chores;
    private ArrayList<StoreItem> storeInventory;
    private ArrayList<Transaction> transactions;

    public Child createChildAccount(String childName, String userName) {
        if (childName == null || childName.trim().isEmpty()) {
            throw new IllegalArgumentException("Child name cannot be empty");
        }

        Child newChild = new Child(childName, userName, this.parentId);
        children.add(newChild);

        return newChild;
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
}
