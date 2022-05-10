package edu.bluejack21_2.subscriptly.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import edu.bluejack21_2.subscriptly.SubscriptionDetail;
import edu.bluejack21_2.subscriptly.adapter.viewholder.HomeViewHolder;
import edu.bluejack21_2.subscriptly.models.Subscription;

public class HomeRecyclerAdapter extends RecyclerView.Adapter<HomeViewHolder> {

    private final Context context;
    public ArrayList<Subscription> subscriptions;
    private final int template;


    public HomeRecyclerAdapter(ArrayList<Subscription> subscriptions, Context context, int template) {
        this.context = context;
        this.subscriptions = subscriptions;
        this.template = template;
    }

    @Override
    public HomeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView
                = LayoutInflater
                .from(parent.getContext())
                .inflate(template,
                        parent,
                        false);

        return new HomeViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, int position) {
        holder.subscriptionMonth.setText(subscriptions.get(position).getDuration().toString());
        holder.subscriptionItems.setAdapter(new SubscriptionItemListAdapter(holder.subscriptionGroupItem.getContext(), subscriptions));
    }

    @Override
    public int getItemCount() {
        return subscriptions.size();
    }
}
