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
import edu.bluejack21_2.subscriptly.databinding.FriendItemBinding;
import edu.bluejack21_2.subscriptly.interfaces.QueryChangeListener;
import edu.bluejack21_2.subscriptly.models.User;
import edu.bluejack21_2.subscriptly.repositories.SubscriptionRepository;
import edu.bluejack21_2.subscriptly.utils.UserHelper;

public class ChooseFriendRecyclerAdapter extends RecyclerView.Adapter<FriendViewHolder> {

    private final ChosenUserRecyclerAdapter chosenUserRecyclerAdapter;
    private final LayoutInflater mInflater;
    private final Comparator<User> mComparator;
    private final QueryChangeListener<ArrayList<User>> queryChangeListener;
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

    public ChooseFriendRecyclerAdapter(Comparator<User> comparator, Context context, ChosenUserRecyclerAdapter chosenUserRecyclerAdapter, QueryChangeListener<ArrayList<User>> queryChangeListener) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.mComparator = comparator;
        this.chosenUserRecyclerAdapter = chosenUserRecyclerAdapter;
        this.queryChangeListener = queryChangeListener;
    }

    @Override
    public FriendViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final FriendItemBinding binding = FriendItemBinding.inflate(mInflater, parent, false);
        return new FriendViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(FriendViewHolder holder, int position) {
        final User model = mSortedList.get(position);
        holder.bind(model);

        Glide.with(context).load(model.getImage()).into(holder.friendProfilePicture);

        holder.chooseFriendBox.setChecked(UserHelper.userAlreadyExist(SubscriptionRepository.chosenFriends, model.getUserID()));


//        SubscriptionRepository.isInvited(FirebaseAuth.getInstance().getCurrentUser().getUid(), model.getUserID(), SubscriptionRepository.ACTIVE_SUBSCRIPTION.getSubscriptionId(), invited -> {
//            if (invited == null)
//                Toast.makeText(context, "Error getting data", Toast.LENGTH_SHORT).show();
//            else if (invited == true) holder.invitedText.setVisibility(View.VISIBLE);
//            else {
                holder.chooseFriendBox.setVisibility(View.VISIBLE);
                holder.container.setOnClickListener(view -> {
                    holder.chooseFriendBox.setChecked(!holder.chooseFriendBox.isChecked());
                    updateSelectedUser(holder, model);
                });
                holder.chooseFriendBox.setOnClickListener(view -> {
                    updateSelectedUser(holder, model);
                });
//            }
//        });
//        holder.chooseFriendBox.setVisibility(View.VISIBLE);
//        holder.chooseFriendBox.setOnClickListener(view -> {
//            updateSelectedUser(holder, model);
//        });
    }

    private void updateSelectedUser(FriendViewHolder holder, User model) {
        Boolean status = holder.chooseFriendBox.isChecked();
        if (status) {
            SubscriptionRepository.chosenFriends.add(model);
        } else {
            SubscriptionRepository.chosenFriends.remove(model);
        }
        chosenUserRecyclerAdapter.notifyDataSetChanged();
        queryChangeListener.onChange(SubscriptionRepository.chosenFriends);
    }

    @Override
    public int getItemCount() {
        Log.d("SIZE FRIEND", mSortedList.size() + "");
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
        mSortedList.addAll(models);
    }

    public void remove(List<User> models) {
        mSortedList.beginBatchedUpdates();
        for (User model : models) {
            mSortedList.remove(model);
        }
        mSortedList.endBatchedUpdates();
    }
}
