package com.school.final_project;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "parents")
public class Parent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String parentId;

    private String name;

    @Column(unique = true)
    private String email;

    @Column(unique = true, nullable = false)
    private String username;

    private String passwordHash;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Child> children = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "parent_id")
    private List<Chore> chores = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "parent_id")
    private List<StoreItem> storeInventory = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "parent_id")
    private List<Transaction> transactions = new ArrayList<>();

    public Parent() {
    }

    public Parent(String parentId, String name, String email, String username) {
        this.parentId = parentId;
        this.name = name;
        this.email = email;
        this.username = username;
        this.children = new ArrayList<>();
        this.chores = new ArrayList<>();
        this.storeInventory = new ArrayList<>();
        this.transactions = new ArrayList<>();
    }

    public String getParentId() {
        return parentId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public ArrayList<Child> getChildren() {
        return new ArrayList<>(children);
    }

    public ArrayList<Chore> getChores() {
        return new ArrayList<>(chores);
    }

    public ArrayList<StoreItem> getStoreInventory() {
        return new ArrayList<>(storeInventory);
    }

    public ArrayList<Transaction> getTransactions() {
        return new ArrayList<>(transactions);
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public void setChildren(ArrayList<Child> children) {
        this.children = children;
    }

    public void setChores(ArrayList<Chore> chores) {
        this.chores = chores;
    }

    public void setStoreInventory(ArrayList<StoreItem> storeInventory) {
        this.storeInventory = storeInventory;
    }

    public void setTransactions(ArrayList<Transaction> transactions) {
        this.transactions = transactions;
    }

    // Methods that modify the actual lists (not copies)

    public void addChore(Chore chore) {
        chores.add(chore);
    }

    public boolean removeChore(String choreId) {
        return chores.removeIf(c -> c.getChoreId().equals(choreId));
    }

    public Chore getChoreById(String choreId) {
        return chores.stream()
                .filter(c -> c.getChoreId().equals(choreId))
                .findFirst()
                .orElse(null);
    }

    public void addStoreItem(StoreItem item) {
        storeInventory.add(item);
    }

    public boolean removeStoreItem(String itemId) {
        return storeInventory.removeIf(i -> i.getItemID().equals(itemId));
    }

    public StoreItem getStoreItemById(String itemId) {
        return storeInventory.stream()
                .filter(i -> i.getItemID().equals(itemId))
                .findFirst()
                .orElse(null);
    }

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    public void addChild(Child child) {
        children.add(child);
    }

    public Child createChildAccount(String childName, String userName, String childId) {
        if (childName == null || childName.trim().isEmpty()) {
            throw new IllegalArgumentException("Child name cannot be empty");
        }
        Child newChild = new Child(childName, userName, this, childId);
        children.add(newChild);
        return newChild;
    }

    public void payChildForChore(Child child, Chore chore) {
        Transaction transaction = new Transaction(
                TransactionType.ALLOWANCE,
                (float) chore.getChorePrice(),
                child.getChildId(),
                "Completed: " + chore.getChoreName(),
                LocalDateTime.now());

        this.transactions.add(transaction);
        child.addTransaction(transaction);
        child.addBalance(chore.getChorePrice());
    }
}