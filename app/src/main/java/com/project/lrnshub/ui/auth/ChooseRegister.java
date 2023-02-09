package com.project.lrnshub.ui.auth;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.project.lrnshub.AdminActivity;
import com.project.lrnshub.MainActivity;
import com.project.lrnshub.R;
import com.project.lrnshub.models.Users;
import com.project.lrnshub.preference.UserPreference;

public class ChooseRegister extends AppCompatActivity {
    Button loginButton, signupButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_signup);

        loginButton = findViewById(R.id.login_button);
        signupButton = findViewById(R.id.signup_button);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        Users userLogin = new UserPreference(ChooseRegister.this).getUser();
        if (userLogin != null) {
            switch (userLogin.getUserType()) {
                case 1:
                    startActivity(new Intent(ChooseRegister.this, AdminActivity.class));
                    finish();
                    return;
                case 2:
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finishAffinity();
                    finish();
                    return;
                default:
                    break;

            }
        }

        loginButton.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), Login.class));
        });

        signupButton.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), Signup.class));
        });
    }

}