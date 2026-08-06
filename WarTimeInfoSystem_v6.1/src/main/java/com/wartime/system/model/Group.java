package com.wartime.system.model;

import java.util.ArrayList;
import java.util.List;

public class Group {
    private String name;
    private String type; // For "Other" type, this holds the custom type name
    private GroupType category;
    private List<AbstractUser> members = new ArrayList<>();
    private AbstractUser creator;

    public Group(String name, GroupType category, String customType, AbstractUser creator) {
        this.name = name;
        this.category = category;
        this.type = (category == GroupType.OTHER) ? customType : category.name();
        this.creator = creator;
        this.members.add(creator);
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public GroupType getCategory() {
        return category;
    }

    public List<AbstractUser> getMembers() {
        return members;
    }

    public AbstractUser getCreator() {
        return creator;
    }

    public void addMember(AbstractUser user) {
        if (!members.contains(user)) {
            members.add(user);
        }
    }

    public boolean isMember(AbstractUser user) {
        return members.contains(user);
    }
}
