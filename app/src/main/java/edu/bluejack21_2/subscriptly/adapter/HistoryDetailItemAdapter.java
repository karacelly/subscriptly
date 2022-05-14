package edu.bluejack21_2.subscriptly.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import edu.bluejack21_2.subscriptly.R;
import edu.bluejack21_2.subscriptly.adapter.viewholder.HistoryDetailItemViewHolder;
import edu.bluejack21_2.subscriptly.databinding.AdapterHistoryDetailItemBinding;
import edu.bluejack21_2.subscriptly.models.Subscription;
import edu.bluejack21_2.subscriptly.models.TransactionDetail;
import edu.bluejack21_2.subscriptly.models.User;

public class HistoryDetailItemAdapter extends RecyclerView.Adapter<HistoryDetailItemViewHolder> {
    private Context context;
    private final LayoutInflater mInflater;
    private Subscription subscription;
    private ArrayList<TransactionDetail> details;

    public HistoryDetailItemAdapter(Context context, Subscription subscription, ArrayList<TransactionDetail> details) {
        this.context = context;
        this.subscription = subscription;
        this.details = details;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public HistoryDetailItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_history_detail_item, parent, false);

        return new HistoryDetailItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryDetailItemViewHolder holder, int position) {
        User u = subscription.getMembers().get(position);
        TransactionDetail detail = null;
        for (TransactionDetail td: details) {
            if(td.getUser().getUserID().equals(u.getUserID())) {
                detail = td;
            }
        }
        holder.bind(u, detail);
    }

    @Override
    public int getItemCount() {
        return subscription.getMembers().size();
    }
}