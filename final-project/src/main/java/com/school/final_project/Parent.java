package com.school.final_project;

import java.util.ArrayList;
import java.util.List;

public class Parent extends User {

    private String parentId;
    private String name;
    private ArrayList<Child> children;

    public Child createChildAccount(String childName, String userName){
        if (childName == null || childName.trim().isEmpty()) {
            throw new IllegalArgumentException("Child name cannot be empty");
        }

        Child newChild = new Child(childName, userName, this.parentId);
        children.add(newChild);

        return newChild;
    }
}
