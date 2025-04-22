package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AdminDashboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_dashboard);

        // Handling window insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.bill), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Finding CardViews
        CardView addAdminCard = findViewById(R.id.addAdminCard);
        CardView addStaffCard = findViewById(R.id.addStaffCard);
        CardView viewAdminCard = findViewById(R.id.viewAdminCard);
        CardView viewStaffCard = findViewById(R.id.viewStaffCard);
        CardView manageAdminCard = findViewById(R.id.manageAdminCard);
        CardView manageStaffCard = findViewById(R.id.manageStaffCard);
        CardView manageReportCard = findViewById(R.id.manageReportCard); // New CardView for Manage Report

        // Setting Click Listeners
        setCardClickListener(addAdminCard, AddAdmin.class);
        setCardClickListener(addStaffCard, AddStaff.class);
        setCardClickListener(viewAdminCard, ViewAdmin.class);
        setCardClickListener(viewStaffCard, ViewStaff.class);
        setCardClickListener(manageAdminCard, ManageAdmin.class);
        setCardClickListener(manageStaffCard, ManageStaff.class);
        setCardClickListener(manageReportCard, ManageReport.class); // Click listener for Manage Report
    }

    // Method to set click listener for CardViews
    private void setCardClickListener(CardView cardView, Class<?> activityClass) {
        if (cardView != null) {
            cardView.setOnClickListener(view -> openActivity(activityClass));
        }
    }

    // Method to Open Activity
    private void openActivity(Class<?> activityClass) {
        Intent intent = new Intent(AdminDashboard.this, activityClass);
        startActivity(intent);
    }
}
