package edu.bluejack21_2.subscriptly.repositories;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthCredential;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import edu.bluejack21_2.subscriptly.MainActivity;
import edu.bluejack21_2.subscriptly.database.SubscriptlyDB;
import edu.bluejack21_2.subscriptly.interfaces.QueryFinishListener;
import edu.bluejack21_2.subscriptly.models.User;
import edu.bluejack21_2.subscriptly.utils.Crypt;

public class UserRepository {

    public static final CollectionReference userRef = SubscriptlyDB.getDB().collection("users");
    public static final CollectionReference friendRequestRef = SubscriptlyDB.getDB().collection("friend_requests");
    public static final CollectionReference friendRef = SubscriptlyDB.getDB().collection("friends");
    public static User LOGGED_IN_USER = null;

    private static FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    public static User getLoggedInUser(){
        if(LOGGED_IN_USER == null) {
            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

            getUser(firebaseUser.getUid(), user -> {
                LOGGED_IN_USER = user;
            });
        }

        while(LOGGED_IN_USER == null){

        }
        return LOGGED_IN_USER;
    }

    public static void logOutFirebaseUser(){
        firebaseAuth.signOut();
    }

    public static void insertUser(String name, String username, String email, String password) {

        User tempInsert = new User("", name, username, email, Crypt.generateHash(password));

        Map<String, Object> userData = tempInsert.dataToMap();
        userRef.add(userData)
                .addOnSuccessListener(documentReference -> Log.d("Success", "document Snapshot added with ID: " + documentReference.getId()))
                .addOnFailureListener(e -> Log.w("Failed", "Error adding document", e));
    }

    public static void signUp(String name, String username, String email, String password, QueryFinishListener<Boolean> listener){
        String TAG = "Sign_Up";
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "createUserWithEmail:success");

