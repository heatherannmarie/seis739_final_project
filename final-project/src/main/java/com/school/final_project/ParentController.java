package com.school.final_project;

import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/parents")
@CrossOrigin(origins = "*")
public class ParentController {

    // In-memory storage (temporary - replace with database later)
    private Map<String, Parent> parents = new HashMap<>();
    private Map<String, Child> children = new HashMap<>();

    // Create a new parent account
    @PostMapping
    public Parent createParent(@RequestBody Map<String, String> request) {
        String parentId = "parent_" + System.currentTimeMillis();
        String name = request.get("name");

        Parent parent = new Parent(parentId, name);
        parents.put(parentId, parent);

        return parent;
    }

    // Get parent by ID
    @GetMapping("/{parentId}")
    public Parent getParent(@PathVariable String parentId) {
        return parents.get(parentId);
    }

    // Create a child account
    @PostMapping("/{parentId}/children")
    public Child createChild(
            @PathVariable String parentId,
            @RequestBody Map<String, String> request) {

        Parent parent = parents.get(parentId);
        if (parent == null) {
            throw new RuntimeException("Parent not found");
        }

        String childId = "child_" + System.currentTimeMillis();
        String childName = request.get("name");
        String username = request.get("username");

        Child child = parent.createChildAccount(childName, username, childId);
        children.put(childId, child);

        return child;
    }

    // Get all children for a parent
    @GetMapping("/{parentId}/children")
    public ArrayList<Child> getChildren(@PathVariable String parentId) {
        Parent parent = parents.get(parentId);
        if (parent == null) {
            throw new RuntimeException("Parent not found");
        }
        return parent.getChildren();
    }

    @PostMapping("/{parentId}/chores")
    public Chore addChore(
            @PathVariable String parentId,
            @RequestBody Map<String, Object> request) {

        Parent parent = parents.get(parentId);
        if (parent == null) {
            throw new RuntimeException("Parent not found");
        }

        String choreId = "chore_" + System.currentTimeMillis();
        Chore chore = new Chore(
                (String) request.get("choreName"), // choreName
                (String) request.get("choreDescription"), // choreDescription
                ((Number) request.get("chorePrice")).doubleValue(), // chorePrice (changed to doubleValue())
                (String) request.get("assignedChildId"), // assignedChildId
                true, // isAvailable
                choreId); // choreID (last!)

        parent.addChore(chore);
        return chore;
    }

    // Get all chores
    @GetMapping("/{parentId}/chores")
    public ArrayList<Chore> getChores(@PathVariable String parentId) {
        Parent parent = parents.get(parentId);
        if (parent == null) {
            throw new RuntimeException("Parent not found");
        }
        return parent.getChores();
    }

    // Add a store item
    @PostMapping("/{parentId}/store-items")
    public StoreItem addStoreItem(
            @PathVariable String parentId,
            @RequestBody Map<String, Object> request) {

        Parent parent = parents.get(parentId);
        if (parent == null) {
            throw new RuntimeException("Parent not found");
        }

        String itemId = "item_" + System.currentTimeMillis();
        StoreItem item = new StoreItem(
                (String) request.get("itemName"),
                ((Number) request.get("availableInventory")).intValue(),
                ((Number) request.get("itemPrice")).floatValue(),
                itemId);

        parent.addStoreItem(item);
        return item;
    }

    // Pay child for chore
    @PostMapping("/{parentId}/pay-child")
    public Transaction payChildForChore(
            @PathVariable String parentId,
            @RequestBody Map<String, String> request) {

        Parent parent = parents.get(parentId);
        String childId = request.get("childId");
        String choreId = request.get("choreId");

        Child child = children.get(childId);

        // Find the chore
        Chore chore = parent.getChores().stream()
                .filter(c -> c.getChoreId().equals(choreId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Chore not found"));

        parent.payChildForChore(child, chore);

        // Return the most recent transaction
        return parent.getTransactions().get(parent.getTransactions().size() - 1);
    }
}
