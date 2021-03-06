package edu.bluejack21_2.subscriptly.ui.subscriptions;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.annotation.Nullable;

import edu.bluejack21_2.subscriptly.R;
import edu.bluejack21_2.subscriptly.adapter.InvitedSubscriptionRecyclerAdapter;
import edu.bluejack21_2.subscriptly.adapter.SubscriptionRecyclerAdapter;
import edu.bluejack21_2.subscriptly.databinding.FragmentFriendsBinding;
import edu.bluejack21_2.subscriptly.databinding.FragmentSubscriptionsBinding;
import edu.bluejack21_2.subscriptly.interfaces.QueryChangeListener;
import edu.bluejack21_2.subscriptly.models.Subscription;
import edu.bluejack21_2.subscriptly.models.SubscriptionInvitation;
import edu.bluejack21_2.subscriptly.models.User;
import edu.bluejack21_2.subscriptly.repositories.SubscriptionRepository;
import edu.bluejack21_2.subscriptly.repositories.UserRepository;
import edu.bluejack21_2.subscriptly.utils.RecyclerViewHelper;

public class SubscriptionsFragment extends Fragment implements QueryChangeListener<Boolean> {

    private static final Comparator<Subscription> ALPHABETICAL_COMPARATOR = new Comparator<Subscription>() {
        @Override
        public int compare(Subscription a, Subscription b) {
            return a.getName().compareTo(b.getName());
        }
    };

    private Button sortAZ, sortZA, sortHighLow, sortLowHigh, sortNewest, sortOldest;

    private SearchView fieldSearchSubscription;
    private static SubscriptionsFragment fragment;
    private LinearLayout containerInvitation;
    private RecyclerView invitedRecycler, subscriptionRecycler;
    private ArrayList<Subscription> subscriptions;
    private SubscriptionRecyclerAdapter subscriptionRecyclerAdapter;
    private FragmentSubscriptionsBinding binding;

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
        binding = FragmentSubscriptionsBinding.inflate(inflater, container, false);
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_subscriptions, container, false);

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        fieldSearchSubscription = view.findViewById(R.id.field_search_subscription);
        subscriptionRecycler = view.findViewById(R.id.recycler_subscriptions);
        invitedRecycler = view.findViewById(R.id.recycler_invited_subscriptions);
        containerInvitation = view.findViewById(R.id.container_subscription_invitations);

        sortAZ = view.findViewById(R.id.action_sort_alphabetical);
        sortZA = view.findViewById(R.id.action_sort_alphabetical_inverse);
        sortHighLow = view.findViewById(R.id.action_sort_price_high_to_low);
        sortLowHigh = view.findViewById(R.id.action_sort_price_low_to_high);
        sortNewest = view.findViewById(R.id.action_sort_newest_to_oldest);
        sortOldest = view.findViewById(R.id.action_sort_oldest_to_newest);

        initListeners();
    }

    private void initListeners() {

        sortAZ.setOnClickListener(v -> {
            subscriptionRecyclerAdapter.setComparator(Comparator.comparing(Subscription::getName));
            subscriptionRecycler.scrollToPosition(0);
        });

        sortZA.setOnClickListener(v -> {
            subscriptionRecyclerAdapter.setComparator(Comparator.comparing(Subscription::getName).reversed());
            subscriptionRecycler.scrollToPosition(0);
        });

        sortHighLow.setOnClickListener(v -> {
            subscriptionRecyclerAdapter.setComparator(Comparator.comparing(Subscription::getDistributedBill).reversed());
            subscriptionRecycler.scrollToPosition(0);
        });

        sortLowHigh.setOnClickListener(v -> {
            subscriptionRecyclerAdapter.setComparator(Comparator.comparing(Subscription::getDistributedBill));
            subscriptionRecycler.scrollToPosition(0);
        });

        sortNewest.setOnClickListener(v -> {
            subscriptionRecyclerAdapter.setComparator(Comparator.comparing(Subscription::getStartAt).reversed());
            subscriptionRecycler.scrollToPosition(0);
        });

        sortOldest.setOnClickListener(v -> {
            subscriptionRecyclerAdapter.setComparator(Comparator.comparing(Subscription::getStartAt));
            subscriptionRecycler.scrollToPosition(0);
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        fetchData();
    }

    private static List<Subscription> filter(List<Subscription> subscriptions, String query) {
        final String lowerCaseQuery = query.toLowerCase();

        final List<Subscription> filteredModelList = new ArrayList<>();
        for (Subscription model : subscriptions) {
            final String text = model.getName().toLowerCase();
            if (text.contains(lowerCaseQuery)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }

    private void fetchData() {
        containerInvitation.setVisibility(View.GONE);
        DocumentReference currentUserRef = UserRepository.userRef.document(FirebaseAuth.getInstance().getCurrentUser().getUid());
        ArrayList<SubscriptionInvitation> invitations = new ArrayList<>();
        InvitedSubscriptionRecyclerAdapter invitedSubscriptionRecyclerAdapter = new InvitedSubscriptionRecyclerAdapter(getContext(), this, invitations, R.layout.subscriptions_subscription_item, this);
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
                                        invitedSubscriptionRecyclerAdapter.setInvitations(invitations);
                                        invitedSubscriptionRecyclerAdapter.notifyDataSetChanged();
                                        RecyclerViewHelper.setRecyclerView(getContext(), invitedSubscriptionRecyclerAdapter, LinearLayoutManager.VERTICAL, invitedRecycler);

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
        subscriptions = new ArrayList<>();

        Log.d("FETCH DATA", subscriptions.size()+"");
        SubscriptionRepository.getUserSubscriptions(FirebaseAuth.getInstance().getCurrentUser().getUid(), subs -> {
            if(subs != null) {
                Log.d("BEFORE SUBSCRIPTION SIZE", subscriptions.size()+"");
//                if(subscriptions.size() < subs.size())
                subscriptions.addAll(subs);
                Log.d("AFTER SUBSCRIPTION SIZE", subscriptions.size()+"");
                subscriptionRecyclerAdapter = new SubscriptionRecyclerAdapter(getContext(), subscriptions, R.layout.subscriptions_subscription_item, ALPHABETICAL_COMPARATOR);
                RecyclerViewHelper.setRecyclerView(getContext(), subscriptionRecyclerAdapter, LinearLayoutManager.VERTICAL, subscriptionRecycler);

                fieldSearchSubscription.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return false;
                    }
                    @Override
                    public boolean onQueryTextChange(String query) {
                        final List<Subscription> filteredModelList = filter(subscriptions, query);
                        subscriptionRecyclerAdapter.replaceAll(filteredModelList);
                        subscriptionRecycler.scrollToPosition(0);
                        return true;
                    }
                });
            }
        });
    }

    @Override
    public void onChange(Boolean data) {
        if(data) {
            fetchData();
        }
    }
}