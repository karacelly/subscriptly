package edu.bluejack21_2.subscriptly.ui.subs_detail;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import edu.bluejack21_2.subscriptly.R;
import edu.bluejack21_2.subscriptly.models.TransactionHeader;

public class SubscriptionDetailHistoryFragment extends Fragment {
    private ArrayList<TransactionHeader> transactions;

    public SubscriptionDetailHistoryFragment() { }

    public SubscriptionDetailHistoryFragment(ArrayList<TransactionHeader> transactions) {
        this.transactions = transactions;
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