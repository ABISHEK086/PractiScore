package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;  
import android.util.Log;  
import android.widget.Button; 
import android.widget.EditText;
import android.widget.Toast;   
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull; 

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginStudent extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private Button loginButton;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login_student);


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.a), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        emailEditText = findViewById(R.id.username); 
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginButton);

        databaseReference = FirebaseDatabase.getInstance().getReference("users/students");


        loginButton.setOnClickListener(v -> {
            String enteredEmail = emailEditText.getText().toString().trim();
            String enteredPassword = passwordEditText.getText().toString().trim();

            if (enteredEmail.isEmpty() || enteredPassword.isEmpty()) {
                Toast.makeText(LoginStudent.this, "Please enter both username and password", Toast.LENGTH_SHORT).show();
            } else {
                authenticateAdmin(enteredEmail, enteredPassword);
            }
        });
    }

    private void authenticateAdmin(String enteredEmail, String enteredPassword) {
        databaseReference.orderByChild("username").equalTo(enteredEmail).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot adminSnapshot : snapshot.getChildren()) {
                        String storedPassword = adminSnapshot.child("password").getValue(String.class);
                        if (storedPassword != null && storedPassword.equals(enteredPassword)) {
                            Toast.makeText(LoginStudent.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginStudent.this, StudentDashboard.class));
                            finish();
                            return;
                        }
                    }
                    Toast.makeText(LoginStudent.this, "Invalid password", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginStudent.this, "Student not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(LoginStudent.this, "Database Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("FirebaseError", error.getMessage());
            }
        });
    }
}
