package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class LoginStaff extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_staff);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Initialize Views
        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        Button loginButton = findViewById(R.id.loginButton);

        // Login Button Click Listener
        loginButton.setOnClickListener(v -> {
            String enteredEmail = emailEditText.getText().toString().trim();
            String enteredPassword = passwordEditText.getText().toString().trim();

            if (enteredEmail.isEmpty() || enteredPassword.isEmpty()) {
                Toast.makeText(LoginStaff.this, "Please enter both email and password", Toast.LENGTH_SHORT).show();
            } else {
                loginStaff(enteredEmail, enteredPassword);
            }
        });
    }

    private void loginStaff(String enteredEmail, String enteredPassword) {
        mAuth.signInWithEmailAndPassword(enteredEmail, enteredPassword)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            Log.d("LoginDebug", "Login successful for: " + user.getEmail());
                            Toast.makeText(LoginStaff.this, "Login Successful!", Toast.LENGTH_SHORT).show();

                            // Navigate to StaffDashboard
                            Intent intent = new Intent(LoginStaff.this, StaffDashboard.class);
                            startActivity(intent);
                            finish();
                        }
                    } else {
                        Log.e("LoginDebug", "Login failed: " + Objects.requireNonNull(task.getException()).getMessage());
                        Toast.makeText(LoginStaff.this, "Login Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
