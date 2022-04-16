package edu.bluejack21_2.subscriptly.database;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class SubscriptlyDB {
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static FirebaseStorage storage = FirebaseStorage.getInstance();
    private static StorageReference storageRef = storage.getReference();

    public static FirebaseFirestore getDB(){
        return db;
    }

    public static StorageReference getStorageDB() {
        return storageRef;
    }


}
