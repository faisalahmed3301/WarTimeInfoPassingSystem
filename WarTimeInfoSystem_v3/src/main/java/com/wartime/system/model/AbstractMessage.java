package com.wartime.system.model;

public abstract class AbstractMessage {
    protected String content;
    protected Rank senderRank;
    protected MessageStatus status;

    public AbstractMessage(String content, Rank senderRank) {
        this.content = content;
        this.senderRank = senderRank;
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
}
