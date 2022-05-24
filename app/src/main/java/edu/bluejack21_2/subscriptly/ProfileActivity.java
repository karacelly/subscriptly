package edu.bluejack21_2.subscriptly;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import edu.bluejack21_2.subscriptly.models.User;
import edu.bluejack21_2.subscriptly.repositories.ImageRepository;
import edu.bluejack21_2.subscriptly.repositories.UserRepository;

public class ProfileActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView nameTxt, usernameTxt, emailTxt;
    private Button editProfileBtn, changePasswordBtn;
    private ImageView profilePicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        initComponents();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        UserRepository.getLoggedInUser(user -> {
            nameTxt.setText(user.getName());
            usernameTxt.setText(user.getUsername());
            emailTxt.setText(user.getEmail());
        });
    }

    public void initComponents() {
        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.back_arrow);
        toolbar.getNavigationIcon().mutate().setTint(getResources().getColor(R.color.primary_color));

        nameTxt = findViewById(R.id.profile_name_txt);
        usernameTxt = findViewById(R.id.profile_uname_txt);
        emailTxt = findViewById(R.id.profile_email_txt);
        profilePicture = findViewById(R.id.profile_picture);

        UserRepository.getLoggedInUser(user -> {
            nameTxt.setText(user.getName());
            usernameTxt.setText(user.getUsername());
            emailTxt.setText(user.getEmail());

            Glide.with(getApplicationContext()).load(user.getImage()).into(profilePicture);
        });



        editProfileBtn = findViewById(R.id.profile_edit_button);
        editProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ProfileActivity.this, EditProfileActivity.class);
                startActivity(i);
            }
        });

        changePasswordBtn = findViewById(R.id.change_pass_btn);
        if(UserRepository.GOOGLE_LOGGED != null) {
            changePasswordBtn.setVisibility(View.GONE);
        } else {
            changePasswordBtn.setVisibility(View.VISIBLE);
            changePasswordBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(ProfileActivity.this, ChangePasswordActivity.class);
                    startActivity(i);
                }
            });
        }
    }
}