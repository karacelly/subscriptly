package edu.bluejack21_2.subscriptly.models;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import edu.bluejack21_2.subscriptly.repositories.SubscriptionRepository;
import edu.bluejack21_2.subscriptly.repositories.UserRepository;

public class Subscription {
    private String subscriptionId, name, image;
    private Long bill;
    private Integer duration;
    private Timestamp startAt;

    public Subscription(String subscriptionId, String name, String image, Long bill, Integer duration, Timestamp startAt, ArrayList<User> members) {
        this.subscriptionId = subscriptionId;
        this.name = name;
        this.image = image;
        this.bill = bill;
        this.duration = duration;
        this.startAt = startAt;
        this.members = members;
    }

    private ArrayList<User> members;

    public Subscription(String subscriptionId, String name, Long bill, Integer duration, ArrayList<User> members) {
        this.subscriptionId = Subscription.this.subscriptionId;
        this.name = name;
        this.bill = bill;
        this.duration = duration;
        this.members = members;
    }

    public Subscription(String subscriptionId, String name, Long bill, Integer duration, Timestamp startAt, ArrayList<User> members) {
        this.subscriptionId = Subscription.this.subscriptionId;
        this.name = name;
        this.bill = bill;
        this.duration = duration;
        this.startAt = startAt;
        this.members = members;
    }

    public String getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(String subscriptionId) {
        this.subscriptionId = Subscription.this.subscriptionId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Timestamp getStartAt() {
        return startAt;
    }

    public void setStartAt(Timestamp startAt) {
        this.startAt = startAt;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getBill() {
        return bill;
    }

    public void setBill(Long bill) {
        this.bill = bill;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public ArrayList<User> getMembers() {
        return members;
    }

    public void setMembers(ArrayList<User> members) {
        this.members = members;
    }

    public Map<String, Object> dataToMap() {
        Map<String, Object> subscriptionData = new HashMap<>();
        subscriptionData.put("name", name);
        subscriptionData.put("bill", bill);
        subscriptionData.put("image", image);
        subscriptionData.put("month_duration", duration);
        subscriptionData.put("start_at", startAt);

        return subscriptionData;
    }

    public Map<String, Object> membersToMap(String subscriptionId) {
        Map<String, Object> memberData = new HashMap<>();
        memberData.put("creator", UserRepository.userRef.document(UserRepository.LOGGED_IN_USER.getUserID()));
        memberData.put("subscription", SubscriptionRepository.subscriptionRef.document(subscriptionId));
        SubscriptionRepository.chosenFriends.add(UserRepository.LOGGED_IN_USER);
        ArrayList<DocumentReference> users = new ArrayList<>();
        for (User user:
             SubscriptionRepository.chosenFriends) {
            users.add(UserRepository.userRef.document(user.getUserID()));
        }
        memberData.put("users", users);
        memberData.put("valid_from", Timestamp.now());
        memberData.put("valid_to", null);

        return memberData;
    }

}
