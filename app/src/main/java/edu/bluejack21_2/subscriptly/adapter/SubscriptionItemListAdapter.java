package edu.bluejack21_2.subscriptly.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import edu.bluejack21_2.subscriptly.R;
import edu.bluejack21_2.subscriptly.SubscriptionDetail;
import edu.bluejack21_2.subscriptly.models.Subscription;
import edu.bluejack21_2.subscriptly.utils.Currency;

public class SubscriptionItemListAdapter extends BaseAdapter {
    private Context ctx;
    private ArrayList<Subscription> subscriptions;

    public SubscriptionItemListAdapter(Context ctx, ArrayList<Subscription> subs){
        this.ctx = ctx;
        this.subscriptions = subs;
    }

    @Override
    public int getCount() {
        return subscriptions.size();
    }

    @Override
    public Object getItem(int position) {
        return subscriptions.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(ctx);
        convertView = inflater.inflate(R.layout.home_subscription_item, null);

        ImageView iconPaid = convertView.findViewById(R.id.indicator_paid_or_not);
        TextView subscriptionName = convertView.findViewById(R.id.text_subscription_name);
        TextView subscriptionBill = convertView.findViewById(R.id.text_subscription_bill);

        Subscription s = subscriptions.get(position);
        subscriptionName.setText(s.getName());
        subscriptionBill.setText(Currency.formatToRupiah(Double.parseDouble(s.getBill().toString())));

        convertView.setOnClickListener(v->{
            Intent detail = new Intent(ctx, SubscriptionDetail.class);
            detail.putExtra("subscriptionID", subscriptions.get(position).getKey());
            ctx.startActivity(detail);
        });

        return convertView;
    }
}
