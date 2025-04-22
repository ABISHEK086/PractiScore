package com.example.myapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ManageReport extends AppCompatActivity {
    private ListView listView;
    private List<String> reportList;
    private ArrayAdapter<String> adapter;
    private DatabaseReference databaseReference; // Firebase Reference

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_report);

        listView = findViewById(R.id.list_reports);
        reportList = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("marks"); // Reference to marks

        loadReports();

        // Long press to delete
        listView.setOnItemLongClickListener((parent, view, position, id) -> {
            String selectedReport = reportList.get(position);
            showDeleteConfirmationDialog(selectedReport, position);
            return true;
        });
    }

    private void loadReports() {
        File downloadsFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File[] files = downloadsFolder.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.getName().endsWith(".pdf")) {
                    reportList.add(file.getName());
                }
            }
        }

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, reportList);
        listView.setAdapter(adapter);
    }

    private void showDeleteConfirmationDialog(String reportName, int position) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Report")
                .setMessage("Do you want to delete this report?")
                .setPositiveButton("Delete", (dialog, which) -> deleteReport(reportName, position))
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void deleteReport(String reportName, int position) {
        // Delete from Firebase
        databaseReference.child(reportName.replace(".pdf", "")).removeValue()
                .addOnSuccessListener(aVoid -> {
                    // Delete from Local Storage
                    File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), reportName);
                    if (file.exists() && file.delete()) {
                        reportList.remove(position);
                        adapter.notifyDataSetChanged();
                        Toast.makeText(ManageReport.this, "Report deleted", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ManageReport.this, "Failed to delete from storage", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(ManageReport.this, "Failed to delete report", Toast.LENGTH_SHORT).show());
    }
}
