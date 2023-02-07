package com.project.lrnshub.ui.auth;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.lrnshub.MainActivity;
import com.project.lrnshub.databinding.ActivityLoginBinding;

public class Login extends AppCompatActivity {
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();


        binding.backButton.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), ChooseRegister.class);
            startActivity(intent);
            finishAffinity();
        });

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        if(fAuth.getCurrentUser() != null) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finishAffinity();
        }

        binding.loginButton.setOnClickListener( v -> {
            String email = binding.emailAddress.getText().toString();
            String password = binding.password.getText().toString();
            if(TextUtils.isEmpty(email)){
                binding.emailAddress.setError("Email is Required.");
                return;
            }

            if(TextUtils.isEmpty(password)){
                binding.password.setError("Password is Required.");
                return;
            }

            if(password.length() < 6){
                binding.password.setError("Password Must be >= 6 Characters");
                return;
            }

            binding.progressBar.setVisibility(View.VISIBLE);

            // authenticate the user

            fAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    String userID = fAuth.getCurrentUser().getUid();
                    Toast.makeText(Login.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                    DocumentReference documentReference = fStore.collection("users").document(userID);
                    documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @SuppressLint("CheckResult")
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            finishAffinity();
                        }
                    });
                }else {
                    Toast.makeText(Login.this, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    binding.progressBar.setVisibility(View.GONE);
                }


            });

        });

        binding.signupButton.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), Signup.class));
            finishAffinity();
        });
    }

}