                            fillUserInformation(name, username, flag -> {
                                if(flag) {
                                    listener.onFinish(true);
                                }else{
                                    listener.onFinish(false);
                                }
                            });
                        }else{
                            listener.onFinish(false);
                        }
                    }
                });
    }

    public static void updateUserData(String userId, String name, String username, String email, String fileName, QueryFinishListener<Boolean> listener) {
        DocumentReference user = userRef.document(userId);

        user.update(
                "name", name,
                "username", username,
                "email", email,
                "image", fileName).addOnSuccessListener(task -> {
            listener.onFinish(true);
        }).addOnFailureListener(e -> {
            listener.onFinish(false);
        });
    }

    public static void updateUserPassword(String userId, String password, QueryFinishListener<Boolean> listener) {
        DocumentReference user = userRef.document(userId);

        user.update(
                "password", Crypt.generateHash(password)).addOnSuccessListener(task -> {
            listener.onFinish(true);
        }).addOnFailureListener(e -> {
            listener.onFinish(false);
        });
    }

    public static User documentToUser(DocumentSnapshot userDoc) {
        String key = userDoc.getId();
        String name = userDoc.getString("name");
        String email = userDoc.getString("email");
        String password = userDoc.getString("password");
        String username = userDoc.getString("username");
        String image = userDoc.getString("image");
        ArrayList<String> friends = new ArrayList<>();
        if (userDoc.contains("friends")) {
            for (DocumentReference ref :
                    (ArrayList<DocumentReference>) userDoc.get("friends")) {
                friends.add(ref.getId());
            }
        }
        return new User(key, name, username, email, password, image, friends);
    }

    public static void authenticateLogin(String email, String password, QueryFinishListener<User> listener) {
        Query findUserEmail = userRef.whereEqualTo("email", email);

        findUserEmail.get().addOnSuccessListener(documentSnapshots -> {
            if (!documentSnapshots.isEmpty()) {
                DocumentSnapshot userDocument = documentSnapshots.getDocuments().get(0);

                String databaseUserPassword = userDocument.getString("password");
                if (Crypt.verifyHash(password, databaseUserPassword)) {
                    LOGGED_IN_USER = documentToUser(userDocument);
                    listener.onFinish(LOGGED_IN_USER);
                } else
                    listener.onFinish(null);
            } else
                listener.onFinish(null);
        });
    }

    public static void authWithGoogleAccount(GoogleSignInAccount account, QueryFinishListener<Boolean> newUser) {
        String tag = "GOOGLE_SIGN_IN_TAG";
        Log.d(tag, "authWithGoogleAccount: begin firebase auth with google account");
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);

        firebaseAuth = FirebaseAuth.getInstance();

        firebaseAuth.signInWithCredential(credential)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Log.d(tag, "onSuccess: Logged in");

                        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

                        String uid = firebaseUser.getUid();
                        String email = firebaseUser.getEmail();

                        Log.d(tag, "onSuccess: Email: " + email);
                        Log.d(tag, "onSuccess: UID: " + uid);

                        if(authResult.getAdditionalUserInfo().isNewUser()) {
                            newUser.onFinish(true);
                            Log.d(tag, "onSuccess: Account created..\n" + email);
                        }else {
                            Log.d(tag, "onSuccess: Existing user..\n" + email);
                            newUser.onFinish(false);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(tag, "onFailure: Login failed " + e.getMessage());
                    }
                });
    }

    public static void fillUserInformation(String name, String username, QueryFinishListener<Boolean> listener) {
        String TAG = "GOOGLE_SIGN_IN_TAG";
        firebaseAuth = FirebaseAuth.getInstance();

        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        User user = new User(firebaseUser.getUid(), name, username, firebaseUser.getEmail());
        Map<String, Object> userData = user.dataNoPasswordToMap();

        userRef.document(firebaseUser.getUid()).set(userData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        LOGGED_IN_USER = user;
                        listener.onFinish(true);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        listener.onFinish(false);
                    }
                });
    }

    public static void emailIsUnique(String email, QueryFinishListener<Boolean> listener) {
        Query findUserEmail = userRef.whereEqualTo("email", email);

        findUserEmail.get().addOnSuccessListener(documentSnapshots -> {
            if (documentSnapshots.isEmpty()) {
                listener.onFinish(true);
            } else
                listener.onFinish(false);
        }).addOnFailureListener(e -> {
            listener.onFinish(false);
        });
    }

    public static void usernameIsUnique(String username, QueryFinishListener<Boolean> listener) {
        Query findUserUsername = userRef.whereEqualTo("username", username);

        findUserUsername.get().addOnSuccessListener(documentSnapshots -> {
            if (documentSnapshots.isEmpty()) {
                listener.onFinish(true);
            } else
                listener.onFinish(false);
        }).addOnFailureListener(e -> {
            listener.onFinish(false);
        });
    }

    public static void getUser(String id, QueryFinishListener<User> listener) {
        DocumentReference user = userRef.document(id);
        user.get().addOnSuccessListener(userSnapshot -> {
                if (userSnapshot.exists()) {
                    listener.onFinish(documentToUser(userSnapshot));
                } else
                    listener.onFinish(null);
            }).addOnFailureListener(e -> {
                listener.onFinish(null);
        });
    }

    public static void sendFriendRequest(String senderId, String receiverId, QueryFinishListener<Boolean> listener) {
        DocumentReference sender = userRef.document(senderId);
        DocumentReference receiver = userRef.document(receiverId);
        Map<String, Object> friendRequestMap = new HashMap<>();
        friendRequestMap.put("sender", sender);
        friendRequestMap.put("receiver", receiver);
        friendRequestRef.add(friendRequestMap)
                .addOnSuccessListener(documentReference -> {
                    Log.d("Success", "Friend Request document Snapshot added with ID: " + documentReference.getId());
                    listener.onFinish(true);
                })
                .addOnFailureListener(e -> {
                    Log.w("Failed", "Error adding document", e);
                    listener.onFinish(false);
                });
    }

    public static void rejectFriendRequest(String senderId, String receiverId, QueryFinishListener<Boolean> listener) {
        DocumentReference sender = userRef.document(senderId);
        DocumentReference receiver = userRef.document(receiverId);
        Query findRequest = friendRequestRef.whereEqualTo("sender", sender).whereEqualTo("receiver", receiver).limit(1);

        findRequest.get().addOnSuccessListener(task -> {
            if (!task.isEmpty() && !task.getDocuments().isEmpty()) {
                DocumentSnapshot receiverDS = task.getDocuments().get(0);
                receiverDS.getReference().delete().addOnSuccessListener(task1 -> {
                    listener.onFinish(true);
                }).addOnFailureListener(e -> {
                    listener.onFinish(false);
                });

            } else {
                listener.onFinish(false);
            }
        }).addOnFailureListener(e -> {
            listener.onFinish(false);
        });
    }

    public static void acceptFriendRequest(String senderId, String receiverId, QueryFinishListener<Boolean> listener) {
        DocumentReference sender = userRef.document(senderId);
        DocumentReference receiver = userRef.document(receiverId);
        Query findRequest = friendRequestRef.whereEqualTo("sender", sender).whereEqualTo("receiver", receiver).limit(1);

        findRequest.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot receiverQS = task.getResult();
                if (!receiverQS.isEmpty() && !receiverQS.getDocuments().isEmpty()) {
                    DocumentSnapshot receiverDS = receiverQS.getDocuments().get(0);
                    if (receiverDS != null) {
                        receiverDS.getReference().delete().addOnCompleteListener(task1 -> {
                            UserRepository.addFriend(senderId, receiverId, data -> {
                                if (data) {
                                    listener.onFinish(true);
                                } else {
                                    listener.onFinish(false);
                                }
                            });
                        }).addOnFailureListener(e -> {
                            listener.onFinish(false);
                        });
                    }
                } else {
                    listener.onFinish(false);
                }
            } else {
                listener.onFinish(false);
            }
        }).addOnFailureListener(e -> {
            listener.onFinish(false);
        });
    }

    public static void initializeFriend(String userId, QueryFinishListener<DocumentReference> listener) {
        DocumentReference user = userRef.document(userId);
        Map<String, Object> friendMap = new HashMap<>();
        friendMap.put("user", user);
        friendRef.add(friendMap)
                .addOnSuccessListener(documentReference -> listener.onFinish(documentReference))
                .addOnFailureListener(e -> listener.onFinish(null));
    }

    public static void makeConnection(String userId, String newFriendId, QueryFinishListener<Boolean> listener) {
        DocumentReference mainUser = userRef.document(userId);
        DocumentReference friend = userRef.document(newFriendId);

        mainUser.update("friends", FieldValue.arrayUnion(friend)).addOnSuccessListener(task -> {
            listener.onFinish(true);
        }).addOnFailureListener(e -> {
            listener.onFinish(false);
        });
    }

    public static void removeConnection(String userId, String friendId, QueryFinishListener<Boolean> listener) {
        DocumentReference mainUser = userRef.document(userId);
        DocumentReference friend = userRef.document(friendId);

        mainUser.update("friends", FieldValue.arrayRemove(friend)).addOnSuccessListener(task -> {
            listener.onFinish(true);
        }).addOnFailureListener(e -> {
            listener.onFinish(false);
        });
    }

    public static void addFriend(String firstUserId, String secondUserId, QueryFinishListener<Boolean> listener) {
        UserRepository.makeConnection(firstUserId, secondUserId, data -> {
            if (!data) {
                listener.onFinish(false);
            }
        });

        UserRepository.makeConnection(secondUserId, firstUserId, data -> {
            if (!data) {
                listener.onFinish(false);
            } else {
                listener.onFinish(true);
            }
        });
    }

    public static void removeFriend(String firstUserId, String secondUserId, QueryFinishListener<Boolean> listener) {
        UserRepository.removeConnection(firstUserId, secondUserId, data -> {
            if (!data) {
                listener.onFinish(false);
            }
        });

        UserRepository.removeConnection(secondUserId, firstUserId, data -> {
            if (!data) {
                listener.onFinish(false);
            } else {
                listener.onFinish(true);
            }
        });
    }


    public static Boolean checkFriend(User user, String friendUserId) {
        if(user == null || user.getFriends() == null) return false;
        return user.getFriends().contains(friendUserId);
    }

    public static void userCheck(String userId, QueryFinishListener<User> listener) {
        DocumentReference user = userRef.document(userId);

        user.get().addOnSuccessListener(userDocument -> {
            if (userDocument.exists()) {
                listener.onFinish(documentToUser(userDocument));
            } else
                listener.onFinish(null);
        }).addOnFailureListener(e -> {
            listener.onFinish(null);
        });

    }
}
