package com.example.angkorriceapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class AdminDashboardActivity extends AppCompatActivity {

    Button btnAdd, btnProducts, btnOrders, btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        btnAdd = findViewById(R.id.btn_add_product);
        btnProducts = findViewById(R.id.btn_view_products);
        btnOrders = findViewById(R.id.btn_view_orders);
        btnLogout = findViewById(R.id.btn_admin_logout);

        btnAdd.setOnClickListener(v ->
                startActivity(new Intent(this, AdminAddProductActivity.class)));

        btnProducts.setOnClickListener(v ->
                startActivity(new Intent(this, AdminProductListActivity.class)));

        btnOrders.setOnClickListener(v ->
                startActivity(new Intent(this, AdminOrdersActivity.class)));

        btnLogout.setOnClickListener(v -> {
            startActivity(new Intent(this, AdminLoginActivity.class));
            finish();
        });
    }
}

