package com.project.lrnshub.ui.auth;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.lrnshub.AdminActivity;
import com.project.lrnshub.MainActivity;
import com.project.lrnshub.databinding.ActivityLoginBinding;
import com.project.lrnshub.models.Users;
import com.project.lrnshub.preference.UserPreference;

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

        Users userLogin = new UserPreference(Login.this).getUser();
        if (userLogin != null) {
            switch (userLogin.getUserType()) {
                case 1:
                    startActivity(new Intent(Login.this, AdminActivity.class));
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

        binding.backButton.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), ChooseRegister.class);
            startActivity(intent);
            finishAffinity();
        });

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        if (fAuth.getCurrentUser() != null) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finishAffinity();
        }

        binding.loginButton.setOnClickListener(v -> {
            String email = binding.emailAddress.getText().toString();
            String password = binding.password.getText().toString();


            if (TextUtils.isEmpty(email)) {
                binding.emailAddress.setError("Email is Required.");
                return;
            }

            if (TextUtils.isEmpty(password)) {
                binding.password.setError("Password is Required.");
                return;
            }

            if (email.equals("defaultadmin") && password.equals("superadmin")) {
                Toast.makeText(Login.this, "Login Successfully as Admin", Toast.LENGTH_SHORT).show();
                Users users = new Users();
                users.setEmail(email);
                users.setPassword(password);
                users.setUserType(1);
                new UserPreference(Login.this).saveLogin(users);
                startActivity(new Intent(Login.this, AdminActivity.class));
                finish();
                return;
            }

            if (password.length() < 6) {
                binding.password.setError("Password Must be >= 6 Characters");
                return;
            }

            binding.progressBar.setVisibility(View.VISIBLE);


            // authenticate the user

            fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    String userID = fAuth.getCurrentUser().getUid();
                    Toast.makeText(Login.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                    DocumentReference documentReference = fStore.collection("users").document(userID);
                    documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @SuppressLint("CheckResult")
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            Log.e("IDSS", documentSnapshot.getId());
                            Users users = new Users();
                            users.setDocID(documentSnapshot.getId());
                            users.setEmail(email);
                            users.setPassword(password);
                            users.setUserType(2);
                            new UserPreference(Login.this).saveLogin(users);
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            finishAffinity();
                            finish();
                        }
                    });
                } else {
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