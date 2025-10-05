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

public class AddStaff extends AppCompatActivity {

    private EditText emailField, passwordField;
    private Button registerButton;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_staff);

        // Initialize Firebase Authent
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("users").child("staff");

        // Initialize UI elements
        emailField = findViewById(R.id.mail);
        passwordField = findViewById(R.id.password);
        registerButton = findViewById(R.id.loginButton);
        progressBar = new ProgressBar(this);
        progressBar.setVisibility(View.GONE);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerStaff();
            }
        });
    }

    private void registerStaff() {
        String email = emailField.getText().toString().trim();
        String password = passwordField.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(AddStaff.this, "Please enter email and password", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    progressBar.setVisibility(View.GONE);
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            saveStaffToDatabase(user.getUid(), email);
                        }
                    } else {
                        Toast.makeText(AddStaff.this, "Registration Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void saveStaffToDatabase(String userId, String email) {
        Staff staff = new Staff(email, "staff");
        databaseReference.child(userId).setValue(staff)
                .addOnSuccessListener(aVoid -> Toast.makeText(AddStaff.this, "Staff Registered Successfully", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(AddStaff.this, "Database Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    static class Staff {
        public String email, role;

        public Staff(String email, String role) {
            this.email = email;
            this.role = role;
        }
    }
}
