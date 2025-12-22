package com.example.angkorriceapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class CheckoutActivity extends AppCompatActivity {

    private Button btnBackToCart;
    private Button btnPlaceOrder;
    private TextView tvSubtotal;
    private TextView tvShipping;
    private TextView tvTotal;
    private TextView tvOrderItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        
        // Set status bar color
        getWindow().setStatusBarColor(getResources().getColor(R.color.header_color, getTheme()));
        
        setContentView(R.layout.activity_checkout);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initializeUI();
        setupListeners();
        updateOrderSummary();
    }

    private void initializeUI() {
        btnBackToCart = findViewById(R.id.btnBackToCart);
        btnPlaceOrder = findViewById(R.id.btnPlaceOrder);
        tvSubtotal = findViewById(R.id.tvSubtotal);
        tvShipping = findViewById(R.id.tvShipping);
        tvTotal = findViewById(R.id.tvTotal);
        tvOrderItems = findViewById(R.id.tvOrderItems);
    }

    private void setupListeners() {
        // Back button
        btnBackToCart.setOnClickListener(v -> finish());

        // Place Order button
        btnPlaceOrder.setOnClickListener(v -> {
            double total = CartManager.getInstance().getTotalPrice();
            if (CartManager.getInstance().getCartItems().isEmpty()) {
                Toast.makeText(this, "Cart is empty", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Order placed successfully! Total: $" + String.format("%.2f", total), Toast.LENGTH_SHORT).show();
                // Clear cart after successful order
                CartManager.getInstance().getCartItems().clear();
                // Navigate back to cart
                finish();
            }
        });
    }

    private void updateOrderSummary() {
        double subtotal = CartManager.getInstance().getTotalPrice();
        double shipping = 5.00; // Fixed shipping cost
        double total = subtotal + shipping;
        int itemCount = CartManager.getInstance().getTotalItems();

        tvOrderItems.setText("Items (" + itemCount + ")");
        tvSubtotal.setText("$ " + String.format("%.2f", subtotal));
        tvShipping.setText("$ " + String.format("%.2f", shipping));
        tvTotal.setText("$ " + String.format("%.2f", total));
    }
}
