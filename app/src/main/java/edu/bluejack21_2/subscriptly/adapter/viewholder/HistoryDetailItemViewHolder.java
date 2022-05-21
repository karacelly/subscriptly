package edu.bluejack21_2.subscriptly.adapter.viewholder;

import android.content.res.Resources;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import edu.bluejack21_2.subscriptly.R;
import edu.bluejack21_2.subscriptly.models.TransactionDetail;
import edu.bluejack21_2.subscriptly.models.TransactionHeader;
import edu.bluejack21_2.subscriptly.models.User;
import edu.bluejack21_2.subscriptly.repositories.SubscriptionRepository;

public class HistoryDetailItemViewHolder extends RecyclerView.ViewHolder {
    private TextView memberTxt, paidStatusTxt;
    public Button verifyButton;
    private View view;

    public HistoryDetailItemViewHolder(View itemView) {
        super(itemView);
        view = itemView;
        memberTxt = itemView.findViewById(R.id.member_name);
        paidStatusTxt = itemView.findViewById(R.id.member_paid_status);
        verifyButton = itemView.findViewById(R.id.member_verify_button);
    }

    public void bind(User u, TransactionDetail transactionDetail) {
        memberTxt.setText(u.getUsername());

        if(transactionDetail == null){
            view.setBackgroundColor(view.getResources().getColor(R.color.light_red));
            paidStatusTxt.setVisibility(View.GONE);
            verifyButton.setVisibility(View.GONE);
        } else if(!FirebaseAuth.getInstance().getUid().equals(transactionDetail.getSubscription().getCreator().getUserID())) {
            verifyButton.setVisibility(View.GONE);
        } else {
            view.setBackgroundColor(view.getResources().getColor(R.color.light_blue));

            if(transactionDetail.getVerified()) {
                paidStatusTxt.setVisibility(View.VISIBLE);
                verifyButton.setVisibility(View.GONE);
            }else{
                paidStatusTxt.setVisibility(View.GONE);
                verifyButton.setVisibility(View.VISIBLE);
            }
        }


    }
}
