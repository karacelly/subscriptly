package edu.bluejack21_2.subscriptly.ui.subscriptions;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.bluejack21_2.subscriptly.R;

public class SubscriptionsFragment extends Fragment {

    public SubscriptionsFragment() {
        // Required empty public constructor
    }

    public static SubscriptionsFragment newInstance() {
        SubscriptionsFragment fragment = new SubscriptionsFragment();
//        Bundle args = new Bundle();

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
}