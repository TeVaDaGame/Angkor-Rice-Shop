package com.example.angkorriceapp;


import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

public class AdminOrdersActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_orders);

        ListView list = findViewById(R.id.list_orders);
        // TODO: Add adapter for orders
    }
}
