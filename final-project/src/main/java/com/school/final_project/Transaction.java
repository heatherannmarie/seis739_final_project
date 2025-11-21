package com.school.final_project;

import java.time.*;

public class Transaction {
    private TransactionType type;
    private double amount;
    private String childId;
    private String description;
    private LocalDateTime timestamp;

    public Transaction(TransactionType type, double amount, String childId, String description, LocalDateTime timestamp) {
        this.type = type;
        this.amount = amount;
        this.childId = childId;
        this.description = description;
        this.timestamp = timestamp;
    }

    public TransactionType getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    public String getChildId() {
        return childId;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
