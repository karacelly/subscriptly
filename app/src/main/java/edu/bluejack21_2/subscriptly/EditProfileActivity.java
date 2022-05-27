package edu.bluejack21_2.subscriptly;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.google.android.flexbox.FlexboxLayout;
import com.google.firebase.auth.FirebaseAuth;

import edu.bluejack21_2.subscriptly.models.User;
import edu.bluejack21_2.subscriptly.repositories.ImageRepository;
import edu.bluejack21_2.subscriptly.repositories.UserRepository;
import edu.bluejack21_2.subscriptly.utils.Field;
import edu.bluejack21_2.subscriptly.utils.GlobalVariable;
import edu.bluejack21_2.subscriptly.utils.ImageHelper;

public class EditProfileActivity extends AppCompatActivity {

    private FlexboxLayout containerEmailField;
    private Toolbar toolbar;
    private EditText nameTxt, usernameTxt, emailTxt;
    private Button saveChanges;
    private CardView profilePictureCard;
    private ImageView profilePictureImage;
    private String finalFileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        initComponents();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
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

                        if (data != null) {
                            Uri selectedImage = data.getData();
                            profilePictureImage.setImageURI(selectedImage);
                            String fileName = ImageHelper.getImageFileName(getApplicationContext(), selectedImage);
                            ImageRepository.InsertImage(EditProfileActivity.this, "profile", fileName, selectedImage, listener -> {
                                if(listener == null) {
//                                    Toast.makeText(getApplicationContext(), "Upload Image Failed! Try again!", Toast.LENGTH_SHORT).show();
                                } else {
                                    finalFileName = listener;
                                }
                            });
                        }
                    }
                }
            });

    public void initComponents() {

        containerEmailField = findViewById(R.id.container_email_field);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.back_arrow);
        toolbar.getNavigationIcon().mutate().setTint(getResources().getColor(R.color.primary_color));

        nameTxt = findViewById(R.id.profile_name_txt);
        usernameTxt = findViewById(R.id.profile_uname_txt);
        emailTxt = findViewById(R.id.profile_email_txt);
        saveChanges = findViewById(R.id.action_save_changes);
        profilePictureCard = findViewById(R.id.profile_picture_card);
        profilePictureImage = findViewById(R.id.profile_picture);

        if(UserRepository.GOOGLE_LOGGED != null) {
            containerEmailField.setVisibility(View.GONE);
        } else {
            containerEmailField.setVisibility(View.VISIBLE);
        }

        UserRepository.getLoggedInUser(result -> {
            Glide.with(getApplicationContext()).load(result.getImage()).into(profilePictureImage);

            nameTxt.setHint(result.getName());
            nameTxt.setText(result.getName());
            usernameTxt.setHint(result.getUsername());
            usernameTxt.setText(result.getUsername());
            emailTxt.setHint(result.getEmail());
            emailTxt.setText(result.getEmail());

            profilePictureCard.setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                ImageHelper.chooseImageAndUpload(this, profilePictureImage, GlobalVariable.FOLDER_PROFILE, listener -> {
//                    finalFileName = listener;
//                }).launch(intent);
                pickImageActivityResultLauncher.launch(intent);
            });

            saveChanges.setOnClickListener(view -> {
                String name = Field.getContent(nameTxt.getText());
                String username = Field.getContent(usernameTxt.getText());
                String email = Field.getContent(emailTxt.getText());
                String file = finalFileName == null ? result.getImage() : finalFileName;
                if (name.length() > 0 && username.length() > 0 && email.length() > 0) {
                    if (!username.equals(result.getUsername())) {
                        UserRepository.usernameIsUnique(username, usernameListener -> {
                            if (usernameListener) {
                                if (!email.equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())) {
                                    UserRepository.emailIsUnique(email, emailListener -> {
                                        if (emailListener) {
                                            UserRepository.updateUserData(name, username, email, file, updateListener -> {
                                                if (updateListener) {
                                                    result.setName(name);
                                                    result.setUsername(username);
                                                    result.setEmail(email);
                                                    result.setImage(file);
//                                                    Toast.makeText(getApplicationContext(), "Update Profile is Successful", Toast.LENGTH_SHORT).show();
                                                    onBackPressed();
                                                } else {
//                                                    Toast.makeText(getApplicationContext(), "System Error! Try again!", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        } else {
//                                            Toast.makeText(getApplicationContext(), "Email is taken!", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                } else {
                                    UserRepository.updateUserData(name, username, email, file, updateListener -> {
                                        if (updateListener) {
                                            result.setName(name);
                                            result.setUsername(username);
                                            result.setEmail(email);
                                            result.setImage(file);
//                                            Toast.makeText(getApplicationContext(), "Update Profile is Successful", Toast.LENGTH_SHORT).show();
                                            onBackPressed();
                                        } else {
//                                            Toast.makeText(getApplicationContext(), "System Error! Try again!", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            } else {
//                                Toast.makeText(getApplicationContext(), "Username is taken!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else if (!email.equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())) {
                        UserRepository.emailIsUnique(email, emailListener -> {
                            if (emailListener) {
                                UserRepository.updateUserData(name, username, email, file, updateListener -> {
                                    if (updateListener) {
                                        result.setName(name);
                                        result.setUsername(username);
                                        result.setEmail(email);
                                        result.setImage(file);
//                                        Toast.makeText(getApplicationContext(), "Update Profile is Successful", Toast.LENGTH_SHORT).show();
                                        onBackPressed();
                                    } else {
//                                        Toast.makeText(getApplicationContext(), "System Error! Try again!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
//                                Toast.makeText(getApplicationContext(), "Email is taken!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        UserRepository.updateUserData(name, username, email, file, updateListener -> {
                            if (updateListener) {
                                result.setName(name);
                                result.setUsername(username);
                                result.setEmail(email);
                                result.setImage(file);
//                                Toast.makeText(getApplicationContext(), "Update Profile is Successful", Toast.LENGTH_SHORT).show();
                                onBackPressed();
                            } else {
//                                Toast.makeText(getApplicationContext(), "System Error! Try again!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } else {
//                    Toast.makeText(getApplicationContext(), "Fields cannot be empty", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}