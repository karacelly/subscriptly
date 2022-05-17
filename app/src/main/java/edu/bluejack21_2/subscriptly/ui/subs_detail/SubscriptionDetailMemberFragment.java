package edu.bluejack21_2.subscriptly.ui.subs_detail;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import edu.bluejack21_2.subscriptly.ChooseFriendActivity;
import edu.bluejack21_2.subscriptly.InviteFriendActivity;
import edu.bluejack21_2.subscriptly.R;
import edu.bluejack21_2.subscriptly.adapter.MemberItemAdapter;
import edu.bluejack21_2.subscriptly.models.Subscription;
import edu.bluejack21_2.subscriptly.models.User;
import edu.bluejack21_2.subscriptly.repositories.SubscriptionRepository;

public class SubscriptionDetailMemberFragment extends Fragment {
    private Subscription subscription;
    private LinearLayout addMemberContainer;

    public SubscriptionDetailMemberFragment(Subscription subscription) {
        this.subscription = subscription;
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
        rv.setAdapter(new MemberItemAdapter(getActivity(), subscription));

        Log.d("UID | Firebase", FirebaseAuth.getInstance().getUid());
        Log.d("UID | UserId", FirebaseAuth.getInstance().getCurrentUser().getUid());

        if(FirebaseAuth.getInstance().getCurrentUser().getUid().equals(subscription.getCreator().getUserID())) {
            addMemberContainer.setVisibility(View.VISIBLE);
        }
        addMemberContainer.setOnClickListener(v -> {
            Intent addMember = new Intent(v.getContext(), InviteFriendActivity.class);
            startActivity(addMember);
        });
    }
}