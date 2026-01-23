package com.example.angkorriceapp;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.widget.NestedScrollView;

import com.example.angkorriceapp.Model.Order;
import com.example.angkorriceapp.Model.OrderItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class CheckoutActivity extends AppCompatActivity {

    private Button btnBackToCart;
    private Button btnPlaceOrder;
    private Button btnPickFromMap;
    private TextView tvSubtotal;
    private TextView tvShipping;
    private TextView tvTotal;
    private TextView tvOrderItems;
    private TextView tvItemsTotal;
    private EditText etDeliveryAddress;
    private EditText etCustomerName;
    private EditText etCustomerPhone;
    private NestedScrollView scrollView;
    private LinearLayout llProductsList;

    private ActivityResultLauncher<Intent> mapPickerLauncher;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        
        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        currentUser = mAuth.getCurrentUser();
        
        // Set status bar color
        getWindow().setStatusBarColor(getResources().getColor(R.color.gold, getTheme()));
        
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
        tvItemsTotal = findViewById(R.id.tvItemsTotal);
        etDeliveryAddress = findViewById(R.id.etDeliveryAddress);
        etCustomerName = findViewById(R.id.etCustomerName);
        etCustomerPhone = findViewById(R.id.etCustomerPhone);
        scrollView = findViewById(R.id.scrollView);
        llProductsList = findViewById(R.id.llProductsList);
    }
    
    private void setScrollOnFocusListener(EditText editText) {
        // Removed auto-scroll - users can manually scroll to see fields
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
            String phone = etCustomerPhone.getText().toString().trim();
            String address = etDeliveryAddress.getText().toString().trim();
            
            if (name.isEmpty()) {
                Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show();
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
                placeOrder(name, phone, address, total);
            }
        });
    }

    private void placeOrder(String name, String phone, String address, double total) {
        if (currentUser == null) {
            Toast.makeText(this, "Error: User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create order items list from cart
        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItem cartItem : CartManager.getInstance().getCartItems()) {
            OrderItem item = new OrderItem(
                cartItem.getName(),
                cartItem.getPrice(),
                cartItem.getQuantity(),
                cartItem.getSize()
            );
            orderItems.add(item);
        }

        // Create order object
        String orderId = db.collection("orders").document().getId();
        Order order = new Order(
            orderId,
            currentUser.getUid(),
            "Pending",
            System.currentTimeMillis(),
            total,
            orderItems,
            address
        );

        // Save to Firestore
        db.collection("orders").document(orderId)
            .set(order)
            .addOnSuccessListener(aVoid -> {
                Log.d("CheckoutActivity", "Order placed successfully: " + orderId);
                Toast.makeText(CheckoutActivity.this, "Order placed successfully!", Toast.LENGTH_SHORT).show();
                
                // Clear cart after successful order
                CartManager.getInstance().getCartItems().clear();
                
                // Clear all input fields
                etCustomerName.setText("");
                etCustomerPhone.setText("");
                etDeliveryAddress.setText("");
                
                // Navigate back to cart
                finish();
            })
            .addOnFailureListener(e -> {
                Log.e("CheckoutActivity", "Error placing order: " + e.getMessage(), e);
                Toast.makeText(CheckoutActivity.this, "Error placing order: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
    }

    private void updateOrderSummary() {
        // Clear previous items
        llProductsList.removeAllViews();
        
        // Add each cart item to the display
        double subtotal = 0;
        for (CartItem item : CartManager.getInstance().getCartItems()) {
            LinearLayout itemLayout = new LinearLayout(this);
            itemLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            itemLayout.setOrientation(LinearLayout.HORIZONTAL);
            itemLayout.setPadding(0, 8, 0, 8);
            
            // Product name and size
            TextView tvProductName = new TextView(this);
            tvProductName.setLayoutParams(new LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1f
            ));
            tvProductName.setText(item.getName() + " (" + item.getSize() + ") x" + item.getQuantity());
            tvProductName.setTextSize(13);
            tvProductName.setTextColor(getResources().getColor(android.R.color.black, getTheme()));
            
            // Product price
            TextView tvProductPrice = new TextView(this);
            tvProductPrice.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            tvProductPrice.setText("$ " + String.format("%.2f", item.getTotal()));
            tvProductPrice.setTextSize(13);
            tvProductPrice.setTextColor(getResources().getColor(android.R.color.black, getTheme()));
            tvProductPrice.setTypeface(null, Typeface.BOLD);
            
            itemLayout.addView(tvProductName);
            itemLayout.addView(tvProductPrice);
            
            llProductsList.addView(itemLayout);
            subtotal += item.getTotal();
        }
        
        double shipping = 5.00; // Fixed shipping cost
        double total = subtotal + shipping;
        int itemCount = CartManager.getInstance().getTotalItems();

        tvOrderItems.setText("Items (" + itemCount + ")");
        tvItemsTotal.setText("$ " + String.format("%.2f", subtotal));
        tvSubtotal.setText("$ " + String.format("%.2f", subtotal));
        tvShipping.setText("$ " + String.format("%.2f", shipping));
        tvTotal.setText("$ " + String.format("%.2f", total));
    }
}
