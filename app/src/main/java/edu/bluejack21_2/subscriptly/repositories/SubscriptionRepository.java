package edu.bluejack21_2.subscriptly.repositories;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import edu.bluejack21_2.subscriptly.database.SubscriptlyDB;

public class SubscriptionRepository {
    private static CollectionReference userRef = SubscriptlyDB.getDB().collection("subscriptions");
    private static StorageReference subscriptionStorageRef = SubscriptlyDB.getStorageDB().child("subscriptions");

    private static void insertSubscription(String name) {

    }

}
