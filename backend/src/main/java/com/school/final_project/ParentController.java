package com.school.final_project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
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
}