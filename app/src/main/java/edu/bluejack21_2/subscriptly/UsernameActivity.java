package edu.bluejack21_2.subscriptly;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;

import edu.bluejack21_2.subscriptly.repositories.UserRepository;

public class UsernameActivity extends AppCompatActivity {
    private static final String TAG = "GOOGLE_SIGN_IN_TAG";
    private EditText nameTxt, usernameTxt;
    private MaterialButton doneBtn;

    private void initComponents(){
        nameTxt = findViewById(R.id.fill_field_name);
        usernameTxt = findViewById(R.id.fill_field_username);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_username);

        initComponents();

        doneBtn.setOnClickListener(v -> {
            String name = nameTxt.getText().toString();
            String username = usernameTxt.getText().toString();

            if(name.isEmpty()) {
                nameTxt.setError("Name cannot be empty!");
            } else if(name.length() < 3) {
                nameTxt.setError("Name must be at least 3 characters long");
            } else if(username.isEmpty()) {
                usernameTxt.setError("Username cannot be empty!");
            } else if(username.length() < 3) {
                usernameTxt.setError("Username must be at least 3 characters long");
            } else{
                try {
                    GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
                    if(account != null) {

                    }else{
                        Log.d(TAG, "onCreate UsernameActivity: account null");
                    }
                }catch (Exception e) {
                    Log.d(TAG, "onCreate UsernameActivity: "+ e.getMessage());
                }
            }
        });
    }
}