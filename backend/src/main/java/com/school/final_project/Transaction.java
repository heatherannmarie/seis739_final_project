package com.school.final_project;

import jakarta.persistence.*;
import java.time.*;

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type")
    private TransactionType type;

    @Column(name = "amount")
    private float amount;

    @Column(name = "child_id")
    private String childId;

    @Column(name = "description")
    private String description;

    @Column(name = "timestamp")
    private LocalDateTime timestamp;

    public Transaction() {
    }

    public Transaction(TransactionType type, double amount, String childId,
            String description, LocalDateTime timestamp) {
        this.type = type;
        this.amount = amount;
        this.childId = childId;
        this.description = description;
        this.timestamp = timestamp;
    }

    public Long getId() {
        return id;
    }

    public TransactionType getType() {
        return type;
    }

    public float getAmount() {
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

    public void setType(TransactionType type) {
        this.type = type;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setChildId(String childId) {
        this.childId = childId;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}