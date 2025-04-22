package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationView;

public class StaffDashboard extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_dashboard);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);

        // Ensure menu items load


        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Handle navigation menu clicks
        navigationView.setNavigationItemSelectedListener(this::onNavigationItemSelected);

        // Handle CardView clicks (NO CHANGES HERE)
        findViewById(R.id.add_student_card).setOnClickListener(v -> startActivity(new Intent(this, AddStudent.class)));
        findViewById(R.id.view_students_card).setOnClickListener(v -> startActivity(new Intent(this, ViewStudent.class)));
        findViewById(R.id.remove_student_card).setOnClickListener(v -> startActivity(new Intent(this, RemoveStudent.class)));
    }

    private boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            startActivity(new Intent(this, Front.class));
        } else if (id == R.id.nav_enter_marks) {
            startActivity(new Intent(this, EnterMarks.class));
        } else if (id == R.id.nav_reports) {
            startActivity(new Intent(this, Reports.class));
        }
        drawerLayout.closeDrawers();
        return true;
    }
}
