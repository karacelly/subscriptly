package edu.bluejack21_2.subscriptly.ui.friends_tab;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import edu.bluejack21_2.subscriptly.R;
import edu.bluejack21_2.subscriptly.adapter.FriendRecyclerAdapter;
import edu.bluejack21_2.subscriptly.adapter.PendingFriendRecyclerAdapter;
import edu.bluejack21_2.subscriptly.models.FriendRequest;
import edu.bluejack21_2.subscriptly.models.User;
import edu.bluejack21_2.subscriptly.repositories.UserRepository;

public class FriendsPendingFragment extends Fragment {
    private static ArrayList<User> users = new ArrayList<>();
    private final int LIMIT = 8;
    boolean isFetchingData = false;
    private PendingFriendRecyclerAdapter adapter;

    public FriendsPendingFragment() {
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        adapter = new PendingFriendRecyclerAdapter(getActivity());
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
        UserRepository.getRelatedRequest(FirebaseAuth.getInstance().getUid(), relatedRequests -> {
            for (FriendRequest request:
                 relatedRequests) {
                if(!request.getSender().equals(FirebaseAuth.getInstance().getUid())) {
                    UserRepository.getUser(request.getSender(), user -> {
                        if(user != null) {
                            users.add(user);
                            if(users.size() == relatedRequests.size()) {
                                adapter.setUsers(users);
                            }
                        }
                    });
                } else {
                    UserRepository.getUser(request.getReceiver(), user -> {
                        if(user != null) {
                            users.add(user);
                            if(users.size() == relatedRequests.size()) {
                                adapter.setUsers(users);
                            }
                        }
                    });
                }
            }
            adapter.setRequests(relatedRequests);
        });
//        for (String id:
//                UserRepository.LOGGED_IN_USER.getFriends()) {
//            UserRepository.getUser(id, user -> {
//                friends.add(user);
//                if(friends.size() == UserRepository.LOGGED_IN_USER.getFriends().size()) {
//                    adapter.setUsers(friends);
//                }
//            });
//        }
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
//        SubscriptionRepository.lastTransactionHeader = null;
//        transactions.clear();
        users.clear();
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