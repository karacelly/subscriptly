package edu.bluejack21_2.subscriptly;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;

import edu.bluejack21_2.subscriptly.repositories.SubscriptionRepository;
import edu.bluejack21_2.subscriptly.ui.subs_detail.SubscriptionDetailFragment;

public class SubscriptionDetail extends AppCompatActivity {
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription_detail);
        initComponents();
        setFragment();

        toolbar.setNavigationIcon(R.drawable.back_arrow);
        toolbar.getNavigationIcon().mutate().setTint(ContextCompat.getColor(getApplicationContext(), R.color.primary_color));

        toolbar.setNavigationOnClickListener(view -> onBackPressed());
    }

    public void initComponents() {
        toolbar = findViewById(R.id.toolbar);
    }

    private void setFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.subsDetailFragmentPlaceholder, new SubscriptionDetailFragment(SubscriptionRepository.ACTIVE_SUBSCRIPTION));
        ft.commit();
    }
}