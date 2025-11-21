package com.school.final_project;

import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/children")
@CrossOrigin(origins = "*")
public class ChildController {

    // Share the same storage as ParentController
    // In a real app, this would be a database
    private static Map<String, Child> children = new HashMap<>();
    private static Map<String, Parent> parents = new HashMap<>();

    // Get child by ID
    @GetMapping("/{childId}")
    public Child getChild(@PathVariable String childId) {
        return children.get(childId);
    }

    // Get child's balance
    @GetMapping("/{childId}/balance")
    public Map<String, Double> getBalance(@PathVariable String childId) {
        Child child = children.get(childId);
        if (child == null) {
            throw new RuntimeException("Child not found");
        }

        Map<String, Double> response = new HashMap<>();
        response.put("balance", child.getBalance());
        return response;
    }

    // Get child's transaction history
    @GetMapping("/{childId}/transactions")
    public ArrayList<Transaction> getTransactions(@PathVariable String childId) {
        Child child = children.get(childId);
        if (child == null) {
            throw new RuntimeException("Child not found");
        }
        return child.getTransactionHistory();
    }

    // View available chores (from parent)
    @GetMapping("/{childId}/available-chores")
    public ArrayList<Chore> getAvailableChores(@PathVariable String childId) {
        Child child = children.get(childId);
        if (child == null) {
            throw new RuntimeException("Child not found");
        }

        Parent parent = parents.get(child.getParentId());
        return parent.getChores();
    }

    // View store items (from parent)
    @GetMapping("/{childId}/store-items")
    public ArrayList<StoreItem> getStoreItems(@PathVariable String childId) {
        Child child = children.get(childId);
        if (child == null) {
            throw new RuntimeException("Child not found");
        }

        Parent parent = parents.get(child.getParentId());
        return parent.getStoreInventory();
    }
}
