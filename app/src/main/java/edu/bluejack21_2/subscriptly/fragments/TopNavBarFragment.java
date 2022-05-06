package edu.bluejack21_2.subscriptly.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.bluejack21_2.subscriptly.R;

public class TopNavBarFragment extends Fragment {

    public TopNavBarFragment() {
        // Required empty public constructor
    }

    public static TopNavBarFragment newInstance() {
        TopNavBarFragment fragment = new TopNavBarFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_top_nav_bar, container, false);
    }
}