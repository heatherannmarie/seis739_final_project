package com.school.final_project;

import java.util.ArrayList;

public class Child extends User {
    private String childId;
    private String name;
    private String username;
    private String parentId;
    private double balance;

    public Child(String name, String username, String parentId, String childID) {
        this.name = name;
        this.username = username;
        this.parentId = parentId;
        this.balance = 0.0;
        this.childId = childID;
    }
}
