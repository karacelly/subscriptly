package edu.bluejack21_2.subscriptly.repositories;

import android.util.Log;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import edu.bluejack21_2.subscriptly.database.SubscriptlyDB;
import edu.bluejack21_2.subscriptly.interfaces.QueryFinishListener;
import edu.bluejack21_2.subscriptly.models.Subscription;
import edu.bluejack21_2.subscriptly.models.TransactionDetail;
import edu.bluejack21_2.subscriptly.models.TransactionHeader;
import edu.bluejack21_2.subscriptly.models.User;

public class SubscriptionRepository {
    public static Subscription ACTIVE_SUBSCRIPTION = null;
    public static CollectionReference subscriptionRef = SubscriptlyDB.getDB().collection("subscriptions");
    public static CollectionReference subscriptionInvitationRef = SubscriptlyDB.getDB().collection("subscription_invitations");
    public static CollectionReference memberRef = SubscriptlyDB.getDB().collection("members");
    public static StorageReference subscriptionStorageRef = SubscriptlyDB.getStorageDB().child("subscriptions");

    public static ArrayList<User> chosenFriends = new ArrayList<>();

    public static void insertSubscription(Subscription subscription, Calendar calendar, QueryFinishListener<Boolean> listener) {
        Log.d("INSERT SUBSCRIPTION", subscription.getName());
        DocumentReference creator = UserRepository.userRef.document(FirebaseAuth.getInstance().getCurrentUser().getUid());
        Map<String, Object> subscriptionData = subscription.dataToMap();
        subscriptionRef.add(subscriptionData)
                .addOnSuccessListener(documentReference -> {
                    for (User user :
                            subscription.getMembers()) {
                        sendInvitation(creator.getId(), documentReference.getId(), user.getUserID(), sendInvitationListener -> {
                            if (!sendInvitationListener) {
                                listener.onFinish(false);
                            }
                        });
                    }

                    Map<String, Object> memberData = subscription.membersToMap(documentReference.getId());
                    memberRef.add(memberData).addOnSuccessListener(docMemberRef -> {
                        SubscriptionRepository.chosenFriends.clear();
                        CollectionReference transactionHeaderRef = getTransactionHeaderRef(documentReference.getId());

                        for (int i = 0; i < subscription.getDuration(); i++) {
                            Map<String, Object> billingData = new HashMap<>();
                            calendar.add(Calendar.MONTH, 1);
                            Timestamp timestamp = new Timestamp(calendar.getTime());
                            billingData.put("billing_date", timestamp);
                            int finalI = i;
                            transactionHeaderRef.add(billingData).addOnSuccessListener(docTransactionHeaderRef -> {
                                if (finalI == subscription.getDuration() - 1) {
                                    listener.onFinish(true);
                                }
                            }).addOnFailureListener(e -> {
                                listener.onFinish(false);
                            });
                        }
                        listener.onFinish(true);
                    }).addOnFailureListener(e -> {
                        listener.onFinish(false);
                    });
                })
                .addOnFailureListener(e -> {
                    listener.onFinish(false);
                });
    }

    public static void sendInvitation(String creatorId, String subscriptionId, String invitedId, QueryFinishListener<Boolean> listener) {
        DocumentReference creator = UserRepository.userRef.document(creatorId);
        DocumentReference invited = UserRepository.userRef.document(invitedId);
        DocumentReference subscription = SubscriptionRepository.subscriptionRef.document(subscriptionId);

        Map<String, Object> invitationData = new HashMap<>();
        invitationData.put("creator", creator);
        invitationData.put("invited", invited);
        invitationData.put("subscription", subscription);

        Log.d("SEND INVITATION", "OTW SENDING");
        subscriptionInvitationRef.add(invitationData).addOnSuccessListener(invitationDocRef -> {
            Log.d("SEND INVITATION", "SUKSES");
            listener.onFinish(true);
        }).addOnFailureListener(e -> {
            Log.d("SEND INVITATION", "GAGAL");
            Log.d("SEND INVITATION", e.toString());
            listener.onFinish(false);
        });
    }

