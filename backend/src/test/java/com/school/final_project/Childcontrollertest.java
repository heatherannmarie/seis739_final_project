package com.school.final_project;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ChildControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ParentRepository parentRepository;

    @Autowired
    private ChildRepository childRepository;

    private Parent testParent;
    private Child testChild;

    @BeforeEach
    void setUp() {
        // Clean up database
        childRepository.deleteAll();
        parentRepository.deleteAll();

        // Create test parent and child
        testParent = new Parent("parent_test_1", "Test Parent", "test@example.com", "testparent");
        testChild = testParent.createChildAccount("Test Child", "testchild", "child_test_1");
        testChild.setPin("123456"); // Set known PIN for testing
        parentRepository.save(testParent);
    }

    @Test
    void testLogin() throws Exception {
        Map<String, String> request = new HashMap<>();
        request.put("username", "testchild");
        request.put("pin", "123456");

        mockMvc.perform(post("/api/children/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.childId").value("child_test_1"))
                .andExpect(jsonPath("$.username").value("testchild"))
                .andExpect(jsonPath("$.name").value("Test Child"))
                .andExpect(jsonPath("$.pin").doesNotExist()); // PIN should not be returned
    }

    @Test
    void testLoginWithInvalidPin() throws Exception {
        Map<String, String> request = new HashMap<>();
        request.put("username", "testchild");
        request.put("pin", "000000");

        mockMvc.perform(post("/api/children/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("Invalid username or PIN"));
    }

    @Test
    void testLoginWithMissingUsername() throws Exception {
        Map<String, String> request = new HashMap<>();
        request.put("pin", "123456");

        mockMvc.perform(post("/api/children/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Username is required"));
    }

    @Test
    void testGetChild() throws Exception {
        mockMvc.perform(get("/api/children/" + testChild.getChildId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.childId").value("child_test_1"))
                .andExpect(jsonPath("$.name").value("Test Child"))
                .andExpect(jsonPath("$.pin").doesNotExist()); // PIN should not be exposed
    }

    @Test
    void testGetBalance() throws Exception {
        testChild.setBalance(25.50);
        childRepository.save(testChild);

        mockMvc.perform(get("/api/children/" + testChild.getChildId() + "/balance"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(25.50));
    }

    @Test
    void testGetAvailableChores() throws Exception {
        Chore chore1 = new Chore("Clean Room", "Clean bedroom", 5.0, null, "chore_1");
        Chore chore2 = new Chore("Mow Lawn", "Mow the lawn", 10.0, null, "chore_2");
        testParent.addChore(chore1);
        testParent.addChore(chore2);
        parentRepository.save(testParent);

        mockMvc.perform(get("/api/children/" + testChild.getChildId() + "/available-chores"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].choreName").exists());
    }

    @Test
    void testGetStoreItems() throws Exception {
        StoreItem item1 = new StoreItem("Toy", 5, 9.99, "item_1");
        StoreItem item2 = new StoreItem("Game", 3, 29.99, "item_2");
        testParent.addStoreItem(item1);
        testParent.addStoreItem(item2);
        parentRepository.save(testParent);

        mockMvc.perform(get("/api/children/" + testChild.getChildId() + "/store-items"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void testRequestChoreCompletion() throws Exception {
        Chore chore = new Chore("Test Chore", "Test description", 7.5, null, "chore_1");
        testParent.addChore(chore);
        parentRepository.save(testParent);

        mockMvc.perform(post("/api/children/" + testChild.getChildId() + "/chores/chore_1/request-completion"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.assignedChildId").value("child_test_1"))
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    void testPurchaseItem() throws Exception {
        testChild.setBalance(50.0);
        childRepository.save(testChild);

        StoreItem item = new StoreItem("Video Game", 2, 29.99, "item_1");
        testParent.addStoreItem(item);
        parentRepository.save(testParent);

        mockMvc.perform(post("/api/children/" + testChild.getChildId() + "/purchase/item_1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.type").value("PURCHASE"))
                .andExpect(jsonPath("$.amount").value(29.99));
    }

    @Test
    void testPurchaseItemInsufficientBalance() throws Exception {
        testChild.setBalance(10.0);
        childRepository.save(testChild);

        StoreItem item = new StoreItem("Expensive Item", 1, 50.0, "item_1");
        testParent.addStoreItem(item);
        parentRepository.save(testParent);

        mockMvc.perform(post("/api/children/" + testChild.getChildId() + "/purchase/item_1"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void testGetTransactions() throws Exception {
        mockMvc.perform(get("/api/children/" + testChild.getChildId() + "/transactions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }
}