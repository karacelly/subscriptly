package edu.bluejack21_2.subscriptly.repositories;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;

import edu.bluejack21_2.subscriptly.database.SubscriptlyDB;
import edu.bluejack21_2.subscriptly.interfaces.QueryFinishListener;
import edu.bluejack21_2.subscriptly.models.User;
import edu.bluejack21_2.subscriptly.utils.Crypt;

public class UserRepository {

    public static User LOGGED_IN_USER = null;
    private static final CollectionReference userRef = SubscriptlyDB.getDB().collection("users");
    private static final boolean uniqueEmail = true;

    public static void insertUser(String name, String username, String email, String password) {

        User tempInsert = new User("", name, username, email, Crypt.generateHash(password));

        Map<String, Object> userData = tempInsert.dataToMap();
        userRef.add(userData)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("Success", "document Snapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Failed", "Error adding document", e);
                    }
                });
    }

    public static void authenticateLogin(String email, String password, QueryFinishListener<User> listener) {
        Query findUserEmail = userRef.whereEqualTo("email", email);

        findUserEmail.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot documentSnapshots) {
                if (!documentSnapshots.isEmpty()) {
                    DocumentSnapshot userDocument = documentSnapshots.getDocuments().get(0);

                    String databaseUserPassword = userDocument.getString("password");
                    if (Crypt.verifyHash(password, databaseUserPassword)) {
                        String key = userDocument.getId();
                        String name = userDocument.getString("name");
                        String username = userDocument.getString("username");
                        LOGGED_IN_USER = new User(key, name, username, email, password);
                        listener.onFinish(LOGGED_IN_USER);
                    } else
                        listener.onFinish(null);
                } else
                    listener.onFinish(null);
            }
        });
    }

    public static void emailUniqueCheck(String email, QueryFinishListener<Boolean> listener) {
        Query findUserEmail = userRef.whereEqualTo("email", email);

        findUserEmail.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot documentSnapshots) {
//                DocumentSnapshot userDocument = documentSnapshots.getDocuments().get(0);

                if (!documentSnapshots.isEmpty()) {
                    listener.onFinish(false);
                } else
                    listener.onFinish(true);
            }
        });
    }

    public static void usernameUniqueCheck(String username, QueryFinishListener<Boolean> listener) {
        Query findUserUsername = userRef.whereEqualTo("username", username);

        findUserUsername.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot documentSnapshots) {
//                DocumentSnapshot userDocument = documentSnapshots.getDocuments().get(0);

                if (!documentSnapshots.isEmpty()) {
                    listener.onFinish(false);
                } else
                    listener.onFinish(true);
            }
        });
    }

    public static void getUser(String id, QueryFinishListener<User> listener) {
        DocumentReference findUser = userRef.document(id);

        findUser.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot userDocument = task.getResult();
                    if (userDocument.exists()) {
                        String databaseUserPassword = userDocument.getString("password");
                        String key = userDocument.getId();
                        String name = userDocument.getString("name");
                        String email = userDocument.getString("email");
                        String username = userDocument.getString("username");
                        listener.onFinish(new User(key, name, username, email, databaseUserPassword));
                    } else
                        listener.onFinish(null);
                }
            }
        });
    }

    public static void userCheck(String email, QueryFinishListener<User> listener) {
        Query findUserEmail = userRef.whereEqualTo("email", email);

        findUserEmail.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot documentSnapshots) {

                if (!documentSnapshots.isEmpty()) {
                    DocumentSnapshot userDocument = documentSnapshots.getDocuments().get(0);
                    String databaseUserPassword = userDocument.getString("password");
                    String key = userDocument.getId();
                    String name = userDocument.getString("name");
                    String username = userDocument.getString("username");
                    LOGGED_IN_USER = new User(key, name, username, email, databaseUserPassword);
                    listener.onFinish(LOGGED_IN_USER);
                } else
                    listener.onFinish(null);
            }
        });
    }
}
