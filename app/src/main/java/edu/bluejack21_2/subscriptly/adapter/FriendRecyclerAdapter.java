package edu.bluejack21_2.subscriptly.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SortedList;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import edu.bluejack21_2.subscriptly.adapter.viewholder.FriendViewHolder;
import edu.bluejack21_2.subscriptly.database.SubscriptlyDB;
import edu.bluejack21_2.subscriptly.databinding.FriendItemBinding;
import edu.bluejack21_2.subscriptly.models.FriendRequest;
import edu.bluejack21_2.subscriptly.models.User;
import edu.bluejack21_2.subscriptly.repositories.UserRepository;
import edu.bluejack21_2.subscriptly.utils.ImageHelper;
import edu.bluejack21_2.subscriptly.utils.UserHelper;

public class FriendRecyclerAdapter extends RecyclerView.Adapter<FriendViewHolder> {

    private final LayoutInflater mInflater;
    private final Context context;
    private ArrayList<User> users;
    private FriendViewHolder friendViewHolder;

    public void setUsers(ArrayList<User> users) {
        this.users = users;
        notifyDataSetChanged();
    }

    public FriendRecyclerAdapter(Context context) {
        this.context = context;
        this.users = users == null ? new ArrayList<>() : users;
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
        holder.removeFriend.setVisibility(View.VISIBLE);
    }

    private void setListeners(FriendViewHolder holder, User model) {
        holder.removeFriend.setOnClickListener(v ->
            UserRepository.removeFriend(FirebaseAuth.getInstance().getUid(), model.getUserID(), listener -> {
                if (listener) {
                    users.remove(model);
                    notifyDataSetChanged();
                }
            })
        );
    }

    @Override
    public int getItemCount() {
        return users.size();
    }
}
