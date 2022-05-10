package edu.bluejack21_2.subscriptly.repositories;

import android.util.Log;

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
import java.util.List;

import edu.bluejack21_2.subscriptly.database.SubscriptlyDB;
import edu.bluejack21_2.subscriptly.interfaces.QueryFinishListener;
import edu.bluejack21_2.subscriptly.models.Subscription;
import edu.bluejack21_2.subscriptly.models.User;

public class SubscriptionRepository {
    public static Subscription ACTIVE_SUBSCRIPTION = null;
    public static CollectionReference subscriptionRef = SubscriptlyDB.getDB().collection("subscriptions");
    public static CollectionReference memberRef = SubscriptlyDB.getDB().collection("members");
    public static StorageReference subscriptionStorageRef = SubscriptlyDB.getStorageDB().child("subscriptions");

    public static ArrayList<User> chosenFriends = new ArrayList<>();
    private static void insertSubscription(Subscription subscription) {

    }

    public static ArrayList<Subscription> getAllSubscriptions () {
        ArrayList<Subscription> subscriptions = new ArrayList<>();

        SubscriptlyDB.getDB().collection("subscriptions").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                subscriptions.add(new Subscription(document.getId(), document.getString("name"), Long.parseLong(document.get("bill").toString()), Integer.parseInt(document.get("duration").toString()), new ArrayList<User>()));
                            }
                        }
                    }
                });

        return subscriptions;
    }

    public static void documentToSubscription(DocumentSnapshot subsDoc, QueryFinishListener<Subscription> listener) {
        ArrayList<User> members = new ArrayList<>();

        SubscriptlyDB.getDB().collection("members").whereEqualTo("subscription_id", subsDoc.getId()).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    List<DocumentReference> userRefs = (List<DocumentReference>) document.get("users");
                    for (DocumentReference userRef : userRefs) {
                        userRef.get().addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                DocumentSnapshot userDoc = task1.getResult();
                                members.add(UserRepository.documentToUser(userDoc));
                                if (members.size() == userRefs.size()) {
                                    Subscription s = new Subscription(subsDoc.getId(), subsDoc.getString("name"), Long.parseLong(subsDoc.get("bill").toString()), Integer.parseInt(subsDoc.get("duration").toString()), members);
                                    listener.onFinish(s);
                                }
                            } else {
                                listener.onFinish(null);
                            }
                        });
                    }
                }
            } else {
                listener.onFinish(null);
            }
        });
    }
}
