package edu.bluejack21_2.subscriptly;

import static edu.bluejack21_2.subscriptly.utils.Currency.formatToRupiah;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
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

import edu.bluejack21_2.subscriptly.adapter.ChosenUserRecyclerAdapter;
import edu.bluejack21_2.subscriptly.utils.Image;

public class AddSubscriptionActivity extends AppCompatActivity {

    // Subscription Image
    CardView imageToggle;
    ImageView imageSubscription;
    EditText subscriptionBill;
    // Friend List
    LinearLayout layoutFriendList;
    ImageButton buttonAdd;
    EditText friendName;
    MaterialButton buttonCreateSubscription;
    Context context;
    private RecyclerView chosenUserRecycler;
    public ChosenUserRecyclerAdapter chosenUserAdapter;

    ActivityResultLauncher<Intent> pickImageActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
//                    if(result.getResultCode() == 3) {
//
//                    }
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();

                        if (data != null) {
                            Uri selectedImage = data.getData();
                            imageSubscription.setImageURI(selectedImage);
                            String fileName = Image.getImageFileName(context, selectedImage);
                        }
                    }
                }
            });
    private Toolbar toolbar;

    private void initComponents() {
        imageToggle = findViewById(R.id.action_pick_image);
        imageSubscription = findViewById(R.id.image_subscription);

        subscriptionBill = findViewById(R.id.field_subscription_bill);

        layoutFriendList = findViewById(R.id.layout_friend_list);
        buttonAdd = findViewById(R.id.action_add_friend);
        buttonCreateSubscription = findViewById(R.id.action_create_subscription);
        toolbar = findViewById(R.id.toolbar);
        toolbar.getNavigationIcon().mutate().setTint(getResources().getColor(R.color.primary_color));
        toolbar.setNavigationIcon(R.drawable.back_arrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        chosenUserRecycler = findViewById(R.id.recycler_users_chosen);
        chosenUserAdapter = new ChosenUserRecyclerAdapter(this);
        setRecyclerView(chosenUserAdapter, LinearLayoutManager.HORIZONTAL, chosenUserRecycler);
    }

    private void showChosenFriends() {
        chosenUserAdapter.notifyDataSetChanged();
    }

    private void setRecyclerView(RecyclerView.Adapter adapter, int layout, RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(this, layout, false));
        recyclerView.setAdapter(adapter);
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

//            private String current = "";

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            //            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
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

        });

    }

    private void addView() {
        String friendNameContent = friendName.getText().toString().trim();
        if (friendNameContent.length() > 0) {
            final View friendView = getLayoutInflater().inflate(R.layout.row_add_friend, null, false);

            EditText friendNameAppend = friendView.findViewById(R.id.field_name_friend);
            ImageButton removeFriend = friendView.findViewById(R.id.action_remove_friend);

            friendNameAppend.setText(friendNameContent);
            removeFriend.setOnClickListener(v -> {
                removeView(friendView);
            });

            layoutFriendList.addView(friendView, 0);
//            layoutFriendList.addView(friendView);
        }
        friendName.setText("");
    }

    private void removeView(View v) {
        layoutFriendList.removeView(v);
    }


}