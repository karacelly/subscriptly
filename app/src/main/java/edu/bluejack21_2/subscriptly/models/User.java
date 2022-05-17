package edu.bluejack21_2.subscriptly.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class User {
    private String userID, name, username, email, password, image;
    private ArrayList<String> friends;
    private ArrayList<TransactionDetail> notifications = new ArrayList<>();

    public User(String userID, String name, String username, String email) {
        this.userID = userID;
        this.name = name;
        this.username = username;
        this.email = email;
    }

    public String getImage() {
        if(image == null) {
            return "https://firebasestorage.googleapis.com/v0/b/subscriptly-3ccfb.appspot.com/o/profile%2FDefaultProfile.jpg?alt=media&token=0a6f7d26-e3ab-4a20-8ba5-14e8885821f6";
        }
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public User(String userID, String name, String username, String email, String image, ArrayList<String> friends) {
        this.userID = userID;
        this.name = name;
        this.username = username;
        this.email = email;
        this.image = image;
        this.friends = friends;
    }

    public ArrayList<String> getFriends() {
        return friends != null ? friends : new ArrayList<>();
    }

    public void setFriends(ArrayList<String> friends) {
        this.friends = friends;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ArrayList<TransactionDetail> getNotifications() {
        return notifications;
    }

    public void setNotifications(ArrayList<TransactionDetail> notifications) {
        this.notifications = notifications;
    }

    public Map<String, Object> dataToMap() {
        Map<String, Object> userData = new HashMap<>();
        userData.put("name", name);
        userData.put("username", username);
        userData.put("email", email);
        userData.put("password", password);

        return userData;
    }

    public Map<String, Object> dataNoPasswordToMap() {
        Map<String, Object> userData = new HashMap<>();
        userData.put("name", name);
        userData.put("username", username);
        userData.put("email", email);

        return userData;
    }
}
