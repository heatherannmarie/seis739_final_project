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
class ParentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ParentRepository parentRepository;

    @Autowired
    private ChildRepository childRepository;

    private Parent testParent;

    @BeforeEach
    void setUp() {
        // Clean up database
        childRepository.deleteAll();
        parentRepository.deleteAll();

        // Create test parent
        testParent = new Parent("parent_test_1", "Test Parent", "test@example.com", "testparent");
        parentRepository.save(testParent);
    }

    @Test
    void testCreateParent() throws Exception {
        Map<String, String> request = new HashMap<>();
        request.put("name", "New Parent");
        request.put("email", "newparent@example.com");
        request.put("username", "newparent");

        mockMvc.perform(post("/api/parents")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("New Parent"))
                .andExpect(jsonPath("$.email").value("newparent@example.com"))
                .andExpect(jsonPath("$.username").value("newparent"))
                .andExpect(jsonPath("$.parentId").exists());
    }

    @Test
    void testCreateParentWithMissingName() throws Exception {
        Map<String, String> request = new HashMap<>();
        request.put("email", "test@example.com");
        request.put("username", "testuser");

        mockMvc.perform(post("/api/parents")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Name is required"));
    }

    @Test
    void testCreateParentWithDuplicateUsername() throws Exception {
        Map<String, String> request = new HashMap<>();
        request.put("name", "Another Parent");
        request.put("email", "another@example.com");
        request.put("username", "testparent"); // Already exists

        mockMvc.perform(post("/api/parents")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Username already taken"));
    }

    @Test
    void testLogin() throws Exception {
        Map<String, String> request = new HashMap<>();
        request.put("username", "testparent");

        mockMvc.perform(post("/api/parents/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.parentId").value("parent_test_1"))
                .andExpect(jsonPath("$.username").value("testparent"));
    }

    @Test
    void testLoginWithInvalidUsername() throws Exception {
        Map<String, String> request = new HashMap<>();
        request.put("username", "nonexistent");

        mockMvc.perform(post("/api/parents/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("Invalid username"));
    }

    @Test
    void testGetParent() throws Exception {
        mockMvc.perform(get("/api/parents/" + testParent.getParentId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.parentId").value("parent_test_1"))
                .andExpect(jsonPath("$.name").value("Test Parent"));
    }

    @Test
    void testCreateChild() throws Exception {
        Map<String, String> request = new HashMap<>();
        request.put("name", "Test Child");
        request.put("username", "testchild");

        mockMvc.perform(post("/api/parents/" + testParent.getParentId() + "/children")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Child"))
                .andExpect(jsonPath("$.username").value("testchild"))
                .andExpect(jsonPath("$.pin").exists())
                .andExpect(jsonPath("$.balance").value(0.0));
    }

    @Test
    void testGetChildren() throws Exception {
        // Create a child first
        Child child = testParent.createChildAccount("Child 1", "child1", "child_1");
        parentRepository.save(testParent);

        mockMvc.perform(get("/api/parents/" + testParent.getParentId() + "/children"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("Child 1"));
    }

    @Test
    void testAddChore() throws Exception {
        Map<String, Object> request = new HashMap<>();
        request.put("choreName", "Wash Car");
        request.put("choreDescription", "Wash the family car");
        request.put("chorePrice", 10.0);
        request.put("assignedChildId", null);

        mockMvc.perform(post("/api/parents/" + testParent.getParentId() + "/chores")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.choreName").value("Wash Car"))
                .andExpect(jsonPath("$.chorePrice").value(10.0));
    }

    @Test
    void testGetChores() throws Exception {
        Chore chore = new Chore("Test Chore", "Description", 5.0, null, "chore_1");
        testParent.addChore(chore);
        parentRepository.save(testParent);

        mockMvc.perform(get("/api/parents/" + testParent.getParentId() + "/chores"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].choreName").value("Test Chore"));
    }

    @Test
    void testAddStoreItem() throws Exception {
        Map<String, Object> request = new HashMap<>();
        request.put("itemName", "Toy Car");
        request.put("availableInventory", 5);
        request.put("itemPrice", 15.99);

        mockMvc.perform(post("/api/parents/" + testParent.getParentId() + "/store-items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.itemName").value("Toy Car"))
                .andExpect(jsonPath("$.itemPrice").value(closeTo(15.99, 0.01)));
    }

    @Test
    void testDeleteChore() throws Exception {
        Chore chore = new Chore("Delete Me", "This chore will be deleted", 3.0, null, "chore_delete");
        testParent.addChore(chore);
        parentRepository.save(testParent);

        mockMvc.perform(delete("/api/parents/" + testParent.getParentId() + "/chores/chore_delete"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Chore deleted successfully"));
    }

    @Test
    void testAddAllowance() throws Exception {
        Child child = testParent.createChildAccount("Child", "childuser", "child_1");
        parentRepository.save(testParent);

        Map<String, Object> request = new HashMap<>();
        request.put("amount", 20.0);

        mockMvc.perform(post("/api/parents/" + testParent.getParentId() + "/children/child_1/add-allowance")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(20.0));
    }
}