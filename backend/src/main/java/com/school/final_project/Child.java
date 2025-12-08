package com.school.final_project;

import jakarta.persistence.*;
import com.school.final_project.ChoreStatus;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "children")
public class Child {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String childId;

    private String name;
    private String username;
    private double balance;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "parent_id", nullable = false)
    private Parent parent;

    @Column(name = "parent_id_string")
    private String parentIdString;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "child_id")
    private List<Transaction> transactionHistory = new ArrayList<>();

    public Child() {
    }

    public Child(String name, String username, Parent parent, String childID) {
        this.name = name;
        this.username = username;
        this.parent = parent;
        this.parentIdString = parent.getParentId();
        this.balance = 0.0;
        this.childId = childID;
        this.transactionHistory = new ArrayList<>();
    }

    public String getChildId() {
        return childId;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public double getBalance() {
        return balance;
    }

    public Parent getParent() {
        return parent;
    }

    public String getParentId() {
        return parentIdString;
    }

    public ArrayList<Transaction> getTransactionHistory() {
        return new ArrayList<>(transactionHistory);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void setParent(Parent parent) {
        this.parent = parent;
    }

    public void setParentId(String parentId) {
        this.parentIdString = parentId;
    }

    public void setTransactionHistory(ArrayList<Transaction> transactionHistory) {
        this.transactionHistory = transactionHistory;
    }

    public void addBalance(double amount) {
        balance += amount;
    }

    public void subtractBalance(double amount) {
        balance -= amount;
    }

    public List<Chore> viewAvailableChores() {
        return parent.getChores().stream()
                .filter(c -> c.getStatus() == ChoreStatus.AVAILABLE)
                .toList();
    }

    public List<StoreItem> viewStoreItems() {
        return parent.getStoreInventory();
    }

    public void selectChore(String choreId) {
        Chore chore = parent.getChores().stream()
                .filter(c -> c.getChoreId().equals(choreId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Chore not found"));

        if (chore.getStatus() != ChoreStatus.AVAILABLE) {
            throw new RuntimeException("Chore is not available");
        }

        chore.setAssignedChildId(this.childId);
    }

    public Transaction markChoreComplete(String choreId) {
        Chore chore = parent.getChores().stream()
                .filter(c -> c.getChoreId().equals(choreId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Chore not found"));

        if (!chore.getAssignedChildId().equals(this.childId)) {
            throw new RuntimeException("This chore is not assigned to you");
        }

        parent.payChildForChore(this, chore);
        return this.transactionHistory.get(this.transactionHistory.size() - 1);
    }

    public Transaction purchaseItem(String itemId) {
        StoreItem item = parent.getStoreInventory().stream()
                .filter(i -> i.getItemID().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Item not found"));

        if (!item.isAvailable()) {
            throw new RuntimeException("Item is out of stock");
        }

        if (this.balance < item.getItemPrice()) {
            throw new RuntimeException("Insufficient balance");
        }

        item.purchase();
        this.balance -= item.getItemPrice();

        Transaction transaction = new Transaction(
                TransactionType.PURCHASE,
                (float) item.getItemPrice(),
                this.childId,
                "Purchased: " + item.getItemName(),
                java.time.LocalDateTime.now());

        this.transactionHistory.add(transaction);
        parent.addTransaction(transaction);

        return transaction;
    }

    public void addTransaction(Transaction transaction) {
        this.transactionHistory.add(transaction);
    }
}