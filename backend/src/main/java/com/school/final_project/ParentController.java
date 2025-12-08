package com.school.final_project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/parents")
@CrossOrigin(origins = "*")
public class ParentController {

    @Autowired
    private DataSharing dataStore;

    @PostMapping
    public Parent createParent(@RequestBody Map<String, String> request) {
        String parentId = "parent_" + System.currentTimeMillis();
        String name = request.get("name");

        Parent parent = new Parent(parentId, name);
        dataStore.addParent(parent);

        return parent;
    }

    @GetMapping("/{parentId}")
    public Parent getParent(@PathVariable String parentId) {
        return dataStore.getParent(parentId);
    }

    @PostMapping("/{parentId}/children")
    public Child createChild(
            @PathVariable String parentId,
            @RequestBody Map<String, String> request) {

        Parent parent = dataStore.getParent(parentId);
        if (parent == null) {
            throw new RuntimeException("Parent not found");
        }

        String childId = "child_" + System.currentTimeMillis();
        String childName = request.get("name");
        String username = request.get("username");

        Child child = parent.createChildAccount(childName, username, childId);
        dataStore.addChild(child);

        return child;
    }

    @GetMapping("/{parentId}/children")
    public ArrayList<Child> getChildren(@PathVariable String parentId) {
        Parent parent = dataStore.getParent(parentId);
        if (parent == null) {
            throw new RuntimeException("Parent not found");
        }
        return parent.getChildren();
    }

    @PostMapping("/{parentId}/chores")
    public Chore addChore(
            @PathVariable String parentId,
            @RequestBody Map<String, Object> request) {

        Parent parent = dataStore.getParent(parentId);
        if (parent == null) {
            throw new RuntimeException("Parent not found");
        }

        String choreId = "chore_" + System.currentTimeMillis();
        Chore chore = new Chore(
                (String) request.get("choreName"),
                (String) request.get("choreDescription"),
                ((Number) request.get("chorePrice")).doubleValue(),
                (String) request.get("assignedChildId"),
                true,
                choreId);

        parent.addChore(chore);
        return chore;
    }

    @GetMapping("/{parentId}/chores")
    public ArrayList<Chore> getChores(@PathVariable String parentId) {
        Parent parent = dataStore.getParent(parentId);
        if (parent == null) {
            throw new RuntimeException("Parent not found");
        }
        return parent.getChores();
    }

    @PostMapping("/{parentId}/store-items")
    public StoreItem addStoreItem(
            @PathVariable String parentId,
            @RequestBody Map<String, Object> request) {

        Parent parent = dataStore.getParent(parentId);
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

    @PostMapping("/{parentId}/pay-child")
    public Transaction payChildForChore(
            @PathVariable String parentId,
            @RequestBody Map<String, String> request) {

        Parent parent = dataStore.getParent(parentId);
        String childId = request.get("childId");
        String choreId = request.get("choreId");

        Child child = dataStore.getChild(childId);

        Chore chore = parent.getChores().stream()
                .filter(c -> c.getChoreId().equals(choreId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Chore not found"));

        parent.payChildForChore(child, chore);

        return parent.getTransactions().get(parent.getTransactions().size() - 1);
    }

    @PutMapping("/{parentId}/chores/{choreId}")
    public Chore updateChore(
            @PathVariable String parentId,
            @PathVariable String choreId,
            @RequestBody Map<String, Object> request) {

        Parent parent = dataStore.getParent(parentId);
        if (parent == null) {
            throw new RuntimeException("Parent not found");
        }

        Chore chore = parent.getChores().stream()
                .filter(c -> c.getChoreId().equals(choreId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Chore not found"));

        // Update fields if they're provided in the request
        if (request.containsKey("choreName")) {
            chore.setChoreName((String) request.get("choreName"));
        }
        if (request.containsKey("choreDescription")) {
            chore.setChoreDescription((String) request.get("choreDescription"));
        }
        if (request.containsKey("chorePrice")) {
            chore.setChorePrice(((Number) request.get("chorePrice")).doubleValue());
        }
        if (request.containsKey("assignedChildId")) {
            chore.setAssignedChildId((String) request.get("assignedChildId"));
        }

        dataStore.addParent(parent); // Save changes
        return chore;
    }

    @DeleteMapping("/{parentId}/chores/{choreId}")
    public Map<String, String> deleteChore(
            @PathVariable String parentId,
            @PathVariable String choreId) {

        Parent parent = dataStore.getParent(parentId);
        if (parent == null) {
            throw new RuntimeException("Parent not found");
        }

        boolean removed = parent.getChores().removeIf(c -> c.getChoreId().equals(choreId));

        if (!removed) {
            throw new RuntimeException("Chore not found");
        }

        dataStore.addParent(parent); // Save changes

        Map<String, String> response = new HashMap<>();
        response.put("message", "Chore deleted successfully");
        return response;
    }

    @PutMapping("/{parentId}/store-items/{itemId}")
    public StoreItem updateStoreItem(
            @PathVariable String parentId,
            @PathVariable String itemId,
            @RequestBody Map<String, Object> request) {

        Parent parent = dataStore.getParent(parentId);
        if (parent == null) {
            throw new RuntimeException("Parent not found");
        }

        StoreItem item = parent.getStoreInventory().stream()
                .filter(c -> c.getItemID().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Store Item not found"));

        if (request.containsKey("itemName")) {
            item.setItemName((String) request.get("itemName"));
        }

        if (request.containsKey("availableInventory")) {
            item.setAvailableInventory((Number) request.get("availableInventory"));
        }

        if (request.containsKey("itemPrice")) {
            item.setItemPrice(((Number) request.get("itemPrice")).doubleValue());
        }

        return item;
    }

    @DeleteMapping("/{parentId}/store-items/{itemId}")
    public Map<String, String> deleteItem(
            @PathVariable String parentId,
            @PathVariable String itemId) {

        Parent parent = dataStore.getParent(parentId);
        if (parent == null) {
            throw new RuntimeException("Parent not found");
        }

        boolean removed = parent.getStoreInventory().removeIf(c -> c.getItemID().equals(itemId));

        if (!removed) {
            throw new RuntimeException("Item not found");
        }

        dataStore.addParent(parent);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Item deleted successfully");
        return response;
    }
}