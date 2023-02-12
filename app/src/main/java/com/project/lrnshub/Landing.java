package com.project.lrnshub;


import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.project.lrnshub.databinding.ActivityLandingBinding;
import com.project.lrnshub.util.SplashScreen;

public class Landing extends AppCompatActivity {

    ActivityLandingBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLandingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }


        binding.loginBtn.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), SplashScreen.class));
            finishAffinity();
        }
    );
    }

}