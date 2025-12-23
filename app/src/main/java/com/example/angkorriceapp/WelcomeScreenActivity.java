package com.example.angkorriceapp;



import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class WelcomeScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
