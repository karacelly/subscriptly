package edu.bluejack21_2.subscriptly.models;

public class SubscriptionInvitation {
    private String creatorId, invitedId, subscriptionId;

    public SubscriptionInvitation(String creatorId, String invitedId, String subscriptionId) {
        this.creatorId = creatorId;
        this.invitedId = invitedId;
        this.subscriptionId = subscriptionId;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public String getInvitedId() {
        return invitedId;
    }

    public void setInvitedId(String invitedId) {
        this.invitedId = invitedId;
    }

    public String getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(String subscriptionId) {
        this.subscriptionId = subscriptionId;
    }
}
