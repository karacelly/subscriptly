package edu.bluejack21_2.subscriptly.ui.subscriptions;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;

import javax.annotation.Nullable;

import edu.bluejack21_2.subscriptly.R;
import edu.bluejack21_2.subscriptly.adapter.InvitedSubscriptionRecyclerAdapter;
import edu.bluejack21_2.subscriptly.adapter.SubscriptionRecyclerAdapter;
import edu.bluejack21_2.subscriptly.models.Subscription;
import edu.bluejack21_2.subscriptly.models.SubscriptionInvitation;
import edu.bluejack21_2.subscriptly.models.User;
import edu.bluejack21_2.subscriptly.repositories.SubscriptionRepository;
import edu.bluejack21_2.subscriptly.repositories.UserRepository;
import edu.bluejack21_2.subscriptly.utils.RecyclerViewHelper;

public class SubscriptionsFragment extends Fragment {

    private static SubscriptionsFragment fragment;
    private LinearLayout containerInvitation;
    private RecyclerView invitedRecycler, subscriptionRecycler;
    private ArrayList<Subscription> subscriptions;

    public SubscriptionsFragment() {
        // Required empty public constructor
    }

    public static SubscriptionsFragment newInstance() {
        fragment = new SubscriptionsFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_subscriptions, container, false);

        return rootView;
//        return inflater.inflate(R.layout.fragment_subscriptions, container, false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        subscriptionRecycler = view.findViewById(R.id.recycler_subscriptions);
        invitedRecycler = view.findViewById(R.id.recycler_invited_subscriptions);
        containerInvitation = view.findViewById(R.id.container_subscription_invitations);
        subscriptions = new ArrayList<>();
        fetchData();
    }

    @Override
    public void onResume() {
        super.onResume();
        fetchData();
    }

    private void fetchData() {
        containerInvitation.setVisibility(View.GONE);
        DocumentReference currentUserRef = UserRepository.userRef.document(UserRepository.LOGGED_IN_USER.getUserID());
        ArrayList<SubscriptionInvitation> invitations = new ArrayList<>();
        SubscriptionRepository.subscriptionInvitationRef.whereEqualTo("invited", currentUserRef).get()
                .addOnSuccessListener(invitationSnapshots -> {
                    if(!invitationSnapshots.isEmpty()) {
                        containerInvitation.setVisibility(View.VISIBLE);
                    }
                    for (DocumentSnapshot invitationSnapshot :
                            invitationSnapshots) {
                        String id = invitationSnapshot.getId();
                        DocumentReference creator = invitationSnapshot.getDocumentReference("creator");
                        DocumentReference invited = invitationSnapshot.getDocumentReference("invited");
                        DocumentReference subscription = invitationSnapshot.getDocumentReference("subscription");
                        creator.get().addOnSuccessListener(creatorSnapshot -> {
                            User creatorModel = UserRepository.documentToUser(creatorSnapshot);
                            invited.get().addOnSuccessListener(invitedSnapshot -> {
                                User invitedModel = UserRepository.documentToUser(invitedSnapshot);
                                subscription.get().addOnSuccessListener(subscriptionSnapshot -> {
                                    SubscriptionRepository.documentToSubscription(subscriptionSnapshot, sub -> {
                                        invitations.add(new SubscriptionInvitation(id, creatorModel, invitedModel, sub));
                                        InvitedSubscriptionRecyclerAdapter adapter = new InvitedSubscriptionRecyclerAdapter(getContext(), this, invitations, R.layout.subscriptions_subscription_item);
                                        RecyclerViewHelper.setRecyclerView(getContext(), adapter, LinearLayoutManager.VERTICAL, invitedRecycler);
                                    });
                                }).addOnFailureListener(e -> {

                                });
                            }).addOnFailureListener(e -> {

                            });

                        }).addOnFailureListener(e -> {

                        });
                    }
                }).addOnFailureListener(e -> {

        });
        ArrayList<Subscription> subscriptions = new ArrayList<>();
        SubscriptionRepository.getUserSubscriptions(UserRepository.LOGGED_IN_USER.getUserID(), subs -> {
            if(subs != null) {
                subscriptions.addAll(subs);
                SubscriptionRecyclerAdapter adapter = new SubscriptionRecyclerAdapter(getContext(), subscriptions, R.layout.subscriptions_subscription_item);
                RecyclerViewHelper.setRecyclerView(getContext(), adapter, LinearLayoutManager.VERTICAL, subscriptionRecycler);
            }
        });
    }

}