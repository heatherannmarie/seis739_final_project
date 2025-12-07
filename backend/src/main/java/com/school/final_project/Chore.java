package com.school.final_project;

import jakarta.persistence.*;

@Entity
@Table(name = "chores")
public class Chore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String choreId;

    private String choreName;
    private String choreDescription;
    private double chorePrice;
    private String assignedChildId;
    private boolean isAvailable;

    public Chore() {
    }

    public Chore(String choreName, String choreDescription, double chorePrice,
            String assignedChildId, boolean isAvailable, String choreId) {
        this.choreName = choreName;
        this.choreDescription = choreDescription;
        this.chorePrice = chorePrice;
        this.assignedChildId = assignedChildId;
        this.isAvailable = isAvailable;
        this.choreId = choreId;
    }

    public String getChoreId() {
        return choreId;
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

    public void setChoreName(String choreName) {
        this.choreName = choreName;
    }

    public void setChoreDescription(String choreDescription) {
        this.choreDescription = choreDescription;
    }

    public void setChorePrice(double chorePrice) {
        this.chorePrice = chorePrice;
    }

    public void setAssignedChildId(String assignedChildId) {
        this.assignedChildId = assignedChildId;
    }

    public void setAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    public void changePrice(double newPrice) {
        this.chorePrice = newPrice;
    }
}