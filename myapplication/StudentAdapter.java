package com.example.myapplication;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.ViewHolder> {

    private ArrayList<String> studentList;
    private DatabaseReference databaseReference;

    public StudentAdapter(ArrayList<String> studentList, DatabaseReference databaseReference) {
        this.studentList = studentList;
        this.databaseReference = databaseReference;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_student, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        String studentName = studentList.get(position);
        holder.studentUsername.setText(studentName);

        // Delete student on button click
        holder.deleteStudent.setOnClickListener(v -> {
            databaseReference.orderByChild("username").equalTo(studentName)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot child : snapshot.getChildren()) {
                                child.getRef().removeValue()
                                        .addOnSuccessListener(aVoid -> {
                                            Toast.makeText(v.getContext(), "Student removed", Toast.LENGTH_SHORT).show();
                                            studentList.remove(position);
                                            notifyDataSetChanged();
                                        })
                                        .addOnFailureListener(e -> {
                                            Toast.makeText(v.getContext(), "Failed to remove", Toast.LENGTH_SHORT).show();
                                        });
                            }
                        }

                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(v.getContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView studentUsername;
        ImageView deleteStudent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            studentUsername = itemView.findViewById(R.id.studentUsername);
            deleteStudent = itemView.findViewById(R.id.deleteStudent);
        }
    }
}
