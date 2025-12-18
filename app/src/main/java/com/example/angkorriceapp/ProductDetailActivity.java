package com.example.angkorriceapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ProductDetailActivity extends AppCompatActivity {

    private TextView tvProductName, tvPrice, tvDescription, tvOrigin, tvQuality;
    private Button btnMinus, btnPlus, btnAddToCart, btnBack;
    private TextView tvQuantity;
    private int quantity = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        // Get product data from intent
        String productName = getIntent().getStringExtra("productName");
        double price = getIntent().getDoubleExtra("price", 0);
        String description = getIntent().getStringExtra("description");
        String origin = getIntent().getStringExtra("origin");
        String quality = getIntent().getStringExtra("quality");

        // Initialize views
        tvProductName = findViewById(R.id.tvProductName);
        tvPrice = findViewById(R.id.tvPrice);
        tvDescription = findViewById(R.id.tvDescription);
        tvOrigin = findViewById(R.id.tvOrigin);
        tvQuality = findViewById(R.id.tvQuality);
        btnMinus = findViewById(R.id.btnMinus);
        btnPlus = findViewById(R.id.btnPlus);
        tvQuantity = findViewById(R.id.tvQuantity);
        btnAddToCart = findViewById(R.id.btnAddToCart);
        btnBack = findViewById(R.id.btnBack);

        // Set product data
        tvProductName.setText(productName);
        tvPrice.setText("$ " + String.format("%.2f", price));
        tvDescription.setText(description);
        tvOrigin.setText("From: " + origin);
        tvQuality.setText(quality);

        // Setup listeners
        btnMinus.setOnClickListener(v -> decreaseQuantity());
        btnPlus.setOnClickListener(v -> increaseQuantity());
        btnAddToCart.setOnClickListener(v -> addToCart(productName, price));
        btnBack.setOnClickListener(v -> finish());
    }

    private void increaseQuantity() {
        quantity++;
        tvQuantity.setText(String.valueOf(quantity));
    }

    private void decreaseQuantity() {
        if (quantity > 1) {
            quantity--;
            tvQuantity.setText(String.valueOf(quantity));
        }
    }

    private void addToCart(String productName, double price) {
        Toast.makeText(this, quantity + "x " + productName + " added to cart!", Toast.LENGTH_SHORT).show();
    }
}
