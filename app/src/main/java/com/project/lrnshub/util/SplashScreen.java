package com.project.lrnshub.util;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.lrnshub.ui.auth.ChooseRegister;
import com.project.lrnshub.MainActivity;
import com.project.lrnshub.R;


public class SplashScreen extends AppCompatActivity {
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }


            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    if(fAuth.getCurrentUser() != null) {
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finishAffinity();

                    } else {
                        Intent intent = new Intent(SplashScreen.this, ChooseRegister.class);
                        SplashScreen.this.startActivity(intent);
                        SplashScreen.this.finish();
                    }
                }
            }, 2000);
    }

}