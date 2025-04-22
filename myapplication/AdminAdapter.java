package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class AdminAdapter extends RecyclerView.Adapter<AdminAdapter.ViewHolder> {

    private List<Admin> adminList;

    public AdminAdapter(List<Admin> adminList) {
        this.adminList = adminList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Admin admin = adminList.get(position);
        holder.emailTextView.setText("Email: " + admin.getEmail());
        holder.roleTextView.setText("Role: " + admin.getRole());

        // Delete Button Click Event
        holder.deleteAdmin.setOnClickListener(v -> deleteAdminFromFirebase(admin, position, holder));
    }

    @Override
    public int getItemCount() {
        return adminList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView emailTextView, roleTextView;
        ImageView deleteAdmin;

        public ViewHolder(View itemView) {
            super(itemView);
            emailTextView = itemView.findViewById(R.id.adminEmail);
            roleTextView = itemView.findViewById(R.id.adminRole);
            deleteAdmin = itemView.findViewById(R.id.deleteAdmin);
        }
    }

    // Method to delete an admin from Firebase
    private void deleteAdminFromFirebase(Admin admin, int position, ViewHolder holder) {
        if (position >= 0 && position < adminList.size()) {  // Ensure valid position
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child("admins");

            databaseReference.orderByChild("email").equalTo(admin.getEmail()).get().addOnCompleteListener(task -> {
                if (task.isSuccessful() && task.getResult().exists()) {
                    for (DataSnapshot snapshot : task.getResult().getChildren()) {
                        snapshot.getRef().removeValue().addOnSuccessListener(aVoid -> {
                            // Remove from the local list and notify adapter
                            adminList.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, adminList.size());
                            Toast.makeText(holder.itemView.getContext(), "Admin Deleted", Toast.LENGTH_SHORT).show();
                        }).addOnFailureListener(e -> {
                            Toast.makeText(holder.itemView.getContext(), "Failed to Delete Admin", Toast.LENGTH_SHORT).show();
                        });
                    }
                } else {
                    Toast.makeText(holder.itemView.getContext(), "Admin not found!", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(holder.itemView.getContext(), "Invalid admin position", Toast.LENGTH_SHORT).show();
        }
    }
}
