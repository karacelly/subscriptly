package edu.bluejack21_2.subscriptly.adapter.viewholder;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import edu.bluejack21_2.subscriptly.R;
import edu.bluejack21_2.subscriptly.databinding.AdapterMemberItemBinding;
import edu.bluejack21_2.subscriptly.models.Subscription;
import edu.bluejack21_2.subscriptly.models.User;

public class MemberItemViewHolder extends RecyclerView.ViewHolder {
    private final AdapterMemberItemBinding binding;
    private View view;
    private ImageView userImage;
    private TextView usernameTxt;
    private Button kickMember;

    public MemberItemViewHolder(AdapterMemberItemBinding binding) {
        super(binding.getRoot());
        view = binding.getRoot();
        this.binding = binding;

        userImage = view.findViewById(R.id.member_photo);
        usernameTxt = view.findViewById(R.id.member_name);
        kickMember = view.findViewById(R.id.action_kick_member);
    }

    public void bind(User user, Subscription subscription) {
        binding.setModel(user);
        if(user.getUserID().equals(subscription.getCreator().getUserID())) {
            kickMember.setVisibility(View.GONE);
        }
        kickMember.setOnClickListener(v -> {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(view.getContext());
            dialogBuilder.setMessage("Remove " + user.getName() + " from " + subscription.getName() + " subscription?");
            dialogBuilder.setCancelable(true);

            dialogBuilder.setNeutralButton(
                    "Cancel",
                    (dialog, id)-> {
                        dialog.dismiss();
                    });

            dialogBuilder.setPositiveButton(
                    "Yes",
                    (dialog, id) -> {
                        dialog.dismiss();

                    });
            AlertDialog alert = dialogBuilder.create();
            alert.show();

        });
        Glide.with(view.getContext()).load(user.getImage()).into(userImage);
    }

    public ImageView getUserImage() {
        return userImage;
    }

    public TextView getUsernameTxt() {
        return usernameTxt;
    }
}
