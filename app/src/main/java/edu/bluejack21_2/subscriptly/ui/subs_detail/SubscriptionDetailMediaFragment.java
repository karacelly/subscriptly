package edu.bluejack21_2.subscriptly.ui.subs_detail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import edu.bluejack21_2.subscriptly.R;
import edu.bluejack21_2.subscriptly.adapter.MediaItemRecyclerAdapter;
import edu.bluejack21_2.subscriptly.models.Subscription;

public class SubscriptionDetailMediaFragment extends Fragment {
    private Subscription subscription;
    private FrameLayout layout;

    public SubscriptionDetailMediaFragment() { }

    public SubscriptionDetailMediaFragment(Subscription subscription, FrameLayout layout) {
        this.subscription = subscription;
        this.layout = layout;
    }

    @Override
    public void onViewCreated(View view,Bundle savedInstanceState) {
        initComponents(view);
    }

    private void initComponents(View view) {
        RecyclerView rv = view.findViewById(R.id.recycler_media);
        rv.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        rv.setAdapter(new MediaItemRecyclerAdapter(getActivity(), subscription, layout));
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