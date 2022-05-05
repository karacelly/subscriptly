package edu.bluejack21_2.subscriptly.models;

public class FriendRequest {
    private String receiverId, senderId;

    public FriendRequest(String receiverId, String senderId) {
        this.receiverId = receiverId;
        this.senderId = senderId;
    }

    public String getReceiver() {
        return receiverId;
    }

    public void setReceiver(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getSender() {
        return senderId;
    }

    public void setSender(String senderId) {
        this.senderId = senderId;
    }
}
