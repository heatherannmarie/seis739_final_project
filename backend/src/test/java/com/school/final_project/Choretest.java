package com.school.final_project;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ChoreTest {

    private Chore chore;

    @BeforeEach
    void setUp() {
        chore = new Chore("Clean Kitchen", "Clean all kitchen surfaces", 7.5, null, "chore_1");
    }

    @Test
    void testChoreCreation() {
        assertNotNull(chore);
        assertEquals("Clean Kitchen", chore.getChoreName());
        assertEquals("Clean all kitchen surfaces", chore.getChoreDescription());
        assertEquals(7.5, chore.getChorePrice());
        assertNull(chore.getAssignedChildId());
        assertEquals("chore_1", chore.getChoreId());
        assertEquals(ChoreStatus.AVAILABLE, chore.getStatus());
    }

    @Test
    void testSetChoreName() {
        chore.setChoreName("Wash Dishes");
        assertEquals("Wash Dishes", chore.getChoreName());
    }

    @Test
    void testSetChoreDescription() {
        chore.setChoreDescription("Wash all the dishes in the sink");
        assertEquals("Wash all the dishes in the sink", chore.getChoreDescription());
    }

    @Test
    void testSetChorePrice() {
        chore.setChorePrice(10.0);
        assertEquals(10.0, chore.getChorePrice());
    }

    @Test
    void testChangePrice() {
        chore.changePrice(12.0);
        assertEquals(12.0, chore.getChorePrice());
    }

    @Test
    void testSetAssignedChildId() {
        chore.setAssignedChildId("child_1");
        assertEquals("child_1", chore.getAssignedChildId());
    }

    @Test
    void testSetStatus() {
        chore.setStatus(ChoreStatus.PENDING);
        assertEquals(ChoreStatus.PENDING, chore.getStatus());

        chore.setStatus(ChoreStatus.COMPLETED);
        assertEquals(ChoreStatus.COMPLETED, chore.getStatus());
    }

    @Test
    void testChoreStatusTransition() {
        // Available -> Pending
        assertEquals(ChoreStatus.AVAILABLE, chore.getStatus());
        chore.setStatus(ChoreStatus.PENDING);
        assertEquals(ChoreStatus.PENDING, chore.getStatus());

        // Pending -> Completed
        chore.setStatus(ChoreStatus.COMPLETED);
        assertEquals(ChoreStatus.COMPLETED, chore.getStatus());
    }

    @Test
    void testMultipleChoresWithDifferentStatuses() {
        Chore chore1 = new Chore("Chore 1", "Description 1", 5.0, null, "chore_1");
        Chore chore2 = new Chore("Chore 2", "Description 2", 6.0, null, "chore_2");
        Chore chore3 = new Chore("Chore 3", "Description 3", 7.0, null, "chore_3");

        chore1.setStatus(ChoreStatus.AVAILABLE);
        chore2.setStatus(ChoreStatus.PENDING);
        chore3.setStatus(ChoreStatus.COMPLETED);

        assertEquals(ChoreStatus.AVAILABLE, chore1.getStatus());
        assertEquals(ChoreStatus.PENDING, chore2.getStatus());
        assertEquals(ChoreStatus.COMPLETED, chore3.getStatus());
    }
}
