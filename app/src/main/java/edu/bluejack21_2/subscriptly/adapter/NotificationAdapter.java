package edu.bluejack21_2.subscriptly.adapter;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import edu.bluejack21_2.subscriptly.R;
import edu.bluejack21_2.subscriptly.adapter.viewholder.NotificationViewHolder;
import edu.bluejack21_2.subscriptly.databinding.AdapterNotificationBinding;
import edu.bluejack21_2.subscriptly.models.TransactionDetail;
import edu.bluejack21_2.subscriptly.repositories.UserRepository;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationViewHolder> {
    private Context context;
    private final LayoutInflater mInflater;
    private ArrayList<TransactionDetail> notifications = new ArrayList<>();

    public void setNotifications(ArrayList<TransactionDetail> notifications) {
        this.notifications = notifications;
    }

    public NotificationAdapter(Context context) {
        this.context = context;
        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final AdapterNotificationBinding binding = AdapterNotificationBinding.inflate(mInflater, parent, false);

        return new NotificationViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        holder.bind(notifications.get(position));
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }
}