package edu.bluejack21_2.subscriptly.adapter.viewholder;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import edu.bluejack21_2.subscriptly.databinding.AdapterHistoryItemBinding;
import edu.bluejack21_2.subscriptly.models.TransactionHeader;

public class HistoryItemViewHolder extends RecyclerView.ViewHolder {
    private final AdapterHistoryItemBinding binding;

    public HistoryItemViewHolder(AdapterHistoryItemBinding binding) {
        super(binding.getRoot());
        View view = binding.getRoot();
        this.binding = binding;
    }

    public void bind (TransactionHeader th) {
        binding.setModel(th);
    }
}
