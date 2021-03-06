package edu.bluejack21_2.subscriptly;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Comparator;

import edu.bluejack21_2.subscriptly.models.TransactionHeader;
import edu.bluejack21_2.subscriptly.repositories.ImageRepository;
import edu.bluejack21_2.subscriptly.repositories.SubscriptionRepository;
import edu.bluejack21_2.subscriptly.utils.DateHelper;
import edu.bluejack21_2.subscriptly.utils.GlobalVariable;
import edu.bluejack21_2.subscriptly.utils.ImageHelper;
import edu.bluejack21_2.subscriptly.utils.SubscriptionHelper;
import edu.bluejack21_2.subscriptly.utils.UserHelper;

public class PaySubscriptionActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private Spinner spinnerMonth;
    private ImageButton receiptImage;
    private Button uploadReceipt;
    private String imageURL;
    private TransactionHeader chosenHeader;

    private final ActivityResultLauncher<Intent> imageActivity = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
//                    if(result.getResultCode() == 3) {
//
//                    }
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();

                    if (data != null) {
                        Uri selectedImage = data.getData();
                        if (receiptImage != null) {
                            receiptImage.setImageURI(selectedImage);
                        }
                        String fileName = ImageHelper.getImageFileName(this, selectedImage);
                        ImageRepository.InsertImage(PaySubscriptionActivity.this, GlobalVariable.FOLDER_RECEIPT, fileName, selectedImage, imageURL -> {
                            this.imageURL = imageURL;
//                            Toast.makeText(this, "Upload Image Failed!", Toast.LENGTH_SHORT).show();
                        });
                    }
                }
            });

    private void initComponents() {
        toolbar = findViewById(R.id.toolbar);
        spinnerMonth = findViewById(R.id.spinner_month_options);
        receiptImage = findViewById(R.id.image_receipt);
        uploadReceipt = findViewById(R.id.action_upload_receipt);

        toolbar.setNavigationIcon(R.drawable.back_arrow);
        toolbar.getNavigationIcon().mutate().setTint(ContextCompat.getColor(getApplicationContext(), R.color.primary_color));

        toolbar.setNavigationOnClickListener(view -> onBackPressed());
    }

    private void initListeners() {
        ArrayList<String> months = new ArrayList<>();
        ArrayList<TransactionHeader> availableHeaders = new ArrayList<>();
        for (TransactionHeader header :
                SubscriptionRepository.ACTIVE_SUBSCRIPTION.getHeaders()) {
            if (SubscriptionHelper.getUserPaidDetail(header, FirebaseAuth.getInstance().getUid()) == null && UserHelper.userAlreadyExist(header.getActiveMembers(), FirebaseAuth.getInstance().getUid())) {
                availableHeaders.add(header);
            }
        }
        availableHeaders.sort(Comparator.comparing(TransactionHeader::getBillingDate).reversed());
        for (TransactionHeader header :
                availableHeaders) {
            months.add(DateHelper.formatDate(header.getBillingDate(), "MMMM, YYYY"));
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, months);
        spinnerMonth.setAdapter(arrayAdapter);
        spinnerMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int black = Color.parseColor("#000000");
                spinnerMonth.setOutlineAmbientShadowColor(black);
                spinnerMonth.setOutlineSpotShadowColor(black);
                chosenHeader = availableHeaders.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        receiptImage.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            imageActivity.launch(intent);
        });

        uploadReceipt.setOnClickListener(view -> {
            upload();
        });
    }

    private void upload() {
        boolean error = false;
        if (chosenHeader == null) {
            error = true;
            int colorRed = Color.parseColor("#FF0000");
            spinnerMonth.setOutlineAmbientShadowColor(colorRed);
            spinnerMonth.setOutlineSpotShadowColor(colorRed);
        }
        if (imageURL == null) {
            error = true;
//            Toast.makeText(this, "Please upload the receipt!", Toast.LENGTH_SHORT).show();
        }

        if (!error) {
            SubscriptionRepository.uploadReceipt(SubscriptionRepository.ACTIVE_SUBSCRIPTION, chosenHeader.getTransactionId(), FirebaseAuth.getInstance().getCurrentUser().getUid(), imageURL, listener -> {
                if (listener) {
                    onBackPressed();
                } else {
//                    Toast.makeText(this, R.string.error_upload, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_subscription);
        initComponents();
        initListeners();
    }
}