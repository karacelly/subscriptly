package edu.bluejack21_2.subscriptly;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;

import edu.bluejack21_2.subscriptly.interfaces.QueryFinishListener;
import edu.bluejack21_2.subscriptly.models.User;
import edu.bluejack21_2.subscriptly.repositories.UserRepository;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "GOOGLE_SIGN_IN_TAG";

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
                .requestIdToken(getString(R.string.default_web_client_id))
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

            UserRepository.authenticateLogin(email, password, data -> {
                if(data != null) {
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("userID", data.getUserID());

                    editor.commit();

                    Intent i = new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(i);
                    finish();
                } else
                    Toast.makeText(this, "Invalid Credentials!", Toast.LENGTH_SHORT).show();
            });
        });

        btnSignInWithGoogle.setOnClickListener(v -> {
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            activityResultLauncher.launch(signInIntent);
        });
    }

    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Log.d(TAG, "onActivityResult: Google signin intent result");

                        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                        try {
                            GoogleSignInAccount account = task.getResult(ApiException.class);
                            UserRepository.authWithGoogleAccount(account, newUser -> {
                                if(newUser) {
                                    Intent i = new Intent(MainActivity.this, UsernameActivity.class);
                                    startActivity(i);
                                    finish();
                                }else{
//                                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
//                                    SharedPreferences.Editor editor = prefs.edit();
//                                    editor.putString("userID", account.getId());
//
//                                    editor.commit();


                                    Intent i = new Intent(MainActivity.this, HomeActivity.class);
                                    startActivity(i);
                                    finish();
                                }
                            });
                        }catch (Exception e) {
                            Log.d(TAG, "onActivityResult: "+ e.getMessage());
                        }
                    }
                }
            });
}