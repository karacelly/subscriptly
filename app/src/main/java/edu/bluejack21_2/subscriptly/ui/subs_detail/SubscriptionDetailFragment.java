package edu.bluejack21_2.subscriptly.ui.subs_detail;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.bluejack21_2.subscriptly.R;
import edu.bluejack21_2.subscriptly.models.Subscription;

public class SubscriptionDetailFragment extends Fragment {
    private Subscription subscription;

    public SubscriptionDetailFragment(Subscription subscription) {
        this.subscription = subscription;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_subscription_detail, container, false);
    }

    @Override
    public void onViewCreated(View view,Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
}