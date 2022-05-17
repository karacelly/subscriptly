package edu.bluejack21_2.subscriptly.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
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
import edu.bluejack21_2.subscriptly.utils.RecyclerViewHelper;
import edu.bluejack21_2.subscriptly.utils.SubscriptionHelper;

public class HomeFragment extends Fragment {

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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        subscriptions = new ArrayList<>();
        subscriptionGroupRecycler = view.findViewById(R.id.recycler_subscription_group);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("HOME FRAGMENT", "OnResume");
        fetchData();
    }

    private void fetchData() {
        Log.d("HOME FRAGMENT", "FETCH DATA");
        ArrayList<TransactionHeader> uniqueMonths = new ArrayList<>();
        ArrayList<Subscription> subscriptions = new ArrayList<>();
        SubscriptionRepository.getUserSubscriptions(FirebaseAuth.getInstance().getCurrentUser().getUid(), subs -> {
            if(subs != null) {
                subscriptions.addAll(subs);
                for (Subscription subscription:
                     subscriptions) {
                    for (TransactionHeader header:
                            subscription.getHeaders()) {
//                      if(!isMonthYearTransactionExists(uniqueMonths, header) && header.getBillingDate().getTime().compareTo(Timestamp.now().toDate()) < 0) {
                        if(!SubscriptionHelper.isMonthYearTransactionExists(uniqueMonths, header)) {
                            uniqueMonths.add(header);
                            uniqueMonths.sort(Comparator.comparing(TransactionHeader::getBillingDate).reversed());
                        }
                    }
                }
                HomeRecyclerAdapter adapter = new HomeRecyclerAdapter(subscriptions, uniqueMonths, getActivity(), R.layout.home_subscription_group_item);
                RecyclerViewHelper.setRecyclerView(getContext(), adapter, LinearLayoutManager.VERTICAL, subscriptionGroupRecycler);

            }
        });
    }
}