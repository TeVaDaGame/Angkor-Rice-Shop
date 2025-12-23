package com.example.angkorriceapp;



import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Button getStartBtn = findViewById(R.id.btn_get_start);

        // When button clicked → go to HomeActivity
        getStartBtn.setOnClickListener(v -> {
            Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
            startActivity(intent);
            finish(); // close splash screen
        });
    }
}
