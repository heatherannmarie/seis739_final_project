package com.school.final_project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/children")
@CrossOrigin(origins = "*")
public class ChildController {

    @Autowired
    private DataSharing dataStore;

    @Autowired
    private TransactionRepository transactionRepository;

    @GetMapping("/{childId}")
    public Child getChild(@PathVariable String childId) {
        Child child = dataStore.getChild(childId);
        if (child == null) {
            throw new RuntimeException("Child not found");
        }
        return child;
    }

    @GetMapping("/{childId}/balance")
    public Map<String, Double> getBalance(@PathVariable String childId) {
        Child child = dataStore.getChild(childId);
        if (child == null) {
            throw new RuntimeException("Child not found");
        }

        Map<String, Double> response = new HashMap<>();
        response.put("balance", child.getBalance());
        return response;
    }

    @GetMapping("/{childId}/transactions")
    public List<Transaction> getTransactions(@PathVariable String childId) {
        Child child = dataStore.getChild(childId);
        if (child == null) {
            throw new RuntimeException("Child not found");
        }
        return transactionRepository.findByChildId(childId);
    }

    @GetMapping("/{childId}/available-chores")
    public ArrayList<Chore> getAvailableChores(@PathVariable String childId) {
        Child child = dataStore.getChild(childId);
        if (child == null) {
            throw new RuntimeException("Child not found");
        }

        Parent parent = dataStore.getParent(child.getParentId());
        if (parent == null) {
            throw new RuntimeException("Parent not found");
        }

        return parent.getChores();
    }

    @GetMapping("/{childId}/store-items")
    public ArrayList<StoreItem> getStoreItems(@PathVariable String childId) {
        Child child = dataStore.getChild(childId);
        if (child == null) {
            throw new RuntimeException("Child not found");
        }

        Parent parent = dataStore.getParent(child.getParentId());
        if (parent == null) {
            throw new RuntimeException("Parent not found");
        }

        return parent.getStoreInventory();
    }

    @PostMapping("/{childId}/chores/{choreId}/request-completion")
    public Chore requestChoreCompletion(
            @PathVariable String childId,
            @PathVariable String choreId) {

        Child child = dataStore.getChild(childId);
        if (child == null) {
            throw new RuntimeException("Child not found");
        }

        Parent parent = dataStore.getParent(child.getParentId());
        if (parent == null) {
            throw new RuntimeException("Parent not found");
        }

        Chore chore = parent.getChoreById(choreId);
        if (chore == null) {
            throw new RuntimeException("Chore not found");
        }

        chore.setAssignedChildId(childId);
        chore.setStatus(ChoreStatus.PENDING);

        dataStore.addParent(parent);
        return chore;
    }

    @PostMapping("/{childId}/purchase/{itemId}")
    public Transaction purchaseItem(
            @PathVariable String childId,
            @PathVariable String itemId) {

        Child child = dataStore.getChild(childId);
        if (child == null) {
            throw new RuntimeException("Child not found");
        }

        Parent parent = dataStore.getParent(child.getParentId());
        if (parent == null) {
            throw new RuntimeException("Parent not found");
        }

        StoreItem item = parent.getStoreItemById(itemId);
        if (item == null) {
            throw new RuntimeException("Item not found");
        }

        if (!item.isAvailable()) {
            throw new RuntimeException("Item is out of stock");
        }

        if (child.getBalance() < item.getItemPrice()) {
            throw new RuntimeException("Insufficient balance");
        }

        // Process the purchase
        item.purchase();
        child.subtractBalance(item.getItemPrice());

        // Create transaction record
        Transaction transaction = new Transaction(
                TransactionType.PURCHASE,
                (float) item.getItemPrice(),
                childId,
                "Purchased: " + item.getItemName(),
                LocalDateTime.now());

        child.addTransaction(transaction);
        parent.addTransaction(transaction);

        // Save changes
        dataStore.addChild(child);
        dataStore.addParent(parent);

        return transaction;
    }
}