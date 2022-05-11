package edu.bluejack21_2.subscriptly.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;

import javax.annotation.Nullable;

import edu.bluejack21_2.subscriptly.R;
import edu.bluejack21_2.subscriptly.adapter.HomeRecyclerAdapter;
import edu.bluejack21_2.subscriptly.interfaces.QueryFinishListener;
import edu.bluejack21_2.subscriptly.models.Subscription;
import edu.bluejack21_2.subscriptly.models.User;
import edu.bluejack21_2.subscriptly.repositories.SubscriptionRepository;
import edu.bluejack21_2.subscriptly.repositories.UserRepository;

public class HomeFragment extends Fragment implements QueryFinishListener<User> {

    private static HomeFragment fragment;
    private RecyclerView subscriptionGroupRecycler;
    private ArrayList<Subscription> subscriptions;

    public HomeFragment() {
    }

    public static HomeFragment newInstance() {
        fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_home, container, false);

        return rootView;
    }
//    public View onCreateView(@NonNull LayoutInflater inflater,
//                             ViewGroup container, Bundle savedInstanceState) {
//        HomeViewModel homeViewModel =
//                new ViewModelProvider(this).get(HomeViewModel.class);
//
//        binding = FragmentHomeBinding.inflate(inflater, container, false);
//        View root = binding.getRoot();
//
////        final TextView textView = binding.textHome;
////        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
//        return root;
//    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Log.d("ON VIEW CREATED", "HOME");
        subscriptionGroupRecycler = view.findViewById(R.id.recycler_subscription_group);
        subscriptions = new ArrayList<>();
        fetchData();
    }

    private void fetchData() {
        DocumentReference user = UserRepository.userRef.document(UserRepository.LOGGED_IN_USER.getUserID());
        SubscriptionRepository.memberRef.whereEqualTo("valid_to", null).whereArrayContains("users", user).get()
            .addOnSuccessListener(memberSnapshots -> {
                for (DocumentSnapshot memberSnapshot :
                        memberSnapshots) {
                    DocumentReference subscription = memberSnapshot.getDocumentReference("subscription");
                    Log.d("FETCH DATA: SUBSCRIPTION ID", subscription.getId());
                    Log.d("FETCH DATA: SUBSCRIPTION", subscription.toString());

                    if(subscription != null) {
                        Log.d("FETCH DATA: SUBSCRIPTION", subscription.toString());
                        subscription.get().addOnSuccessListener(subscriptionSnapshot -> {
                            Log.d("FETCH DATA: SUBSCRIPTION SNAPSHOT", subscriptionSnapshot.toString());
                            SubscriptionRepository.documentToSubscription(subscriptionSnapshot, data -> {
                                Log.d("FETCH DATA: SUBSCRIPTION DATA", data.toString());
                                if (data != null) {
                                    Log.d("SET RECYCLER VIEW", "HOME");
                                    subscriptions.add(data);
                                    setRecyclerView(subscriptions, subscriptionGroupRecycler);
                                }
                            });
                        }).addOnFailureListener(e -> {
                                Log.d("FETCH DATA: SUBSCRIPTION", e.toString());
                        });
                    }
                }
            }).addOnFailureListener(e -> {
        });
    }

    private void setRecyclerView(ArrayList<Subscription> data, RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(new HomeRecyclerAdapter(data, getActivity(), R.layout.home_subscription_group_item));
    }

    @Override
    public void onFinish(User data) {

    }
}