    public static void getSubscriptionMemberReference(String subscriptionId, QueryFinishListener<DocumentSnapshot> listener) {
        DocumentReference subscription = subscriptionRef.document(subscriptionId);
        Query findMembers = memberRef.whereEqualTo("subscription", subscription).whereEqualTo("valid_to", null).limit(1);
        findMembers.get().addOnSuccessListener(qMemberSnapshot -> {
            if (qMemberSnapshot.getDocuments().size() > 0) {
                DocumentSnapshot memberSnapshot = qMemberSnapshot.getDocuments().get(0);
                listener.onFinish(memberSnapshot);
            } else {
                listener.onFinish(null);
            }
        }).addOnFailureListener(e -> {
            listener.onFinish(null);
        });
    }

    public static Map<String, Object> memberToMap(String creatorId, String subscriptionId, ArrayList<DocumentReference> users) {
        DocumentReference creator = UserRepository.userRef.document(creatorId);
        DocumentReference subscription = subscriptionRef.document(subscriptionId);

        Map<String, Object> newMemberData = new HashMap<>();
        newMemberData.put("creator", creator);
        newMemberData.put("subscription", subscription);
        newMemberData.put("users", users);
        newMemberData.put("valid_from", Timestamp.now());
        newMemberData.put("valid_to", null);

        return newMemberData;
    }


    public static void removeMember(String userId, String subscriptionId, QueryFinishListener<Boolean> listener) {
        getSubscriptionMemberReference(subscriptionId, memberSnapshot -> {
            if (memberSnapshot != null) {
                DocumentReference oldMemberDocRef = memberSnapshot.getReference();
                DocumentReference creator = memberSnapshot.getDocumentReference("creator");
                ArrayList<DocumentReference> users = (ArrayList<DocumentReference>) memberSnapshot.get("users");

                for (DocumentReference userRef :
                        users) {
                    if (userRef.getId().equals(userId)) {
                        users.remove(userRef);
                    }
                }

                Map<String, Object> newMemberData = memberToMap(creator.getId(), subscriptionId, users);

                oldMemberDocRef.update("valid_to", Timestamp.now()).addOnSuccessListener(updateMember -> {
                    memberRef.add(newMemberData).addOnSuccessListener(statusAdd -> {
                        listener.onFinish(true);
                    }).addOnFailureListener(e -> {
                        listener.onFinish(false);
                    });
                }).addOnFailureListener(e -> {
                    listener.onFinish(false);
                });
            }
        });
    }


    public static void getUserSubscriptions(String userId, QueryFinishListener<ArrayList<Subscription>> listener) {
        DocumentReference user = UserRepository.userRef.document(userId);
        ArrayList<Subscription> subscriptions = new ArrayList<>();
        SubscriptionRepository.memberRef.whereEqualTo("valid_to", null).whereArrayContains("users", user).get()
                .addOnSuccessListener(memberSnapshots -> {
                    for (DocumentSnapshot memberSnapshot :
                            memberSnapshots) {
                        DocumentReference subscription = memberSnapshot.getDocumentReference("subscription");

                        if (subscription != null) {
                            subscription.get().addOnSuccessListener(subscriptionSnapshot -> {
                                SubscriptionRepository.documentToSubscription(subscriptionSnapshot, data -> {
                                    if (data != null) {
                                        subscriptions.add(data);
                                        if (subscriptions.size() == memberSnapshots.size()) {
                                            listener.onFinish(subscriptions);
                                        }
                                    }
                                });
                            }).addOnFailureListener(e -> {
                                listener.onFinish(null);
                            });
                        }
                    }
                }).addOnFailureListener(e -> {
            listener.onFinish(null);
        });
    }


