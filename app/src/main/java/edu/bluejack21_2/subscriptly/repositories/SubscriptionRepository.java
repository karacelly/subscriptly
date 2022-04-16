package edu.bluejack21_2.subscriptly.repositories;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import edu.bluejack21_2.subscriptly.database.SubscriptlyDB;
import edu.bluejack21_2.subscriptly.models.Subscription;
import edu.bluejack21_2.subscriptly.models.User;

public class SubscriptionRepository {
    private static CollectionReference userRef = SubscriptlyDB.getDB().collection("subscriptions");
    private static StorageReference subscriptionStorageRef = SubscriptlyDB.getStorageDB().child("subscriptions");

    private static void insertSubscription(String name) {

    }

    public static ArrayList<Subscription> getAllSubscriptions () {
        ArrayList<Subscription> subscriptions = new ArrayList<>();

        SubscriptlyDB.getDB().collection("subscriptions").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                subscriptions.add(new Subscription(document.getId(), document.getString("name"), Integer.parseInt(document.get("bill").toString()), Integer.parseInt(document.get("duration").toString()), new ArrayList<User>()));
                            }
                        }
                    }
                });

        return subscriptions;
    }


}
