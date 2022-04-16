package edu.bluejack21_2.subscriptly.ui.subscriptions;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import javax.annotation.Nullable;

import edu.bluejack21_2.subscriptly.R;
import edu.bluejack21_2.subscriptly.adapter.HomeRecyclerAdapter;
import edu.bluejack21_2.subscriptly.database.SubscriptlyDB;
import edu.bluejack21_2.subscriptly.models.Subscription;
import edu.bluejack21_2.subscriptly.models.User;
import edu.bluejack21_2.subscriptly.ui.home.HomeFragment;

public class SubscriptionsFragment extends Fragment {

    private RecyclerView subscriptionRecycler;
    private ArrayList<Subscription> subscriptions;
    private static SubscriptionsFragment fragment;

    public SubscriptionsFragment() {
        // Required empty public constructor
    }

    public static SubscriptionsFragment newInstance() {
        fragment = new SubscriptionsFragment();
//        Bundle args = new Bundle();

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
        subscriptionRecycler = view.findViewById(R.id.recycler_subscription_group);
        subscriptions = new ArrayList<>();
        fetchData();
    }

    private void fetchData() {
        SubscriptlyDB.getDB().collection("subscriptions").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                ArrayList<String> memberIDs = (ArrayList<String>)document.get("members");
//                                for (String id:
//                                     memberIDs) {
//                                    UserRepository.getUser(id, fragment);
//                                }
//                                ArrayList<User> members = (ArrayList<User>)document.get("members");
//                                Log.d("Members", document.get("members").getClass().toString());
                                subscriptions.add(new Subscription(document.getId(), document.getString("name"), Integer.parseInt(document.get("bill").toString()), Integer.parseInt(document.get("duration").toString()), new ArrayList<User>()));
                            }
                            setRecyclerView(subscriptions, subscriptionGroupRecycler);
                        } else {

                        }
                    }
                });
    }

    private void setRecyclerView(ArrayList<Subscription> data, RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(new HomeRecyclerAdapter(data, getActivity(), R.layout.home_subscription_group_item));
    }
}