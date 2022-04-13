package edu.bluejack21_2.subscriptly;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import edu.bluejack21_2.subscriptly.adapter.ViewPagerAdapter;
import edu.bluejack21_2.subscriptly.ui.home.HomeFragment;

public class HomeActivity extends AppCompatActivity {
    private BottomNavigationView mBottomNavigation;
    private ViewPager2 viewPager;
    private ViewPagerAdapter mViewPagerAdapter;

    private void initComponents() {
        mBottomNavigation = findViewById(R.id.buttom_navigation);
//        mBottomNavigation.
        viewPager = findViewById(R.id.view_pager);
        mViewPagerAdapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(mViewPagerAdapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

//        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
//        setSupportActionBar(myToolbar);
        initComponents();

        mBottomNavigation.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    viewPager.setCurrentItem(0);
                    break;
                case R.id.navigation_subscriptions:
                    viewPager.setCurrentItem(1);
                    break;
                case R.id.navigation_friends:
                    viewPager.setCurrentItem(2);
                    break;
                case R.id.navigation_subscriptions_add:
                    Intent i = new Intent(HomeActivity.this, AddSubscriptionActivity.class);
                    startActivity(i);
                    break;
            }
            return true;
        });

        loadFragment(new HomeFragment());
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.container_frame_layout, fragment);
            ft.commit();
            return true;
        }
        return false;
    }
}