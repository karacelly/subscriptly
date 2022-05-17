package edu.bluejack21_2.subscriptly;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

import edu.bluejack21_2.subscriptly.adapter.TransactionAdapter;
import edu.bluejack21_2.subscriptly.models.Subscription;
import edu.bluejack21_2.subscriptly.models.TransactionDetail;
import edu.bluejack21_2.subscriptly.models.TransactionHeader;
import edu.bluejack21_2.subscriptly.repositories.SubscriptionRepository;

public class MyTransactionsActivity extends AppCompatActivity {
    private String TAG = "MyTransactionsActivity";
    private Toolbar toolbar;
    private ArrayList<Subscription> subscriptionDetails = new ArrayList<>();
    private ArrayList<TransactionDetail> details = new ArrayList<>();
    private TransactionAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_transactions);
        initComponents();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        RecyclerView rv = findViewById(R.id.my_transaction_recycles);
        rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new TransactionAdapter(this, subscriptionDetails, details);
        rv.setAdapter(adapter);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        SubscriptionRepository.getUserSubscriptions(user.getUid(), subscriptions -> {
            for(Subscription s  : subscriptions) {
                for (TransactionHeader th : s.getHeaders()) {
                    for(TransactionDetail td : th.getDetails()){
                        if(td.getUser().getUserID().equals(user.getUid())) {
                            details.add(td);
                            subscriptionDetails.add(s);
                        }
                    }
                }
            }
            adapter.setDetails(details);
            adapter.setSubscriptions(subscriptionDetails);
            adapter.notifyDataSetChanged();
        });

    }

    public void initComponents() {
        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.back_arrow);
        toolbar.getNavigationIcon().mutate().setTint(getResources().getColor(R.color.primary_color));
    }
}