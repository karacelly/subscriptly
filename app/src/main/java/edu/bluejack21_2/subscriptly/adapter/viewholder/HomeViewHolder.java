package edu.bluejack21_2.subscriptly.adapter.viewholder;

import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.flexbox.FlexboxLayout;

import edu.bluejack21_2.subscriptly.R;

public class HomeViewHolder extends RecyclerView.ViewHolder {

    // Text View
    public TextView subscriptionMonth;
    public CardView subscriptionGroupItem;
    public ListView subscriptionItems;
    public FlexboxLayout subscriptionLayout;
//            ImageView shopImage;

    // parameterised constructor for View Holder class
    // which takes the view as a parameter
    public HomeViewHolder(View view) {
        super(view);
        Log.d("VIEWHOME", view+"");
        subscriptionMonth = view.findViewById(R.id.text_month_year_subscriptions);
        subscriptionGroupItem = view.findViewById(R.id.container_subscription_group_item);
        subscriptionItems = view.findViewById(R.id.list_subscription_item);
        subscriptionLayout = view.findViewById(R.id.home_subscription_layout);
    }
}
