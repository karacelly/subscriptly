package edu.bluejack21_2.subscriptly.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import edu.bluejack21_2.subscriptly.R;
import edu.bluejack21_2.subscriptly.SubscriptionDetail;
import edu.bluejack21_2.subscriptly.adapter.viewholder.SubscriptionItemViewHolder;
import edu.bluejack21_2.subscriptly.models.Subscription;
import edu.bluejack21_2.subscriptly.models.TransactionDetail;
import edu.bluejack21_2.subscriptly.models.TransactionHeader;
import edu.bluejack21_2.subscriptly.models.User;
import edu.bluejack21_2.subscriptly.repositories.SubscriptionRepository;
import edu.bluejack21_2.subscriptly.repositories.UserRepository;
import edu.bluejack21_2.subscriptly.utils.Currency;
import edu.bluejack21_2.subscriptly.utils.SubscriptionHelper;
import edu.bluejack21_2.subscriptly.utils.UserHelper;

public class SubscriptionItemRecyclerAdapter extends RecyclerView.Adapter<SubscriptionItemViewHolder> {

    private final ArrayList<Subscription> subscriptions;
    private final ArrayList<TransactionHeader> headers;
    private final int template;
    private final Context ctx;

    public SubscriptionItemRecyclerAdapter(Context ctx, ArrayList<Subscription> subscriptions, ArrayList<TransactionHeader> headers, int template) {
        this.ctx = ctx;
        this.subscriptions = subscriptions;
        this.headers = headers;
        this.template = template;
    }

    @Override
    public SubscriptionItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView
                = LayoutInflater
                .from(parent.getContext())
                .inflate(template,
                        parent,
                        false);
        return new SubscriptionItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SubscriptionItemViewHolder holder, int position) {
        Subscription s = subscriptions.get(position);
        TransactionHeader transactionHeader = headers.get(position);
        Integer memberCount = transactionHeader.getActiveMembers().size();
        holder.subscriptionName.setText(s.getName());
        if(memberCount <= 0) {
            holder.subscriptionBill.setText("Error");
        } else {
            holder.subscriptionBill.setText(Currency.formatToRupiah(s.getBill().doubleValue() / memberCount));
        }
        TransactionDetail transactionDetail = SubscriptionHelper.getUserPaidDetail(transactionHeader, FirebaseAuth.getInstance().getCurrentUser().getUid());
        if(transactionDetail == null) {
            holder.iconPaid.setImageResource(R.drawable.ic_remove_red_foreground);
        } else {
            holder.iconPaid.setImageResource(R.drawable.ic_check_foreground);
        }
        holder.subscriptionContainer.setOnClickListener(v -> {
            Context c = v.getContext();
            Intent detail = new Intent(c, SubscriptionDetail.class);
            SubscriptionRepository.ACTIVE_SUBSCRIPTION = s;
            detail.putExtra("subscriptionID", s.getSubscriptionId());
            c.startActivity(detail);
        });
    }

    @Override
    public int getItemCount() {
        Log.d("SUBSCRIPTIONS | SIZE", subscriptions.size()+"");
        return subscriptions.size();
    }
}
