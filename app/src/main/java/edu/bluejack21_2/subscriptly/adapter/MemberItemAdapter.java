package edu.bluejack21_2.subscriptly.adapter;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Vector;

import edu.bluejack21_2.subscriptly.R;
import edu.bluejack21_2.subscriptly.models.User;

public class MemberItemAdapter extends BaseAdapter {
    private Context ctx;
    private ArrayList<User> members;

    public MemberItemAdapter(Context ctx, ArrayList<User> members) {
        this.ctx = ctx;
        this.members = members;
    }

    @Override
    public int getCount() {
        return members.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(ctx);
        view = inflater.inflate(R.layout.adapter_member_item, null);

        ImageView userImage = view.findViewById(R.id.member_photo);
        TextView userName = view.findViewById(R.id.member_name);

        userName.setText(members.get(i).getUsername());

        return view;
    }
}