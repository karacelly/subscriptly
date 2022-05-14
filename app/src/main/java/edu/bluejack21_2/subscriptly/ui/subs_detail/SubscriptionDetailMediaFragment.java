package edu.bluejack21_2.subscriptly.ui.subs_detail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import edu.bluejack21_2.subscriptly.R;
import edu.bluejack21_2.subscriptly.adapter.MediaItemRecyclerAdapter;
import edu.bluejack21_2.subscriptly.models.Subscription;

public class SubscriptionDetailMediaFragment extends Fragment {
    private Subscription subscription;

    public SubscriptionDetailMediaFragment() { }

    public SubscriptionDetailMediaFragment(Subscription subscription) {
        this.subscription = subscription;
    }

    @Override
    public void onViewCreated(View view,Bundle savedInstanceState) {
        RecyclerView rv = view.findViewById(R.id.recycler_media);
        rv.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        rv.setAdapter(new MediaItemRecyclerAdapter(getActivity(), subscription));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_subscription_detail_media, container, false);
    }

}