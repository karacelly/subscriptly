package edu.bluejack21_2.subscriptly;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;

import edu.bluejack21_2.subscriptly.ui.subs_detail.SubscriptionDetailFragment;

public class SubscriptionDetail extends AppCompatActivity {
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription_detail);
        initComponents();
        setFragment();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                onBackPressed();
            }
        });
    }

    public void initComponents(){
        toolbar = findViewById(R.id.toolbar);
    }

    private void setFragment(){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.subsDetailFragmentPlaceholder, new SubscriptionDetailFragment());
        ft.commit();
    }
}