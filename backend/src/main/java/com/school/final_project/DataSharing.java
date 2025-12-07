package com.school.final_project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DataSharing {

    @Autowired
    private ParentRepository parentRepository;

    @Autowired
    private ChildRepository childRepository;

    public void addParent(Parent parent) {
        parentRepository.save(parent);
    }

    public Parent getParent(String parentId) {
        return parentRepository.findByParentId(parentId).orElse(null);
    }

    public void addChild(Child child) {
        childRepository.save(child);
    }

    public Child getChild(String childId) {
        return childRepository.findByChildId(childId).orElse(null);
    }
}