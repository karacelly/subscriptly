package edu.bluejack21_2.subscriptly.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import edu.bluejack21_2.subscriptly.SubscriptionDetail;
import edu.bluejack21_2.subscriptly.adapter.viewholder.SubscriptionViewHolder;
import edu.bluejack21_2.subscriptly.models.Subscription;
import edu.bluejack21_2.subscriptly.models.SubscriptionInvitation;
import edu.bluejack21_2.subscriptly.repositories.SubscriptionRepository;
import edu.bluejack21_2.subscriptly.utils.Currency;

public class InvitedSubscriptionRecyclerAdapter extends RecyclerView.Adapter<SubscriptionViewHolder> {

    private final ArrayList<SubscriptionInvitation> invitations;
    private final int template;

    public InvitedSubscriptionRecyclerAdapter(ArrayList<Subscription> invitations, int template) {
        this.invitations = InvitedSubscriptionRecyclerAdapter.this.invitations;
        this.template = template;
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
                } else {
                    Toast.makeText(view.getContext(), "Accept Invitation Error!", Toast.LENGTH_SHORT).show();
                }
            });
        });
        holder.rejectInvitation.setVisibility(View.VISIBLE);
        holder.rejectInvitation.setOnClickListener(view -> {
            SubscriptionRepository.rejectInvitation(subscriptionInvitation.getInvitationId(), done -> {
                if(done) {
                    invitations.remove(subscriptionInvitation);
                    notifyDataSetChanged();
                } else {
                    Toast.makeText(view.getContext(), "Reject Invitation Error!", Toast.LENGTH_SHORT).show();
                }
            });
        });
//        holder.subscriptionContainer.setOnClickListener(v -> {
//            Context c =  v.getContext();
//            Intent detail = new Intent(c, SubscriptionDetail.class);
//            SubscriptionRepository.ACTIVE_SUBSCRIPTION = new Subscription(s.getSubscriptionId(), s.getName(), s.getBill(), s.getDuration(), s.getMembers());
//            detail.putExtra("subscriptionID", s.getSubscriptionId());
//            c.startActivity(detail);
//        });
    }

    @Override
    public int getItemCount() {
        return invitations.size();
    }
}
