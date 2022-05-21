package edu.bluejack21_2.subscriptly.adapter;

import android.content.Context;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import java.util.ArrayList;

import edu.bluejack21_2.subscriptly.R;
import edu.bluejack21_2.subscriptly.adapter.viewholder.HistoryItemViewHolder;
import edu.bluejack21_2.subscriptly.databinding.AdapterHistoryItemBinding;
import edu.bluejack21_2.subscriptly.models.Subscription;
import edu.bluejack21_2.subscriptly.models.TransactionHeader;

public class HistoryItemAdapter extends RecyclerView.Adapter<HistoryItemViewHolder> {
    private Context context;
    private final LayoutInflater mInflater;
    private Subscription subscription;
    private ArrayList<TransactionHeader> transactions;

    public void setTransactions(ArrayList<TransactionHeader> transactions) {
        this.transactions = transactions;
    }

    private int lastPosition = -1;

    private RecyclerView.RecycledViewPool
            viewPool
            = new RecyclerView
            .RecycledViewPool();

    public HistoryItemAdapter(Context context, Subscription subscription) {
        this.context = context;
        this.subscription = subscription;
        this.transactions = transactions == null ? new ArrayList<>() : transactions;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public HistoryItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_history_item, parent, false);

        return new HistoryItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HistoryItemViewHolder holder, int position) {
        TransactionHeader transactionHeader = transactions.get(position);
        holder.bind(subscription, transactionHeader);

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(
                holder
                        .getHistoryDetailRV()
                         .getContext(),
                LinearLayoutManager.VERTICAL,
                false);

        layoutManager.setInitialPrefetchItemCount(transactionHeader.getActiveMembers().size());

        HistoryDetailItemAdapter historyDetailItemAdapter = new HistoryDetailItemAdapter(mInflater.getContext(), transactionHeader);
        holder.historyDetailRV.setLayoutManager(layoutManager);
        holder.historyDetailRV.setAdapter(historyDetailItemAdapter);
        holder.historyDetailRV.setRecycledViewPool(viewPool);
        setAnimation(holder.itemView, position);
    }

    private void setAnimation(View viewToAnimate, int position)
    {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        Log.d("History Item Adapter | Transaction Size", transactions.size()+"");
        return transactions.size();
    }
}