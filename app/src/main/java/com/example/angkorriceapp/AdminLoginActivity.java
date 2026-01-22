package com.example.angkorriceapp;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AdminLoginActivity extends AppCompatActivity {

    EditText email, password;
    Button btnLogin;

    // Admin account (hardcoded)
    private final String ADMIN_EMAIL = "admin@gmail.com";
    private final String ADMIN_PASS = "123456";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        email = findViewById(R.id.admin_email);
        password = findViewById(R.id.admin_password);
        btnLogin = findViewById(R.id.btn_admin_login);

        btnLogin.setOnClickListener(v -> loginAdmin());
    }

    private void loginAdmin() {
        String e = email.getText().toString().trim();
        String p = password.getText().toString().trim();

        if (e.equals(ADMIN_EMAIL) && p.equals(ADMIN_PASS)) {
            Toast.makeText(this, "Admin Login Success", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, AdminDashboardActivity.class));
            finish();
        } else {
            Toast.makeText(this, "Wrong Admin Email or Password", Toast.LENGTH_SHORT).show();
        }
    }
}
