package edu.bluejack21_2.subscriptly.adapter.viewholder;

import android.content.Context;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import edu.bluejack21_2.subscriptly.R;
import edu.bluejack21_2.subscriptly.databinding.AdapterHistoryItemBinding;
import edu.bluejack21_2.subscriptly.databinding.MediaItemBinding;
import edu.bluejack21_2.subscriptly.models.Subscription;
import edu.bluejack21_2.subscriptly.models.TransactionDetail;
import edu.bluejack21_2.subscriptly.models.TransactionHeader;
import edu.bluejack21_2.subscriptly.repositories.UserRepository;
import edu.bluejack21_2.subscriptly.utils.DateHelper;

public class MediaItemViewHolder extends RecyclerView.ViewHolder {
    private final MediaItemBinding binding;
    private final View view;
    private ImageView image;

    public MediaItemViewHolder(MediaItemBinding binding) {
        super(binding.getRoot());
        view = binding.getRoot();
        this.binding = binding;

        image = view.findViewById(R.id.image_receipt);
    }

    public void bind (TransactionDetail transactionDetail, FrameLayout layout) {
        binding.setModel(transactionDetail);
        Glide.with(view.getContext()).load(transactionDetail.getImage()).into(image);
        image.setOnClickListener(view -> {
            LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            // Inflate the custom layout/view
            View customView = inflater.inflate(R.layout.popup_image,null);
            PopupWindow popupWindow = new PopupWindow(
                    customView,
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.MATCH_PARENT
            );
            if(Build.VERSION.SDK_INT>=21){
                popupWindow.setElevation(5.0f);
            }
            ImageView fullSizeImage = customView.findViewById(R.id.image_popup);
            Glide.with(view.getContext()).load(transactionDetail.getImage()).into(fullSizeImage);
            ImageButton closeButton = customView.findViewById(R.id.action_close_popup);
            closeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popupWindow.dismiss();
                }
            });
            popupWindow.showAtLocation(layout, Gravity.CENTER,0,0);
        });
    }
}
