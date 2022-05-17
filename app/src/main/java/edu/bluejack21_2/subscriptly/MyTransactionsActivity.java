package edu.bluejack21_2.subscriptly;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

import edu.bluejack21_2.subscriptly.adapter.TransactionAdapter;
import edu.bluejack21_2.subscriptly.models.Subscription;
import edu.bluejack21_2.subscriptly.models.TransactionDetail;
import edu.bluejack21_2.subscriptly.models.TransactionHeader;
import edu.bluejack21_2.subscriptly.repositories.SubscriptionRepository;

public class MyTransactionsActivity extends AppCompatActivity {
    private String TAG = "MyTransactionsActivity";
    private Toolbar toolbar;
    private Spinner sortSpinner;

//    private static ArrayList<TransactionDetail> details = new ArrayList<>();
    private TransactionAdapter adapter;
    private Boolean sortNewest = true;

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

        ArrayList<String> sortOpts = new ArrayList<>();
        sortOpts.add("Sort by Newest");
        sortOpts.add("Sort by Highest Price");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, sortOpts);
        sortSpinner.setAdapter(arrayAdapter);
        int black = Color.parseColor("#000000");
        sortSpinner.setOutlineAmbientShadowColor(black);
        sortSpinner.setOutlineSpotShadowColor(black);

        ArrayList<TransactionDetail> details = new ArrayList<>();
        RecyclerView rv = findViewById(R.id.my_transaction_recycles);
        rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new TransactionAdapter(this, details);
        rv.setAdapter(adapter);

        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i == 0) sortNewest = true;
                else if(i == 1) sortNewest = false;
                setData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                setData();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setData();
    }

    private void setData(){
        ArrayList<TransactionDetail> details = new ArrayList<>();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        SubscriptionRepository.getUserSubscriptions(user.getUid(), subscriptions -> {
            for(Subscription s  : subscriptions) {
                for (TransactionHeader th : s.getHeaders()) {
                    for(TransactionDetail td : th.getDetails()){
                        if(td.getUser().getUserID().equals(user.getUid())) {
                            td.setSubscription(s);
                            details.add(td);
                        }
                    }
                }
            }

            if(sortNewest){
                details.sort(Comparator.comparing(TransactionDetail::getPaymentDate).reversed());
            }else{
                details.sort(Comparator.comparing(TransactionDetail::getSubscriptionPrice).reversed());
            }

            adapter.setDetails(details);
            adapter.notifyDataSetChanged();
        });
    }

    public void initComponents() {
        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.back_arrow);
        toolbar.getNavigationIcon().mutate().setTint(getResources().getColor(R.color.primary_color));

        sortSpinner = findViewById(R.id.sort_spinner);
    }
}