package edu.bluejack21_2.subscriptly.adapter.viewholder;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;

import edu.bluejack21_2.subscriptly.R;
import edu.bluejack21_2.subscriptly.databinding.AdapterNotificationBinding;
import edu.bluejack21_2.subscriptly.models.Subscription;
import edu.bluejack21_2.subscriptly.models.TransactionDetail;

public class NotificationViewHolder  extends RecyclerView.ViewHolder {
    private final AdapterNotificationBinding binding;
    private final View view;
    private TextView notifTime, notifText;

    public NotificationViewHolder(AdapterNotificationBinding binding) {
        super(binding.getRoot());
        view = binding.getRoot();
        this.binding = binding;

        notifText = view.findViewById(R.id.notification_text);
        notifTime = view.findViewById(R.id.notification_time);
    }

    public void bind(Context context, TransactionDetail detail){
        binding.setUser(detail.getUser());
        binding.setSubscription(detail.getSubscription());

        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");

        String notificationText = context.getString(R.string.notification_text, detail.getUser().getName(), detail.getSubscription().getName());
        notifText.setText(notificationText);
        notifTime.setText(sdf.format(detail.getPaymentDate().getTime()));
    }
}
