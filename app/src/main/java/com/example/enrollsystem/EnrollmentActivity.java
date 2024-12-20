package com.example.enrollsystem;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EnrollmentActivity extends AppCompatActivity {

    private ListView subjectListView;
    private TextView totalCreditsTextView;
    private Button btnSubmitEnrollment;
    private ArrayList<Subject> enrolledSubjects;
    private ArrayList<Subject> availableSubjects;
    private FirebaseFirestore db;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enrollment);

        // Inisialisasi Firestore
        db = FirebaseFirestore.getInstance();

        subjectListView = findViewById(R.id.subject_list_view);
        totalCreditsTextView = findViewById(R.id.total_credits_text_view);
        btnSubmitEnrollment = findViewById(R.id.btnSubmitEnrollment);

        // Mengambil daftar mata kuliah yang tersedia
        availableSubjects = getAvailableSubjects();

        // Inisialisasi daftar mata kuliah yang sudah dipilih
        enrolledSubjects = new ArrayList<>();

        // Mengatur adapter untuk ListView
        SubjectAdapter adapter = new SubjectAdapter(this, availableSubjects);
        subjectListView.setAdapter(adapter);

        // Ketika item di ListView dipilih
        subjectListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parentView, View view, int position, long id) {
                Subject selectedSubject = availableSubjects.get(position);
                if (!enrolledSubjects.contains(selectedSubject)) {
                    enrolledSubjects.add(selectedSubject);
                    Toast.makeText(EnrollmentActivity.this, selectedSubject.getName() + " added.", Toast.LENGTH_SHORT).show();
                } else {
                    enrolledSubjects.remove(selectedSubject);
                    Toast.makeText(EnrollmentActivity.this, selectedSubject.getName() + " removed.", Toast.LENGTH_SHORT).show();
                }
                updateTotalCredits();
            }
        });

        // Menangani submit pendaftaran
        btnSubmitEnrollment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleEnrollment();
            }
        });
    }

    private void updateTotalCredits() {
        int totalCredits = calculateTotalCredits();
        if (totalCredits > 24) {
            Toast.makeText(this, "Total credits cannot exceed 24.", Toast.LENGTH_SHORT).show();
            totalCredits = 24;
        }
        totalCreditsTextView.setText("Total Credits: " + totalCredits);
    }

    private void handleEnrollment() {
        int totalCredits = calculateTotalCredits();
        if (totalCredits >= 24) {
            saveEnrollmentToFirestore();
        } else {
            Toast.makeText(this, "You must select at least 24 credits.", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveEnrollmentToFirestore() {
        // Menyiapkan data untuk disimpan
        Map<String, Object> enrollmentData = new HashMap<>();
        ArrayList<String> subjectNames = new ArrayList<>();
        for (Subject subject : enrolledSubjects) {
            subjectNames.add(subject.getName());
        }

        enrollmentData.put("enrolledSubjects", subjectNames);
        enrollmentData.put("totalCredits", calculateTotalCredits());
        enrollmentData.put("timestamp", System.currentTimeMillis());

        // Simpan ke Firestore
        db.collection("enrollments")
                .add(enrollmentData)
                .addOnSuccessListener(documentReference ->
                        Toast.makeText(EnrollmentActivity.this, "Enrollment saved with ID: " + documentReference.getId(), Toast.LENGTH_SHORT).show()
                )
                .addOnFailureListener(e ->
                        Toast.makeText(EnrollmentActivity.this, "Failed to save enrollment: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
    }

    private int calculateTotalCredits() {
        int totalCredits = 0;
        for (Subject subject : enrolledSubjects) {
            totalCredits += subject.getCredits();
        }
        return totalCredits;
    }

    private ArrayList<Subject> getAvailableSubjects() {
        ArrayList<Subject> subjects = new ArrayList<>();
        subjects.add(new Subject("Mathematics", 8));
        subjects.add(new Subject("Physics", 3));
        subjects.add(new Subject("Computer Science", 10));
        subjects.add(new Subject("Chemistry", 8));
        subjects.add(new Subject("Biology", 15));
        subjects.add(new Subject("Literature", 9));
        return subjects;
    }
}
