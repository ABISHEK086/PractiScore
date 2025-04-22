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

public class StaffAdapter extends RecyclerView.Adapter<StaffAdapter.ViewHolder> {

    private List<StaffModel.Staff> staffList;

    public StaffAdapter(List<StaffModel.Staff> staffList) {
        this.staffList = staffList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.staff_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        StaffModel.Staff staff = staffList.get(position);
        holder.emailTextView.setText("Email: " + staff.getEmail());
        holder.roleTextView.setText("Role: " + staff.getRole());

        // Delete Button Click Event
        holder.deleteStaff.setOnClickListener(v -> deleteStaffFromFirebase(staff, position, holder));
    }

    @Override
    public int getItemCount() {
        return staffList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView emailTextView, roleTextView;
        ImageView deleteStaff;

        public ViewHolder(View itemView) {
            super(itemView);
            emailTextView = itemView.findViewById(R.id.staffEmail);
            roleTextView = itemView.findViewById(R.id.staffRole);
            deleteStaff = itemView.findViewById(R.id.deleteStaff);
        }
    }

    // Method to delete a staff member from Firebase
    private void deleteStaffFromFirebase(StaffModel.Staff staff, int position, ViewHolder holder) {
        if (position >= 0 && position < staffList.size()) {  // Ensure valid position
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child("staff");

            databaseReference.orderByChild("email").equalTo(staff.getEmail()).get().addOnCompleteListener(task -> {
                if (task.isSuccessful() && task.getResult().exists()) {
                    for (DataSnapshot snapshot : task.getResult().getChildren()) {
                        snapshot.getRef().removeValue().addOnSuccessListener(aVoid -> {
                            // First, remove from the list
                            staffList.remove(position);
                            // Notify the adapter that the item is removed
                            notifyItemRemoved(position);
                            // Notify item range change after deletion
                            notifyItemRangeChanged(position, staffList.size());

                            Toast.makeText(holder.itemView.getContext(), "Staff Deleted", Toast.LENGTH_SHORT).show();
                        }).addOnFailureListener(e -> {
                            Toast.makeText(holder.itemView.getContext(), "Failed to Delete Staff", Toast.LENGTH_SHORT).show();
                        });
                    }
                } else {
                    Toast.makeText(holder.itemView.getContext(), "Staff not found!", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(holder.itemView.getContext(), "Invalid staff position", Toast.LENGTH_SHORT).show();
        }
    }
}
