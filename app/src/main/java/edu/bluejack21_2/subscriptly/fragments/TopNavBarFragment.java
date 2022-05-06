package edu.bluejack21_2.subscriptly.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import edu.bluejack21_2.subscriptly.MainActivity;
import edu.bluejack21_2.subscriptly.ProfileActivity;
import edu.bluejack21_2.subscriptly.R;
import edu.bluejack21_2.subscriptly.repositories.UserRepository;
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
//                popupMenu.getMenu().getItem(R.id.menu_logout).setOnMenuItemClickListener(menuItem -> {
                popupMenu.getMenu().getItem(0).setOnMenuItemClickListener(menuItem -> {
                    Intent i = new Intent(getActivity(), ProfileActivity.class);
                    startActivity(i);
                    return true;
                });

                popupMenu.getMenu().getItem(2).setOnMenuItemClickListener(menuItem -> {
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.clear();
                    editor.commit();
                    Intent i = new Intent(getActivity(), MainActivity.class);
                    startActivity(i);
                    getActivity().finish();
                    return true;
                });


                popupMenu.show();
            }
        });
    }
}