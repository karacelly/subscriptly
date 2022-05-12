package edu.bluejack21_2.subscriptly.adapter.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import edu.bluejack21_2.subscriptly.R;
import edu.bluejack21_2.subscriptly.databinding.AdapterMemberItemBinding;
import edu.bluejack21_2.subscriptly.models.User;

public class MemberItemViewHolder extends RecyclerView.ViewHolder {
    private final AdapterMemberItemBinding binding;
    private View view;
    private ImageView userImage;
    private TextView usernameTxt;

    public MemberItemViewHolder(AdapterMemberItemBinding binding) {
        super(binding.getRoot());
        view = binding.getRoot();
        this.binding = binding;

        userImage = view.findViewById(R.id.member_photo);
        usernameTxt = view.findViewById(R.id.member_name);
    }

    public void bind(User u) {
        binding.setModel(u);
    }

    public ImageView getUserImage() {
        return userImage;
    }

    public TextView getUsernameTxt() {
        return usernameTxt;
    }
}
