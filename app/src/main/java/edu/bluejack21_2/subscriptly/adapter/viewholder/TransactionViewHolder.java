package edu.bluejack21_2.subscriptly.adapter.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;

import edu.bluejack21_2.subscriptly.R;
import edu.bluejack21_2.subscriptly.databinding.AdapterTransactionBinding;
import edu.bluejack21_2.subscriptly.models.Subscription;
import edu.bluejack21_2.subscriptly.models.TransactionDetail;

public class TransactionViewHolder extends RecyclerView.ViewHolder {
    private final AdapterTransactionBinding binding;
    private final View view;

    private TextView paymentDateTxt;
    private ImageView subscriptionPhoto;

    public TransactionViewHolder(AdapterTransactionBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
        this.view = binding.getRoot();

        subscriptionPhoto = view.findViewById(R.id.subs_photo);
        paymentDateTxt = view.findViewById(R.id.payment_date);
    }

    public void bind(TransactionDetail td) {
        binding.setSubscription(td.getSubscription());

        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy '|' HH:mm:ss");

        paymentDateTxt.setText(sdf.format(td.getPaymentDate().getTime()));
        Glide.with(view.getContext()).load(td.getSubscription().getImage()).into(subscriptionPhoto);
    }
}
