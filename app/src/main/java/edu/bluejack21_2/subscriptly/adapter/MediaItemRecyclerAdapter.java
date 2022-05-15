package edu.bluejack21_2.subscriptly.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import edu.bluejack21_2.subscriptly.adapter.viewholder.MediaItemViewHolder;
import edu.bluejack21_2.subscriptly.databinding.MediaItemBinding;
import edu.bluejack21_2.subscriptly.models.Subscription;
import edu.bluejack21_2.subscriptly.models.TransactionDetail;
import edu.bluejack21_2.subscriptly.models.TransactionHeader;

public class MediaItemRecyclerAdapter extends RecyclerView.Adapter<MediaItemViewHolder> {
    private final LayoutInflater mInflater;
    private final Context context;
    private final ArrayList<TransactionDetail> transactionDetails;
    private final FrameLayout layout;

    public MediaItemRecyclerAdapter(Context context, Subscription subscription, FrameLayout layout) {
        this.context = context;
        this.layout = layout;
        mInflater = LayoutInflater.from(context);
        transactionDetails = getAllDetails(subscription.getHeaders());
    }

    private ArrayList<TransactionDetail> getAllDetails(ArrayList<TransactionHeader> transactionHeaders) {
        ArrayList<TransactionDetail> details = new ArrayList<>();
        for (TransactionHeader header :
                transactionHeaders) {
            details.addAll(header.getDetails());
        }
        return details;
    }


    @Override
    public MediaItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final MediaItemBinding binding = MediaItemBinding.inflate(mInflater, parent, false);

        return new MediaItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(MediaItemViewHolder holder, int position) {
        holder.bind(transactionDetails.get(position), layout);
    }

    @Override
    public int getItemCount() {
        return transactionDetails.size();
    }
}