package edu.bluejack21_2.subscriptly.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SortedList;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import edu.bluejack21_2.subscriptly.adapter.viewholder.FriendViewHolder;
import edu.bluejack21_2.subscriptly.databinding.FriendItemBinding;
import edu.bluejack21_2.subscriptly.models.User;

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

//    public FriendRecyclerAdapter(Context context, Comparator<User> comparator) {
//        mInflater = LayoutInflater.from(context);
//        mComparator = comparator;
//    }
    private final Context context;
    private final ArrayList<User> users;
    private final int template;

    // Constructor for HomeAdapter class
    // which takes a users of String type
    public FriendRecyclerAdapter(ArrayList<User> users, Comparator<User> comparator, Context context, int template) {
        Log.d("FLOW", "FriendRecyclerAdapter");
        this.context = context;
        this.users = users;
        this.template = template;
        mInflater = LayoutInflater.from(context);
        mComparator = comparator;
    }

    @Override
    public FriendViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d("FLOW", "onCreateViewHolder");
        final FriendItemBinding binding = FriendItemBinding.inflate(mInflater, parent, false);
        if (binding == null) {
            Log.d("NULL", "FriendItemBinding");
        }
        View itemView
                = LayoutInflater
                .from(parent.getContext())
                .inflate(template,
                        parent,
                        false);
        return new FriendViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(FriendViewHolder holder, int position) {
        Log.d("FLOW", "onBindViewHolder");
        holder.friendName.setText(users.get(position).getName());
        final User model = mSortedList.get(position);
        holder.bind(model);
    }

    @Override
    public int getItemCount() {
        Log.d("FLOW", "getItemCountFriendRecyclerAdapter");
        Log.d("COUNT", mSortedList.size() + "");
        return mSortedList.size();
    }

    // Override onCreateViewHolder which deals
    // with the inflation of the card layout
    // as an item for the RecyclerView.
//        @Override
//        public FriendViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
//        {
//
//            // Inflate item.xml using LayoutInflator
//            View itemView
//                    = LayoutInflater
//                    .from(parent.getContext())
//                    .inflate(template,
//                            parent,
//                            false);
//
//            return new FriendViewHolder(itemView);
//        }

//    @Override
//    public void onBindViewHolder(@NonNull FriendViewHolder holder, int position) {
//        holder.friendName.setText(users.get(position).getName());
//    }

//    @Override
//    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
////        Log.d("BindViewHolder", users.get(position).getDuration().toString());
//
//        //        Picasso.get().load(users.get(position).getImage()).into(holder.shopImage);
////        holder.shopName.setText(users.get(position).getName());
////        holder.shopLocation.setText(users.get(position).getLocation());
////        int shopID = users.get(position).getShopID();
////        holder.plantShopCard.setOnClickListener(v -> {
////            Intent detail = new Intent(context, DetailActivity.class);
////            detail.putExtra("shopID", shopID);
////            context.startActivity(detail);
////        });
//    }

//    @Override
//        public int getItemCount()
//        {
//            return users.size();
//        }

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
    }

    public void remove(List<User> models) {
        mSortedList.beginBatchedUpdates();
        for (User model : models) {
            mSortedList.remove(model);
        }
        mSortedList.endBatchedUpdates();
    }
}
