package com.example.angkorriceapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.angkorriceapp.Model.Order;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class OrderDetailActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private Button btnBack;
    private LinearLayout itemsContainer;
    private TextView orderIdText, orderDateText, orderStatusText, deliveryAddressText, totalAmountText;
    private String orderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        db = FirebaseFirestore.getInstance();

        // Get order ID from intent
        orderId = getIntent().getStringExtra("orderId");

        // Initialize views
        btnBack = findViewById(R.id.btn_back);
        orderIdText = findViewById(R.id.order_id_text);
        orderDateText = findViewById(R.id.order_date_text);
        orderStatusText = findViewById(R.id.order_status_text);
        deliveryAddressText = findViewById(R.id.delivery_address_text);
        totalAmountText = findViewById(R.id.total_amount_text);
        itemsContainer = findViewById(R.id.items_container);

        // Set back button listener
        btnBack.setOnClickListener(v -> finish());

        // Load order details
        if (orderId != null) {
            loadOrderDetails(orderId);
        } else {
            Toast.makeText(this, "Error: Order ID not found", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void loadOrderDetails(String orderId) {
        Log.d("OrderDetailActivity", "Loading order details for: " + orderId);

        db.collection("orders").document(orderId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Order order = documentSnapshot.toObject(Order.class);
                        if (order != null) {
                            displayOrderDetails(order);
                        }
                    } else {
                        Log.d("OrderDetailActivity", "Order not found");
                        Toast.makeText(OrderDetailActivity.this, "Order not found", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("OrderDetailActivity", "Error loading order: " + e.getMessage(), e);
                    Toast.makeText(OrderDetailActivity.this, "Error loading order details", Toast.LENGTH_SHORT).show();
                    finish();
                });
    }

    private void displayOrderDetails(Order order) {
        // Set order header information
        orderIdText.setText("Order #" + (order.getOrderId() != null ? order.getOrderId().substring(Math.max(0, order.getOrderId().length() - 5)) : "N/A"));
        orderDateText.setText(formatDate(order.getTimestamp()));
        orderStatusText.setText(order.getStatus() != null ? order.getStatus() : "Pending");
        setStatusColor(orderStatusText, order.getStatus());

        deliveryAddressText.setText(order.getDeliveryAddress() != null ? order.getDeliveryAddress() : "Address not provided");
        totalAmountText.setText(String.format(Locale.getDefault(), "$%.2f", order.getTotalAmount()));

        // Display order items
        if (order.getItems() != null && !order.getItems().isEmpty()) {
            itemsContainer.removeAllViews();
            for (Order.OrderItem item : order.getItems()) {
                LinearLayout itemView = (LinearLayout) getLayoutInflater().inflate(
                        R.layout.item_order_detail_line,
                        itemsContainer,
                        false
                );

                TextView itemName = itemView.findViewById(R.id.item_name);
                TextView itemQuantity = itemView.findViewById(R.id.item_quantity);
                TextView itemPrice = itemView.findViewById(R.id.item_price);

                itemName.setText(item.getProductName());
                itemQuantity.setText("Qty: " + item.getQuantity());
                itemPrice.setText(String.format(Locale.getDefault(), "$%.2f", item.getPrice() * item.getQuantity()));

                itemsContainer.addView(itemView);
            }
        }
    }

    private String formatDate(long timestamp) {
        try {
            Date date = new Date(timestamp);
            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault());
            return sdf.format(date);
        } catch (Exception e) {
            Log.e("OrderDetailActivity", "Error formatting date: " + e.getMessage());
            return "Date N/A";
        }
    }

    private void setStatusColor(TextView statusView, String status) {
        if (status == null) {
            status = "Pending";
        }

        int color;
        switch (status.toLowerCase()) {
            case "delivered":
                color = getResources().getColor(android.R.color.holo_green_dark);
                break;
            case "cancelled":
                color = getResources().getColor(android.R.color.holo_red_dark);
                break;
            case "pending":
                color = getResources().getColor(android.R.color.holo_orange_dark);
                break;
            case "processing":
            case "shipped":
                color = getResources().getColor(android.R.color.holo_blue_dark);
                break;
            default:
                color = getResources().getColor(android.R.color.darker_gray);
        }

        statusView.setTextColor(color);
    }
}
