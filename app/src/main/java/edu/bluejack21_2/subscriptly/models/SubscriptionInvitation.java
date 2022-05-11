package edu.bluejack21_2.subscriptly.models;

public class SubscriptionInvitation {
    private String invitationId;
    private User creator, invited;
    private Subscription subscription;

    public SubscriptionInvitation(String invitationId, User creator, User invited, Subscription subscription) {
        this.invitationId = invitationId;
        this.creator = creator;
        this.invited = invited;
        this.subscription = subscription;
    }

    public String getInvitationId() {
        return invitationId;
    }

    public void setInvitationId(String invitationId) {
        this.invitationId = invitationId;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public User getInvited() {
        return invited;
    }

    public void setInvited(User invited) {
        this.invited = invited;
    }

    public Subscription getSubscription() {
        return subscription;
    }

    public void setSubscription(Subscription subscription) {
        this.subscription = subscription;
    }
}
