package com.school.final_project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import main.java.com.school.final_project.ChoreStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/children")
@CrossOrigin(origins = "*")
public class ChildController {

    @Autowired
    private DataSharing dataStore;

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
    public ArrayList<Transaction> getTransactions(@PathVariable String childId) {
        Child child = dataStore.getChild(childId);
        if (child == null) {
            throw new RuntimeException("Child not found");
        }
        return child.getTransactionHistory();
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

        Chore chore = parent.getChores().stream()
                .filter(c -> c.getChoreId().equals(choreId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Chore not found"));

        chore.setAssignedChildId(childId);
        chore.setStatus(ChoreStatus.PENDING);

        dataStore.addParent(parent);
        return chore;
    }
}