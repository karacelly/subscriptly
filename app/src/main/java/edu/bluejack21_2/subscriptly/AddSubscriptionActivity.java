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

import com.google.android.material.button.MaterialButton;

import edu.bluejack21_2.subscriptly.utils.Image;

public class AddSubscriptionActivity extends AppCompatActivity {

    private Toolbar toolbar;
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
    ActivityResultLauncher<Intent> pickImageActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
//                if(result.getResultCode() == 3) {
//
//                }
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();

                        if (data != null) {
                            Uri selectedImage = data.getData();
                            imageSubscription.setImageURI(selectedImage);
                            Log.d("Image URI", selectedImage.toString());
//                          doSomeOperations();
//                          String extension = getMimeType(context, selectedImage);
//                            Log.d("Image Extension", extension);
//                            String uuid = UUID.randomUUID().toString();
//                            Log.d("Image UUID", uuid+"."+extension);
                            String fileName = Image.getImageFileName(context, selectedImage);

//                            FirebaseStorage storage = FirebaseStorage.getInstance();
//                            StorageReference storageRef = storage.getReference();
//                            StorageReference subscriptionStorageRef = storageRef.child("subscriptions/" + selectedImage.getLastPathSegment());
//                            UploadTask uploadTask;
//                            uploadTask = subscriptionStorageRef.putFile(selectedImage);
//
//                            uploadTask.addOnFailureListener(new OnFailureListener() {
//                                @Override
//                                public void onFailure(@NonNull Exception exception) {
//                                    Log.d("Upload", exception.toString());
//                                }
//                            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                                @Override
//                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                                    Log.d("Upload", taskSnapshot.getMetadata().toString());
//                                }
//                            });
                        }
                    }
                }
            });

    private void initComponents() {
        imageToggle = findViewById(R.id.action_pick_image);
        imageSubscription = findViewById(R.id.image_subscription);

        subscriptionBill = findViewById(R.id.field_subscription_bill);

        layoutFriendList = findViewById(R.id.layout_friend_list);
        buttonAdd = findViewById(R.id.action_add_friend);
        friendName = findViewById(R.id.field_name_friend_default);
        buttonCreateSubscription = findViewById(R.id.action_create_subscription);

        toolbar = findViewById(R.id.toolbar);
        toolbar.getNavigationIcon().mutate().setTint(getResources().getColor(R.color.primary_color));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
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
            Intent i = new Intent(AddSubscriptionActivity.this, HomeActivity.class);
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
//                if (!s.toString().equals(current)) {
//                    subscriptionBill.removeTextChangedListener(this);
//
//                    String cleanString = s.toString().replaceAll("[Rp. ]", "");
//                    Log.d("Clean String", cleanString);
//                    Double parsed = Double.parseDouble(cleanString);
//
//                    String formatted = formatRupiah.format(parsed);
//
//                    current = formatted;
//                    Log.d("Formatted String", formatted);
//                    subscriptionBill.setText(formatted);
//                    subscriptionBill.setSelection(formatted.length());
//                    subscriptionBill.addTextChangedListener(this);
//                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        buttonCreateSubscription.setOnClickListener(v -> {
            for (int i = 0; i < layoutFriendList.getChildCount(); i++) {
                if (layoutFriendList.getChildAt(i) instanceof LinearLayout) {
                    LinearLayout row = (LinearLayout) layoutFriendList.getChildAt(i);
                    for (int j = 0; j < row.getChildCount(); j++) {
                        if (row.getChildAt(j) instanceof EditText) {
                            EditText friendName = (EditText) row.getChildAt(j);
                            if (friendName.getId() == R.id.field_name_friend) {
                                Toast.makeText(context, "" + friendName.getText().toString(), Toast.LENGTH_SHORT);
                            }
                            Log.d("FRIEND NAME", friendName.getText().toString());
                        }
                    }
                }
            }
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