    public static void documentToSubscription(DocumentSnapshot subscriptionDoc, QueryFinishListener<Subscription> listener) {
        String id = subscriptionDoc.getId();
        Long bill = subscriptionDoc.getLong("bill");
        String image = subscriptionDoc.getString("image");
        Integer duration = subscriptionDoc.getLong("month_duration").intValue();
        String name = subscriptionDoc.getString("name");
        Timestamp startAt = subscriptionDoc.getTimestamp("start_at");
        ArrayList<TransactionHeader> headers = new ArrayList<>();
        subscriptionDoc.getReference().collection("transaction_headers").get()
                .addOnSuccessListener(headerSnapshots -> {
                    memberRef.whereEqualTo("subscription", subscriptionDoc.getReference()).whereEqualTo("valid_to", null).get().addOnSuccessListener(memberSnapshots -> {

                        if (memberSnapshots.isEmpty()) {
                            listener.onFinish(null);
                        } else {
                            DocumentSnapshot member = memberSnapshots.getDocuments().get(0);
                            ArrayList<User> users = new ArrayList<>();
                            ArrayList<DocumentReference> userRefs = (ArrayList<DocumentReference>) member.get("users");
                            if (userRefs.isEmpty()) {
                                listener.onFinish(new Subscription(id, name, image, bill, duration, startAt, headers, users));
                            } else {
                                for (DocumentReference userRef :
                                        userRefs) {
                                    UserRepository.getUser(userRef.getId(), userData -> {
                                        if (userData != null) {
                                            users.add(userData);
                                            if (users.size() == userRefs.size()) {
                                                for (DocumentSnapshot headerSnapshot :
                                                        headerSnapshots.getDocuments()) {
                                                    documentToTransactionHeader(headerSnapshot, header -> {
                                                        if (header != null) {
                                                            headers.add(header);
                                                            if (headers.size() == headerSnapshots.size()) {
                                                                listener.onFinish(new Subscription(id, name, image, bill, duration, startAt, headers, users));
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
                        }
                    }).addOnFailureListener(e -> {
                        listener.onFinish(null);
                    });
                }).addOnFailureListener(e -> {
            listener.onFinish(null);
        });
    }

    public static void documentToTransactionHeader(DocumentSnapshot transactionHeaderDoc, QueryFinishListener<TransactionHeader> listener) {
        String id = transactionHeaderDoc.getId();
        Timestamp billingDate = transactionHeaderDoc.getTimestamp("billing_date");
        ArrayList<TransactionDetail> details = new ArrayList<>();
        transactionHeaderDoc.getReference().collection("transaction_details").get().addOnSuccessListener(detailSnapshots -> {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(billingDate.toDate());
            if (detailSnapshots.isEmpty()) {
                listener.onFinish(new TransactionHeader(id, calendar, details));
            } else {
                for (DocumentSnapshot detailSnapshot :
                        detailSnapshots) {
                    documentToTransactionDetail(detailSnapshot, detail -> {
                        if (detail != null) {
                            details.add(detail);
                            if (details.size() == detailSnapshots.size()) {
                                listener.onFinish(new TransactionHeader(id, calendar, details));
                            }
                        } else {
                            listener.onFinish(null);
                        }
                    });
                }
            }
        }).addOnFailureListener(e -> {
            listener.onFinish(null);
        });
    }

    public static void documentToTransactionDetail(DocumentSnapshot transactionDetailDoc, QueryFinishListener<TransactionDetail> listener) {
        String id = transactionDetailDoc.getId();
        String image = transactionDetailDoc.getString("image");
        Boolean verified = transactionDetailDoc.getBoolean("verified");
        Timestamp paymentDate = transactionDetailDoc.getTimestamp("payment_date");
        DocumentReference userRef = transactionDetailDoc.getDocumentReference("user");
        UserRepository.getUser(userRef.getId(), userData -> {
            if (userData != null) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(paymentDate.toDate());
                listener.onFinish(new TransactionDetail(id, image, userData, verified, calendar));
            } else {
                listener.onFinish(null);
            }
        });
    }

    public static void acceptInvitation(String invitationId, QueryFinishListener<Boolean> listener) {
        DocumentReference invitation = subscriptionInvitationRef.document(invitationId);
        invitation.get().addOnSuccessListener(documentSnapshot -> {
            DocumentReference user = (DocumentReference) documentSnapshot.get("invited");
            DocumentReference subscription = (DocumentReference) documentSnapshot.get("subscription");

            invitation.delete().addOnSuccessListener(unused -> {
                getSubscriptionMemberReference(subscription.getId(), memberSnapshot -> {
                    if (memberSnapshot != null) {
                        DocumentReference memberDocRef = memberSnapshot.getReference();
                        DocumentReference creator = (DocumentReference) memberSnapshot.get("creator");
                        ArrayList<DocumentReference> users = (ArrayList<DocumentReference>) memberSnapshot.get("users");
                        users.add(user);

                        Map<String, Object> newMemberData = memberToMap(creator.getId(), subscription.getId(), users);

                        memberDocRef.update("valid_to", Timestamp.now()).addOnSuccessListener(updateMember -> {
                            memberRef.add(newMemberData).addOnSuccessListener(statusAdd -> {
                                listener.onFinish(true);
                            }).addOnFailureListener(e -> {
                                listener.onFinish(false);
                            });
                        }).addOnFailureListener(e -> {
                            listener.onFinish(false);
                        });
                    }
                });

            }).addOnFailureListener(e -> {
                listener.onFinish(false);
            });
        });
    }

    public static void rejectInvitation(String invitationId, QueryFinishListener<Boolean> listener) {
        DocumentReference invitation = subscriptionInvitationRef.document(invitationId);
        invitation.delete().addOnSuccessListener(success -> {
            listener.onFinish(true);
        }).addOnFailureListener(e -> {
            listener.onFinish(false);
        });
    }

    public static void uploadReceipt(String subscriptionId, String transactionHeaderId, String userId, String image, QueryFinishListener<Boolean> listener) {
        DocumentReference user = UserRepository.userRef.document(userId);
        Map<String, Object> receiptData = new HashMap<>();
        receiptData.put("user", user);
        receiptData.put("payment_date", Timestamp.now());
        receiptData.put("verified", false);
        receiptData.put("image", image);
        getTransactionDetailRef(subscriptionId, transactionHeaderId).add(receiptData).addOnSuccessListener(document -> {
            listener.onFinish(true);
        }).addOnFailureListener(e -> {
            listener.onFinish(false);
        });
    }


    private static CollectionReference getTransactionHeaderRef(String subscriptionId) {
        return subscriptionRef.document(subscriptionId).collection("transaction_headers");
    }

    private static CollectionReference getTransactionDetailRef(String subscriptionId, String transactionHeaderId) {
        return subscriptionRef.document(subscriptionId).collection("transaction_headers").document(transactionHeaderId).collection("transaction_details");
    }

    public static void isUniqueSubscriptionNameForCreator(String creatorId, String subscriptionName, QueryFinishListener<Boolean> listener) {
        DocumentReference creator = UserRepository.userRef.document(creatorId);
        Query associatedMembers = memberRef.whereEqualTo("creator", creator).whereEqualTo("valid_to", null);
        associatedMembers.get()
                .addOnSuccessListener(documentSnapshots -> {
                    int counter = 0;
                    if(documentSnapshots.isEmpty()) {
                        listener.onFinish(true);
                    } else {
                        for (DocumentSnapshot snapshot :
                                documentSnapshots) {
                            counter++;
                            int finalCounter = counter;
                            snapshot.getDocumentReference("subscription").get().addOnSuccessListener(documentSnapshot -> {
                                if (documentSnapshot.get("name").toString().equals(subscriptionName)) {
                                    listener.onFinish(false);
                                } else if (finalCounter == documentSnapshots.size()) {
                                    listener.onFinish(true);
                                }
                            }).addOnFailureListener(e -> {
                                listener.onFinish(false);
                            });
                        }
                    }
                }).addOnFailureListener(e -> {
            listener.onFinish(false);
        });
    }

    public static ArrayList<Subscription> getAllSubscriptions() {
        ArrayList<Subscription> subscriptions = new ArrayList<>();

        SubscriptlyDB.getDB().collection("subscriptions").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            subscriptions.add(new Subscription(document.getId(), document.getString("name"), Long.parseLong(document.get("bill").toString()), Integer.parseInt(document.get("duration").toString()), new ArrayList<User>()));
                        }
                    }
                });

        return subscriptions;
    }
}
