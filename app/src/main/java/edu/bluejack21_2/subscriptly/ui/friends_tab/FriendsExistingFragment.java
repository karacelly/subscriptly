package edu.bluejack21_2.subscriptly.ui.friends_tab;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Comparator;

import edu.bluejack21_2.subscriptly.R;
import edu.bluejack21_2.subscriptly.adapter.FriendRecyclerAdapter;
import edu.bluejack21_2.subscriptly.adapter.HistoryItemAdapter;
import edu.bluejack21_2.subscriptly.models.Subscription;
import edu.bluejack21_2.subscriptly.models.TransactionDetail;
import edu.bluejack21_2.subscriptly.models.TransactionHeader;
import edu.bluejack21_2.subscriptly.models.User;
import edu.bluejack21_2.subscriptly.repositories.SubscriptionRepository;
import edu.bluejack21_2.subscriptly.repositories.UserRepository;

public class FriendsExistingFragment extends Fragment {
    
    private static ArrayList<User> friends = new ArrayList<>();
    private final int LIMIT = 8;
    boolean isFetchingData = false;
    private FriendRecyclerAdapter adapter;

    public FriendsExistingFragment() {
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        adapter = new FriendRecyclerAdapter(getActivity());
        RecyclerView rv = view.findViewById(R.id.recycler_existing_friends);
        rv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        rv.setAdapter(adapter);

//        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
//                int totalItem = transactions.size();
//                int lastVisible = linearLayoutManager.findLastCompletelyVisibleItemPosition();
//
//                if (lastVisible >= totalItem - 2) {
//                    if (!isFetchingData) {
//                        isFetchingData = true;
//                        fetchData();
//                    }
//                }
//            }
//        });
    }

    private void fetchData() {
        UserRepository.forceRefreshLoggedInUser(done -> {
            if(done) {
                UserRepository.getLoggedInUser(user -> {
                    for (String id:
                            user.getFriends()) {
                        UserRepository.getUser(id, u -> {
                            friends.add(u);
                            if(friends.size() == user.getFriends().size()) {
                                adapter.setUsers(friends);
                            }
                        });
                    }
                });
            }
        });
    }
//    private void fetchPaginateData() {
//        SubscriptionRepository.paginateMonthlyTransaction(subscription.getSubscriptionId(), LIMIT, transactionHeaders -> {
//            if (!transactionHeaders.isEmpty()) {
//                transactionHeaders.sort(Comparator.comparing(TransactionHeader::getBillingDate).reversed());
//                for (TransactionHeader header:
//                     transactionHeaders) {
//                    for (TransactionDetail detail:
//                         header.getDetails()) {
//                        detail.setSubscription(subscription);
//                    }
//                }
//                transactions.addAll(transactionHeaders);
//                adapter.setTransactions(transactions);
//                adapter.notifyDataSetChanged();
//            }
//            isFetchingData = false;
//        });
//    }

    @Override
    public void onResume() {
        super.onResume();
        friends.clear();
        fetchData();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_friends_tab_existing, container, false);
    }

}