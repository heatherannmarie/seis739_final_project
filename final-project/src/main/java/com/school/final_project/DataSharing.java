package com.school.final_project;

import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;

@Component
public class DataSharing {

    private Map<String, Parent> parents = new HashMap<>();
    private Map<String, Child> children = new HashMap<>();

    public void addParent(Parent parent) {
        parents.put(parent.getParentId(), parent);
    }

    public Parent getParent(String parentId) {
        return parents.get(parentId);
    }

    public Map<String, Parent> getAllParents() {
        return parents;
    }

    public void addChild(Child child) {
        children.put(child.getChildId(), child);
    }

    public Child getChild(String childId) {
        return children.get(childId);
    }

    public Map<String, Child> getAllChildren() {
        return children;
    }
}
