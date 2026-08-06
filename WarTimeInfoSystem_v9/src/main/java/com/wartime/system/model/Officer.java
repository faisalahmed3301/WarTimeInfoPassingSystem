package com.wartime.system.model;

public class Officer extends AbstractUser {

    public Officer(String name) {
        super(name, Rank.OFFICER);
    }

    @Override
    public boolean canAccess(Rank senderRank) {
        // Officer can access if sender is Commander or Officer
        // Cannot access if sender is... wait, strictly based on requirements:
        // Logic: 
        // 1. Commander sends -> Only Commander sees. (Officer CANNOT see) - Requirement 5a
        // 2. Officer sends -> Commander & Officer see. - Requirement 5b
        // 3. Soldier sends -> All see. - Requirement 5c
        
        // This logic depends on the message SENDER's rank, not just the viewer's rank.
        // So this method might need to be "canView(Message m)" effectively.
        // But simply returning boolean based on senderRank passed in:
        
        if (senderRank == Rank.COMMANDER) return false;
        if (senderRank == Rank.OFFICER) return true;
        if (senderRank == Rank.SOLDIER) return true;
        return false;
    }
}
