package edu.bluejack21_2.subscriptly;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

import edu.bluejack21_2.subscriptly.interfaces.QueryFinishListener;
import edu.bluejack21_2.subscriptly.models.User;
import edu.bluejack21_2.subscriptly.repositories.UserRepository;

public class MainActivity extends AppCompatActivity implements QueryFinishListener<User> {

    private EditText fieldEmail, fieldPassword;
    private MaterialButton btnSignIn;
    private TextView redirectSignUp;

    private void initComponents() {
        fieldEmail = findViewById(R.id.field_email);
        fieldPassword = findViewById(R.id.field_password);
        btnSignIn = findViewById(R.id.action_sign_in);
//        btnSignInFacebook = findViewById(R.id.btn_sign_in_facebook);
        redirectSignUp = findViewById(R.id.redirect_sign_up);
//        callbackManager = CallbackManager.Factory.create();
//        loginManager = LoginManager.getInstance();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initComponents();

        redirectSignUp.setOnClickListener(v -> {
            Intent i = new Intent(MainActivity.this, SignUpActivity.class);
            startActivity(i);
        });

        btnSignIn.setOnClickListener(v -> {
            String email, password;
            email = fieldEmail.getText().toString();
            password = fieldPassword.getText().toString();

            UserRepository.authenticateLogin(email, password, this);

        });
    }

    @Override
    public void onFinish(User data) {
        if(data != null) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("userID", data.getUserID());
//            editor.putString("email", data.getEmail());
//            editor.putString("name", data.getName());
//            editor.putString("username", data.getUsername());
            editor.commit();

            Intent i = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(i);
            finish();
        } else
            Toast.makeText(this, "Invalid Credentials!", Toast.LENGTH_SHORT).show();
    }
}