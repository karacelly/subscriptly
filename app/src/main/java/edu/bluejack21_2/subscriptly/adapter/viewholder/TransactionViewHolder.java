package edu.bluejack21_2.subscriptly.adapter.viewholder;

import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import edu.bluejack21_2.subscriptly.R;
import edu.bluejack21_2.subscriptly.databinding.AdapterTransactionBinding;
import edu.bluejack21_2.subscriptly.models.Subscription;

public class TransactionViewHolder extends RecyclerView.ViewHolder {
    private final AdapterTransactionBinding binding;
    private final View view;
    public ImageView subscriptionPhoto;

    public TransactionViewHolder(AdapterTransactionBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
        this.view = binding.getRoot();

        subscriptionPhoto = view.findViewById(R.id.subs_photo);
    }

    public void bind(Subscription s) {
        binding.setSubscription(s);

        Glide.with(view.getContext()).load(s.getImage()).into(subscriptionPhoto);
    }
}
