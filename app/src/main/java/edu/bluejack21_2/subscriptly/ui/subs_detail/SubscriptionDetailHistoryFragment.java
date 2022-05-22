package edu.bluejack21_2.subscriptly.ui.subs_detail;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Comparator;

import edu.bluejack21_2.subscriptly.R;
import edu.bluejack21_2.subscriptly.adapter.HistoryItemAdapter;
import edu.bluejack21_2.subscriptly.models.Subscription;
import edu.bluejack21_2.subscriptly.models.TransactionDetail;
import edu.bluejack21_2.subscriptly.models.TransactionHeader;
import edu.bluejack21_2.subscriptly.repositories.SubscriptionRepository;

public class SubscriptionDetailHistoryFragment extends Fragment {
    private Subscription subscription;
    private static ArrayList<TransactionHeader> transactions = new ArrayList<>();
    private final int LIMIT = 8;
    boolean isFetchingData = false;
    private HistoryItemAdapter adapter;

    public SubscriptionDetailHistoryFragment() {
    }

    public SubscriptionDetailHistoryFragment(Subscription subscription) {
        this.subscription = subscription;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        adapter = new HistoryItemAdapter(getActivity(), subscription);
        Log.d("SubscriptionDetailHistoryFragment | Subscription Header Size", subscription.getHeaders().size()+"");
        if(subscription.getHeaders().size() > 0) {
            Log.d("SubscriptionDetailHistoryFragment | Subscription Header Active Member Size", subscription.getHeaders().get(0).getActiveMembers().size()+"");
        }
        RecyclerView rv = view.findViewById(R.id.recycler_history);
        rv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        rv.setAdapter(adapter);

        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
//                int totalItem = linearLayoutManager.getItemCount();
                int totalItem = transactions.size();
                int lastVisible = linearLayoutManager.findLastCompletelyVisibleItemPosition();
//                Log.d("RECYCLER STATS", "TOTAL ITEM: " + totalItem + " | LAST VISIBLE: " + lastVisible);

                if (lastVisible >= totalItem - 2) {
//                    Log.d("RECYCLER STATS", "ADA PERMINTAAN FETCHING");
                    if (!isFetchingData) {
                        isFetchingData = true;
                        fetchData();
                    }
                }
            }
        });
    }

    private void fetchData() {
        SubscriptionRepository.paginateMonthlyTransaction(subscription.getSubscriptionId(), LIMIT, transactionHeaders -> {
            Log.d("TRANSACTION HEADER | Subscription Detail Fragment History | Size Fetched", transactionHeaders.size() + "");
            if (!transactionHeaders.isEmpty()) {
                transactionHeaders.sort(Comparator.comparing(TransactionHeader::getBillingDate).reversed());
                for (TransactionHeader header:
                     transactionHeaders) {
                    for (TransactionDetail detail:
                         header.getDetails()) {
                        detail.setSubscription(subscription);
                    }
                }
                transactions.addAll(transactionHeaders);
                adapter.setTransactions(transactions);
                adapter.notifyDataSetChanged();
            }
            isFetchingData = false;
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        SubscriptionRepository.lastTransactionHeader = null;
        transactions.clear();
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
        return inflater.inflate(R.layout.fragment_subscription_detail_history, container, false);
    }

}