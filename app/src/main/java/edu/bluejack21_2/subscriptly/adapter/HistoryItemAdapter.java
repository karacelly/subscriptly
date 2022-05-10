package edu.bluejack21_2.subscriptly.adapter;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import edu.bluejack21_2.subscriptly.adapter.viewholder.HistoryItemViewHolder;
import edu.bluejack21_2.subscriptly.databinding.AdapterHistoryItemBinding;

public class HistoryItemAdapter extends RecyclerView.Adapter<HistoryItemViewHolder> {
    private Context ctx;
    private final LayoutInflater mInflater;

    public HistoryItemAdapter(Context context) {
        this.ctx = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public HistoryItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final AdapterHistoryItemBinding binding = AdapterHistoryItemBinding.inflate(mInflater, parent, false);

        return new HistoryItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(HistoryItemViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}