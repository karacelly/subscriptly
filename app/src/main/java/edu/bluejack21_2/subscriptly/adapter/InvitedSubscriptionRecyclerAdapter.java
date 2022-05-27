package edu.bluejack21_2.subscriptly.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import edu.bluejack21_2.subscriptly.SubscriptionDetail;
import edu.bluejack21_2.subscriptly.adapter.viewholder.SubscriptionViewHolder;
import edu.bluejack21_2.subscriptly.interfaces.QueryChangeListener;
import edu.bluejack21_2.subscriptly.interfaces.QueryFinishListener;
import edu.bluejack21_2.subscriptly.models.Subscription;
import edu.bluejack21_2.subscriptly.models.SubscriptionInvitation;
import edu.bluejack21_2.subscriptly.repositories.SubscriptionRepository;
import edu.bluejack21_2.subscriptly.utils.Currency;

public class InvitedSubscriptionRecyclerAdapter extends RecyclerView.Adapter<SubscriptionViewHolder> {

    public void setInvitations(ArrayList<SubscriptionInvitation> invitations) {
        this.invitations = invitations;
    }

    private ArrayList<SubscriptionInvitation> invitations;
    private final int template;
    private final Context context;
    private final Fragment fragment;
    private final QueryChangeListener<Boolean> listener;

    public InvitedSubscriptionRecyclerAdapter(Context context, Fragment fragment, ArrayList<SubscriptionInvitation> invitations, int template, QueryChangeListener<Boolean> listener) {
        this.invitations = invitations;
        this.template = template;
        this.context = context;
        this.fragment = fragment;
        this.listener = listener;
    }

    @Override
    public SubscriptionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView
                = LayoutInflater
                .from(parent.getContext())
                .inflate(template,
                        parent,
                        false);

        return new SubscriptionViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SubscriptionViewHolder holder, int position) {
        SubscriptionInvitation subscriptionInvitation = invitations.get(position);
        holder.subscriptionName.setText(subscriptionInvitation.getSubscription().getName());
        holder.subscriptionPrice.setText(Currency.formatToRupiah(subscriptionInvitation.getSubscription().getBill().doubleValue()) + " / Month");
        holder.subscriptionMemberCounts.setText(subscriptionInvitation.getCreator().getUsername() + " invites you!");

        holder.acceptInvitation.setVisibility(View.VISIBLE);
        holder.acceptInvitation.setOnClickListener(view -> {
            SubscriptionRepository.acceptInvitation(subscriptionInvitation.getInvitationId(), done -> {
                if(done) {
                    invitations.remove(subscriptionInvitation);
                    notifyDataSetChanged();
//                    updateFragment();
                    listener.onChange(true);
                } else {
//                    Toast.makeText(view.getContext(), "Accept Invitation Error!", Toast.LENGTH_SHORT).show();
                }
            });
        });
        holder.rejectInvitation.setVisibility(View.VISIBLE);
        holder.rejectInvitation.setOnClickListener(view -> {
            SubscriptionRepository.rejectInvitation(subscriptionInvitation.getInvitationId(), done -> {
                if(done) {
                    invitations.remove(subscriptionInvitation);
                    notifyDataSetChanged();
//                    updateFragment();
                    listener.onChange(true);
                } else {
//                    Toast.makeText(view.getContext(), "Reject Invitation Error!", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private void updateFragment() {
        ((AppCompatActivity)context).getSupportFragmentManager().beginTransaction().detach(fragment).attach(fragment).commit();
    }

    @Override
    public int getItemCount() {
        return invitations.size();
    }
}
