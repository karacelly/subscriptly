package edu.bluejack21_2.subscriptly.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import edu.bluejack21_2.subscriptly.SubscriptionDetail;
import edu.bluejack21_2.subscriptly.adapter.viewholder.SubscriptionViewHolder;
import edu.bluejack21_2.subscriptly.models.Subscription;
import edu.bluejack21_2.subscriptly.repositories.SubscriptionRepository;
import edu.bluejack21_2.subscriptly.repositories.UserRepository;
import edu.bluejack21_2.subscriptly.utils.Currency;

public class SubscriptionRecyclerAdapter extends RecyclerView.Adapter<SubscriptionViewHolder> {

    private final ArrayList<Subscription> subscriptions;
    private final int template;
    private final Context context;

    public SubscriptionRecyclerAdapter(Context context, ArrayList<Subscription> subscriptions, int template) {
        this.subscriptions = subscriptions;
        this.template = template;
        this.context = context;
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
        Subscription s = subscriptions.get(position);
        Integer memberCount = s.getMembers().size();
        holder.subscriptionName.setText(s.getName());
        holder.subscriptionPrice.setText(Currency.formatToRupiah(s.getBill().doubleValue()/memberCount));
        holder.subscriptionMemberCounts.setText(memberCount + " people");

        Glide.with(context).load(s.getImage()).into(holder.subscriptionImage);
        holder.subscriptionContainer.setOnClickListener(v -> {
            Context c =  v.getContext();
            Intent detail = new Intent(c, SubscriptionDetail.class);
            SubscriptionRepository.ACTIVE_SUBSCRIPTION = s;
            detail.putExtra("subscriptionID", s.getSubscriptionId());
            c.startActivity(detail);
        });
    }

    @Override
    public int getItemCount() {
        return subscriptions.size();
    }
}
