package com.example.myapplication; 
 
import android.os.Bundle; 
import android.view.View;  
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar; 
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddStudent extends AppCompatActivity {

    private EditText usernameField, passwordField;
    private Button registerButton;
    private ProgressBar progressBar;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);

        databaseReference = FirebaseDatabase.getInstance().getReference("users").child("students");

    
        usernameField = findViewById(R.id.username);
        passwordField = findViewById(R.id.password);
        registerButton = findViewById(R.id.loginButton);
        progressBar = new ProgressBar(this);
        progressBar.setVisibility(View.GONE);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerStudent();
            }
        });
    }

    private void registerStudent() {
        String username = usernameField.getText().toString().trim();
        String password = passwordField.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(AddStudent.this, "Please enter username and password", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        String studentId = databaseReference.push().getKey();
        Student student = new Student(username, password);

        databaseReference.child(studentId).setValue(student)
                .addOnSuccessListener(aVoid -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(AddStudent.this, "Student Registered Successfully", Toast.LENGTH_SHORT).show();
                    usernameField.setText("");
                    passwordField.setText("");
                })
                .addOnFailureListener(e -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(AddStudent.this, "Database Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    static class Student {
        public String username, password;

        public Student(String username, String password) {
            this.username = username;
            this.password = password;
        }
    }
}
