package edu.bluejack21_2.subscriptly.adapter;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import  androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;

import edu.bluejack21_2.subscriptly.ui.friends.FriendsFragment;
import edu.bluejack21_2.subscriptly.ui.home.HomeFragment;
import edu.bluejack21_2.subscriptly.ui.subscriptions.SubscriptionsFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {

    private final int NUM_PAGES = 4;
    private final ArrayList<Fragment> fragments = new ArrayList<>();

    public ViewPagerAdapter(FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Log.d("ViewPager Position", position+"");
        if(position == 0)
            return HomeFragment.newInstance();
        else if (position == 1)
            return SubscriptionsFragment.newInstance();
        else if (position == 3 || position == 2)
            return FriendsFragment.newInstance();
        Log.d("ViewPager CreateFragment", "RETURNING NULL");
        return null;
    }

    @Override
    public int getItemCount() {
        return NUM_PAGES;
    }
}
