package edu.bluejack21_2.subscriptly.repositories;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
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
import java.util.List;
import java.util.Map;

import edu.bluejack21_2.subscriptly.HomeActivity;
import edu.bluejack21_2.subscriptly.database.SubscriptlyDB;
import edu.bluejack21_2.subscriptly.interfaces.QueryFinishListener;
import edu.bluejack21_2.subscriptly.models.Subscription;
import edu.bluejack21_2.subscriptly.models.TransactionDetail;
import edu.bluejack21_2.subscriptly.models.TransactionHeader;
import edu.bluejack21_2.subscriptly.models.User;
import edu.bluejack21_2.subscriptly.utils.BroadcastManager;

public class SubscriptionRepository {
    public static Subscription ACTIVE_SUBSCRIPTION = null;
    public static CollectionReference subscriptionRef = SubscriptlyDB.getDB().collection("subscriptions");
    public static CollectionReference subscriptionInvitationRef = SubscriptlyDB.getDB().collection("subscription_invitations");
    public static CollectionReference memberRef = SubscriptlyDB.getDB().collection("members");
    public static StorageReference subscriptionStorageRef = SubscriptlyDB.getStorageDB().child("subscriptions");

    public static ArrayList<User> chosenFriends = new ArrayList<>();

    public static void insertSubscription(Context ctx, Subscription subscription, Calendar calendar, QueryFinishListener<Boolean> listener) {
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

                            Intent intent = new Intent(ctx, BroadcastManager.class);
                            intent.putExtra("name", subscription.getName());
                            PendingIntent pendingIntent = PendingIntent.getBroadcast(ctx, 0, intent, 0);

                            AlarmManager alarmManager = (AlarmManager) ctx.getSystemService(ctx.ALARM_SERVICE);

                            long time = calendar.getTimeInMillis();

                            alarmManager.set(AlarmManager.RTC_WAKEUP, time, pendingIntent);
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

    public static void removeSubscription(String subscriptionId, QueryFinishListener<Boolean> listener) {
        DocumentReference subscription = SubscriptionRepository.subscriptionRef.document(subscriptionId);
        subscription.update("valid_to", Timestamp.now()).addOnSuccessListener(data -> {
            memberRef.whereEqualTo("subscription", subscription).whereEqualTo("valid_to", null).limit(1).get()
                    .addOnSuccessListener(memberSnapshots -> {
                        if (!memberSnapshots.isEmpty()) {
                            DocumentSnapshot memberSnapshot = memberSnapshots.getDocuments().get(0);
                            memberSnapshot.getReference().update("valid_to", Timestamp.now())
                                    .addOnSuccessListener(success -> listener.onFinish(true))
                                    .addOnFailureListener(e -> listener.onFinish(false));
                        } else {
                            listener.onFinish(true);
                        }
                    }).addOnFailureListener(e -> listener.onFinish(false));
        }).addOnFailureListener(e -> {
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

        subscriptionInvitationRef.add(invitationData).addOnSuccessListener(invitationDocRef -> {
            listener.onFinish(true);
        }).addOnFailureListener(e -> {
            listener.onFinish(false);
        });
    }

    public static void isInvited(String userId, String invitedId, String subscriptionId, QueryFinishListener<Boolean> listener) {
        DocumentReference creator = UserRepository.userRef.document(userId);
        DocumentReference invited = UserRepository.userRef.document(invitedId);
        DocumentReference subscription = SubscriptionRepository.subscriptionRef.document(subscriptionId);

        Query findInvitation = subscriptionInvitationRef.whereEqualTo("creator", creator).whereEqualTo("invited", invited).whereEqualTo("subscription", subscription).limit(1);
        findInvitation.get().addOnSuccessListener(invitationSnapshot -> {
            if (invitationSnapshot.isEmpty()) {
                listener.onFinish(false);
            } else {
                listener.onFinish(true);
            }
        }).addOnFailureListener(e -> {
            listener.onFinish(null);
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

    public static Map<String, Object> memberToMap(String creatorId, String subscriptionId, ArrayList<DocumentReference> users, Timestamp validFrom) {
        DocumentReference creator = UserRepository.userRef.document(creatorId);
        DocumentReference subscription = subscriptionRef.document(subscriptionId);

        Map<String, Object> newMemberData = new HashMap<>();
        newMemberData.put("creator", creator);
        newMemberData.put("subscription", subscription);
        newMemberData.put("users", users);
        newMemberData.put("valid_from", validFrom);
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

                Map<String, Object> newMemberData = memberToMap(creator.getId(), subscriptionId, users, getNextMonthTimestamp());

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
        Log.d("Subscription Repository", "getUserSubscriptions");
        DocumentReference user = UserRepository.userRef.document(userId);
        ArrayList<Subscription> subscriptions = new ArrayList<>();
        SubscriptionRepository.memberRef.whereEqualTo("valid_to", null).whereArrayContains("users", user).get()
                .addOnSuccessListener(memberSnapshots -> {
                    for (DocumentSnapshot memberSnapshot :
                            memberSnapshots) {
                        DocumentReference subscription = memberSnapshot.getDocumentReference("subscription");

                        if (subscription != null) {
                            subscription.get().addOnSuccessListener(subscriptionSnapshot -> {
                                Log.d("Subscription Repository", "documentToSubscription from getUserSubscription");
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
        Log.d("Subscription Repository", "Document to Subscription");
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
                            DocumentReference creatorRef = member.getDocumentReference("creator");
                            ArrayList<User> users = new ArrayList<>();
                            ArrayList<DocumentReference> userRefs = (ArrayList<DocumentReference>) member.get("users");
                            UserRepository.getUser(creatorRef.getId(), creator -> {
                                if (userRefs.isEmpty()) {
                                    listener.onFinish(new Subscription(id, name, image, bill, duration, startAt, creator, headers, users));
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
                                                                    listener.onFinish(new Subscription(id, name, image, bill, duration, startAt, creator, headers, users));
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

                            });
                        }
                    }).addOnFailureListener(e -> {
                        listener.onFinish(null);
                    });
                }).addOnFailureListener(e -> {
            listener.onFinish(null);
        });
    }

    public static DocumentSnapshot getSimilarSnapshot(List<DocumentSnapshot> snapshotsA, List<DocumentSnapshot> snapshotsB) {
        for (DocumentSnapshot snapshotA :
                snapshotsA) {
            for (DocumentSnapshot snapshotB :
                    snapshotsB) {
                if (snapshotA.getId().equals(snapshotB.getId())) {
                    return snapshotA;
                }
            }
        }
        return null;
    }

    public static void getNewestMember(String subscriptionId, QueryFinishListener<ArrayList<User>> listener) {
        ArrayList<User> members = new ArrayList<>();
        Query newestMember = memberRef.whereEqualTo("valid_to", null).whereEqualTo("subscription", subscriptionRef.document(subscriptionId)).limit(1);
        newestMember.get().addOnSuccessListener(memberSnapshots -> {
            if (memberSnapshots.isEmpty()) {
                listener.onFinish(members);
            } else {
                DocumentSnapshot memberSnapshot = memberSnapshots.getDocuments().get(0);
                documentToMembers(memberSnapshot, members2 -> {
                    listener.onFinish(members2);
                });
            }
        }).addOnFailureListener(e -> {
            listener.onFinish(members);
        });
    }

    public static void documentToMembers(DocumentSnapshot memberDoc, QueryFinishListener<ArrayList<User>> listener) {
        ArrayList<DocumentReference> memberRefs = (ArrayList<DocumentReference>) memberDoc.get("users");
        ArrayList<User> members = new ArrayList<>();
        if (memberRefs.isEmpty()) listener.onFinish(members);
        else {
            for (DocumentReference memberRef :
                    memberRefs) {
                memberRef.get().addOnSuccessListener(doc -> {
                    members.add(UserRepository.documentToUser(memberDoc));
                    if (members.size() == memberRefs.size()) {
                        listener.onFinish(members);
                    }
                });
            }
        }
    }


    public static void documentToTransactionHeader(DocumentSnapshot transactionHeaderDoc, QueryFinishListener<TransactionHeader> listener) {
        Timestamp billingDate = transactionHeaderDoc.getTimestamp("billing_date");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(billingDate.toDate());

        DocumentReference subscription = transactionHeaderDoc.getReference().getParent().getParent();

        Query validMemberBottomRange = memberRef.whereLessThanOrEqualTo("valid_from", billingDate).whereEqualTo("subscription", subscription);
        validMemberBottomRange.get().addOnSuccessListener(memberSnapshots -> {
            Query validMemberRange = memberRef.whereGreaterThanOrEqualTo("valid_to", billingDate).whereEqualTo("subscription", subscription);
            validMemberRange.get().addOnSuccessListener(validMemberSnapshots -> {
//                Log.d("TransactionHeader", transactionHeaderDoc.getId()+"");
//
//                Log.d("BOTTOM RANGE SIZE", memberSnapshots.getDocuments().size()+"");
//                for (DocumentSnapshot snapshotA:
//                        memberSnapshots.getDocuments()) {
//                    Log.d("BOTTOM RANGE MEMBER ID", snapshotA.getId());
//                }
//
//                Log.d("UPPER RANGE SIZE", validMemberSnapshots.getDocuments().size()+"");
//                for (DocumentSnapshot snapshotB:
//                        validMemberSnapshots.getDocuments()) {
//                    Log.d("UPPER RANGE MEMBER ID", snapshotB.getId());
//                }
                DocumentSnapshot similar = getSimilarSnapshot(memberSnapshots.getDocuments(), validMemberSnapshots.getDocuments());

//                Log.d("SIMILAR MEMBER ID", similar+"\n\n");

                if (similar == null) {
                    getNewestMember(subscription.getId(), newestMembers -> {
                        getTransactionHeader(transactionHeaderDoc.getReference(), calendar, newestMembers, transactionHeader -> {
                            listener.onFinish(transactionHeader);
                        });
                    });
                } else {
                    documentToMembers(similar, validMembers -> {
                        getTransactionHeader(transactionHeaderDoc.getReference(), calendar, validMembers, transactionHeader -> {
                            listener.onFinish(transactionHeader);
                        });
                    });
                }
            }).addOnFailureListener(e -> {
                getNewestMember(subscription.getId(), newestMembers -> {
                    getTransactionHeader(transactionHeaderDoc.getReference(), calendar, newestMembers, transactionHeader -> {
                        listener.onFinish(transactionHeader);
                    });
                });
            });
        }).addOnFailureListener(e -> {
            listener.onFinish(null);
        });
    }

    public static void getTransactionHeader(DocumentReference transactionHeaderRef, Calendar calendar, ArrayList<User> activeMembers, QueryFinishListener<TransactionHeader> listener) {
        ArrayList<TransactionDetail> details = new ArrayList<>();
        transactionHeaderRef.collection("transaction_details").get().addOnSuccessListener(detailSnapshots -> {

            if (detailSnapshots.isEmpty()) {
                listener.onFinish(new TransactionHeader(transactionHeaderRef.getId(), calendar, details, activeMembers));
            } else {
                for (DocumentSnapshot detailSnapshot :
                        detailSnapshots) {
                    documentToTransactionDetail(detailSnapshot, detail -> {
                        if (detail != null) {
                            details.add(detail);
                            if (details.size() == detailSnapshots.size()) {
                                listener.onFinish(new TransactionHeader(transactionHeaderRef.getId(), calendar, details, activeMembers));
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

    public static void getSubscription(String id, QueryFinishListener<Subscription> listener) {
        DocumentReference subs = subscriptionRef.document(id);
        subs.get().addOnSuccessListener(subsSnapshot -> {
            if (subsSnapshot.exists()) {
                Log.d("getSubscription", "exist...");
                documentToSubscription(subsSnapshot, subscription -> {
                    listener.onFinish(subscription);
                });
            } else {
                Log.d("getSubscription", "not exist...");
                listener.onFinish(null);
            }
        }).addOnFailureListener(e -> {
            Log.d("getSubscription", e.getMessage());
            listener.onFinish(null);
        }).addOnCompleteListener(complete -> {
            Log.d("getSubscription", "complete: " + complete);
        });
    }

    public static void documentToTransactionDetail(DocumentSnapshot transactionDetailDoc, QueryFinishListener<TransactionDetail> listener) {
        Log.d("Subscription Repository", "document to Transaction Detail");
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

    private static Timestamp getNextMonthTimestamp() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(Timestamp.now().toDate());
        calendar.add(Calendar.MONTH, 1);
        calendar.set(Calendar.DATE, 1);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return new Timestamp(calendar.getTime());
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

                        Map<String, Object> newMemberData = memberToMap(creator.getId(), subscription.getId(), users, getNextMonthTimestamp());

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

    public static void uploadReceipt(Subscription subscription, String transactionHeaderId, String userId, String image, QueryFinishListener<Boolean> listener) {
        DocumentReference user = UserRepository.userRef.document(userId);
        Map<String, Object> receiptData = new HashMap<>();
        receiptData.put("user", user);
        receiptData.put("payment_date", Timestamp.now());
        receiptData.put("verified", false);
        receiptData.put("image", image);

        DocumentReference subsRef = subscriptionRef.document(subscription.getSubscriptionId());
        receiptData.put("subscription", subsRef);
        getTransactionDetailRef(subscription.getSubscriptionId(), transactionHeaderId).add(receiptData).addOnSuccessListener(document -> {
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
                    if (documentSnapshots.isEmpty()) {
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
