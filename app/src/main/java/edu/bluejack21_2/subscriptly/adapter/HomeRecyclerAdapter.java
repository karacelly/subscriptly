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

    // Override onCreateViewHolder which deals
    // with the inflation of the card layout
    // as an item for the RecyclerView.
    @Override
    public HomeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // Inflate item.xml using LayoutInflator
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
//        Log.d("BindViewHolder", subscriptions.get(position).getDuration().toString());
        holder.subscriptionMonth.setText(subscriptions.get(position).getDuration().toString());
        holder.subscriptionItems.setAdapter(new SubscriptionItemListAdapter(holder.subscriptionGroupItem.getContext(), subscriptions));

        //        Picasso.get().load(subscriptions.get(position).getImage()).into(holder.shopImage);
//        holder.shopName.setText(subscriptions.get(position).getName());
//        holder.shopLocation.setText(subscriptions.get(position).getLocation());
//        int shopID = subscriptions.get(position).getShopID();
//        holder.plantShopCard.setOnClickListener(v -> {
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
