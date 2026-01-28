package com.example.myapplication;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.pdf.PdfDocument; 
import android.os.Bundle; 
import android.os.Environment;  
import android.text.Editable;   
import android.text.TextWatcher;  
import android.widget.Button   
import android.widget.EditText;     
import android.widget.TextView; 
import android.widget.Toast;  
import androidx.annotation.Nullable 
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;  
import androidx.core.content.ContextCompat; 
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference; 
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MarksEntry extends AppCompatActivity {

    private String selectedStudent, selectedSubject;
    private EditText etObservation, etVerification, etViva;
    private TextView tvTotal;
    private DatabaseReference databaseReference;
    private static final int REQUEST_WRITE_STORAGE = 112;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marks_entry);

        selectedStudent = getIntent().getStringExtra("selectedStudent");
        selectedSubject = getIntent().getStringExtra("selectedSubject");

        TextView tvSubject = findViewById(R.id.tv_subject);
        tvSubject.setText("Subject: " + selectedSubject);

        TextView tvUsername = findViewById(R.id.tv_username);
        etObservation = findViewById(R.id.et_observation);
        etVerification = findViewById(R.id.et_verification);
        etViva = findViewById(R.id.et_viva);
        tvTotal = findViewById(R.id.tv_total);
        Button btnGenerateReport = findViewById(R.id.btn_generate_report);

        tvUsername.setText(selectedStudent);
        databaseReference = FirebaseDatabase.getInstance().getReference("marks");

        etObservation.addTextChangedListener(new MarksTextWatcher(etObservation, 10));
        etVerification.addTextChangedListener(new MarksTextWatcher(etVerification, 10));
        etViva.addTextChangedListener(new MarksTextWatcher(etViva, 5));

        findViewById(R.id.btn_save).setOnClickListener(v -> saveMarks());
        btnGenerateReport.setOnClickListener(v -> generateReport());

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_STORAGE);
        }
    }

    private void calculateTotal() {
        int obs = parseInt(etObservation.getText().toString(), 10);
        int ver = parseInt(etVerification.getText().toString(), 10);
        int viva = parseInt(etViva.getText().toString(), 5);
        int total = obs + ver + viva;
        tvTotal.setText(String.valueOf(total));
    }

    private int parseInt(String value, int max) {
        try {
            int num = Integer.parseInt(value);
            return Math.min(num, max);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private void saveMarks() {
        int observation = parseInt(etObservation.getText().toString(), 10);
        int verification = parseInt(etVerification.getText().toString(), 10);
        int viva = parseInt(etViva.getText().toString(), 5);
        int total = observation + verification + viva;

        Map<String, Object> marksData = new HashMap<>();
        marksData.put("observation", observation);
        marksData.put("verification", verification);
        marksData.put("viva", viva);
        marksData.put("total", total);

        databaseReference.child(selectedStudent).child(selectedSubject).setValue(marksData)
                .addOnSuccessListener(aVoid -> Toast.makeText(MarksEntry.this, "Marks saved successfully", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(MarksEntry.this, "Failed to save marks", Toast.LENGTH_SHORT).show());
    }

    private void generateReport() {
        databaseReference.child(selectedStudent).child(selectedSubject).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    int observation = snapshot.child("observation").getValue(Integer.class);
                    int verification = snapshot.child("verification").getValue(Integer.class);
                    int viva = snapshot.child("viva").getValue(Integer.class);
                    int total = snapshot.child("total").getValue(Integer.class);

                    String dateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
                    String reportContent = "Student: " + selectedStudent + "\n" +
                            "Subject: " + selectedSubject + "\n" +
                            "Date & Time: " + dateTime + "\n" +
                            "Observation: " + observation + "\n" +
                            "Verification: " + verification + "\n" +
                            "Viva: " + viva + "\n" +
                            "Total: " + total;

                    createPDF(reportContent);
                } else {
                    Toast.makeText(MarksEntry.this, "No data found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(MarksEntry.this, "Failed to retrieve data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createPDF(String content) {
        PdfDocument pdfDocument = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(300, 400, 1).create();
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);

        page.getCanvas().drawText(content, 10, 25, new android.graphics.Paint());
        pdfDocument.finishPage(page);

        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), selectedStudent + "_report.pdf");

        try {
            FileOutputStream fos = new FileOutputStream(file);
            pdfDocument.writeTo(fos);
            fos.close();
            pdfDocument.close();
            Toast.makeText(this, "Report saved in Downloads", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(this, "Failed to save report", Toast.LENGTH_SHORT).show();
        }
    }

    private class MarksTextWatcher implements TextWatcher {
        private final EditText editText;
        private final int maxMarks;

        public MarksTextWatcher(EditText editText, int maxMarks) {
            this.editText = editText;
            this.maxMarks = maxMarks;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            try {
                int value = Integer.parseInt(s.toString());
                if (value > maxMarks) {
                    editText.setError("Maximum marks is " + maxMarks);
                    editText.setText(String.valueOf(maxMarks));
                }
            } catch (NumberFormatException ignored) {}
        }
    }
}
