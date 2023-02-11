package com.project.lrnshub.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.lrnshub.Landing;
import com.project.lrnshub.databinding.ActivitySignupBinding;
import com.project.lrnshub.models.Users;
import com.project.lrnshub.preference.UserPreference;

import java.util.HashMap;
import java.util.Map;

public class Signup extends AppCompatActivity {
    public static final String TAG = "TAG";
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;

    ActivitySignupBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ChooseRegister.class);
                startActivity(intent);
                finishAffinity();
            }
        });

        binding.signupButton.setOnClickListener(v -> register());

        binding.loginButton.setOnClickListener(v ->
        {
            startActivity(new Intent(getApplicationContext(), Signup.class));
            finishAffinity();
        });

    }

    private void register() {
        final String email = binding.Email.getText().toString();
        final String password = binding.password.getText().toString();
        final String fullName = binding.name.getText().toString();

        if (TextUtils.isEmpty(email)) {
            binding.Email.setError("Email is Required.");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            binding.password.setError("Password is Required.");
            return;
        }

        if (password.length() < 6) {
            binding.password.setError("Password Must be >= 6 Characters");
            return;
        }

        binding.progressBar.setVisibility(View.VISIBLE);

        // register the user in firebase
        fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(Signup.this, "User Created.", Toast.LENGTH_SHORT).show();
                userID = fAuth.getCurrentUser().getUid();
                DocumentReference documentReference = fStore.collection("users").document(userID);
                Map<String, Object> user = new HashMap<>();
                user.put("name", fullName);
                user.put("email", email);
                user.put("password", password);
                user.put("id", "");
                documentReference.set(user).addOnSuccessListener(aVoid ->
                        {
                            Users users = new Users();
                            users.setName(fullName);
                            users.setEmail(email);
                            users.setPassword(password);
                            users.setDocID(userID);
                            new UserPreference(Signup.this).saveLogin(users);
                            Intent intent = new Intent(getApplicationContext(), Landing.class);
                            startActivity(intent);
                            finishAffinity();
                        }
                ).addOnFailureListener(e -> Log.d(TAG, "onFailure: " + e));
            } else {
                Toast.makeText(Signup.this, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                binding.progressBar.setVisibility(View.GONE);
            }
        });
    }

}