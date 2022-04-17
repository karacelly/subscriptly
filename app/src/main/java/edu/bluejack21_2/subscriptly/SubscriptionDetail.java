package edu.bluejack21_2.subscriptly;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;

import edu.bluejack21_2.subscriptly.database.SubscriptlyDB;
import edu.bluejack21_2.subscriptly.models.Subscription;
import edu.bluejack21_2.subscriptly.models.User;
import edu.bluejack21_2.subscriptly.repositories.SubscriptionRepository;
import edu.bluejack21_2.subscriptly.ui.subs_detail.SubscriptionDetailFragment;

public class SubscriptionDetail extends AppCompatActivity {
    private Toolbar toolbar;
    private Subscription subscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription_detail);
        initComponents();
        setFragment();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void initComponents(){
        toolbar = findViewById(R.id.toolbar);
    }

    private void setFragment(){
        Intent intent = getIntent();

        DocumentReference findSubscription = SubscriptlyDB.getDB().collection("subscriptions").document(intent.getStringExtra("subscriptionID"));

        findSubscription.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        Subscription s = new Subscription(document.getId(), document.getString("name"), Integer.parseInt(document.get("bill").toString()), Integer.parseInt(document.get("duration").toString()), new ArrayList<User>());
                        ft.replace(R.id.subsDetailFragmentPlaceholder, new SubscriptionDetailFragment(SubscriptionRepository.ACTIVE_SUBSCRIPTION));
//                        ft.replace(R.id.subsDetailFragmentPlaceholder, new SubscriptionDetailFragment(s));
                        ft.commit();
                    }
                }
            }
        });
    }
}