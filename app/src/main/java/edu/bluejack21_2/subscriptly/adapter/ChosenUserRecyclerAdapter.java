package edu.bluejack21_2.subscriptly.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import edu.bluejack21_2.subscriptly.ChooseFriendActivity;
import edu.bluejack21_2.subscriptly.adapter.viewholder.ChosenUserViewHolder;
import edu.bluejack21_2.subscriptly.databinding.FriendItemBinding;
import edu.bluejack21_2.subscriptly.databinding.SimpleUserItemBinding;
import edu.bluejack21_2.subscriptly.models.User;
import edu.bluejack21_2.subscriptly.repositories.ImageRepository;
import edu.bluejack21_2.subscriptly.repositories.SubscriptionRepository;

public class ChosenUserRecyclerAdapter extends RecyclerView.Adapter<ChosenUserViewHolder> {

    private final LayoutInflater mInflater;
    private final Context context;

    public ChosenUserRecyclerAdapter(Context context) {
        this.context = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public ChosenUserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final SimpleUserItemBinding binding = SimpleUserItemBinding.inflate(mInflater, parent, false);
        return new ChosenUserViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ChosenUserViewHolder holder, int position) {
        final User model = SubscriptionRepository.chosenFriends.get(position);
        holder.bind(model);

        Glide.with(context).load(model.getImage()).into(holder.userImage);

        holder.removeUserButton.setOnClickListener(view -> {
            SubscriptionRepository.chosenFriends.remove(model);
            notifyDataSetChanged();
            ChooseFriendActivity.updateChooseFriendRecycler();
        });
    }

    @Override
    public int getItemCount() {
        return SubscriptionRepository.chosenFriends.size();
    }
}
