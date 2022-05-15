package edu.bluejack21_2.subscriptly.ui.subs_detail;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import edu.bluejack21_2.subscriptly.ChooseFriendActivity;
import edu.bluejack21_2.subscriptly.InviteFriendActivity;
import edu.bluejack21_2.subscriptly.R;
import edu.bluejack21_2.subscriptly.adapter.MemberItemAdapter;
import edu.bluejack21_2.subscriptly.models.User;
import edu.bluejack21_2.subscriptly.repositories.SubscriptionRepository;

public class SubscriptionDetailMemberFragment extends Fragment {
    private ArrayList<User> members;
    private LinearLayout addMemberContainer;

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

        addMemberContainer = view.findViewById(R.id.container_add_member);
        RecyclerView rv = view.findViewById(R.id.subsMemberList);
        rv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        rv.setAdapter(new MemberItemAdapter(getActivity(), members, SubscriptionRepository.ACTIVE_SUBSCRIPTION));

        addMemberContainer.setOnClickListener(v -> {
            Intent addMember = new Intent(v.getContext(), InviteFriendActivity.class);
            startActivity(addMember);
        });
    }
}