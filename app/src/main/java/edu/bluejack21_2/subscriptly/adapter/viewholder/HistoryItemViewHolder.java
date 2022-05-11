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
import edu.bluejack21_2.subscriptly.models.TransactionHeader;
import edu.bluejack21_2.subscriptly.utils.DateHelper;

public class HistoryItemViewHolder extends RecyclerView.ViewHolder {
    private final AdapterHistoryItemBinding binding;
    private TextView monthTxt;

    public HistoryItemViewHolder(AdapterHistoryItemBinding binding) {
        super(binding.getRoot());
        View view = binding.getRoot();
        this.binding = binding;

        monthTxt = view.findViewById(R.id.text_month_history);
    }

    public void bind (TransactionHeader th) {
        binding.setModel(th);

        monthTxt.setText(DateHelper.formatDate(th.getBillingDate(), "MMMM, YYYY").toUpperCase());
    }
}
