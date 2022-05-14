package edu.bluejack21_2.subscriptly.adapter.viewholder;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import edu.bluejack21_2.subscriptly.R;
import edu.bluejack21_2.subscriptly.models.TransactionDetail;
import edu.bluejack21_2.subscriptly.models.User;

public class HistoryDetailItemViewHolder extends RecyclerView.ViewHolder {
    private TextView memberTxt, paidStatusTxt;
    private Button verifyButton;
    private View view;

    public HistoryDetailItemViewHolder(View itemView) {
        super(itemView);
        view = itemView;
        memberTxt = itemView.findViewById(R.id.member_name);
        paidStatusTxt = itemView.findViewById(R.id.member_paid_status);
        verifyButton = itemView.findViewById(R.id.member_verify_button);
    }

    public void bind(User u, TransactionDetail td) {
        memberTxt.setText(u.getUsername());

        if(td == null){
            view.setBackgroundColor(view.getResources().getColor(R.color.light_red));
            paidStatusTxt.setVisibility(View.GONE);
            verifyButton.setVisibility(View.GONE);
        }else {
            view.setBackgroundColor(view.getResources().getColor(R.color.light_blue));

            if(td.getVerified()) {
                paidStatusTxt.setVisibility(View.VISIBLE);
                verifyButton.setVisibility(View.GONE);
            }else{
                paidStatusTxt.setVisibility(View.GONE);
                verifyButton.setVisibility(View.VISIBLE);
            }
        }
    }
}
