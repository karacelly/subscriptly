package edu.bluejack21_2.subscriptly.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import edu.bluejack21_2.subscriptly.R;
import edu.bluejack21_2.subscriptly.adapter.viewholder.HistoryDetailItemViewHolder;
import edu.bluejack21_2.subscriptly.databinding.AdapterHistoryDetailItemBinding;
import edu.bluejack21_2.subscriptly.models.Subscription;
import edu.bluejack21_2.subscriptly.models.TransactionDetail;
import edu.bluejack21_2.subscriptly.models.TransactionHeader;
import edu.bluejack21_2.subscriptly.models.User;
import edu.bluejack21_2.subscriptly.repositories.SubscriptionRepository;

public class HistoryDetailItemAdapter extends RecyclerView.Adapter<HistoryDetailItemViewHolder> {
    private Context context;
    private final LayoutInflater mInflater;
    private TransactionHeader transactionHeader;

    public HistoryDetailItemAdapter(Context context, TransactionHeader transactionHeader) {
        this.context = context;
        this.transactionHeader = transactionHeader;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public HistoryDetailItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_history_detail_item, parent, false);

        return new HistoryDetailItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryDetailItemViewHolder holder, int position) {
        User u = transactionHeader.getActiveMembers().get(position);
//        Log.d("USER's Name", u.getName());
        if(u != null) {
            TransactionDetail transactionDetail = null;
            for (TransactionDetail td: transactionHeader.getDetails()) {
                if(td.getUser().getUserID().equals(u.getUserID())) {
                    transactionDetail = td;
                    break;
                }
            }
            holder.bind(u, transactionDetail);
            TransactionDetail finalTransactionDetail = transactionDetail;
            if(finalTransactionDetail != null) {
                holder.verifyButton.setOnClickListener(v -> {
                    SubscriptionRepository.verifyTransaction(finalTransactionDetail.getSubscription().getSubscriptionId(), transactionHeader.getTransactionId(), finalTransactionDetail.getTransactionDetailId(), verification -> {
                        if(verification) {
                            Toast.makeText(v.getContext(), "Verification Transaction Success!", Toast.LENGTH_SHORT).show();
                            finalTransactionDetail.setVerified(true);
                            notifyDataSetChanged();
        //                    holder.verifyButton.setVisibility(View.GONE);
                        }
                        else Toast.makeText(v.getContext(), "Verification Transaction Failed!", Toast.LENGTH_SHORT).show();
                    });
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return transactionHeader.getActiveMembers().size();
    }
}