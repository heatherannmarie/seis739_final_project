package com.school.final_project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

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
        Parent parent = dataStore.getParent(parentId);
        if (parent == null) {
            throw new RuntimeException("Parent not found");
        }
        return parent;
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
        dataStore.addParent(parent);
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

    @PutMapping("/{parentId}/children/{childId}")
    public Child updateChild(
            @PathVariable String parentId,
            @PathVariable String childId,
            @RequestBody Map<String, String> request) {

        Parent parent = dataStore.getParent(parentId);
        if (parent == null) {
            throw new RuntimeException("Parent not found");
        }

        Child child = dataStore.getChild(childId);
        if (child == null) {
            throw new RuntimeException("Child not found");
        }

        if (!child.getParentId().equals(parentId)) {
            throw new RuntimeException("Child does not belong to this parent");
        }

        if (request.containsKey("name")) {
            child.setName(request.get("name"));
        }
        if (request.containsKey("username")) {
            child.setUsername(request.get("username"));
        }

        dataStore.addChild(child);
        return child;
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
                choreId);

        parent.addChore(chore);
        dataStore.addParent(parent);
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

    @PutMapping("/{parentId}/chores/{choreId}")
    public Chore updateChore(
            @PathVariable String parentId,
            @PathVariable String choreId,
            @RequestBody Map<String, Object> request) {

        Parent parent = dataStore.getParent(parentId);
        if (parent == null) {
            throw new RuntimeException("Parent not found");
        }

        Chore chore = parent.getChoreById(choreId);
        if (chore == null) {
            throw new RuntimeException("Chore not found");
        }

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

        dataStore.addParent(parent);
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

        boolean removed = parent.removeChore(choreId);
        if (!removed) {
            throw new RuntimeException("Chore not found");
        }

        dataStore.addParent(parent);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Chore deleted successfully");
        return response;
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
        dataStore.addParent(parent);
        return item;
    }

    @GetMapping("/{parentId}/store-items")
    public ArrayList<StoreItem> getStoreItems(@PathVariable String parentId) {
        Parent parent = dataStore.getParent(parentId);
        if (parent == null) {
            throw new RuntimeException("Parent not found");
        }
        return parent.getStoreInventory();
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

        StoreItem item = parent.getStoreItemById(itemId);
        if (item == null) {
            throw new RuntimeException("Store item not found");
        }

        if (request.containsKey("itemName")) {
            item.setItemName((String) request.get("itemName"));
        }
        if (request.containsKey("availableInventory")) {
            item.setAvailableInventory(((Number) request.get("availableInventory")).intValue());
        }
        if (request.containsKey("itemPrice")) {
            item.setItemPrice(((Number) request.get("itemPrice")).floatValue());
        }

        dataStore.addParent(parent);
        return item;
    }

    @DeleteMapping("/{parentId}/store-items/{itemId}")
    public Map<String, String> deleteStoreItem(
            @PathVariable String parentId,
            @PathVariable String itemId) {

        Parent parent = dataStore.getParent(parentId);
        if (parent == null) {
            throw new RuntimeException("Parent not found");
        }

        boolean removed = parent.removeStoreItem(itemId);
        if (!removed) {
            throw new RuntimeException("Store item not found");
        }

        dataStore.addParent(parent);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Store item deleted successfully");
        return response;
    }

    @GetMapping("/{parentId}/transactions")
    public ArrayList<Transaction> getTransactions(@PathVariable String parentId) {
        Parent parent = dataStore.getParent(parentId);
        if (parent == null) {
            throw new RuntimeException("Parent not found");
        }
        return parent.getTransactions();
    }

    @PostMapping("/{parentId}/pay-child")
    public Transaction payChildForChore(
            @PathVariable String parentId,
            @RequestBody Map<String, String> request) {

        Parent parent = dataStore.getParent(parentId);
        if (parent == null) {
            throw new RuntimeException("Parent not found");
        }

        String childId = request.get("childId");
        String choreId = request.get("choreId");

        Child child = dataStore.getChild(childId);
        if (child == null) {
            throw new RuntimeException("Child not found");
        }

        Chore chore = parent.getChoreById(choreId);
        if (chore == null) {
            throw new RuntimeException("Chore not found");
        }

        parent.payChildForChore(child, chore);

        dataStore.addParent(parent);
        dataStore.addChild(child);

        return parent.getTransactions().get(parent.getTransactions().size() - 1);
    }

    @GetMapping("/{parentId}/chores/pending")
    public ArrayList<Chore> getPendingChores(@PathVariable String parentId) {
        Parent parent = dataStore.getParent(parentId);
        if (parent == null) {
            throw new RuntimeException("Parent not found");
        }

        return parent.getChores().stream()
                .filter(c -> c.getStatus() == ChoreStatus.PENDING)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @PostMapping("/{parentId}/chores/{choreId}/approve")
    public Transaction approveChoreCompletion(
            @PathVariable String parentId,
            @PathVariable String choreId) {

        Parent parent = dataStore.getParent(parentId);
        if (parent == null) {
            throw new RuntimeException("Parent not found");
        }

        Chore chore = parent.getChoreById(choreId);
        if (chore == null) {
            throw new RuntimeException("Chore not found");
        }

        if (chore.getStatus() != ChoreStatus.PENDING) {
            throw new RuntimeException("Chore is not pending approval");
        }

        Child child = dataStore.getChild(chore.getAssignedChildId());
        if (child == null) {
            throw new RuntimeException("Child not found");
        }

        parent.payChildForChore(child, chore);
        chore.setStatus(ChoreStatus.COMPLETED);

        dataStore.addParent(parent);
        dataStore.addChild(child);

        return parent.getTransactions().get(parent.getTransactions().size() - 1);
    }

    @PostMapping("/{parentId}/chores/{choreId}/deny")
    public Chore denyChoreCompletion(
            @PathVariable String parentId,
            @PathVariable String choreId) {

        Parent parent = dataStore.getParent(parentId);
        if (parent == null) {
            throw new RuntimeException("Parent not found");
        }

        Chore chore = parent.getChoreById(choreId);
        if (chore == null) {
            throw new RuntimeException("Chore not found");
        }

        chore.setStatus(ChoreStatus.AVAILABLE);
        chore.setAssignedChildId(null);

        dataStore.addParent(parent);
        return chore;
    }
}