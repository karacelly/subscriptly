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
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

import edu.bluejack21_2.subscriptly.database.SubscriptlyDB;
import edu.bluejack21_2.subscriptly.interfaces.QueryFinishListener;
import edu.bluejack21_2.subscriptly.models.User;
import edu.bluejack21_2.subscriptly.utils.Crypt;

public class UserRepository {

    public static User LOGGED_IN_USER = null;
    private static final CollectionReference userRef = SubscriptlyDB.getDB().collection("users");
    private static final CollectionReference friendRequestRef = SubscriptlyDB.getDB().collection("friend_requests");
    private static final CollectionReference friendRef = SubscriptlyDB.getDB().collection("friends");

    public static void insertUser(String name, String username, String email, String password) {

        User tempInsert = new User("", name, username, email, Crypt.generateHash(password));

        Map<String, Object> userData = tempInsert.dataToMap();
        userRef.add(userData)
                .addOnSuccessListener(documentReference -> Log.d("Success", "document Snapshot added with ID: " + documentReference.getId()))
                .addOnFailureListener(e -> Log.w("Failed", "Error adding document", e));
    }

    public static void authenticateLogin(String email, String password, QueryFinishListener<User> listener) {
        Query findUserEmail = userRef.whereEqualTo("email", email);

        findUserEmail.get().addOnSuccessListener(documentSnapshots -> {
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
        });
    }

    public static void emailUniqueCheck(String email, QueryFinishListener<Boolean> listener) {
        Query findUserEmail = userRef.whereEqualTo("email", email);

        findUserEmail.get().addOnSuccessListener(documentSnapshots -> {
//                DocumentSnapshot userDocument = documentSnapshots.getDocuments().get(0);
            if (documentSnapshots.isEmpty()) {
                listener.onFinish(true);
            } else
                listener.onFinish(false);
        }).addOnFailureListener(e -> {
            listener.onFinish(false);
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

        findRequest.get().addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                QuerySnapshot receiverQS = task.getResult();
                if(!receiverQS.isEmpty() && !receiverQS.getDocuments().isEmpty()) {
                    DocumentSnapshot receiverDS = receiverQS.getDocuments().get(0);
                    receiverDS.getDocumentReference(receiverDS.getId()).delete().addOnCompleteListener(task1 -> {
                        listener.onFinish(true);
                    }).addOnFailureListener(e -> {
                        listener.onFinish(false);
                    });

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

    public static void acceptFriendRequest(String senderId, String receiverId, QueryFinishListener<Boolean> listener) {
        DocumentReference sender = userRef.document(senderId);
        DocumentReference receiver = userRef.document(receiverId);
        Query findRequest = friendRequestRef.whereEqualTo("sender", sender).whereEqualTo("receiver", receiver).limit(1);

        findRequest.get().addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                QuerySnapshot receiverQS = task.getResult();
                if(!receiverQS.isEmpty() && !receiverQS.getDocuments().isEmpty()) {
                    DocumentSnapshot receiverDS = receiverQS.getDocuments().get(0);
                    receiverDS.getDocumentReference(receiverDS.getId()).delete().addOnCompleteListener(task1 -> {
                        UserRepository.addFriend(senderId, receiverId, data -> {
                           if(data) {
                                listener.onFinish(true);
                           } else {
                                listener.onFinish(false);
                           }
                        });
                    }).addOnFailureListener(e -> {
                        listener.onFinish(false);
                    });

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
        DocumentReference newFriend = userRef.document(newFriendId);
        Query mainUserRow = friendRef.whereEqualTo("user", mainUser).limit(1);

        mainUserRow.get().addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                QuerySnapshot receiverQS = task.getResult();
                if(receiverQS.isEmpty() || receiverQS.getDocuments().isEmpty()) {
                    initializeFriend(userId, data -> {
                        if(data != null) {
                            data.update("friends", FieldValue.arrayUnion(newFriend));
                        } else {
                            listener.onFinish(false);
                        }
                    });

                } else {
                    DocumentSnapshot receiverDS = receiverQS.getDocuments().get(0);
                    receiverDS.getDocumentReference(receiverDS.getId()).update("friends", FieldValue.arrayUnion(newFriend));
                }
            } else {
                listener.onFinish(false);
            }
        }).addOnFailureListener(e -> {
            listener.onFinish(false);
        });
    }

    public static void addFriend(String firstUserId, String secondUserId, QueryFinishListener<Boolean> listener) {
        UserRepository.makeConnection(firstUserId, secondUserId, data -> {
            if(!data) {
                listener.onFinish(false);
            }
        });

        UserRepository.makeConnection(secondUserId, firstUserId, data -> {
            if(!data) {
                listener.onFinish(false);
            } else {
                listener.onFinish(true);
            }
        });

//        firstUserFriend.get().addOnCompleteListener(task -> {
//            if(task.isSuccessful()) {
//                QuerySnapshot receiverQS = task.getResult();
//                if(!receiverQS.isEmpty() && !receiverQS.getDocuments().isEmpty()) {
//                    DocumentSnapshot receiverDS = receiverQS.getDocuments().get(0);
//                    receiverDS.getDocumentReference(receiverDS.getId()).delete().addOnCompleteListener(task1 -> {
//                        listener.onFinish(true);
//                    }).addOnFailureListener(e -> {
//                        listener.onFinish(false);
//                    });
//
//                } else {
//                    initializeFriend(firstUserId, data -> {
//                        if(data != null) {
//                            data.update("friends", FieldValue.arrayUnion(secondUser));
//                        } else {
//                            listener.onFinish(false);
//                        }
//                    });
//                }
//            } else {
//                listener.onFinish(false);
//            }
//        }).addOnFailureListener(e -> {
//           listener.onFinish(false);
//        });
//
//        secondUserFriend.get().addOnCompleteListener(task -> {
//            if(task.isSuccessful()) {
//                QuerySnapshot receiverQS = task.getResult();
//                if(!receiverQS.isEmpty() && !receiverQS.getDocuments().isEmpty()) {
//                    DocumentSnapshot receiverDS = receiverQS.getDocuments().get(0);
//                    receiverDS.getDocumentReference(receiverDS.getId()).delete().addOnCompleteListener(task1 -> {
//                        listener.onFinish(true);
//                    }).addOnFailureListener(e -> {
//                        listener.onFinish(false);
//                    });
//
//                } else {
//                    initializeFriend(secondUserId, data -> {
//                        if(data != null) {
//                            data.update("friends", FieldValue.arrayUnion(firstUser));
//                        } else {
//                            listener.onFinish(false);
//                        }
//                    });
//                }
//            } else {
//                listener.onFinish(false);
//            }
//        }).addOnFailureListener(e -> {
//           listener.onFinish(false);
//        });
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
