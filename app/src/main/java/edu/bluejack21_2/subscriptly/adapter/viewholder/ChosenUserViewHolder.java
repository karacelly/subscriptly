package edu.bluejack21_2.subscriptly.adapter.viewholder;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import edu.bluejack21_2.subscriptly.R;
import edu.bluejack21_2.subscriptly.databinding.FriendItemBinding;
import edu.bluejack21_2.subscriptly.models.User;

public class ChosenUserViewHolder extends RecyclerView.ViewHolder {

    public LinearLayout container;
    public TextView userName;
    public ImageView userImage;
    private final FriendItemBinding binding;

    public ChosenUserViewHolder(FriendItemBinding binding) {
        super(binding.getRoot());
        View view = binding.getRoot();
        this.binding = binding;

        container = view.findViewById(R.id.container_user_item);

        userName = view.findViewById(R.id.text_user_name);

        userImage = view.findViewById(R.id.image_user);

    }

    public void bind(User item) {
        binding.setModel(item);
    }

}
