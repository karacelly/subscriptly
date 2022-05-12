package edu.bluejack21_2.subscriptly.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import edu.bluejack21_2.subscriptly.SubscriptionDetail;
import edu.bluejack21_2.subscriptly.adapter.viewholder.SubscriptionItemViewHolder;
import edu.bluejack21_2.subscriptly.models.Subscription;
import edu.bluejack21_2.subscriptly.repositories.SubscriptionRepository;
import edu.bluejack21_2.subscriptly.utils.Currency;

public class SubscriptionItemRecyclerAdapter extends RecyclerView.Adapter<SubscriptionItemViewHolder> {

    private final ArrayList<Subscription> subscriptions;
    private final int template;
    private final Context ctx;

    public SubscriptionItemRecyclerAdapter(Context ctx, ArrayList<Subscription> subs, int template) {
        this.ctx = ctx;
        this.subscriptions = subs;
        this.template = template;
    }

    @Override
    public SubscriptionItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView
                = LayoutInflater
                .from(parent.getContext())
                .inflate(template,
                        parent,
                        false);

        return new SubscriptionItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SubscriptionItemViewHolder holder, int position) {
        Subscription s = subscriptions.get(position);
        Integer memberCount = s.getMembers().size();
        holder.subscriptionName.setText(s.getName());
        holder.subscriptionBill.setText(Currency.formatToRupiah(s.getBill().doubleValue() / memberCount));

        holder.subscriptionContainer.setOnClickListener(v -> {
            Context c = v.getContext();
            Intent detail = new Intent(c, SubscriptionDetail.class);
            SubscriptionRepository.ACTIVE_SUBSCRIPTION = s;
            detail.putExtra("subscriptionID", s.getSubscriptionId());
            c.startActivity(detail);
        });
    }

    @Override
    public int getItemCount() {
        Log.d("SUBSCRIPTIONS | SIZE", subscriptions.size()+"");
        return subscriptions.size();
    }
}
