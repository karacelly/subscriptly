package edu.bluejack21_2.subscriptly;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class BackActionBar extends Fragment {

    public BackActionBar() {
        // Required empty public constructor
    }

    public static BackActionBar newInstance(String param1, String param2) {
        BackActionBar fragment = new BackActionBar();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_back_action_bar, container, false);
    }
}