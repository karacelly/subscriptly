package edu.bluejack21_2.subscriptly.adapter.viewholder;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import edu.bluejack21_2.subscriptly.databinding.AdapterNotificationBinding;
import edu.bluejack21_2.subscriptly.models.Subscription;
import edu.bluejack21_2.subscriptly.models.TransactionDetail;

public class NotificationViewHolder  extends RecyclerView.ViewHolder {
    private final AdapterNotificationBinding binding;
    private final View view;

    public NotificationViewHolder(AdapterNotificationBinding binding) {
        super(binding.getRoot());
        view = binding.getRoot();
        this.binding = binding;
    }

    public void bind(TransactionDetail detail){
        binding.setUser(detail.getUser());
        binding.setSubscription(detail.getSubscription());
    }
}
