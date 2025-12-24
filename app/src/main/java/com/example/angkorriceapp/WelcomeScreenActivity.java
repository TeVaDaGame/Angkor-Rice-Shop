package com.example.angkorriceapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class WelcomeScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Check if user is already logged in
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        
        if (currentUser != null) {
            // User is already logged in, go directly to MainActivity
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }
        
        setContentView(R.layout.activity_welcome_screen);

        Button getStartBtn = findViewById(R.id.btn_get_start);

        // When button clicked → go to RegisterActivity
        getStartBtn.setOnClickListener(v -> {
            Intent intent = new Intent(WelcomeScreenActivity.this, RegisterActivity.class);
            startActivity(intent);
            finish(); // close welcome screen
        });
    }
}
