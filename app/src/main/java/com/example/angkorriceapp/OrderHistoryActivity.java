package com.example.angkorriceapp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.angkorriceapp.Model.Order;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OrderHistoryActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser currentUser;
    private Button btnBack;
    private LinearLayout ordersContainer;
    private LinearLayout emptyStateContainer;
    private List<Order> ordersList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        currentUser = mAuth.getCurrentUser();

        // Initialize views
        btnBack = findViewById(R.id.btn_back);
        ordersContainer = findViewById(R.id.orders_container);
        emptyStateContainer = findViewById(R.id.empty_state_container);
        ordersList = new ArrayList<>();

        // Set back button listener
        btnBack.setOnClickListener(v -> finish());

        // Load orders
        loadUserOrders();
    }

    private void loadUserOrders() {
        if (currentUser != null) {
            String userId = currentUser.getUid();
            Log.d("OrderHistoryActivity", "Loading orders for user: " + userId);

            db.collection("orders")
                    .whereEqualTo("userId", userId)
                    .orderBy("timestamp", com.google.firebase.firestore.Query.Direction.DESCENDING)
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        ordersList.clear();
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            Order order = document.toObject(Order.class);
                            ordersList.add(order);
                        }

                        if (ordersList.isEmpty()) {
                            emptyStateContainer.setVisibility(android.view.View.VISIBLE);
                            ordersContainer.setVisibility(android.view.View.GONE);
                        } else {
                            emptyStateContainer.setVisibility(android.view.View.GONE);
                            ordersContainer.setVisibility(android.view.View.VISIBLE);
                            displayOrders();
                        }

                        Log.d("OrderHistoryActivity", "Loaded " + ordersList.size() + " orders");
                    })
                    .addOnFailureListener(e -> {
                        Log.e("OrderHistoryActivity", "Error loading orders: " + e.getMessage(), e);
                        Toast.makeText(OrderHistoryActivity.this, "Error loading orders", Toast.LENGTH_SHORT).show();
                        emptyStateContainer.setVisibility(android.view.View.VISIBLE);
                        ordersContainer.setVisibility(android.view.View.GONE);
                    });
        } else {
            Log.e("OrderHistoryActivity", "No current user found");
            Toast.makeText(this, "Error: No user logged in", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void displayOrders() {
        ordersContainer.removeAllViews();

        for (Order order : ordersList) {
            // Inflate order item layout
            LinearLayout orderItemView = (LinearLayout) getLayoutInflater().inflate(
                    R.layout.item_order,
                    ordersContainer,
                    false
            );

            // Set order data
            TextView orderId = orderItemView.findViewById(R.id.order_id);
            TextView orderDate = orderItemView.findViewById(R.id.order_date);
            TextView orderStatus = orderItemView.findViewById(R.id.order_status);
            TextView orderItemsCount = orderItemView.findViewById(R.id.order_items_count);
            TextView orderTotal = orderItemView.findViewById(R.id.order_total);
            Button btnViewDetails = orderItemView.findViewById(R.id.btn_view_details);

            // Format order ID
            orderId.setText("Order #" + (order.getOrderId() != null ? order.getOrderId().substring(Math.max(0, order.getOrderId().length() - 5)) : "N/A"));

            // Format date
            String formattedDate = formatDate(order.getTimestamp());
            orderDate.setText(formattedDate);

            // Set status with color
            orderStatus.setText(order.getStatus() != null ? order.getStatus() : "Pending");
            setStatusColor(orderStatus, order.getStatus());

            // Set items count
            int itemCount = order.getItems() != null ? order.getItems().size() : 0;
            orderItemsCount.setText(itemCount + " item" + (itemCount != 1 ? "s" : ""));

            // Set total
            orderTotal.setText(String.format(Locale.getDefault(), "$%.2f", order.getTotalAmount()));

            // View details click listener
            btnViewDetails.setOnClickListener(v -> {
                Intent intent = new Intent(OrderHistoryActivity.this, OrderDetailActivity.class);
                intent.putExtra("orderId", order.getOrderId());
                startActivity(intent);
            });

            ordersContainer.addView(orderItemView);
        }
    }

    private String formatDate(long timestamp) {
        try {
            Date date = new Date(timestamp);
            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
            return sdf.format(date);
        } catch (Exception e) {
            Log.e("OrderHistoryActivity", "Error formatting date: " + e.getMessage());
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
