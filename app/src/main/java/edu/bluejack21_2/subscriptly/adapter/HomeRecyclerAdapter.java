package edu.bluejack21_2.subscriptly.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.flexbox.FlexboxLayout;

import java.util.ArrayList;

import edu.bluejack21_2.subscriptly.R;
import edu.bluejack21_2.subscriptly.models.Subscription;

public class HomeRecyclerAdapter extends RecyclerView.Adapter<HomeRecyclerAdapter.ViewHolder> {


        private Context context;
        private ArrayList<Subscription> subscriptions;
        private int template;

        // View Holder class which
        // extends RecyclerView.ViewHolder
        public class ViewHolder extends RecyclerView.ViewHolder {

            // Text View
            TextView subscriptionMonth;
            CardView subscriptionGroupItem;
//            ImageView shopImage;

            // parameterised constructor for View Holder class
            // which takes the view as a parameter
            public ViewHolder(View view) {
                super(view);

                subscriptionMonth = view.findViewById(R.id.text_month_year_subscriptions);
                subscriptionGroupItem = view.findViewById(R.id.container_subscription_group_item);
            }
        }


        // Constructor for HomeAdapter class
        // which takes a subscriptions of String type
        public HomeRecyclerAdapter(ArrayList<Subscription> subscriptions, Context context, int template)
        {
            this.context = context;
            this.subscriptions = subscriptions;
            this.template = template;
        }

        // Override onCreateViewHolder which deals
        // with the inflation of the card layout
        // as an item for the RecyclerView.
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {

            // Inflate item.xml using LayoutInflator
            View itemView
                    = LayoutInflater
                    .from(parent.getContext())
                    .inflate(template,
                            parent,
                            false);

            return new ViewHolder(itemView);
        }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d("BindViewHolder", subscriptions.get(position).getDuration().toString());
//        holder.subscriptionMonth.setText(subscriptions.get(position).getDuration());
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
        public int getItemCount()
        {
            return subscriptions.size();
        }
}
