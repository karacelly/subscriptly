package edu.bluejack21_2.subscriptly.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import edu.bluejack21_2.subscriptly.R;
import edu.bluejack21_2.subscriptly.models.User;

public class FriendRecyclerAdapter extends RecyclerView.Adapter<FriendRecyclerAdapter.ViewHolder> {


        private Context context;
        private ArrayList<User> users;
        private int template;

        public class ViewHolder extends RecyclerView.ViewHolder {

            TextView friendName;
            ImageView friendProfilePicture;

            public ViewHolder(View view) {
                super(view);

                friendName = view.findViewById(R.id.text_friend_name);
                friendProfilePicture = view.findViewById(R.id.image_friend);
            }
        }


        // Constructor for HomeAdapter class
        // which takes a users of String type
        public FriendRecyclerAdapter(ArrayList<User> users, Context context, int template)
        {
            this.context = context;
            this.users = users;
            this.template = template;
        }

        // Override onCreateViewHolder which deals
        // with the inflation of the card layout
        // as an item for the RecyclerView.
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {

            // Inflate item.xml using LayoutInflator
            View itemView
                    = LayoutInflater
                    .from(parent.getContext())
                    .inflate(template,
                            parent,
                            false);

            return new ViewHolder(itemView);
        }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        Log.d("BindViewHolder", users.get(position).getDuration().toString());
        holder.friendName.setText(users.get(position).getName());
        //        Picasso.get().load(users.get(position).getImage()).into(holder.shopImage);
//        holder.shopName.setText(users.get(position).getName());
//        holder.shopLocation.setText(users.get(position).getLocation());
//        int shopID = users.get(position).getShopID();
//        holder.plantShopCard.setOnClickListener(v -> {
//            Intent detail = new Intent(context, DetailActivity.class);
//            detail.putExtra("shopID", shopID);
//            context.startActivity(detail);
//        });
    }

    @Override
        public int getItemCount()
        {
            return users.size();
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
