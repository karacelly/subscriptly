package edu.bluejack21_2.subscriptly.adapter.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import edu.bluejack21_2.subscriptly.R;

public class SubscriptionViewHolder extends RecyclerView.ViewHolder {

    public LinearLayout subscriptionContainer;
    public TextView subscriptionMemberCounts, subscriptionName, subscriptionPrice;
    public ImageView subscriptionImage;

    public SubscriptionViewHolder(View view) {
        super(view);
        subscriptionName = view.findViewById(R.id.text_subscription_name);
        subscriptionMemberCounts = view.findViewById(R.id.text_subscription_member_counts);
        subscriptionPrice = view.findViewById(R.id.text_subscription_bill);
        subscriptionContainer = view.findViewById(R.id.container_subscription);
        subscriptionImage = view.findViewById(R.id.image_subscription);
    }
}
