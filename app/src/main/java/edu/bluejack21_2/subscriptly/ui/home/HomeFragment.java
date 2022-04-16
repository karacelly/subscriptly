package edu.bluejack21_2.subscriptly.ui.home;

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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import javax.annotation.Nullable;

import edu.bluejack21_2.subscriptly.R;
import edu.bluejack21_2.subscriptly.adapter.HomeRecyclerAdapter;
import edu.bluejack21_2.subscriptly.database.SubscriptlyDB;
import edu.bluejack21_2.subscriptly.interfaces.QueryFinishListener;
import edu.bluejack21_2.subscriptly.models.Subscription;
import edu.bluejack21_2.subscriptly.models.User;
import edu.bluejack21_2.subscriptly.repositories.UserRepository;

public class HomeFragment extends Fragment implements QueryFinishListener<User> {

    private RecyclerView subscriptionGroupRecycler;
    private ArrayList<Subscription> subscriptions;
    private static HomeFragment fragment;
    //    private FragmentHomeBinding binding;
    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        fragment = new HomeFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
        return fragment;
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
//        binding = null;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        subscriptionGroupRecycler = view.findViewById(R.id.recycler_subscription_group);
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
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(new HomeRecyclerAdapter(data, getActivity(), R.layout.home_subscription_group_item));
    }

    @Override
    public void onFinish(User data) {

    }
}