package edu.bluejack21_2.subscriptly.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;

import javax.annotation.Nullable;

import edu.bluejack21_2.subscriptly.R;
import edu.bluejack21_2.subscriptly.adapter.HomeRecyclerAdapter;
import edu.bluejack21_2.subscriptly.interfaces.QueryFinishListener;
import edu.bluejack21_2.subscriptly.models.Subscription;
import edu.bluejack21_2.subscriptly.models.TransactionDetail;
import edu.bluejack21_2.subscriptly.models.TransactionHeader;
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
        ArrayList<TransactionHeader> uniqueMonths = new ArrayList<>();
        DocumentReference user = UserRepository.userRef.document(UserRepository.LOGGED_IN_USER.getUserID());
        SubscriptionRepository.memberRef.whereEqualTo("valid_to", null).whereArrayContains("users", user).get()
            .addOnSuccessListener(memberSnapshots -> {
                for (DocumentSnapshot memberSnapshot :
                        memberSnapshots) {
                    DocumentReference subscription = memberSnapshot.getDocumentReference("subscription");

                    if(subscription != null) {
                        subscription.get().addOnSuccessListener(subscriptionSnapshot -> {
                            SubscriptionRepository.documentToSubscription(subscriptionSnapshot, data -> {
                                if (data != null) {
                                    for (TransactionHeader header:
                                         data.getHeaders()) {
//                                        if(!isMonthYearTransactionExists(uniqueMonths, header) && header.getBillingDate().getTime().compareTo(Timestamp.now().toDate()) < 0) {
                                        if(!isMonthYearTransactionExists(uniqueMonths, header)) {
                                            uniqueMonths.add(header);
                                            uniqueMonths.sort(Comparator.comparing(TransactionHeader::getBillingDate).reversed());
//                                            Collections.sort(uniqueMonths);
                                        }
                                    }
                                    subscriptions.add(data);
                                    setRecyclerView(subscriptions, uniqueMonths, subscriptionGroupRecycler);
                                }
                            });
                        }).addOnFailureListener(e -> {
                        });
                    }
                }
            }).addOnFailureListener(e -> {
        });
    }

    private Boolean isMonthYearTransactionExists(ArrayList<TransactionHeader> headers, TransactionHeader newHeader) {
        for (TransactionHeader transactionHeader:
             headers) {
            if(isSameDateField(transactionHeader.getBillingDate(), newHeader.getBillingDate(), Calendar.MONTH) && isSameDateField(transactionHeader.getBillingDate(), newHeader.getBillingDate(), Calendar.YEAR)) {
                return true;
            }
        }
        return false;
    }

    private Boolean isSameDateField(Calendar first, Calendar second, int field) {
        return first.get(field) == second.get(field);
    }

    private void setRecyclerView(ArrayList<Subscription> data, ArrayList<TransactionHeader> uniqueMonths, RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(new HomeRecyclerAdapter(data, uniqueMonths, getActivity(), R.layout.home_subscription_group_item));
    }

    @Override
    public void onFinish(User data) {

    }
}