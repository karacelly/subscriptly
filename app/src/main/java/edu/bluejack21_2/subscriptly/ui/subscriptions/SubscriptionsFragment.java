package edu.bluejack21_2.subscriptly.ui.subscriptions;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import edu.bluejack21_2.subscriptly.R;
import edu.bluejack21_2.subscriptly.adapter.SubscriptionRecyclerAdapter;
import edu.bluejack21_2.subscriptly.database.SubscriptlyDB;
import edu.bluejack21_2.subscriptly.models.Subscription;
import edu.bluejack21_2.subscriptly.models.User;

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
        SubscriptlyDB.getDB().collection("subscriptions").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        ArrayList<User> members = new ArrayList<>();
                        Subscription s = new Subscription(document.getId(), document.getString("name"), Integer.parseInt(document.get("bill").toString()), Integer.parseInt(document.get("duration").toString()), members);
                        SubscriptlyDB.getDB().collection("members").whereEqualTo("subscription_id", document.getId()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        List<DocumentReference> userRefs = (List<DocumentReference>) document.get("users");
                                        for (DocumentReference userRef : userRefs) {
                                            userRef.get().addOnCompleteListener(task1 -> {
                                                if (task1.isSuccessful()) {
                                                    DocumentSnapshot document1 = task1.getResult();
                                                    String key = document1.getId();
                                                    String name = document1.getString("name");
                                                    String username = document1.getString("username");
                                                    String email = document1.getString("email");
                                                    String password = "";
                                                    Log.d("MEMBER", name);
                                                    members.add(new User(key, name, username, email, password));
                                                    s.setMembers(members);
                                                    setRecyclerView(subscriptions, subscriptionRecycler);
                                                }
                                            });
                                        }
                                    }

                                }
                            }
                        });
                        subscriptions.add(s);
                    }
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