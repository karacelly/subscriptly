package edu.bluejack21_2.subscriptly.ui.subs_detail;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import edu.bluejack21_2.subscriptly.R;
import edu.bluejack21_2.subscriptly.adapter.HistoryItemAdapter;
import edu.bluejack21_2.subscriptly.models.Subscription;
import edu.bluejack21_2.subscriptly.models.TransactionHeader;

public class SubscriptionDetailHistoryFragment extends Fragment {
    private Subscription subscription;

    public SubscriptionDetailHistoryFragment() { }

    public SubscriptionDetailHistoryFragment(Subscription subscription) {
        this.subscription = subscription;
    }

    @Override
    public void onViewCreated(View view,Bundle savedInstanceState) {
        RecyclerView rv = view.findViewById(R.id.recycler_history);
        rv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        rv.setAdapter(new HistoryItemAdapter(getActivity(), subscription));
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