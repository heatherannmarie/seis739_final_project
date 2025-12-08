package com.school.final_project;

import jakarta.persistence.*;
import main.java.com.school.final_project.ChoreStatus;

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
    @Enumerated(EnumType.STRING) // Stores as "AVAILABLE", "PENDING", "COMPLETED"
    @Column(columnDefinition = "VARCHAR(20) DEFAULT 'AVAILABLE'")
    private ChoreStatus status;

    public Chore() {
    }

    public Chore(String choreName, String choreDescription, double chorePrice,
            String assignedChildId, String choreId) {
        this.choreName = choreName;
        this.choreDescription = choreDescription;
        this.chorePrice = chorePrice;
        this.assignedChildId = assignedChildId;
        this.choreId = choreId;
        this.status = ChoreStatus.AVAILABLE;
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

    public void changePrice(double newPrice) {
        this.chorePrice = newPrice;
    }

    public ChoreStatus getStatus() {
        return status;
    }

    public void setStatus(ChoreStatus status) {
        this.status = status;
    }
}