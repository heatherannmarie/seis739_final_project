package com.school.final_project;

public class Chore {
    private String choreName;
    private String choreDescription;
    private float chorePrice;
    private String assignedChildId;
    private boolean isAvailable;

    public Chore(String choreName, String choreDescription, float chorePrice, String assignedChildId,
            boolean isAvailabe) {
        this.choreName = choreName;
        this.choreDescription = choreDescription;
        this.chorePrice = chorePrice;
        this.assignedChildId = assignedChildId;
        this.isAvailable = isAvailabe;
    }

    public void markComplete() {
        this.isAvailable = false;
    }

    public void makeAvailable() {
        this.isAvailable = true;
    }

    public void changePrice(float newPrice) {
        this.chorePrice = newPrice;
    }

    public void assignToChild(String childId) {
        this.assignedChildId = childId;
    }

    public String getChoreName() {
        return choreName;
    }

}
