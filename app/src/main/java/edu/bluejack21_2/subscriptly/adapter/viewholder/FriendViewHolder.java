package edu.bluejack21_2.subscriptly.adapter.viewholder;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import edu.bluejack21_2.subscriptly.R;
import edu.bluejack21_2.subscriptly.databinding.FriendItemBinding;
import edu.bluejack21_2.subscriptly.models.User;

public class FriendViewHolder extends RecyclerView.ViewHolder {

    public TextView friendName;
    public ImageView friendProfilePicture;
    public Button addFriend, removeFriend, acceptFriend, rejectFriend;
    private final FriendItemBinding binding;

    public FriendViewHolder(FriendItemBinding binding) {
//        super(view);
        super(binding.getRoot());
        View view = binding.getRoot();
        Log.d("VIEWFRIEND", view+"");
        this.binding = binding;
        friendName = view.findViewById(R.id.text_friend_name);
        friendProfilePicture = view.findViewById(R.id.image_friend);
        addFriend = view.findViewById(R.id.action_add_friend);
        removeFriend = view.findViewById(R.id.action_remove_friend);
        acceptFriend = view.findViewById(R.id.action_accept_friend);
        rejectFriend = view.findViewById(R.id.action_reject_friend);
    }

    public void bind(User item) {
        binding.setModel(item);
    }



}
