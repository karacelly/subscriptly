package edu.bluejack21_2.subscriptly.utils;

import android.util.Log;

import java.util.ArrayList;

import edu.bluejack21_2.subscriptly.models.FriendRequest;
import edu.bluejack21_2.subscriptly.models.User;

public class Friend {

    private static Boolean checkSimilarity(String compared, String comparison1, String comparison2) {
        return compared.equals(comparison1) ? true : compared.equals(comparison2) ? true : false;
    }

    public static FriendRequest getFriendRequest(ArrayList<FriendRequest> requests, String currentUserId, String friendUserId) {
        for (FriendRequest fr:
             requests) {
            Log.d("GETFRIENDREQUEST", currentUserId + ", " + fr.getSender() + ", " + fr.getReceiver());
            Log.d("GETFRIENDREQUEST", checkSimilarity(currentUserId, fr.getSender(), fr.getReceiver()).toString());
            Log.d("GETFRIENDREQUEST", friendUserId + ", " + fr.getSender() + ", " + fr.getReceiver());
            Log.d("GETFRIENDREQUEST", checkSimilarity(friendUserId, fr.getSender(), fr.getReceiver()).toString());
            if(checkSimilarity(currentUserId, fr.getSender(), fr.getReceiver()) && checkSimilarity(friendUserId, fr.getSender(), fr.getReceiver()))
                return fr;
//            if(fr.getSender().equals(userId) || fr.getReceiver().equals(userId)) {
//                return  fr;
//            }
        }
        return null;
    }

    public static Boolean userAlreadyExist(ArrayList<User> users, String userID) {
        for (User user:
                users) {
            if(user.getUserID().equals(userID)) return true;
        }
        return false;
    }

}
