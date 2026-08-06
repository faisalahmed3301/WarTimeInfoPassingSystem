package com.wartime.system.model;

public abstract class AbstractUser {
    private String name;
    private Rank rank;

    public AbstractUser(String name, Rank rank) {
        this.name = name;
        this.rank = rank;
    }

    public String getName() {
        return name;
    }

    public Rank getRank() {
        return rank;
    }

    // Abstract method to be implemented by subclasses if specific behavior is needed
    public abstract boolean canAccess(Rank senderRank);
}
