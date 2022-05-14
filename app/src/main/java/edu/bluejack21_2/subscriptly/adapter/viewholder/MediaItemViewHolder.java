package edu.bluejack21_2.subscriptly.adapter.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import edu.bluejack21_2.subscriptly.R;
import edu.bluejack21_2.subscriptly.databinding.AdapterHistoryItemBinding;
import edu.bluejack21_2.subscriptly.databinding.MediaItemBinding;
import edu.bluejack21_2.subscriptly.models.Subscription;
import edu.bluejack21_2.subscriptly.models.TransactionDetail;
import edu.bluejack21_2.subscriptly.models.TransactionHeader;
import edu.bluejack21_2.subscriptly.repositories.UserRepository;
import edu.bluejack21_2.subscriptly.utils.DateHelper;

public class MediaItemViewHolder extends RecyclerView.ViewHolder {
    private final MediaItemBinding binding;
    private final View view;
    private ImageView image;

    public MediaItemViewHolder(MediaItemBinding binding) {
        super(binding.getRoot());
        view = binding.getRoot();
        this.binding = binding;

        image = view.findViewById(R.id.image_receipt);
    }

    public void bind (TransactionDetail transactionDetail) {
        binding.setModel(transactionDetail);
        Glide.with(view.getContext()).load(transactionDetail.getImage()).into(image);
    }
}
