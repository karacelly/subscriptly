package edu.bluejack21_2.subscriptly.adapter;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;

import edu.bluejack21_2.subscriptly.adapter.viewholder.HistoryItemViewHolder;
import edu.bluejack21_2.subscriptly.databinding.AdapterHistoryItemBinding;
import edu.bluejack21_2.subscriptly.models.TransactionHeader;

public class HistoryItemAdapter extends RecyclerView.Adapter<HistoryItemViewHolder> {
    private Context context;
    private final LayoutInflater mInflater;
    private ArrayList<TransactionHeader> transactions;

    public HistoryItemAdapter(Context context, ArrayList<TransactionHeader> transactions) {
        this.context = context;
        this.transactions = transactions;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public HistoryItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final AdapterHistoryItemBinding binding = AdapterHistoryItemBinding.inflate(mInflater, parent, false);

        return new HistoryItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(HistoryItemViewHolder holder, int position) {
        final TransactionHeader model = transactions.get(position);
        holder.bind(model);
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }
}