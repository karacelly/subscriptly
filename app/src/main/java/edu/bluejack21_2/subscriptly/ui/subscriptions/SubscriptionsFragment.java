package edu.bluejack21_2.subscriptly.ui.subscriptions;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;

import javax.annotation.Nullable;

import edu.bluejack21_2.subscriptly.R;
import edu.bluejack21_2.subscriptly.adapter.SubscriptionRecyclerAdapter;
import edu.bluejack21_2.subscriptly.models.Subscription;
import edu.bluejack21_2.subscriptly.repositories.SubscriptionRepository;

public class SubscriptionsFragment extends Fragment {

    private static SubscriptionsFragment fragment;
    private RecyclerView subscriptionRecycler;
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
        subscriptions = new ArrayList<>();
        fetchData();
    }

    private void fetchData() {
        SubscriptionRepository.subscriptionRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (DocumentSnapshot document : task.getResult()) {
                    SubscriptionRepository.documentToSubscription(document, data -> {
                        if(data != null) {
                            subscriptions.add(data);
                            setRecyclerView(subscriptions, subscriptionRecycler);
                        }
                    });
                }
            }
        });
    }

    private void setRecyclerView
            (ArrayList<Subscription> data, RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(new SubscriptionRecyclerAdapter(data, R.layout.subscriptions_subscription_item));
    }
}