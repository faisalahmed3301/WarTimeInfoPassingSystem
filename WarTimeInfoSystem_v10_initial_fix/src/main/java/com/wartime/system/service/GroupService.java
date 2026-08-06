package com.wartime.system.service;

import com.wartime.system.model.AbstractUser;
import com.wartime.system.model.Group;
import com.wartime.system.model.GroupType;
import com.wartime.system.model.Rank;
import com.wartime.system.exception.UnauthorizedAccessException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GroupService {
    private static GroupService instance;
    private List<Group> groups = new ArrayList<>();

    private GroupService() {
    }

    public static synchronized GroupService getInstance() {
        if (instance == null) {
            instance = new GroupService();
        }
        return instance;
    }

    public void createGroup(String name, GroupType category, String customType, AbstractUser creator,
            List<AbstractUser> initialMembers, java.time.LocalDate dateCreated) {
        if (creator.getRank() == Rank.SOLDIER) {
            throw new UnauthorizedAccessException("Soldiers cannot create groups.");
        }

        if (creator.getRank() == Rank.OFFICER) {
            boolean commanderPresent = initialMembers.stream()
                    .anyMatch(m -> m.getRank() == Rank.COMMANDER);
            if (!commanderPresent) {
                throw new UnauthorizedAccessException("Officers must add a Commander to create a group.");
            }
        }

        Group group = new Group(name, category, customType, creator, dateCreated);
        for (AbstractUser member : initialMembers) {
            if (member != creator) {
                group.addMember(member);
            }
        }
        groups.add(group);
        com.wartime.system.util.ExcelStorageManager.saveAllData();
    }

    public void updateGroup(Group group, String name, GroupType category, String customType, 
            java.time.LocalDate dateCreated, List<AbstractUser> newMembers, AbstractUser actor) {
        
        if (actor.getRank() != Rank.COMMANDER && !group.getCreator().getName().equals(actor.getName())) {
             // If actor is not commander and not the creator (officer who created it), check if they are an officer member
             if (actor.getRank() != Rank.OFFICER || !group.isMember(actor)) {
                 throw new UnauthorizedAccessException("Only Commanders or group officers can edit this group.");
             }
        }

        group.setName(name);
        group.setCategory(category);
        group.setType((category == GroupType.OTHER) ? customType : category.name());
        group.setDateCreated(dateCreated);
        
        // Members update: ensure creator is always there
        List<AbstractUser> members = new ArrayList<>();
        members.add(group.getCreator());
        for (AbstractUser m : newMembers) {
            if (!members.contains(m)) {
                members.add(m);
            }
        }
        group.setMembers(members);

        com.wartime.system.util.ExcelStorageManager.saveAllData();
    }

    public void loadGroup(Group group) {
        groups.add(group);
    }

    public List<Group> getVisibleGroups(AbstractUser user) {
        if (user.getRank() == Rank.COMMANDER) {
            return groups;
        }
        return groups.stream()
                .filter(g -> g.isMember(user))
                .collect(Collectors.toList());
    }

    public void deleteGroup(Group group, AbstractUser user) {
        if (user.getRank() != Rank.COMMANDER) {
            throw new UnauthorizedAccessException("Only Commanders can delete groups.");
        }
        groups.remove(group);
        MessageService.getInstance().deleteGroupMessages(group);
        com.wartime.system.util.ExcelStorageManager.saveAllData();
    }

    public List<Group> getAllGroups() {
        return groups;
    }
}
