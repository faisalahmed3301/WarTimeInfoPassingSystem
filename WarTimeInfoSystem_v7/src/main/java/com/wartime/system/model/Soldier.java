package com.wartime.system.model;

public class Soldier extends AbstractUser {

    public Soldier(String name) {
        super(name, Rank.SOLDIER);
    }

    @Override
    public boolean canAccess(Rank senderRank) {
        // Soldier can only see texts from Soldier
        // Commander sends -> Only Commander sees.
        // Officer sends -> Commander & Officer see.
        // Soldier sends -> All see.
        
        if (senderRank == Rank.SOLDIER) return true;
        return false;
    }
}
