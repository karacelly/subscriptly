package edu.bluejack21_2.subscriptly.adapter.viewholder;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

import edu.bluejack21_2.subscriptly.R;
import edu.bluejack21_2.subscriptly.databinding.AdapterMemberItemBinding;
import edu.bluejack21_2.subscriptly.interfaces.QueryChangeListener;
import edu.bluejack21_2.subscriptly.models.Subscription;
import edu.bluejack21_2.subscriptly.models.User;
import edu.bluejack21_2.subscriptly.repositories.SubscriptionRepository;
import edu.bluejack21_2.subscriptly.repositories.UserRepository;

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

    public void bind(User user, Subscription subscription, QueryChangeListener<Boolean> listener) {
        binding.setModel(user);
        if(!FirebaseAuth.getInstance().getCurrentUser().getUid().equals(subscription.getCreator().getUserID())) {
            kickMember.setVisibility(View.GONE);
        }
        if(user.getUserID().equals(subscription.getCreator().getUserID())) {
            kickMember.setVisibility(View.GONE);
        }
        kickMember.setOnClickListener(v -> {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(view.getContext());
            dialogBuilder.setMessage("Remove " + user.getName() + " from " + subscription.getName() + " subscription ?");
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
                        SubscriptionRepository.removeMember(user.getUserID(), subscription.getSubscriptionId(), successful -> {
                            if(successful) {
                                subscription.getMembers().remove(user);
                                listener.onChange(true);
                            } else {
                                Toast.makeText(view.getContext(), "Failed Remove Member!", Toast.LENGTH_SHORT).show();
                            }
                        });
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
