package edu.bluejack21_2.subscriptly;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class AddSubscriptionActivity extends AppCompatActivity {

    CardView imageToggle;
    ImageView imageSubscription;

    private void initComponents() {
        imageToggle = findViewById(R.id.action_pick_image);
        imageSubscription = findViewById(R.id.image_subscription);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_subscription);

        initComponents();

        imageToggle.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            pickImageActivityResultLauncher.launch(intent);
        });
    }

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

                    if(data != null) {
                        Uri selectedImage = data.getData();
                        imageSubscription.setImageURI(selectedImage);
                        Log.d("Image URI", selectedImage.toString());
//                      doSomeOperations();
                        FirebaseStorage storage = FirebaseStorage.getInstance();
                        StorageReference storageRef = storage.getReference();
                        StorageReference subscriptionStorageRef = storageRef.child("subscriptions/"+selectedImage.getLastPathSegment());
                        UploadTask uploadTask;
                        uploadTask = subscriptionStorageRef.putFile(selectedImage);

// Register observers to listen for when the download is done or if it fails
                        uploadTask.addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle unsuccessful uploads
                                Log.d("Upload", exception.toString());
                            }
                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                                // ...
                                Log.d("Upload", taskSnapshot.getMetadata().toString());
                            }
                        });
                    }
                }
            }
    });
}