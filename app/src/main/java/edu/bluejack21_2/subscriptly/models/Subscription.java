package edu.bluejack21_2.subscriptly.models;

import com.google.firebase.firestore.FieldValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Subscription {
    private String name;
    private Integer bill, duration;
    private ArrayList<User> members;

    public Subscription(String name, Integer bill, Integer duration, ArrayList<User> members) {
        this.name = name;
        this.bill = bill;
        this.duration = duration;
        this.members = members;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getBill() {
        return bill;
    }

    public void setBill(Integer bill) {
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
        subscriptionData.put("duration", duration);
//        Map<String, Object> membersMap = new HashMap<>();
//        membersMap
        subscriptionData.put("members", FieldValue.arrayUnion());

        return subscriptionData;
    }

//    public Map<String, Object> membersToMap() {
//
//    }

}
