package com.project.lrnshub.ui.auth;

import android.content.Intent;
import android.os.Bundle;

import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.project.lrnshub.R;

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

        loginButton.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), Login.class));
        });

        signupButton.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), Signup.class));
        });
    }

}