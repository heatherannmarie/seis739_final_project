package com.school.final_project;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ParentTest {

    private Parent parent;

    @BeforeEach
    void setUp() {
        parent = new Parent("parent_1", "John Doe", "john@example.com", "johndoe");
    }

    @Test
    void testParentCreation() {
        assertNotNull(parent);
        assertEquals("parent_1", parent.getParentId());
        assertEquals("John Doe", parent.getName());
        assertEquals("john@example.com", parent.getEmail());
        assertEquals("johndoe", parent.getUsername());
    }

    @Test
    void testCreateChildAccount() {
        Child child = parent.createChildAccount("Alice", "alice123", "child_1");

        assertNotNull(child);
        assertEquals("Alice", child.getName());
        assertEquals("alice123", child.getUsername());
        assertEquals("child_1", child.getChildId());
        assertEquals(1, parent.getChildren().size());
        assertEquals(0.0, child.getBalance());
    }

    @Test
    void testCreateChildAccountWithEmptyName() {
        assertThrows(IllegalArgumentException.class, () -> {
            parent.createChildAccount("", "username", "child_1");
        });
    }

    @Test
    void testAddChore() {
        Chore chore = new Chore("Clean Room", "Clean your bedroom", 5.0, null, "chore_1");
        parent.addChore(chore);

        assertEquals(1, parent.getChores().size());
        assertEquals("Clean Room", parent.getChores().get(0).getChoreName());
    }

    @Test
    void testGetChoreById() {
        Chore chore = new Chore("Wash Dishes", "Wash all dishes", 3.0, null, "chore_1");
        parent.addChore(chore);

        Chore found = parent.getChoreById("chore_1");
        assertNotNull(found);
        assertEquals("Wash Dishes", found.getChoreName());
    }

    @Test
    void testGetChoreByIdNotFound() {
        Chore found = parent.getChoreById("nonexistent");
        assertNull(found);
    }

    @Test
    void testRemoveChore() {
        Chore chore = new Chore("Mow Lawn", "Mow the lawn", 10.0, null, "chore_1");
        parent.addChore(chore);

        assertTrue(parent.removeChore("chore_1"));
        assertEquals(0, parent.getChores().size());
    }

    @Test
    void testRemoveNonexistentChore() {
        assertFalse(parent.removeChore("nonexistent"));
    }

    @Test
    void testAddStoreItem() {
        StoreItem item = new StoreItem("Toy Car", 5, 15.99, "item_1");
        parent.addStoreItem(item);

        assertEquals(1, parent.getStoreInventory().size());
        assertEquals("Toy Car", parent.getStoreInventory().get(0).getItemName());
    }

    @Test
    void testGetStoreItemById() {
        StoreItem item = new StoreItem("Video Game", 3, 59.99, "item_1");
        parent.addStoreItem(item);

        StoreItem found = parent.getStoreItemById("item_1");
        assertNotNull(found);
        assertEquals("Video Game", found.getItemName());
    }

    @Test
    void testRemoveStoreItem() {
        StoreItem item = new StoreItem("Book", 10, 12.99, "item_1");
        parent.addStoreItem(item);

        assertTrue(parent.removeStoreItem("item_1"));
        assertEquals(0, parent.getStoreInventory().size());
    }

    @Test
    void testPayChildForChore() {
        Child child = parent.createChildAccount("Bob", "bob123", "child_1");
        Chore chore = new Chore("Take Out Trash", "Take out the trash", 2.5, "child_1", "chore_1");
        parent.addChore(chore);

        double initialBalance = child.getBalance();
        parent.payChildForChore(child, chore);

        assertEquals(initialBalance + 2.5, child.getBalance());
        assertEquals(1, parent.getTransactions().size());
        assertEquals(TransactionType.ALLOWANCE, parent.getTransactions().get(0).getType());
    }

    @Test
    void testAddTransaction() {
        Transaction transaction = new Transaction(
                TransactionType.ALLOWANCE,
                10.0f,
                "child_1",
                "Weekly allowance",
                java.time.LocalDateTime.now());

        parent.addTransaction(transaction);
        assertEquals(1, parent.getTransactions().size());
    }
}