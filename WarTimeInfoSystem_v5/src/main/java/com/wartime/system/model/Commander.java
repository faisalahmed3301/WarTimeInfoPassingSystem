package com.wartime.system.model;

public class Commander extends AbstractUser {

    public Commander(String name) {
        super(name, Rank.COMMANDER);
    }

    @Override
    public boolean canAccess(Rank senderRank) {
        // Commander can see everything
        return true;
    }
}
