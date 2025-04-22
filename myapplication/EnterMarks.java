package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EnterMarks extends AppCompatActivity {

    private Spinner spinnerStudents, spinnerSubject;
    private Button btnContinue;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_marks);

        // Initialize UI elements
        spinnerStudents = findViewById(R.id.spinner_students);
        spinnerSubject = findViewById(R.id.spinner_subject);
        btnContinue = findViewById(R.id.btn_continue);

        // Firebase reference to students
        databaseReference = FirebaseDatabase.getInstance().getReference("users/students");

        // Load student usernames from Firebase
        loadStudentUsernames();

        // Load subjects into the spinner
        List<String> subjects = Arrays.asList("C", "C++", "Java", "Relational Management System",
                "Mobile Application Development", "Operating System",
                "Open Source Software", "Python Programming");
        ArrayAdapter<String> subjectAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, subjects);
        spinnerSubject.setAdapter(subjectAdapter);

        // Handle Continue button click
        btnContinue.setOnClickListener(v -> {
            String selectedStudent = spinnerStudents.getSelectedItem().toString();
            String selectedSubject = spinnerSubject.getSelectedItem().toString();

            if (selectedStudent.equals("No students found")) {
                Toast.makeText(EnterMarks.this, "Please select a valid student", Toast.LENGTH_SHORT).show();
                return;
            }

            // Navigate to MarksEntry activity
            Intent intent = new Intent(EnterMarks.this, MarksEntry.class);
            intent.putExtra("selectedStudent", selectedStudent);
            intent.putExtra("selectedSubject", selectedSubject);
            startActivity(intent);
        });
    }

    private void loadStudentUsernames() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> studentList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String username = snapshot.child("username").getValue(String.class);
                    if (username != null) {
                        studentList.add(username);
                    }
                }
                if (studentList.isEmpty()) {
                    studentList.add("No students found");
                }
                ArrayAdapter<String> studentAdapter = new ArrayAdapter<>(EnterMarks.this, android.R.layout.simple_spinner_dropdown_item, studentList);
                spinnerStudents.setAdapter(studentAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(EnterMarks.this, "Failed to load students", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
