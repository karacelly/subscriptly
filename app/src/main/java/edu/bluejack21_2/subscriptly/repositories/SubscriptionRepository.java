package edu.bluejack21_2.subscriptly.repositories;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class SubscriptionRepository {
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static CollectionReference userRef = db.collection("subscriptions");

    private static FirebaseStorage storage = FirebaseStorage.getInstance();
    private static StorageReference storageRef = storage.getReference();

    StorageReference subscriptionStorageRef = storageRef.child("subscriptions");
}
