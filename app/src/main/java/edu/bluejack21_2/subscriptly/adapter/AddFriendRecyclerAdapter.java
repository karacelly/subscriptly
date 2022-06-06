package edu.bluejack21_2.subscriptly.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import edu.bluejack21_2.subscriptly.adapter.viewholder.FriendViewHolder;
import edu.bluejack21_2.subscriptly.databinding.FriendItemBinding;
import edu.bluejack21_2.subscriptly.interfaces.QueryChangeListener;
import edu.bluejack21_2.subscriptly.models.FriendRequest;
import edu.bluejack21_2.subscriptly.models.User;
import edu.bluejack21_2.subscriptly.repositories.UserRepository;
import edu.bluejack21_2.subscriptly.utils.ImageHelper;
import edu.bluejack21_2.subscriptly.utils.UserHelper;

public class AddFriendRecyclerAdapter extends RecyclerView.Adapter<FriendViewHolder> {

    private final LayoutInflater mInflater;
    private final Context context;

    private ArrayList<User> users;
    private QueryChangeListener<User> listener;

    public void setUsers(ArrayList<User> users) {
        this.users = users;
        notifyDataSetChanged();
    }

    private FriendViewHolder friendViewHolder;

    public AddFriendRecyclerAdapter(Context context, QueryChangeListener<User> listener) {
        this.context = context;
        this.users = users == null ? new ArrayList<>() : users;
        this.listener = listener;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public FriendViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        FriendItemBinding binding = FriendItemBinding.inflate(mInflater, parent, false);

        friendViewHolder = new FriendViewHolder(binding);
        return friendViewHolder;
    }

    @Override
    public void onBindViewHolder(FriendViewHolder holder, int position) {
        final User model = users.get(position);

        ImageHelper.LoadImage(context, model.getImage(), holder.friendProfilePicture);
        holder.bind(model);

        holder.removeFriend.setVisibility(View.GONE);
        holder.addFriend.setVisibility(View.GONE);
        holder.cancelFriend.setVisibility(View.GONE);
        holder.acceptFriend.setVisibility(View.GONE);
        holder.rejectFriend.setVisibility(View.GONE);

        setListeners(holder, model);

        holder.addFriend.setVisibility(View.VISIBLE);
    }

    private void setListeners(FriendViewHolder holder, User model) {
        holder.addFriend.setOnClickListener(v -> {
            UserRepository.sendFriendRequest(FirebaseAuth.getInstance().getUid(), model.getUserID(), data -> {
                if (data) {
                    users.remove(model);
                    listener.onChange(model);
                    notifyDataSetChanged();
//                    Toast.makeText(context, "Success Add Friend", Toast.LENGTH_LONG);
                } else {
//                    Toast.makeText(context, "Failed Add Friend", Toast.LENGTH_LONG);
                }
            });
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }
}
