package com.example.angkorriceapp;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    EditText emailInput, passInput;
    Button btnLogin;
    TextView txtRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailInput = findViewById(R.id.input_email);
        passInput = findViewById(R.id.input_password);
        btnLogin = findViewById(R.id.btn_login);
        txtRegister = findViewById(R.id.txt_register);

        btnLogin.setOnClickListener(v -> loginUser());

        txtRegister.setOnClickListener(v -> {
            startActivity(new Intent(this, RegisterActivity.class));
        });
    }

    private void loginUser() {
        String email = emailInput.getText().toString();
        String pass = passInput.getText().toString();

        SharedPreferences prefs = getSharedPreferences("users", MODE_PRIVATE);

        if (!prefs.contains(email)) {
            Toast.makeText(this, "User not found!", Toast.LENGTH_SHORT).show();
            return;
        }

        String savedPass = prefs.getString(email, "");

        if (!savedPass.equals(pass)) {
            Toast.makeText(this, "Incorrect password!", Toast.LENGTH_SHORT).show();
            return;
        }

        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}

