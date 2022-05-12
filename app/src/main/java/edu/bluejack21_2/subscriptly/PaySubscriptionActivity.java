package edu.bluejack21_2.subscriptly;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import java.util.ArrayList;

import edu.bluejack21_2.subscriptly.models.TransactionHeader;
import edu.bluejack21_2.subscriptly.repositories.SubscriptionRepository;
import edu.bluejack21_2.subscriptly.repositories.UserRepository;
import edu.bluejack21_2.subscriptly.utils.DateHelper;
import edu.bluejack21_2.subscriptly.utils.SubscriptionHelper;

public class PaySubscriptionActivity extends AppCompatActivity {

    private Spinner spinnerMonth;

    private void initComponents() {
        spinnerMonth = findViewById(R.id.spinner_month_options);
    }

    private void initListeners(){
        ArrayList<String> months = new ArrayList<>();
        ArrayList<TransactionHeader> availableHeaders = new ArrayList<>();
        for (TransactionHeader header:
                SubscriptionRepository.ACTIVE_SUBSCRIPTION.getHeaders()) {
            if(SubscriptionHelper.getUserPaidDetail(header, UserRepository.LOGGED_IN_USER.getUserID()) == null) {
                months.add(DateHelper.formatDate(header.getBillingDate(), "MMMM, YYYY"));
                availableHeaders.add(header);
            }
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, months);
        spinnerMonth.setAdapter(arrayAdapter);
        spinnerMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("SPINNER", availableHeaders.get(i).getTransactionId().toString());
            }

            @Override
            public void onNothingSelected(AdapterView <?> parent) {
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_subscription);
        initComponents();
        initListeners();
    }
}