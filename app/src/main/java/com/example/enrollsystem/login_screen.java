package com.example.enrollsystem;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class login_screen extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnSubmitLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        // Initialize UI components
        initializeUI();

        // Set login button listener
        setLoginListener();
    }

    private void initializeUI() {
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnSubmitLogin = findViewById(R.id.btnSubmitLogin);
    }

    private void setLoginListener() {
        btnSubmitLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleLogin();
            }
        });
    }

    private void handleLogin() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill out all fields.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Ambil data dari SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String registeredEmail = sharedPreferences.getString("email", null);
        String registeredPassword = sharedPreferences.getString("password", null);

        // Verifikasi kredensial
        if (email.equals(registeredEmail) && password.equals(registeredPassword)) {
            Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show();

            // Pindah ke EnrollmentActivity
            Intent intent = new Intent(login_screen.this, EnrollmentActivity.class);
            startActivity(intent);
            finish(); // Menutup login_screen agar tidak bisa kembali ke login setelah login berhasil
        } else {
            Toast.makeText(this, "Invalid credentials.", Toast.LENGTH_SHORT).show();
        }
    }
}
