package edu.bluejack21_2.subscriptly.adapter;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Vector;

import edu.bluejack21_2.subscriptly.R;
import edu.bluejack21_2.subscriptly.adapter.viewholder.MemberItemViewHolder;
import edu.bluejack21_2.subscriptly.databinding.AdapterMemberItemBinding;
import edu.bluejack21_2.subscriptly.models.Subscription;
import edu.bluejack21_2.subscriptly.models.User;

public class MemberItemAdapter extends RecyclerView.Adapter<MemberItemViewHolder> {
    private Context ctx;
    private final LayoutInflater mInflater;
    private final Subscription subscription;

    public MemberItemAdapter(Context ctx, Subscription subscription) {
        this.ctx = ctx;
        this.subscription = subscription;
        mInflater = LayoutInflater.from(ctx);
    }

    @NonNull
    @Override
    public MemberItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final AdapterMemberItemBinding binding = AdapterMemberItemBinding.inflate(mInflater, parent, false);

        return new MemberItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MemberItemViewHolder holder, int position) {
        final User model = subscription.getMembers().get(position);
        holder.bind(model, subscription);
    }

    @Override
    public int getItemCount() {
        return subscription.getMembers().size();
    }

}