package edu.bluejack21_2.subscriptly.adapter.viewholder;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import edu.bluejack21_2.subscriptly.R;
import edu.bluejack21_2.subscriptly.databinding.AdapterHistoryItemBinding;
import edu.bluejack21_2.subscriptly.models.Subscription;
import edu.bluejack21_2.subscriptly.models.TransactionHeader;
import edu.bluejack21_2.subscriptly.utils.DateHelper;

public class HistoryItemViewHolder extends RecyclerView.ViewHolder {
    private final AdapterHistoryItemBinding binding;
    private TextView monthTxt, paidTxt, unpaidTxt;
    private View view;

    public HistoryItemViewHolder(AdapterHistoryItemBinding binding) {
        super(binding.getRoot());
        view = binding.getRoot();
        this.binding = binding;

        monthTxt = view.findViewById(R.id.text_month_history);
        paidTxt = view.findViewById(R.id.text_paid_history);
        unpaidTxt = view.findViewById(R.id.text_unpaid_history);
    }

    public void bind (Subscription s, TransactionHeader th) {
        int members = s.getMembers().size();
        int paid = th.getDetails().size();
        int unpaid = members - paid;

        binding.setModel(th);
        monthTxt.setText(DateHelper.formatDate(th.getBillingDate(), "MMMM, YYYY").toUpperCase());
        paidTxt.setText("" + paid + " " + view.getResources().getString(R.string.paid_number_label));
        unpaidTxt.setText("" + unpaid + " " + view.getResources().getString(R.string.unpaid_number_label));
    }
}
