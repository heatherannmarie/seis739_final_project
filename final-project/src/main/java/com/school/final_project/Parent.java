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

    public void addChore(Chore chore) {
        chores.add(chore);
    }

    public void addStoreItem(StoreItem item) {
        storeInventory.add(item);
    }
}
