package edu.bluejack21_2.subscriptly.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import edu.bluejack21_2.subscriptly.adapter.viewholder.FriendViewHolder;
import edu.bluejack21_2.subscriptly.databinding.FriendItemBinding;
import edu.bluejack21_2.subscriptly.models.FriendRequest;
import edu.bluejack21_2.subscriptly.models.User;
import edu.bluejack21_2.subscriptly.repositories.UserRepository;
import edu.bluejack21_2.subscriptly.utils.ImageHelper;
import edu.bluejack21_2.subscriptly.utils.UserHelper;

public class PendingFriendRecyclerAdapter extends RecyclerView.Adapter<FriendViewHolder> {

    private final LayoutInflater mInflater;
    private final Context context;

    private ArrayList<User> users;
    private ArrayList<FriendRequest> requests;

    public void setUsers(ArrayList<User> users) {
        this.users = users;
        notifyDataSetChanged();
    }

    public void setRequests(ArrayList<FriendRequest> requests) {
        this.requests = requests;
        notifyDataSetChanged();
    }

    private FriendViewHolder friendViewHolder;

    public PendingFriendRecyclerAdapter(Context context) {
        this.context = context;
        this.users = users == null ? new ArrayList<>() : users;
        this.requests = requests == null ? new ArrayList<>() : requests;
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

        /*
            Current Logged In User (Sender | Receiver)
            if Sender:
                'Cancel Friend Request'
            else if Receiver
                'Accept Friend Request'
                'Reject Friend Request'
         */

        FriendRequest request = null;
        if (FirebaseAuth.getInstance().getUid() != model.getUserID())
            request = UserHelper.getFriendRequest(requests, FirebaseAuth.getInstance().getUid(), model.getUserID());

        setListeners(holder, model, request);

        holder.removeFriend.setVisibility(View.GONE);
        holder.addFriend.setVisibility(View.GONE);
        holder.cancelFriend.setVisibility(View.GONE);
        holder.acceptFriend.setVisibility(View.GONE);
        holder.rejectFriend.setVisibility(View.GONE);

        if (request != null) {
            if (request.getSender().equals(FirebaseAuth.getInstance().getUid())) {
                holder.cancelFriend.setVisibility(View.VISIBLE);
            } else {
                holder.acceptFriend.setVisibility(View.VISIBLE);
                holder.rejectFriend.setVisibility(View.VISIBLE);
            }
        }
    }

    private void setListeners(FriendViewHolder holder, User model, FriendRequest request) {
        holder.cancelFriend.setOnClickListener(v -> {
            UserRepository.rejectFriendRequest(request.getSender(), request.getReceiver(), listener -> {
                if (listener) {
                    users.remove(model);
                    notifyDataSetChanged();
                }
            });
        });

        holder.acceptFriend.setOnClickListener(v -> {
            UserRepository.acceptFriendRequest(request.getSender(), request.getReceiver(), data -> {
                if (data) {
//                    Toast.makeText(context.getApplicationContext(), "Success Accept", Toast.LENGTH_SHORT);
                    users.remove(model);
                    notifyDataSetChanged();
                } else {
//                    Toast.makeText(context.getApplicationContext(), "Failed Accept", Toast.LENGTH_SHORT);
                }
            });
        });
//
        holder.rejectFriend.setOnClickListener(v -> {
            UserRepository.rejectFriendRequest(request.getSender(), request.getReceiver(), data -> {
                if (data) {
                    users.remove(model);
                    notifyDataSetChanged();
//                    Toast.makeText(context.getApplicationContext(), "Success Reject", Toast.LENGTH_SHORT);
                } else {
//                    Toast.makeText(context.getApplicationContext(), "Failed Reject", Toast.LENGTH_SHORT);
                }
            });
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }
}
