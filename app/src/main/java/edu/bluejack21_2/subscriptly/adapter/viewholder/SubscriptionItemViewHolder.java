package edu.bluejack21_2.subscriptly.adapter.viewholder;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.flexbox.FlexboxLayout;

import edu.bluejack21_2.subscriptly.R;

public class SubscriptionItemViewHolder extends RecyclerView.ViewHolder {

    public ImageView iconPaid;
    public TextView subscriptionName, subscriptionBill;
    public FlexboxLayout subscriptionContainer;

    public SubscriptionItemViewHolder(View view) {
        super(view);
        subscriptionName = view.findViewById(R.id.text_subscription_name);
        subscriptionBill = view.findViewById(R.id.text_subscription_bill);
        subscriptionContainer = view.findViewById(R.id.container_subscription_item);
        iconPaid = view.findViewById(R.id.indicator_paid_or_not);
    }
}
