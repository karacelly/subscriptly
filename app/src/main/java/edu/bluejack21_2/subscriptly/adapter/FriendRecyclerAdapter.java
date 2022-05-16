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
import edu.bluejack21_2.subscriptly.utils.UserHelper;

public class FriendRecyclerAdapter extends RecyclerView.Adapter<FriendViewHolder> {

    private final LayoutInflater mInflater;
    private final Comparator<User> mComparator;
    private final SortedList<User> mSortedList = new SortedList<>(User.class, new SortedList.Callback<User>() {
        @Override
        public int compare(User a, User b) {
            return mComparator.compare(a, b);
        }

        @Override
        public void onInserted(int position, int count) {
            notifyItemRangeInserted(position, count);
        }

        @Override
        public void onRemoved(int position, int count) {
            notifyItemRangeRemoved(position, count);
        }

        @Override
        public void onMoved(int fromPosition, int toPosition) {
            notifyItemMoved(fromPosition, toPosition);
        }

        @Override
        public void onChanged(int position, int count) {
            notifyItemRangeChanged(position, count);
        }

        @Override
        public boolean areContentsTheSame(User oldItem, User newItem) {
            return oldItem.equals(newItem);
        }

        @Override
        public boolean areItemsTheSame(User item1, User item2) {
            return item1.getUserID() == item2.getUserID();
        }
    });
    private final Context context;
    private final ArrayList<User> users;
    private final ArrayList<FriendRequest> requests;
    private final int template;
    private FriendViewHolder friendViewHolder;

    public FriendRecyclerAdapter(ArrayList<User> users, ArrayList<FriendRequest> requests, Comparator<User> comparator, Context context, int template) {
        this.context = context;
        this.users = users;
        this.requests = requests;
        this.template = template;
        mInflater = LayoutInflater.from(context);
        mComparator = comparator;
        if (users != null) {
            mSortedList.addAll(users);
        }
    }

    @Override
    public FriendViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        FriendItemBinding binding = FriendItemBinding.inflate(mInflater, parent, false);

        friendViewHolder = new FriendViewHolder(binding);
        return friendViewHolder;
    }

    @Override
    public void onBindViewHolder(FriendViewHolder holder, int position) {
        final User model = mSortedList.get(position);

        Glide.with(context).load(model.getImage()).into(holder.friendProfilePicture);
        holder.bind(model);

        /*
            Current Logged In User (Sender | Receiver)
            if Sender:
                'Cancel Friend Request'
            else if Receiver
                'Accept Friend Request'
                'Reject Friend Request'
         */

        Log.d("FriendRecyclerAdapter", "onBindViewHolder");
        FriendRequest request = null;
        if (FirebaseAuth.getInstance().getCurrentUser().getUid() != model.getUserID())
            request = UserHelper.getFriendRequest(requests, FirebaseAuth.getInstance().getCurrentUser().getUid(), model.getUserID());

        setListeners(holder, model, request);
        if (request != null) {
            holder.addFriend.setVisibility(View.GONE);
            if (request.getSender().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                holder.cancelFriend.setVisibility(View.VISIBLE);
            } else {
                holder.acceptFriend.setVisibility(View.VISIBLE);
                holder.rejectFriend.setVisibility(View.VISIBLE);
            }
        } else {
            UserRepository.getLoggedInUser(user -> {
                if (UserRepository.checkFriend(user, model.getUserID())) {
                    holder.addFriend.setVisibility(View.GONE);
                    holder.removeFriend.setVisibility(View.VISIBLE);
                } else {
                    holder.addFriend.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    private void setListeners(FriendViewHolder holder, User model, FriendRequest request) {
        holder.cancelFriend.setOnClickListener(v -> {
            UserRepository.rejectFriendRequest(request.getSender(), request.getReceiver(), listener -> {
                if (listener) {
                    holder.addFriend.setVisibility(View.VISIBLE);
                    holder.cancelFriend.setVisibility(View.GONE);
                } else {

                }
            });
        });

        holder.acceptFriend.setOnClickListener(v -> {
            UserRepository.acceptFriendRequest(request.getSender(), request.getReceiver(), data -> {
                if (data) {
                    Toast.makeText(context.getApplicationContext(), "Success Accept", Toast.LENGTH_SHORT);
                    holder.acceptFriend.setVisibility(View.GONE);
                    holder.rejectFriend.setVisibility(View.GONE);
                    holder.removeFriend.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(context.getApplicationContext(), "Failed Accept", Toast.LENGTH_SHORT);
                }
            });
        });

        holder.rejectFriend.setOnClickListener(v -> {
            UserRepository.rejectFriendRequest(request.getSender(), request.getReceiver(), data -> {
                if (data) {
                    holder.rejectFriend.setVisibility(View.GONE);
                    holder.acceptFriend.setVisibility(View.GONE);
                    holder.addFriend.setVisibility(View.VISIBLE);
                    Toast.makeText(context.getApplicationContext(), "Success Reject", Toast.LENGTH_SHORT);
                } else {
                    Toast.makeText(context.getApplicationContext(), "Failed Reject", Toast.LENGTH_SHORT);
                }
            });
        });

        holder.addFriend.setOnClickListener(v -> {
            UserRepository.sendFriendRequest(FirebaseAuth.getInstance().getCurrentUser().getUid(), model.getUserID(), data -> {
                if (data) {
                    holder.addFriend.setVisibility(View.GONE);
                    holder.cancelFriend.setVisibility(View.VISIBLE);
                    Toast.makeText(context, "Success Add Friend", Toast.LENGTH_LONG);
                } else {
                    Toast.makeText(context, "Failed Add Friend", Toast.LENGTH_LONG);
                }
            });
        });

        holder.removeFriend.setOnClickListener(v -> {
            UserRepository.removeFriend(FirebaseAuth.getInstance().getCurrentUser().getUid(), model.getUserID(), listener -> {
                if (listener) {
                    holder.removeFriend.setVisibility(View.GONE);
                    holder.addFriend.setVisibility(View.VISIBLE);
                } else {

                }
            });
        });
    }

    @Override
    public int getItemCount() {
        return mSortedList.size();
    }

    public void replaceAll(List<User> models) {
        mSortedList.beginBatchedUpdates();
        for (int i = mSortedList.size() - 1; i >= 0; i--) {
            final User model = mSortedList.get(i);
            if (!models.contains(model)) {
                mSortedList.remove(model);
            }
        }
        mSortedList.addAll(models);
        mSortedList.endBatchedUpdates();
    }

    public void add(User model) {
        mSortedList.add(model);
    }

    public void remove(User model) {
        mSortedList.remove(model);
    }

    public void add(List<User> models) {
        Log.d("FLOW", "AddMSortedList");
        mSortedList.addAll(models);
        Log.d("SortedList Size: ", mSortedList.size() + "");
//        friendViewHolder.bind(mSortedList);
    }

    public void remove(List<User> models) {
        mSortedList.beginBatchedUpdates();
        for (User model : models) {
            mSortedList.remove(model);
        }
        mSortedList.endBatchedUpdates();
    }
}
