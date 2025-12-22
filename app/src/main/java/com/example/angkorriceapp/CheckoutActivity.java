package com.example.angkorriceapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class CheckoutActivity extends AppCompatActivity {

    private Button btnBackToCart;
    private Button btnPlaceOrder;
    private Button btnPickFromMap;
    private TextView tvSubtotal;
    private TextView tvShipping;
    private TextView tvTotal;
    private TextView tvOrderItems;
    private EditText etDeliveryAddress;
    private EditText etCustomerName;
    private EditText etCustomerEmail;
    private EditText etCustomerPhone;

    private ActivityResultLauncher<Intent> mapPickerLauncher;

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

        // Setup map picker launcher
        mapPickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        String address = result.getData().getStringExtra("address");
                        if (address != null) {
                            etDeliveryAddress.setText(address);
                        }
                    }
                });

        initializeUI();
        setupListeners();
        updateOrderSummary();
    }

    private void initializeUI() {
        btnBackToCart = findViewById(R.id.btnBackToCart);
        btnPlaceOrder = findViewById(R.id.btnPlaceOrder);
        btnPickFromMap = findViewById(R.id.btnPickFromMap);
        tvSubtotal = findViewById(R.id.tvSubtotal);
        tvShipping = findViewById(R.id.tvShipping);
        tvTotal = findViewById(R.id.tvTotal);
        tvOrderItems = findViewById(R.id.tvOrderItems);
        etDeliveryAddress = findViewById(R.id.etDeliveryAddress);
        etCustomerName = findViewById(R.id.etCustomerName);
        etCustomerEmail = findViewById(R.id.etCustomerEmail);
        etCustomerPhone = findViewById(R.id.etCustomerPhone);
    }

    private void setupListeners() {
        // Back button
        btnBackToCart.setOnClickListener(v -> finish());

        // Pick from Map button
        btnPickFromMap.setOnClickListener(v -> {
            Intent intent = new Intent(CheckoutActivity.this, MapPickerActivity.class);
            mapPickerLauncher.launch(intent);
        });

        // Place Order button
        btnPlaceOrder.setOnClickListener(v -> {
            String name = etCustomerName.getText().toString().trim();
            String email = etCustomerEmail.getText().toString().trim();
            String phone = etCustomerPhone.getText().toString().trim();
            String address = etDeliveryAddress.getText().toString().trim();
            
            if (name.isEmpty()) {
                Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show();
                return;
            }
            if (email.isEmpty()) {
                Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show();
                return;
            }
            if (phone.isEmpty()) {
                Toast.makeText(this, "Please enter your phone number", Toast.LENGTH_SHORT).show();
                return;
            }
            if (address.isEmpty()) {
                Toast.makeText(this, "Please enter a delivery address", Toast.LENGTH_SHORT).show();
                return;
            }
            
            double total = CartManager.getInstance().getTotalPrice();
            if (CartManager.getInstance().getCartItems().isEmpty()) {
                Toast.makeText(this, "Cart is empty", Toast.LENGTH_SHORT).show();
            } else {
                String orderMessage = String.format("Order placed successfully!\nName: %s\nEmail: %s\nPhone: %s\nDelivery to: %s\nTotal: $%.2f", 
                    name, email, phone, address, total);
                Toast.makeText(this, orderMessage, Toast.LENGTH_LONG).show();
                // Clear cart after successful order
                CartManager.getInstance().getCartItems().clear();
                // Clear all input fields
                etCustomerName.setText("");
                etCustomerEmail.setText("");
                etCustomerPhone.setText("");
                etDeliveryAddress.setText("");
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
