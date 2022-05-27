package edu.bluejack21_2.subscriptly.adapter.viewholder;

import android.content.res.Resources;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import edu.bluejack21_2.subscriptly.R;
import edu.bluejack21_2.subscriptly.models.TransactionDetail;
import edu.bluejack21_2.subscriptly.models.TransactionHeader;
import edu.bluejack21_2.subscriptly.models.User;
import edu.bluejack21_2.subscriptly.repositories.SubscriptionRepository;
import edu.bluejack21_2.subscriptly.utils.ImageHelper;

public class HistoryDetailItemViewHolder extends RecyclerView.ViewHolder {

    private ImageView memberImage;
    private TextView memberTxt, paidStatusTxt;
    public Button verifyButton;
    private View view;

    public HistoryDetailItemViewHolder(View itemView) {
        super(itemView);
        view = itemView;
        memberImage = itemView.findViewById(R.id.member_photo);
        memberTxt = itemView.findViewById(R.id.member_name);
        paidStatusTxt = itemView.findViewById(R.id.member_paid_status);
        verifyButton = itemView.findViewById(R.id.member_verify_button);
    }

    public void bind(User u, TransactionDetail transactionDetail) {


        memberTxt.setText(u.getUsername());
        ImageHelper.LoadImage(view.getContext(), u.getImage(), memberImage);

        if(transactionDetail == null){
            view.setBackgroundColor(ContextCompat.getColor(view.getContext(), R.color.light_red));
            paidStatusTxt.setVisibility(View.GONE);
            verifyButton.setVisibility(View.GONE);
        } else {

            boolean isCreator = FirebaseAuth.getInstance().getUid().equals(transactionDetail.getSubscription().getCreator().getUserID());

            if(transactionDetail.getVerified()) {
                view.setBackgroundColor(ContextCompat.getColor(view.getContext(), R.color.light_blue));
                paidStatusTxt.setVisibility(View.VISIBLE);
                verifyButton.setVisibility(View.GONE);
            }else{
                view.setBackgroundColor(ContextCompat.getColor(view.getContext(), R.color.light_yellow));
                paidStatusTxt.setText(view.getResources().getString(R.string.pending_txt));

                if(isCreator) {
                    verifyButton.setVisibility(View.VISIBLE);
                    paidStatusTxt.setVisibility(View.GONE);
                } else {
                    verifyButton.setVisibility(View.GONE);
                }
            }
        }


    }
}
