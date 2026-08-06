package com.wartime.system.model;

public abstract class AbstractMessage {
    protected String content;
    protected Rank senderRank;
    protected MessageStatus status;
    protected Group targetGroup;
    protected AbstractUser targetUser;

    public AbstractMessage(String content, Rank senderRank, Group targetGroup, AbstractUser targetUser) {
        this.content = content;
        this.senderRank = senderRank;
        this.targetGroup = targetGroup;
        this.targetUser = targetUser;
        this.status = MessageStatus.SENT;
    }

    public String getContent() {
        return content;
    }

    public Rank getSenderRank() {
        return senderRank;
    }

    public MessageStatus getStatus() {
        return status;
    }

    public void setStatus(MessageStatus status) {
        this.status = status;
    }

    public Group getTargetGroup() {
        return targetGroup;
    }

    public AbstractUser getTargetUser() {
        return targetUser;
    }
}
