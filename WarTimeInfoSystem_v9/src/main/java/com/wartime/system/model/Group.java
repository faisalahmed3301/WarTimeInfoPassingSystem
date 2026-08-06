package com.wartime.system.model;

import java.util.ArrayList;
import java.util.List;

public class Group {
    private String name;
    private String type; // For "Other" type, this holds the custom type name
    private GroupType category;
    private List<AbstractUser> members = new ArrayList<>();
    private AbstractUser creator;
    private java.time.LocalDate dateCreated;

    public Group(String name, GroupType category, String customType, AbstractUser creator, java.time.LocalDate dateCreated) {
        this.name = name;
        this.category = category;
        this.type = (category == GroupType.OTHER) ? customType : category.name();
        this.creator = creator;
        this.dateCreated = dateCreated;
        this.members.add(creator);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public GroupType getCategory() {
        return category;
    }

    public void setCategory(GroupType category) {
        this.category = category;
    }

    public List<AbstractUser> getMembers() {
        return members;
    }

    public void setMembers(List<AbstractUser> members) {
        this.members = members;
    }

    public AbstractUser getCreator() {
        return creator;
    }

    public java.time.LocalDate getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(java.time.LocalDate dateCreated) {
        this.dateCreated = dateCreated;
    }

    public void addMember(AbstractUser user) {
        if (!members.contains(user)) {
            members.add(user);
        }
    }

    public void removeMember(AbstractUser user) {
        if (members.contains(user) && user != creator) {
            members.remove(user);
        }
    }

    public boolean isMember(AbstractUser user) {
        return members.contains(user);
    }
}
