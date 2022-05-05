package edu.bluejack21_2.subscriptly.models;

public class FriendRequest {
    private User receiver, sender;

    public FriendRequest(User receiver, User sender) {
        this.receiver = receiver;
        this.sender = sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }
}
