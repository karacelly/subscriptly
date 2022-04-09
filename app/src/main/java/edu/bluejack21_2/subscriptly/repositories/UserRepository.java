package edu.bluejack21_2.subscriptly.repositories;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;

import edu.bluejack21_2.subscriptly.interfaces.QueryFinishListener;
import edu.bluejack21_2.subscriptly.models.User;
import edu.bluejack21_2.subscriptly.utils.Crypt;

public class UserRepository {
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static CollectionReference userRef = db.collection("users");
    public static User LOGGED_IN_USER = null;

    private static boolean uniqueEmail = true;

    public static void insertUser(String name, String username, String email, String password) {

        String key = userRef.getId();
        User tempInsert = new User(key, name, username, email, Crypt.generateHash(password));

        Map<String, Object> userData = tempInsert.dataToMap();
        userRef.add(userData);
    }

    public static void authenticateLogin(String email, String password, QueryFinishListener<User> listener) {
        Query findUserEmail = db.collection("users").whereEqualTo("email", email);

        findUserEmail.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot documentSnapshots) {
                if(!documentSnapshots.isEmpty())
                {
                    DocumentSnapshot userDocument = documentSnapshots.getDocuments().get(0);

                    String databaseUserPassword = userDocument.getString("password");
                    if(Crypt.verifyHash(password, databaseUserPassword))
                    {
                        String key = userDocument.getId();
                        String name = userDocument.getString("name");
                        String username = userDocument.getString("username");
                        LOGGED_IN_USER = new User(key, name, username, email, password);
                        listener.onFinish(LOGGED_IN_USER);
                    }
                    else
                        listener.onFinish(null);
                }
                else
                    listener.onFinish(null);
            }
        });
    }

    public static void emailUniqueCheck(String email, QueryFinishListener<Boolean> listener)
    {
        Query findUserEmail = db.collection("users").whereEqualTo("email", email);

        findUserEmail.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot documentSnapshots) {
//                DocumentSnapshot userDocument = documentSnapshots.getDocuments().get(0);

                if(!documentSnapshots.isEmpty())
                {
                    listener.onFinish(false);
                }
                else
                    listener.onFinish(true);
            }
        });
    }

    public static void userCheck(String email, QueryFinishListener<User> listener)
    {
        Query findUserEmail = db.collection("users").whereEqualTo("email", email);

        findUserEmail.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot documentSnapshots) {

                if(!documentSnapshots.isEmpty())
                {
                    DocumentSnapshot userDocument = documentSnapshots.getDocuments().get(0);
                    String databaseUserPassword = userDocument.getString("password");
                    String key = userDocument.getId();
                    String name = userDocument.getString("name");
                    String username = userDocument.getString("username");
                    LOGGED_IN_USER = new User(key, name, username, email, databaseUserPassword);
                    listener.onFinish(LOGGED_IN_USER);
                }
                else
                    listener.onFinish(null);
            }
        });
    }
}
