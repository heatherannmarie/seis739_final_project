package com.school.final_project;

import java.time.*;

public class Transaction {
    private String type;
    private float amount;
    private String childId;
    private String description;
    private LocalDateTime timestamp;

    public Transaction(String type, float amount, String childId, String description, LocalDateTime timestamp) {
        this.type = type;
        this.amount = amount;
        this.childId = childId;
        this.description = description;
        this.timestamp = timestamp;
    }
}
