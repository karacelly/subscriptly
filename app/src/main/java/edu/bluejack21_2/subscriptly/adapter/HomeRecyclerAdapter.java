package edu.bluejack21_2.subscriptly.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import edu.bluejack21_2.subscriptly.R;
import edu.bluejack21_2.subscriptly.adapter.viewholder.HomeViewHolder;
import edu.bluejack21_2.subscriptly.models.Subscription;
import edu.bluejack21_2.subscriptly.models.TransactionHeader;
import edu.bluejack21_2.subscriptly.utils.DateHelper;

public class HomeRecyclerAdapter extends RecyclerView.Adapter<HomeViewHolder> {

    private final Context context;
    public ArrayList<Subscription> subscriptions;
    public ArrayList<TransactionHeader> uniqueMonths;
    private final int template;



    public HomeRecyclerAdapter(ArrayList<Subscription> subscriptions, ArrayList<TransactionHeader> uniqueMonths, Context context, int template) {
        this.context = context;
        this.subscriptions = subscriptions;
        this.uniqueMonths = uniqueMonths;
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
        TransactionHeader header = uniqueMonths.get(position);
        holder.subscriptionMonth.setText(DateHelper.formatDate(header.getBillingDate(), "MMMM, YYYY").toUpperCase());
        setRecyclerView(subscriptions, holder.subscriptionItems, R.layout.home_subscription_item);
    }

    private void setRecyclerView
            (ArrayList<Subscription> data, RecyclerView recyclerView, int layout) {
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(new SubscriptionItemRecyclerAdapter(context, data, layout));
    }

    @Override
    public int getItemCount() {
        Log.d("UNIQUE MONTHS | SIZE", uniqueMonths.size()+"");
        return uniqueMonths.size();
    }
}
