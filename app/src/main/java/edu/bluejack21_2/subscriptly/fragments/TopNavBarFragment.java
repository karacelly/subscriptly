package edu.bluejack21_2.subscriptly.fragments;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import edu.bluejack21_2.subscriptly.R;
import edu.bluejack21_2.subscriptly.ui.subs_detail.SubscriptionDetailMemberFragment;

public class TopNavBarFragment extends Fragment {
    CardView profileIcon;

    public TopNavBarFragment() {
        // Required empty public constructor
    }

    public static TopNavBarFragment newInstance() {
        TopNavBarFragment fragment = new TopNavBarFragment();
        return fragment;
    }

    @Override
    public void onViewCreated(View view,Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        createMenu(view);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_top_nav_bar, container, false);
    }

    public void createMenu(View v){
        profileIcon = v.findViewById(R.id.profile_icon);

        profileIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(getActivity(), profileIcon);

                popupMenu.getMenuInflater().inflate(R.menu.profile_menu, popupMenu.getMenu());
                popupMenu.show();
            }
        });
    }
}