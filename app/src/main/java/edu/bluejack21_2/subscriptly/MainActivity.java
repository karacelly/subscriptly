package edu.bluejack21_2.subscriptly;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;

import edu.bluejack21_2.subscriptly.repositories.UserRepository;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "GOOGLE_SIGN_IN_TAG";
    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                Log.d("Google ACTIVITY RESULT", result.getResultCode() + "");
                Log.d("Google ACTIVITY RESULT", result + "");
                Log.d("Google ACTIVITY RESULT", result.getData() + "");
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Log.d(TAG, "onActivityResult: Google signin intent result");

                    Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                    try {
                        GoogleSignInAccount account = task.getResult(ApiException.class);
                        UserRepository.authWithGoogleAccount(MainActivity.this, account, newUser -> {
                            Intent i;
                            if (newUser) {
                                i = new Intent(MainActivity.this, UsernameActivity.class);
                            } else {
//                                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
//                                    SharedPreferences.Editor editor = prefs.edit();
//                                    editor.putString("userID", account.getId());
//
//                                    editor.commit();
                                Log.d(TAG, "existing user, redirecting...");
                                i = new Intent(MainActivity.this, HomeActivity.class);
                            }
                            startActivity(i);
                            finish();
                        });
                    } catch (Exception e) {
                        Log.d(TAG, "onActivityResult: " + e.getMessage());
                    }
                }
            });
    private EditText fieldEmail, fieldPassword;
    private MaterialButton btnSignIn;
    private Button btnSignInWithGoogle;
    private TextView redirectSignUp;
    private GoogleSignInClient mGoogleSignInClient;

    private void initComponents() {
        fieldEmail = findViewById(R.id.field_email);
        fieldPassword = findViewById(R.id.field_password);
        btnSignIn = findViewById(R.id.action_sign_in);
        btnSignInWithGoogle = findViewById(R.id.google_sign_in);
//        btnSignInFacebook = findViewById(R.id.btn_sign_in_facebook);
        redirectSignUp = findViewById(R.id.redirect_sign_up);
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
                    startActivity(i);
                    finish();
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
        setContentView(R.layout.activity_main);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("38668327357-01jd8d4jl1u43tg90u2g6dk88qs85jjd.apps.googleusercontent.com")
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        checkUserSession();
        initComponents();

        redirectSignUp.setOnClickListener(v -> {
            Intent i = new Intent(MainActivity.this, SignUpActivity.class);
            startActivity(i);
        });

        btnSignIn.setOnClickListener(v -> {
            String email, password;
            email = fieldEmail.getText().toString();
            password = fieldPassword.getText().toString();

//            UserRepository.authenticateLogin(email, password, data -> {
//                if(data != null) {
//                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
//                    SharedPreferences.Editor editor = prefs.edit();
//                    editor.putString("userID", data.getUserID());
//
//                    editor.commit();
//
//                    Intent i = new Intent(MainActivity.this, HomeActivity.class);
//                    startActivity(i);
//                    finish();
//                } else
//                    Toast.makeText(this, "Invalid Credentials!", Toast.LENGTH_SHORT).show();
//            });

            UserRepository.signIn(email, password, listener -> {
                if (listener) {
                    Intent i = new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(i);
                    finish();
                } else {
                    Log.d("signIn", "Invalid Credentials!");
                    Toast.makeText(this, "Invalid Credentials!", Toast.LENGTH_SHORT).show();
                }
            });
        });

        btnSignInWithGoogle.setOnClickListener(v -> {
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            activityResultLauncher.launch(signInIntent);
        });
    }
}