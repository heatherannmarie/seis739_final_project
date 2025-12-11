package com.school.final_project;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class TransactionTest {

    private Transaction transaction;
    private LocalDateTime timestamp;

    @BeforeEach
    void setUp() {
        timestamp = LocalDateTime.now();
        transaction = new Transaction(
                TransactionType.ALLOWANCE,
                10.0f,
                "child_1",
                "Weekly allowance",
                timestamp);
    }

    @Test
    void testTransactionCreation() {
        assertNotNull(transaction);
        assertEquals(TransactionType.ALLOWANCE, transaction.getType());
        assertEquals(10.0f, transaction.getAmount());
        assertEquals("child_1", transaction.getChildId());
        assertEquals("Weekly allowance", transaction.getDescription());
        assertEquals(timestamp, transaction.getTimestamp());
    }

    @Test
    void testAllowanceTransaction() {
        Transaction allowance = new Transaction(
                TransactionType.ALLOWANCE,
                15.0f,
                "child_2",
                "Chore completion: Wash car",
                LocalDateTime.now());

        assertEquals(TransactionType.ALLOWANCE, allowance.getType());
        assertEquals(15.0f, allowance.getAmount());
    }

    @Test
    void testPurchaseTransaction() {
        Transaction purchase = new Transaction(
                TransactionType.PURCHASE,
                25.99f,
                "child_3",
                "Purchased: Video Game",
                LocalDateTime.now());

        assertEquals(TransactionType.PURCHASE, purchase.getType());
        assertEquals(25.99f, purchase.getAmount(), 0.001f);
        assertTrue(purchase.getDescription().contains("Purchased"));
    }

    @Test
    void testSetType() {
        transaction.setType(TransactionType.PURCHASE);
        assertEquals(TransactionType.PURCHASE, transaction.getType());
    }

    @Test
    void testSetAmount() {
        transaction.setAmount(20.5f);
        assertEquals(20.5f, transaction.getAmount(), 0.001f);
    }

    @Test
    void testSetChildId() {
        transaction.setChildId("child_999");
        assertEquals("child_999", transaction.getChildId());
    }

    @Test
    void testSetDescription() {
        transaction.setDescription("Bonus payment");
        assertEquals("Bonus payment", transaction.getDescription());
    }

    @Test
    void testSetTimestamp() {
        LocalDateTime newTime = LocalDateTime.of(2024, 12, 1, 10, 30);
        transaction.setTimestamp(newTime);
        assertEquals(newTime, transaction.getTimestamp());
    }

    @Test
    void testTransactionWithZeroAmount() {
        Transaction zeroTransaction = new Transaction(
                TransactionType.ALLOWANCE,
                0.0f,
                "child_1",
                "Test transaction",
                LocalDateTime.now());

        assertEquals(0.0f, zeroTransaction.getAmount());
    }

    @Test
    void testTransactionWithLargeAmount() {
        Transaction largeTransaction = new Transaction(
                TransactionType.ALLOWANCE,
                999.99f,
                "child_1",
                "Large allowance",
                LocalDateTime.now());

        assertEquals(999.99f, largeTransaction.getAmount(), 0.001f);
    }

    @Test
    void testMultipleTransactionTypes() {
        Transaction t1 = new Transaction(TransactionType.ALLOWANCE, 5.0f, "child_1", "Allowance", timestamp);
        Transaction t2 = new Transaction(TransactionType.PURCHASE, 3.0f, "child_1", "Purchase", timestamp);

        assertEquals(TransactionType.ALLOWANCE, t1.getType());
        assertEquals(TransactionType.PURCHASE, t2.getType());
        assertNotEquals(t1.getType(), t2.getType());
    }
}