package com.school.final_project;

public class Chore {
    private String choreName;
    private String choreDescription;
    private double chorePrice;
    private String assignedChildId;
    private boolean isAvailable;

    public Chore(String choreName, String choreDescription, double chorePrice, String assignedChildId,
            boolean isAvailabe) {
        this.choreName = choreName;
        this.choreDescription = choreDescription;
        this.chorePrice = chorePrice;
        this.assignedChildId = assignedChildId;
        this.isAvailable = isAvailabe;
    }

    public void changePrice(double newPrice) {
        this.chorePrice = newPrice;
    }

    public void setChoreName(String newName) {
        this.choreName = newName;
    }

    public void setChoreDescription(String newDescription) {
        this.choreDescription = newDescription;
    }

    public void setChorePrice(double newPrice) {
        this.chorePrice = newPrice;
    }

    public void setAssignedChildId(String newChildId) {
        this.assignedChildId = newChildId;
    }

    public void setAvailable(boolean newAvailable) {
        this.isAvailable = newAvailable;
    }

    public String getChoreName() {
        return choreName;
    }

    public String getChoreDescription() {
        return choreDescription;
    }

    public double getChorePrice() {
        return chorePrice;
    }

    public String getAssignedChildId() {
        return assignedChildId;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

}
