package edu.bluejack21_2.subscriptly;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import edu.bluejack21_2.subscriptly.repositories.UserRepository;
import edu.bluejack21_2.subscriptly.utils.Crypt;

public class ChangePasswordActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText currentTxt, newTxt, confirmTxt;
    private Button changeBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        initComponents();
        addListener();
    }

    private void initComponents() {
        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.back_arrow);
        toolbar.getNavigationIcon().mutate().setTint(getResources().getColor(R.color.primary_color));

        currentTxt = findViewById(R.id.curr_pass_txt);
        newTxt = findViewById(R.id.new_pass_txt);
        confirmTxt = findViewById(R.id.confirm_pass_txt);
        changeBtn = findViewById(R.id.action_save_changes);
    }

    public void addListener(){
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        changeBtn.setOnClickListener(v -> {
            String current = currentTxt.getText().toString();
            String newPassword = newTxt.getText().toString();
            String confirmPassword = confirmTxt.getText().toString();

            if(!current.isEmpty() && !newPassword.isEmpty() && !confirmPassword.isEmpty()) {
                if(Crypt.verifyHash(current, UserRepository.LOGGED_IN_USER.getPassword())) {
                    if(newPassword.length() < 8 || newPassword.length() > 30) {
                        Toast.makeText(getApplicationContext(), "Your password must be between 8 and 30 characters", Toast.LENGTH_SHORT).show();
                    }else{
                        if(newPassword.equals(confirmPassword)) {
                            UserRepository.updateUserPassword(UserRepository.LOGGED_IN_USER.getUserID(), newPassword, listener -> {
                                if(listener) {
                                    UserRepository.LOGGED_IN_USER.setPassword(Crypt.generateHash(newPassword));
                                    Toast.makeText(getApplicationContext(), "Change password success!", Toast.LENGTH_SHORT).show();
                                    onBackPressed();
                                }else{
                                    Toast.makeText(getApplicationContext(), "System Error! Try again!", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else{
                            Toast.makeText(getApplicationContext(), "Confirm password must be the same with password", Toast.LENGTH_SHORT).show();
                        }
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "Wrong current password!", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(getApplicationContext(), "Fields cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });
    }
}