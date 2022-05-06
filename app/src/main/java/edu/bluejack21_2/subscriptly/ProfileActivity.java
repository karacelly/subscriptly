package edu.bluejack21_2.subscriptly;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import edu.bluejack21_2.subscriptly.models.User;
import edu.bluejack21_2.subscriptly.repositories.UserRepository;

public class ProfileActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView nameTxt, usernameTxt, emailTxt;

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

    public void initComponents() {
        toolbar = findViewById(R.id.toolbar);

        nameTxt = findViewById(R.id.profile_name_txt);
        usernameTxt = findViewById(R.id.profile_uname_txt);
        emailTxt = findViewById(R.id.profile_email_txt);

        User user = UserRepository.LOGGED_IN_USER;
        nameTxt.setText(user.getName());
        usernameTxt.setText(user.getUsername());
        emailTxt.setText(user.getEmail());
    }
}