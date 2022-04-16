package edu.bluejack21_2.subscriptly.adapter.viewholder;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import edu.bluejack21_2.subscriptly.R;
import edu.bluejack21_2.subscriptly.databinding.FriendItemBinding;
import edu.bluejack21_2.subscriptly.models.User;

public class FriendViewHolder extends RecyclerView.ViewHolder {

    public TextView friendName;
    public ImageView friendProfilePicture;
    private final FriendItemBinding binding;

    public FriendViewHolder(FriendItemBinding binding) {
//        super(view);
        super(binding.getRoot());
        View view = binding.getRoot();
        Log.d("VIEWFRIEND", view+"");
        this.binding = binding;
        friendName = view.findViewById(R.id.text_friend_name);
        friendProfilePicture = view.findViewById(R.id.image_friend);
    }

    public void bind(User item) {
        binding.setModel(item);
    }



}
