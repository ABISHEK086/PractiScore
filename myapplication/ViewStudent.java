package com.example.myapplication;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity; 

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewStudent extends AppCompatActivity {

    private ListView studentsListView;
    private ProgressBar progressBar;
    private DatabaseReference databaseReference;
    private ArrayList<String> studentList;
    private ArrayAdapter<String> adapter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_student);

        studentsListView = findViewById(R.id.students_list_view);
        progressBar = findViewById(R.id.progress_bar);

        studentList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, studentList);
        studentsListView.setAdapter(adapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("users").child("students");

        fetchStudents();
    }

    private void fetchStudents() {
        progressBar.setVisibility(ProgressBar.VISIBLE);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                studentList.clear();
                for (DataSnapshot studentSnapshot : snapshot.getChildren()) {
                    String username = studentSnapshot.child("username").getValue(String.class);
                    if (username != null) {
                        studentList.add(username);
                    }
                }
                adapter.notifyDataSetChanged();
                progressBar.setVisibility(ProgressBar.GONE);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                progressBar.setVisibility(ProgressBar.GONE);
                Toast.makeText(ViewStudent.this, "Failed to load students", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
