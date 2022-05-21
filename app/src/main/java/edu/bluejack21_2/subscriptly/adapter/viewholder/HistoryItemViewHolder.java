package edu.bluejack21_2.subscriptly.adapter.viewholder;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import edu.bluejack21_2.subscriptly.R;
import edu.bluejack21_2.subscriptly.models.Subscription;
import edu.bluejack21_2.subscriptly.models.TransactionHeader;
import edu.bluejack21_2.subscriptly.utils.DateHelper;

public class HistoryItemViewHolder extends RecyclerView.ViewHolder {
    public RelativeLayout container;
    public CardView recyclerCardView;
    public LinearLayout historyHeader;
    public TextView monthTxt, paidTxt, unpaidTxt;
    public RecyclerView historyDetailRV;
    public View view;
    private boolean isOpen = false;

    public HistoryItemViewHolder(final View itemView) {
        super(itemView);
        view = itemView;

        container = view.findViewById(R.id.container_subscription_history);
        recyclerCardView = view.findViewById(R.id.card_recycler_history);
        historyHeader = view.findViewById(R.id.history_parent);
        monthTxt = view.findViewById(R.id.text_month_history);
        paidTxt = view.findViewById(R.id.text_paid_history);
        unpaidTxt = view.findViewById(R.id.text_unpaid_history);
        historyDetailRV = view.findViewById(R.id.recycler_history_detail);
    }

    public void bind(Subscription s, TransactionHeader th) {
        int members = th.getActiveMembers().size();
        int paid = th.getDetails().size();
        int unpaid = members - paid;

        monthTxt.setText(DateHelper.formatDate(th.getBillingDate(), "MMMM, YYYY").toUpperCase());
        paidTxt.setText(paid + " " + view.getResources().getString(R.string.paid_number_label));
        unpaidTxt.setText(unpaid + " " + view.getResources().getString(R.string.unpaid_number_label));

        historyHeader.setZ(999);
        recyclerCardView.setZ(-5);
        recyclerCardView.setVisibility(View.GONE);
        recyclerCardView.setAlpha(0.0f);
        recyclerCardView.setTranslationY(-recyclerCardView.getHeight());

        historyHeader.setOnClickListener(v -> {
            if (!isOpen) {
                recyclerCardView.setVisibility(View.VISIBLE);
                recyclerCardView.setAlpha(0.0f);
                recyclerCardView
                        .animate()
                        .translationY(0)
                        .alpha(1.0f).setListener(null);
            } else {
                recyclerCardView.animate()
                        .translationY(-recyclerCardView.getHeight())
                        .alpha(0.0f)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                recyclerCardView.setVisibility(View.GONE);
                            }
                        });
            }
            isOpen = !isOpen;
        });
    }

    public RecyclerView getHistoryDetailRV() {
        return historyDetailRV;
    }
}
