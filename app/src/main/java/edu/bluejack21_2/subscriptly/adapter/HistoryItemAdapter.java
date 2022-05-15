package edu.bluejack21_2.subscriptly.adapter;

import android.content.Context;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
    private int lastPosition = -1;

    private RecyclerView.RecycledViewPool
            viewPool
            = new RecyclerView
            .RecycledViewPool();

    public HistoryItemAdapter(Context context, Subscription subscription) {
        this.context = context;
        this.subscription = subscription;
        this.transactions = subscription.getHeaders();
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public HistoryItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_history_item, parent, false);

        return new HistoryItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HistoryItemViewHolder holder, int position) {
        final TransactionHeader model = transactions.get(position);
        holder.bind(subscription, model);

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(
                holder
                        .getHistoryDetailRV()
                         .getContext(),
                LinearLayoutManager.VERTICAL,
                false);

        layoutManager.setInitialPrefetchItemCount(subscription.getMembers().size());

        HistoryDetailItemAdapter historyDetailItemAdapter = new HistoryDetailItemAdapter(mInflater.getContext(), subscription, model.getDetails());
        holder.getHistoryDetailRV().setLayoutManager(layoutManager);
        holder.getHistoryDetailRV().setAdapter(historyDetailItemAdapter);
        holder.getHistoryDetailRV().setRecycledViewPool(viewPool);
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
        return transactions.size();
    }
}