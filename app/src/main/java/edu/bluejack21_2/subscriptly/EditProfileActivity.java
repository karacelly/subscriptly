package edu.bluejack21_2.subscriptly;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import edu.bluejack21_2.subscriptly.models.User;
import edu.bluejack21_2.subscriptly.repositories.ImageRepository;
import edu.bluejack21_2.subscriptly.repositories.UserRepository;
import edu.bluejack21_2.subscriptly.utils.Field;
import edu.bluejack21_2.subscriptly.utils.Image;

public class EditProfileActivity extends AppCompatActivity {
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
                            String fileName = Image.getImageFileName(getApplicationContext(), selectedImage);
                            ImageRepository.InsertImage("profile", fileName, selectedImage, listener -> {
                                if(!listener) {
                                    Toast.makeText(getApplicationContext(), "Upload Image Failed! Try again!", Toast.LENGTH_SHORT).show();
                                } else {
                                    finalFileName = fileName;
                                }
                            });
                        }
                    }
                }
            });

    public void initComponents() {
        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.back_arrow);
        toolbar.getNavigationIcon().mutate().setTint(getResources().getColor(R.color.primary_color));

        nameTxt = findViewById(R.id.profile_name_txt);
        usernameTxt = findViewById(R.id.profile_uname_txt);
        emailTxt = findViewById(R.id.profile_email_txt);
        saveChanges = findViewById(R.id.action_save_changes);
        profilePictureCard = findViewById(R.id.profile_picture_card);
        profilePictureImage = findViewById(R.id.profile_picture);
        ImageRepository.storageRef.child(UserRepository.LOGGED_IN_USER.getImage()).getDownloadUrl().addOnSuccessListener(uri -> {
            Glide.with(getApplicationContext()).load(uri.toString()).into(profilePictureImage);
        }).addOnFailureListener(e -> {
           Toast.makeText(getApplicationContext(), "Failed Getting Profile Picture", Toast.LENGTH_SHORT).show();
        });


        User user = UserRepository.LOGGED_IN_USER;
        nameTxt.setHint(user.getName());
        nameTxt.setText(user.getName());
        usernameTxt.setHint(user.getUsername());
        usernameTxt.setText(user.getUsername());
        emailTxt.setHint(user.getEmail());
        emailTxt.setText(user.getEmail());

        profilePictureCard.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            pickImageActivityResultLauncher.launch(intent);
        });

        saveChanges.setOnClickListener(view -> {
            String name = Field.getContent(nameTxt.getText());
            String username = Field.getContent(usernameTxt.getText());
            String email = Field.getContent(emailTxt.getText());
            String file = finalFileName == null ? UserRepository.LOGGED_IN_USER.getImage() : finalFileName;
            if(name.length() > 0 && username.length() > 0 && email.length() > 0) {
                if(!username.equals(UserRepository.LOGGED_IN_USER.getUsername())) {
                    UserRepository.usernameIsUnique(username, usernameListener -> {
                        if(usernameListener) {
                            if (!email.equals(UserRepository.LOGGED_IN_USER.getEmail())) {
                                UserRepository.emailIsUnique(email, emailListener -> {
                                    if (emailListener) {
                                        UserRepository.updateUserData(UserRepository.LOGGED_IN_USER.getUserID(), name, username, email, file, updateListener -> {
                                            if(updateListener) {
                                                UserRepository.LOGGED_IN_USER.setName(name);
                                                UserRepository.LOGGED_IN_USER.setUsername(username);
                                                UserRepository.LOGGED_IN_USER.setEmail(email);
                                                UserRepository.LOGGED_IN_USER.setImage(file);
                                                Toast.makeText(getApplicationContext(), "Update Profile is Successful", Toast.LENGTH_SHORT).show();
                                                onBackPressed();
                                            } else {
                                                Toast.makeText(getApplicationContext(), "System Error! Try again!", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Email is taken!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }  else {
                                UserRepository.updateUserData(UserRepository.LOGGED_IN_USER.getUserID(), name, username, email, file, updateListener -> {
                                    if(updateListener) {
                                        UserRepository.LOGGED_IN_USER.setName(name);
                                        UserRepository.LOGGED_IN_USER.setUsername(username);
                                        UserRepository.LOGGED_IN_USER.setEmail(email);
                                        UserRepository.LOGGED_IN_USER.setImage(file);
                                        Toast.makeText(getApplicationContext(), "Update Profile is Successful", Toast.LENGTH_SHORT).show();
                                        onBackPressed();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "System Error! Try again!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Username is taken!", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else if (!email.equals(UserRepository.LOGGED_IN_USER.getEmail())) {
                    UserRepository.emailIsUnique(email, emailListener -> {
                        if (emailListener) {
                            UserRepository.updateUserData(UserRepository.LOGGED_IN_USER.getUserID(), name, username, email, file, updateListener -> {
                                if(updateListener) {
                                    UserRepository.LOGGED_IN_USER.setName(name);
                                    UserRepository.LOGGED_IN_USER.setUsername(username);
                                    UserRepository.LOGGED_IN_USER.setEmail(email);
                                    UserRepository.LOGGED_IN_USER.setImage(file);
                                    Toast.makeText(getApplicationContext(), "Update Profile is Successful", Toast.LENGTH_SHORT).show();
                                    onBackPressed();
                                } else {
                                    Toast.makeText(getApplicationContext(), "System Error! Try again!", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            Toast.makeText(getApplicationContext(), "Email is taken!", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    UserRepository.updateUserData(UserRepository.LOGGED_IN_USER.getUserID(), name, username, email, file, updateListener -> {
                        if(updateListener) {
                            UserRepository.LOGGED_IN_USER.setName(name);
                            UserRepository.LOGGED_IN_USER.setUsername(username);
                            UserRepository.LOGGED_IN_USER.setEmail(email);
                            UserRepository.LOGGED_IN_USER.setImage(file);
                            Toast.makeText(getApplicationContext(), "Update Profile is Successful", Toast.LENGTH_SHORT).show();
                            onBackPressed();
                        } else {
                            Toast.makeText(getApplicationContext(), "System Error! Try again!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            } else {
                Toast.makeText(getApplicationContext(), "Fields cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });
    }
}