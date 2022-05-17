package edu.bluejack21_2.subscriptly.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

import edu.bluejack21_2.subscriptly.adapter.viewholder.TransactionViewHolder;
import edu.bluejack21_2.subscriptly.databinding.AdapterTransactionBinding;
import edu.bluejack21_2.subscriptly.models.Subscription;
import edu.bluejack21_2.subscriptly.models.TransactionDetail;
import edu.bluejack21_2.subscriptly.models.TransactionHeader;
import edu.bluejack21_2.subscriptly.repositories.SubscriptionRepository;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionViewHolder> {
    private final LayoutInflater mInflater;
    private final Context context;
    private ArrayList<Subscription> subscriptions = new ArrayList<>();
    private ArrayList<TransactionDetail> details = new ArrayList<>();

    public void setSubscriptions(ArrayList<Subscription> subscriptions) {
        this.subscriptions = subscriptions;
    }

    public void setDetails(ArrayList<TransactionDetail> details) {
        this.details = details;
    }

    public TransactionAdapter(Context context, ArrayList<Subscription> subscriptions, ArrayList<TransactionDetail> details) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.subscriptions = subscriptions;
        this.details = details;
    }

    @Override
    public TransactionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final AdapterTransactionBinding binding = AdapterTransactionBinding.inflate(mInflater, parent, false);

        return new TransactionViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(TransactionViewHolder holder, int position) {
        holder.bind(subscriptions.get(position));
    }

    @Override
    public int getItemCount() {
        return details.size();
    }
}