package edu.bluejack21_2.subscriptly;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

import edu.bluejack21_2.subscriptly.interfaces.QueryFinishListener;
import edu.bluejack21_2.subscriptly.repositories.UserRepository;

public class SignUpActivity extends AppCompatActivity {

    private EditText fieldName, fieldUsername, fieldEmail, fieldPassword, fieldPasswordConfirm;
    private MaterialButton btnSignUp, btnSignInFacebook;
    private TextView redirectLogin;

    private void initComponents() {
        fieldName = findViewById(R.id.field_name);
        fieldUsername = findViewById(R.id.field_username);
        fieldEmail = findViewById(R.id.field_email);
        fieldPassword = findViewById(R.id.field_password);
        fieldPasswordConfirm = findViewById(R.id.field_confirm_password);
        btnSignUp = findViewById(R.id.action_sign_up);
//        btnSignUp.setBackgroundColor(Color.parseColor("#707654"));
//        btnSignInFacebook = findViewById(R.id.btn_sign_in_facebook);
        redirectLogin = findViewById(R.id.redirect_sign_in);
//        callbackManager = CallbackManager.Factory.create();
//        loginManager = LoginManager.getInstance();
    }

    private void checkUserSession() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        if (prefs.contains("userID")) {

            UserRepository.userCheck(prefs.getString("userID", null), (data) -> {
                if (data != null) {
                    UserRepository.LOGGED_IN_USER = data;
                    Intent i = new Intent(this, HomeActivity.class);
                    finish();
                    startActivity(i);
                } else {
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.clear();
                    editor.commit();
                }
            });

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        checkUserSession();
        initComponents();

        redirectLogin.setOnClickListener(v -> {
            Intent i = new Intent(SignUpActivity.this, MainActivity.class);
            startActivity(i);
        });

        btnSignUp.setOnClickListener(v -> {
            String name, username, email, password, passwordConfirm;
            name = fieldName.getText().toString();
            username = fieldUsername.getText().toString();
            email = fieldEmail.getText().toString();
            password = fieldPassword.getText().toString();
            passwordConfirm = fieldPasswordConfirm.getText().toString();

            boolean flag = false;
            if(name.isEmpty()) {
                fieldName.setError("Name cannot be empty!");
                flag = true;
            } else if(name.length() < 3) {
                fieldName.setError("Name must be at least 3 characters long");
                flag = true;
            }

            if(username.isEmpty()) {
                fieldUsername.setError("Username cannot be empty!");
                flag = true;
            } else if(username.length() < 3) {
                fieldUsername.setError("Username must be at least 3 characters long");
                flag = true;
            }

            if(email.isEmpty()) {
                fieldEmail.setError("Email cannot be empty!");
                flag = true;
            } else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                fieldEmail.setError("Email must be in a correct format!");
                flag = true;
            }

            if(password.isEmpty()) {
                fieldPassword.setError("Password cannot be empty");
                flag = true;
            }
            else if(password.length() < 8 || password.length() > 30) {
                fieldPassword.setError("Your password must be between 8 and 30 characters");
                flag = true;
            }

            if(!password.equals(passwordConfirm)) {
                fieldPasswordConfirm.setError("Password didn't match!");
                flag = true;
            }

            //IF VALID
            if(!flag) {
                UserRepository.emailIsUnique(email, emailListener -> {
                    if(emailListener) {
                        UserRepository.usernameIsUnique(username, usernameListener -> {
                            if(usernameListener) {
//                                UserRepository.insertUser(name, username, email, password);
                                UserRepository.signUp(name, username, email, password, listener -> {
                                    Intent i = new Intent(SignUpActivity.this, HomeActivity.class);
                                    startActivity(i);
                                    finish();
                                });
                            } else {
                                fieldUsername.setError("Username has been taken!");
                            }
                        });
                    }else{
                        fieldEmail.setError("Email has been taken!");
                    }
                });
            }
        });
    }

}