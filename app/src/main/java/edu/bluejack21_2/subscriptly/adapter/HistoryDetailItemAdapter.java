package edu.bluejack21_2.subscriptly.adapter;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

import edu.bluejack21_2.subscriptly.R;
import edu.bluejack21_2.subscriptly.models.TransactionDetail;

public class HistoryDetailItemAdapter extends BaseAdapter {
    private ArrayList<TransactionDetail> details;

    public HistoryDetailItemAdapter(ArrayList<TransactionDetail> details) {
        this.details = details;
    }

    @Override
    public int getCount() {
        return details.size();
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
        return null;
    }
}