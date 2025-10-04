package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class AddAdmin extends AppCompatActivity {

    private EditText emailField, passwordField;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_admin);

        // Initialize Firebase Authent
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("users").child("admins");

        // Initialize UI elements
        emailField = findViewById(R.id.email);
        passwordField = findViewById(R.id.password);
        Button registerButton = findViewById(R.id.loginButton);
        progressBar = new ProgressBar(this);
        progressBar.setVisibility(View.GONE);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerAdmin();
            }
        });
    }

    private void registerAdmin() {
        String email = emailField.getText().toString().trim();
        String password = passwordField.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(AddAdmin.this, "Please enter email and password", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    progressBar.setVisibility(View.GONE);
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            saveAdminToDatabase(user.getUid(), email);
                        }
                    } else {
                        Toast.makeText(AddAdmin.this, "Registration Failed: " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void saveAdminToDatabase(String userId, String email) {
        Admin admin = new Admin(email, "admin");
        databaseReference.child(userId).setValue(admin)
                .addOnSuccessListener(aVoid -> Toast.makeText(AddAdmin.this, "Admin Registered Successfully", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(AddAdmin.this, "Database Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    static class Admin {
        public String email, role;

        public Admin(String email, String role) {
            this.email = email;
            this.role = role;
        }
    }
}
