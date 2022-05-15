package edu.bluejack21_2.subscriptly.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Calendar;

import edu.bluejack21_2.subscriptly.R;
import edu.bluejack21_2.subscriptly.adapter.viewholder.HomeViewHolder;
import edu.bluejack21_2.subscriptly.models.Subscription;
import edu.bluejack21_2.subscriptly.models.TransactionDetail;
import edu.bluejack21_2.subscriptly.models.TransactionHeader;
import edu.bluejack21_2.subscriptly.repositories.UserRepository;
import edu.bluejack21_2.subscriptly.utils.DateHelper;
import edu.bluejack21_2.subscriptly.utils.RecyclerViewHelper;
import edu.bluejack21_2.subscriptly.utils.SubscriptionHelper;

public class HomeRecyclerAdapter extends RecyclerView.Adapter<HomeViewHolder> {

    private final Context context;
    public ArrayList<Subscription> subscriptions;
    public ArrayList<TransactionHeader> uniqueMonths;
    private final int template;



    public HomeRecyclerAdapter(ArrayList<Subscription> subscriptions, ArrayList<TransactionHeader> uniqueMonths, Context context, int template) {
        this.context = context;
        this.subscriptions = subscriptions;
        this.uniqueMonths = uniqueMonths;
        this.template = template;
    }

    @Override
    public HomeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView
                = LayoutInflater
                .from(parent.getContext())
                .inflate(template,
                        parent,
                        false);

        return new HomeViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, int position) {
        TransactionHeader header = uniqueMonths.get(position);
        holder.subscriptionMonth.setText(DateHelper.formatDate(header.getBillingDate(), "MMMM, YYYY").toUpperCase());
        ArrayList<Subscription> subscriptionOnThatMonth = new ArrayList<>();
        ArrayList<TransactionHeader> transactionHeaderOnThatMonth = new ArrayList<>();
        for (Subscription subscription:
             subscriptions) {
            TransactionHeader particularHeader = SubscriptionHelper.getMonthYearTransactionExists(subscription.getHeaders(), header);
            if(particularHeader != null) {
                subscriptionOnThatMonth.add(subscription);
                transactionHeaderOnThatMonth.add(particularHeader);
                TransactionDetail transactionDetail = SubscriptionHelper.getUserPaidDetail(particularHeader, FirebaseAuth.getInstance().getCurrentUser().getUid());
                if (transactionDetail == null) {
                    int colorRed = Color.parseColor("#FF0000");
                    holder.subscriptionGroupItem.setOutlineAmbientShadowColor(colorRed);
                    holder.subscriptionGroupItem.setOutlineSpotShadowColor(colorRed);
                } else {
                    int colorBlack = Color.parseColor("#000000");
                    holder.subscriptionGroupItem.setOutlineAmbientShadowColor(colorBlack);
                    holder.subscriptionGroupItem.setOutlineSpotShadowColor(colorBlack);
                }
            }
        }
        SubscriptionItemRecyclerAdapter adapter = new SubscriptionItemRecyclerAdapter(context, subscriptionOnThatMonth, transactionHeaderOnThatMonth, R.layout.home_subscription_item);
        RecyclerViewHelper.setRecyclerView(context, adapter, LinearLayoutManager.VERTICAL, holder.subscriptionItems);
    }

    @Override
    public int getItemCount() {
        Log.d("UNIQUE MONTHS | SIZE", uniqueMonths.size()+"");
        return uniqueMonths.size();
    }
}
