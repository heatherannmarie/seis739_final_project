package com.school.final_project;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ChildTest {

    private Parent parent;
    private Child child;

    @BeforeEach
    void setUp() {
        parent = new Parent("parent_1", "Jane Doe", "jane@example.com", "janedoe");
        child = new Child("Tommy", "tommy123", parent, "child_1");
    }

    @Test
    void testChildCreation() {
        assertNotNull(child);
        assertEquals("Tommy", child.getName());
        assertEquals("tommy123", child.getUsername());
        assertEquals("child_1", child.getChildId());
        assertEquals(0.0, child.getBalance());
        assertNotNull(child.getPin());
        assertEquals(6, child.getPin().length());
    }

    @Test
    void testPinGeneration() {
        String pin = child.getPin();
        assertNotNull(pin);
        assertEquals(6, pin.length());
        assertTrue(Integer.parseInt(pin) >= 100000);
        assertTrue(Integer.parseInt(pin) <= 999999);
    }

    @Test
    void testValidatePin() {
        String correctPin = child.getPin();
        assertTrue(child.validatePin(correctPin));
        assertFalse(child.validatePin("000000"));
        assertFalse(child.validatePin("123456"));
    }

    @Test
    void testRegeneratePin() {
        String originalPin = child.getPin();
        child.regeneratePin();
        String newPin = child.getPin();

        assertNotEquals(originalPin, newPin);
        assertEquals(6, newPin.length());
    }

    @Test
    void testAddBalance() {
        child.addBalance(10.0);
        assertEquals(10.0, child.getBalance());

        child.addBalance(5.5);
        assertEquals(15.5, child.getBalance(), 0.001);
    }

    @Test
    void testSubtractBalance() {
        child.addBalance(20.0);
        child.subtractBalance(7.5);
        assertEquals(12.5, child.getBalance(), 0.001);
    }

    @Test
    void testSetBalance() {
        child.setBalance(100.0);
        assertEquals(100.0, child.getBalance());
    }

    @Test
    void testViewAvailableChores() {
        Chore chore1 = new Chore("Clean Room", "Clean bedroom", 5.0, null, "chore_1");
        Chore chore2 = new Chore("Wash Car", "Wash the car", 10.0, null, "chore_2");
        chore2.setStatus(ChoreStatus.PENDING);

        parent.addChore(chore1);
        parent.addChore(chore2);

        var availableChores = child.viewAvailableChores();
        assertEquals(1, availableChores.size());
        assertEquals("Clean Room", availableChores.get(0).getChoreName());
    }

    @Test
    void testViewStoreItems() {
        StoreItem item1 = new StoreItem("Toy", 5, 9.99, "item_1");
        StoreItem item2 = new StoreItem("Book", 10, 12.99, "item_2");

        parent.addStoreItem(item1);
        parent.addStoreItem(item2);

        var items = child.viewStoreItems();
        assertEquals(2, items.size());
    }

    @Test
    void testSelectChore() {
        Chore chore = new Chore("Mow Lawn", "Mow the lawn", 15.0, null, "chore_1");
        parent.addChore(chore);

        child.selectChore("chore_1");
        assertEquals("child_1", chore.getAssignedChildId());
    }

    @Test
    void testSelectUnavailableChore() {
        Chore chore = new Chore("Vacuum", "Vacuum the house", 8.0, "other_child", "chore_1");
        chore.setStatus(ChoreStatus.PENDING);
        parent.addChore(chore);

        assertThrows(RuntimeException.class, () -> {
            child.selectChore("chore_1");
        });
    }

    @Test
    void testSelectNonexistentChore() {
        assertThrows(RuntimeException.class, () -> {
            child.selectChore("nonexistent");
        });
    }

    @Test
    void testPurchaseItem() {
        child.setBalance(50.0);
        StoreItem item = new StoreItem("Video Game", 2, 29.99, "item_1");
        parent.addStoreItem(item);

        Transaction transaction = child.purchaseItem("item_1");

        assertNotNull(transaction);
        assertEquals(20.01, child.getBalance(), 0.001);
        assertEquals(1, item.getAvailableInventory());
        assertEquals(TransactionType.PURCHASE, transaction.getType());
    }

    @Test
    void testPurchaseItemInsufficientBalance() {
        child.setBalance(10.0);
        StoreItem item = new StoreItem("Expensive Item", 1, 50.0, "item_1");
        parent.addStoreItem(item);

        assertThrows(RuntimeException.class, () -> {
            child.purchaseItem("item_1");
        });
    }

    @Test
    void testPurchaseOutOfStockItem() {
        child.setBalance(100.0);
        StoreItem item = new StoreItem("Sold Out", 0, 10.0, "item_1");
        parent.addStoreItem(item);

        assertThrows(RuntimeException.class, () -> {
            child.purchaseItem("item_1");
        });
    }

    @Test
    void testAddTransaction() {
        Transaction transaction = new Transaction(
                TransactionType.ALLOWANCE,
                5.0f,
                "child_1",
                "Bonus",
                java.time.LocalDateTime.now());

        child.addTransaction(transaction);
        assertEquals(1, child.getTransactionHistory().size());
    }

    @Test
    void testGetParentId() {
        assertEquals("parent_1", child.getParentId());
    }
}