package edu.bluejack21_2.subscriptly;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import edu.bluejack21_2.subscriptly.models.User;
import edu.bluejack21_2.subscriptly.repositories.UserRepository;

public class EditProfileActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private EditText nameTxt, usernameTxt, emailTxt;

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

    public void initComponents() {
        toolbar = findViewById(R.id.toolbar);
        toolbar.getNavigationIcon().mutate().setTint(getResources().getColor(R.color.primary_color));

        nameTxt = findViewById(R.id.profile_name_txt);
        usernameTxt = findViewById(R.id.profile_uname_txt);
        emailTxt = findViewById(R.id.profile_email_txt);

        User user = UserRepository.LOGGED_IN_USER;
        nameTxt.setHint(user.getName());
        usernameTxt.setHint(user.getUsername());
        emailTxt.setHint(user.getEmail());
    }
}