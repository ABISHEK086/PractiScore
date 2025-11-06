package com.example.myapplication;  
 
import android.os.Bundle; 
import android.widget.Toast;
 
import androidx.annotation.NonNull; 
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ManageAdmin extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AdminAdapter adapter;
    private List<Admin> adminList;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_admin);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adminList = new ArrayList<>();
        adapter = new AdminAdapter(adminList);
        recyclerView.setAdapter(adapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("users").child("admins");

        fetchAdmins();
    }

    private void fetchAdmins() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                adminList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Admin admin = dataSnapshot.getValue(Admin.class);
                    if (admin != null) {
                        adminList.add(admin);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ManageAdmin.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
