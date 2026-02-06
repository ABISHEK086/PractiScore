package com.example.myapplication;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View; 
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView; 
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ViewMarks extends AppCompatActivity {

    private TableLayout marksTable;
    private DatabaseReference databaseReference;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_marks);

        marksTable = findViewById(R.id.marksTable);
        databaseReference = FirebaseDatabase.getInstance().getReference("marks");

        fetchAllStudentMarks();
    } 

    private void fetchAllStudentMarks() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    Toast.makeText(ViewMarks.this, "No data found", Toast.LENGTH_SHORT).show();
                    return;
                }

                marksTable.removeAllViews(); // Clear previous data

                // Loop through each student
                for (DataSnapshot studentSnapshot : snapshot.getChildren()) {
                    String studentId = studentSnapshot.getKey();

                    // Add Student ID as Header
                    TableRow studentRow = new TableRow(ViewMarks.this);
                    TextView studentHeader = new TextView(ViewMarks.this);
                    studentHeader.setText("Student ID: " + studentId);
                    studentHeader.setPadding(20, 20, 20, 20);
                    studentHeader.setTextSize(18);
                    studentHeader.setTextColor(getResources().getColor(android.R.color.black));
                    studentRow.addView(studentHeader);
                    marksTable.addView(studentRow);

                    // Add Table Header
                    TableRow headerRow = new TableRow(ViewMarks.this);
                    addTableCell(headerRow, "Subject", true);
                    addTableCell(headerRow, "Observation", true);
                    addTableCell(headerRow, "Verification", true);
                    addTableCell(headerRow, "Viva", true);
                    addTableCell(headerRow, "Total", true);
                    marksTable.addView(headerRow); 

                    // Fetch all subjects for this student
                    for (DataSnapshot subjectSnapshot : studentSnapshot.getChildren()) {
                        String subject = subjectSnapshot.getKey();
                        String observation = getValue(subjectSnapshot, "observation");
                        String verification = getValue(subjectSnapshot, "verification");
                        String viva = getValue(subjectSnapshot, "viva");
                        String total = getValue(subjectSnapshot, "total");

                        // Add Row for Each Subject
                        TableRow row = new TableRow(ViewMarks.this);
                        addTableCell(row, subject, false);
                        addTableCell(row, observation, false);
                        addTableCell(row, verification, false);
                        addTableCell(row, viva, false);
                        addTableCell(row, total, false);
                        marksTable.addView(row);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ViewMarks.this, "Failed to load marks", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addTableCell(TableRow row, String text, boolean isHeader) {
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setPadding(20, 10, 20, 10);
        textView.setTextSize(isHeader ? 16 : 14);
        textView.setTextColor(getResources().getColor(android.R.color.black));
        row.addView(textView);
    }

    private String getValue(DataSnapshot snapshot, String key) {
        return snapshot.child(key).exists() ? snapshot.child(key).getValue().toString() : "N/A";
    }
}
