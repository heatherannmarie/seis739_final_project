package com.school.final_project;

public class StoreItem {
    private String itemName;
    private int availableInventory;
    private double itemPrice;
    private String itemID;

    public StoreItem(String itemName, int availableInventory, double itemPrice, String itemID) {
        this.itemName = itemName;
        this.availableInventory = availableInventory;
        this.itemPrice = itemPrice;
        this.itemID = itemID;
    }

    public boolean isAvailable() {
        return availableInventory > 0;
    }

    public boolean purchase() {
        if (availableInventory > 0) {
            availableInventory--;
            return true;
        }
        return false;
    }

    public String getItemName() {
        return itemName;
    }

    public double getItemPrice() {
        return itemPrice;
    }

    public int getAvailableInventory() {
        return availableInventory;
    }

    public String getItemID() {
        return itemID;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setItemPrice(float itemPrice) {
        this.itemPrice = itemPrice;
    }

    public void setAvailableInventory(int availableInventory) {
        this.availableInventory = availableInventory;
    }
}
