package edu.bluejack21_2.subscriptly.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SortedList;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import edu.bluejack21_2.subscriptly.R;
import edu.bluejack21_2.subscriptly.SubscriptionDetail;
import edu.bluejack21_2.subscriptly.adapter.viewholder.SubscriptionViewHolder;
import edu.bluejack21_2.subscriptly.models.Subscription;
import edu.bluejack21_2.subscriptly.models.User;
import edu.bluejack21_2.subscriptly.repositories.SubscriptionRepository;
import edu.bluejack21_2.subscriptly.utils.Currency;

public class SubscriptionRecyclerAdapter extends RecyclerView.Adapter<SubscriptionViewHolder> {

    private final int template;
    private final Context context;
    private Comparator<Subscription> mComparator;
    private final SortedList<Subscription> mSortedList = new SortedList<>(Subscription.class, new SortedList.Callback<Subscription>() {
        @Override
        public int compare(Subscription a, Subscription b) {
            return mComparator.compare(a, b);
        }

        @Override
        public void onInserted(int position, int count) {
            notifyItemRangeInserted(position, count);
        }

        @Override
        public void onRemoved(int position, int count) {
            notifyItemRangeRemoved(position, count);
        }

        @Override
        public void onMoved(int fromPosition, int toPosition) {
            notifyItemMoved(fromPosition, toPosition);
        }

        @Override
        public void onChanged(int position, int count) {
            notifyItemRangeChanged(position, count);
        }

        @Override
        public boolean areContentsTheSame(Subscription oldItem, Subscription newItem) {
            return oldItem.equals(newItem);
        }

        @Override
        public boolean areItemsTheSame(Subscription item1, Subscription item2) {
            return item1.getSubscriptionId() == item2.getSubscriptionId();
        }
    });

    public SubscriptionRecyclerAdapter(Context context, ArrayList<Subscription> subscriptions, int template, Comparator<Subscription> comparator) {
        this.template = template;
        this.context = context;
        this.mComparator = comparator;
        if (subscriptions != null) {
            mSortedList.addAll(subscriptions);
        }
    }

    public void setComparator(Comparator<Subscription> comparator) {
        this.mComparator = comparator;
        ArrayList<Subscription> local = getSubscriptions();
        mSortedList.replaceAll(local);
    }

    @Override
    public SubscriptionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView
                = LayoutInflater
                .from(parent.getContext())
                .inflate(template,
                        parent,
                        false);

        return new SubscriptionViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SubscriptionViewHolder holder, int position) {
        Subscription s = mSortedList.get(position);
        Integer memberCount = s.getMembers().size();
        holder.subscriptionName.setText(s.getName());
        holder.subscriptionPrice.setText(Currency.formatToRupiah(s.getBill().doubleValue() / memberCount));
        holder.subscriptionMemberCounts.setText(memberCount + " " + context.getResources().getString(R.string.people_label));

        Glide.with(context).load(s.getImage()).into(holder.subscriptionImage);
        holder.subscriptionContainer.setOnClickListener(v -> {
            Context c = v.getContext();
            SubscriptionRepository.ACTIVE_SUBSCRIPTION = s;
            Intent detail = new Intent(c, SubscriptionDetail.class);
            detail.putExtra("subscriptionID", s.getSubscriptionId());
            c.startActivity(detail);
        });
    }

    @Override
    public int getItemCount() {
        return mSortedList.size();
    }

    public void replaceAll(List<Subscription> models) {
        mSortedList.beginBatchedUpdates();
        for (int i = mSortedList.size() - 1; i >= 0; i--) {
            final Subscription model = mSortedList.get(i);
            if (!models.contains(model)) {
                mSortedList.remove(model);
            }
        }
        mSortedList.addAll(models);
        mSortedList.endBatchedUpdates();
    }

    public ArrayList<Subscription> getSubscriptions(){
        ArrayList<Subscription> subscriptions = new ArrayList<>();
        for (int i = 0; i < mSortedList.size(); i++) {
            subscriptions.add(mSortedList.get(i));
        }
        return subscriptions;
    }

    public void add(Subscription model) {
        mSortedList.add(model);
    }

    public void remove(Subscription model) {
        mSortedList.remove(model);
    }

    public void add(List<Subscription> models) {
        mSortedList.addAll(models);
    }
}
