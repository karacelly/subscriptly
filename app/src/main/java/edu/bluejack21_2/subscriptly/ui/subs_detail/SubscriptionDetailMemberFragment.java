package edu.bluejack21_2.subscriptly.ui.subs_detail;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.lang.reflect.Member;
import java.util.ArrayList;

import edu.bluejack21_2.subscriptly.R;
import edu.bluejack21_2.subscriptly.adapter.MemberItemAdapter;
import edu.bluejack21_2.subscriptly.models.User;

public class SubscriptionDetailMemberFragment extends Fragment {
    private ArrayList<User> members = new ArrayList<>();

    public SubscriptionDetailMemberFragment(ArrayList<User> members) {
        this.members = members;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_subscription_detail_member, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MemberItemAdapter adapter = new MemberItemAdapter(getActivity(), members);
        ListView list = getView().findViewById(R.id.subsMemberList);
        list.setAdapter(adapter);
    }
}