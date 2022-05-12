package edu.bluejack21_2.subscriptly;

import static edu.bluejack21_2.subscriptly.utils.Currency.formatToRupiah;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import edu.bluejack21_2.subscriptly.adapter.ChosenUserRecyclerAdapter;
import edu.bluejack21_2.subscriptly.models.Subscription;
import edu.bluejack21_2.subscriptly.models.User;
import edu.bluejack21_2.subscriptly.repositories.ImageRepository;
import edu.bluejack21_2.subscriptly.repositories.SubscriptionRepository;
import edu.bluejack21_2.subscriptly.repositories.UserRepository;
import edu.bluejack21_2.subscriptly.utils.Field;
import edu.bluejack21_2.subscriptly.utils.Image;
import edu.bluejack21_2.subscriptly.utils.RecyclerViewHelper;

public class AddSubscriptionActivity extends AppCompatActivity {

    private Context context;

    // Subscription Image
    private CardView imageToggle;
    private ImageView imageSubscription;

    private EditText subscriptionName, subscriptionBill, timeRange, subscriptionStartDate;
//  LinearLayout layoutFriendList;

    private ImageButton buttonAdd;
    private MaterialButton buttonCreateSubscription;

    private RecyclerView chosenUserRecycler;
    public ChosenUserRecyclerAdapter chosenUserAdapter;
    private String imageURL;

    final Calendar myCalendar= Calendar.getInstance();

    private void updateLabel(){
        String myFormat="MM/dd/yy";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
        subscriptionStartDate.setText(dateFormat.format(myCalendar.getTime()));
    }

    ActivityResultLauncher<Intent> pickImageActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
//                    if(result.getResultCode() == 3) {
//
//                    }
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();

                        if (data != null) {
                            Uri selectedImage = data.getData();
                            imageSubscription.setImageURI(selectedImage);
                            String fileName = Image.getImageFileName(context, selectedImage);
                            ImageRepository.InsertImage("subscription", fileName, selectedImage, listener -> {
                                if(listener != null) {
                                    imageURL = listener;
                                } else {
                                    Toast.makeText(AddSubscriptionActivity.this, "Failed Upload Image!", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }
            });
    private Toolbar toolbar;

    private void initComponents() {
        imageToggle = findViewById(R.id.action_pick_image);
        imageSubscription = findViewById(R.id.image_subscription);

        subscriptionBill = findViewById(R.id.field_subscription_bill);
        subscriptionName = findViewById(R.id.field_subscription_name);
        timeRange = findViewById(R.id.field_subscription_duration);
        subscriptionStartDate = findViewById(R.id.date_subscription_start);

        buttonAdd = findViewById(R.id.action_add_friend);
        buttonCreateSubscription = findViewById(R.id.action_create_subscription);

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH,month);
                myCalendar.set(Calendar.DAY_OF_MONTH,day);
                myCalendar.set(Calendar.HOUR_OF_DAY, 0);
                myCalendar.set(Calendar.MINUTE, 0);
                myCalendar.set(Calendar.SECOND, 0);
                updateLabel();
            }
        };
        subscriptionStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(AddSubscriptionActivity.this, date,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        toolbar = findViewById(R.id.toolbar);
        toolbar.getNavigationIcon().mutate().setTint(getColor(R.color.primary_color));
        toolbar.setNavigationIcon(R.drawable.back_arrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        chosenUserRecycler = findViewById(R.id.recycler_users_chosen);
        chosenUserAdapter = new ChosenUserRecyclerAdapter(this);
        RecyclerViewHelper.setRecyclerView(this, chosenUserAdapter, LinearLayoutManager.HORIZONTAL, chosenUserRecycler);
    }

    private void showChosenFriends() {
        chosenUserAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        showChosenFriends();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_subscription);

        initComponents();

        context = this;

        imageToggle.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            pickImageActivityResultLauncher.launch(intent);
        });

        buttonAdd.setOnClickListener(v -> {
            Intent i = new Intent(this, ChooseFriendActivity.class);
            startActivity(i);
//            addView();
        });

        subscriptionBill.addTextChangedListener(new TextWatcher() {
            private String current = subscriptionBill.getText().toString().trim();

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals(current)) {
                    subscriptionBill.removeTextChangedListener(this);
                    String replace = s.toString().replaceAll("[Rp. ]", "");
                    if (!replace.isEmpty())
                        current = formatToRupiah(Double.parseDouble(replace));
                    else
                        current = "";
                    subscriptionBill.setText(current);
                    subscriptionBill.setSelection(current.length());
                    subscriptionBill.addTextChangedListener(this);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        buttonCreateSubscription.setOnClickListener(v -> {
            Boolean flag = true;
            String name = Field.getContent(subscriptionName.getText());
            String bill = Field.getContent(subscriptionBill.getText());
            String duration = Field.getContent(timeRange.getText());
            String date = Field.getContent(subscriptionStartDate.getText());
            bill = bill.replaceAll("[Rp. ]", "");

            if(name.isEmpty()) {
                subscriptionName.setError(getString(R.string.error_empty_fields));
                flag = false;
            }

            Long billLong = Long.valueOf(0);
            if(bill.isEmpty()) {
                subscriptionBill.setError(getString(R.string.error_empty_fields));
                flag = false;
            } else {
                billLong = Long.parseLong(bill);
                if(billLong < 0) {
                    subscriptionBill.setError(getString(R.string.error_negative_number));
                    flag = false;
                }
            }

            Integer durationInt = 0;
            if(duration.isEmpty()) {
                timeRange.setError(getString(R.string.error_empty_fields));
                flag = false;
            } else {
                durationInt = Integer.parseInt(duration);
                if(durationInt <= 0) {
                    timeRange.setError(getString(R.string.error_zero_or_lower));
                    flag = false;
                }
            }

            if(date.isEmpty()) {
                subscriptionStartDate.setError(getString(R.string.error_empty_fields));
                flag = false;
            }

            if(flag) {
                Timestamp timestamp = new Timestamp(myCalendar.getTime());
                Subscription subscription = new Subscription("", name, imageURL, billLong, durationInt, timestamp, SubscriptionRepository.chosenFriends);
                SubscriptionRepository.isUniqueSubscriptionNameForCreator(UserRepository.LOGGED_IN_USER.getUserID(), name, listener -> {
                    if(listener) {
                        SubscriptionRepository.insertSubscription(subscription, myCalendar, subListener -> {
                            if(subListener) {
                                Toast.makeText(AddSubscriptionActivity.this, "Success Add Subscription", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(this, HomeActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(AddSubscriptionActivity.this, "Failed Add Subscription", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        Log.d("ADD SUBSCRIPTION", "Subscription name already exist in your list!");
                        Toast.makeText(AddSubscriptionActivity.this, "Subscription name already exist in your list!", Toast.LENGTH_SHORT).show();
                    }
                });
            }

        });

    }

//    private void addView() {
//        String friendNameContent = friendName.getText().toString().trim();
//        if (friendNameContent.length() > 0) {
//            final View friendView = getLayoutInflater().inflate(R.layout.row_add_friend, null, false);
//
//            EditText friendNameAppend = friendView.findViewById(R.id.field_name_friend);
//            ImageButton removeFriend = friendView.findViewById(R.id.action_remove_friend);
//
//            friendNameAppend.setText(friendNameContent);
////            removeFriend.setOnClickListener(v -> {
////                removeView(friendView);
////            });
//
//////            layoutFriendList.addView(friendView);
//        }
//        friendName.setText("");
//    }

    private void removeView(View v) {
//        layoutFriendList.removeView(v);
    }


}