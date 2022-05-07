package edu.bluejack21_2.subscriptly.adapter.viewholder;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import edu.bluejack21_2.subscriptly.R;
import edu.bluejack21_2.subscriptly.databinding.FriendItemBinding;
import edu.bluejack21_2.subscriptly.models.User;
import edu.bluejack21_2.subscriptly.repositories.ImageRepository;
import edu.bluejack21_2.subscriptly.repositories.UserRepository;

public class FriendViewHolder extends RecyclerView.ViewHolder {

    public TextView friendName;
    public ImageView friendProfilePicture;
    public Button addFriend, cancelFriend, removeFriend, acceptFriend, rejectFriend;
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
        cancelFriend = view.findViewById(R.id.action_cancel_friend);
        removeFriend = view.findViewById(R.id.action_remove_friend);
        acceptFriend = view.findViewById(R.id.action_accept_friend);
        rejectFriend = view.findViewById(R.id.action_reject_friend);
    }

    public void bind(User item) {
        binding.setModel(item);
    }



}
