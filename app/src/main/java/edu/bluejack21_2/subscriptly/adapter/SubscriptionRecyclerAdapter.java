package edu.bluejack21_2.subscriptly.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import edu.bluejack21_2.subscriptly.adapter.viewholder.SubscriptionViewHolder;
import edu.bluejack21_2.subscriptly.adapter.viewholder.SubscriptionViewHolder;
import edu.bluejack21_2.subscriptly.models.Subscription;
import edu.bluejack21_2.subscriptly.utils.Currency;

public class SubscriptionRecyclerAdapter extends RecyclerView.Adapter<SubscriptionViewHolder> {

    private final ArrayList<Subscription> subscriptions;
    private final int template;

    public SubscriptionRecyclerAdapter(ArrayList<Subscription> subscriptions, int template) {
        this.subscriptions = subscriptions;
        this.template = template;
    }

    @Override
    public SubscriptionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // Inflate item.xml using LayoutInflator
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
//        Log.d("BindViewHolder", subscriptions.get(position).getDuration().toString());
        holder.subscriptionName.setText(subscriptions.get(position).getName());
        holder.subscriptionPrice.setText(Currency.formatToRupiah(subscriptions.get(position).getBill().doubleValue()));
        //        Picasso.get().load(subscriptions.get(position).getImage()).into(holder.shopImage);
//        int shopID = subscriptions.get(position).getShopID();
//        holder.subscriptionContainer.setOnClickListener(v -> {
//            Intent detail = new Intent(context, DetailActivity.class);
//            detail.putExtra("shopID", shopID);
//            context.startActivity(detail);
//        });
    }

    @Override
    public int getItemCount() {
        return subscriptions.size();
    }
}
