package edu.bluejack21_2.subscriptly.utils;

import android.content.Context;

import androidx.recyclerview.widget.LinearLayoutManager;

public class RecyclerViewHelper {
    public static void setRecyclerView(Context context, androidx.recyclerview.widget.RecyclerView.Adapter adapter, int layout, androidx.recyclerview.widget.RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(context, layout, false));
        recyclerView.setAdapter(adapter);
    }
}
