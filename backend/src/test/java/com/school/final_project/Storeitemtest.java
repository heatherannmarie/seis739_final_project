package com.school.final_project;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class StoreItemTest {

    private StoreItem item;

    @BeforeEach
    void setUp() {
        item = new StoreItem("Lego Set", 10, 49.99, "item_1");
    }

    @Test
    void testStoreItemCreation() {
        assertNotNull(item);
        assertEquals("Lego Set", item.getItemName());
        assertEquals(10, item.getAvailableInventory());
        assertEquals(49.99, item.getItemPrice());
        assertEquals("item_1", item.getItemID());
    }

    @Test
    void testIsAvailable() {
        assertTrue(item.isAvailable());

        StoreItem outOfStock = new StoreItem("Out of Stock Item", 0, 10.0, "item_2");
        assertFalse(outOfStock.isAvailable());
    }

    @Test
    void testPurchase() {
        int initialInventory = item.getAvailableInventory();
        boolean success = item.purchase();

        assertTrue(success);
        assertEquals(initialInventory - 1, item.getAvailableInventory());
    }

    @Test
    void testPurchaseMultipleTimes() {
        item.purchase();
        item.purchase();
        item.purchase();

        assertEquals(7, item.getAvailableInventory());
    }

    @Test
    void testPurchaseWhenOutOfStock() {
        StoreItem limitedItem = new StoreItem("Limited Item", 1, 19.99, "item_2");

        assertTrue(limitedItem.purchase());
        assertFalse(limitedItem.purchase());
        assertEquals(0, limitedItem.getAvailableInventory());
    }

    @Test
    void testPurchaseWithZeroInventory() {
        StoreItem emptyItem = new StoreItem("Empty Item", 0, 5.0, "item_3");

        assertFalse(emptyItem.purchase());
        assertEquals(0, emptyItem.getAvailableInventory());
    }

    @Test
    void testSetItemName() {
        item.setItemName("Deluxe Lego Set");
        assertEquals("Deluxe Lego Set", item.getItemName());
    }

    @Test
    void testSetItemPrice() {
        item.setItemPrice(59.99f);
        assertEquals(59.99, item.getItemPrice(), 0.001);
    }

    @Test
    void testSetAvailableInventory() {
        item.setAvailableInventory(25);
        assertEquals(25, item.getAvailableInventory());
    }

    @Test
    void testInventoryDepletion() {
        StoreItem limitedItem = new StoreItem("Popular Item", 3, 15.0, "item_4");

        assertTrue(limitedItem.isAvailable());
        limitedItem.purchase();
        assertTrue(limitedItem.isAvailable());
        limitedItem.purchase();
        assertTrue(limitedItem.isAvailable());
        limitedItem.purchase();
        assertFalse(limitedItem.isAvailable());
    }
}