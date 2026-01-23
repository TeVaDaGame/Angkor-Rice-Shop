package com.example.angkorriceapp;


import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import com.google.firebase.firestore.FirebaseFirestore;

public class AdminProductListActivity extends AppCompatActivity {

    ListView list;
    AdminProductAdapter adapter;
    private FirebaseFirestore db;

    // Product data
    private static final String[] PRODUCT_NAMES = {
        "Jasmine Rice", "White Rice", "Brown Rice", 
        "Sticky Rice", "Red Rice", "Fragrant Rice"
    };
    
    private static final String[] PRODUCT_PRICES = {
        "25.00", "22.00", "28.00", "20.00", "24.00", "26.00"
    };
    
    private static final String[] PRODUCT_WEIGHTS = {
        "10kg, 25kg, 50kg", "10kg, 25kg, 50kg", "10kg, 25kg, 50kg",
        "10kg, 25kg, 50kg", "10kg, 25kg, 50kg", "10kg, 25kg, 50kg"
    };

    private static final String[] PRODUCT_ORIGINS = {
        "Battambang Province", "Battambang Province", "Siem Reap Province",
        "Pursat Province", "Battambang Province", "Kampong Thom Province"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_product_list);

        db = FirebaseFirestore.getInstance();
        
        // Initialize products in Firestore if they don't exist
        initializeProducts();

        list = findViewById(R.id.list_products);
        adapter = new AdminProductAdapter(this);
        list.setAdapter(adapter);
    }

    private void initializeProducts() {
        // Check if products already exist in Firestore
        db.collection("products")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    // If no products exist, add them
                    if (queryDocumentSnapshots.isEmpty()) {
                        addDefaultProducts();
                    } else {
                        // Update existing products with origin if they don't have it
                        updateProductsWithOrigin();
                    }
                })
                .addOnFailureListener(e -> {
                    // If error, still try to add products
                    addDefaultProducts();
                });
    }

    private void updateProductsWithOrigin() {
        // Update existing products with origin information
        for (int i = 0; i < PRODUCT_NAMES.length; i++) {
            final int index = i;
            
            db.collection("products")
                    .whereEqualTo("name", PRODUCT_NAMES[i])
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            for (com.google.firebase.firestore.QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                // Update origin if it doesn't exist
                                if (!document.contains("origin")) {
                                    db.collection("products")
                                            .document(document.getId())
                                            .update("origin", PRODUCT_ORIGINS[index])
                                            .addOnSuccessListener(aVoid -> {
                                                // Origin updated successfully
                                            })
                                            .addOnFailureListener(e -> {
                                                // Error updating origin
                                            });
                                }
                            }
                        }
                    });
        }
    }

    private void addDefaultProducts() {
        for (int i = 0; i < PRODUCT_NAMES.length; i++) {
            final int index = i;
            
            // Check if product already exists
            db.collection("products")
                    .whereEqualTo("name", PRODUCT_NAMES[i])
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        // Only add if it doesn't exist
                        if (queryDocumentSnapshots.isEmpty()) {
                            java.util.Map<String, Object> product = new java.util.HashMap<>();
                            product.put("name", PRODUCT_NAMES[index]);
                            product.put("price", PRODUCT_PRICES[index]);
                            product.put("weight", PRODUCT_WEIGHTS[index]);
                            product.put("origin", PRODUCT_ORIGINS[index]);
                            product.put("image", "");
                            
                            db.collection("products")
                                    .add(product)
                                    .addOnSuccessListener(documentReference -> {
                                        // Product added successfully
                                        adapter.notifyDataSetChanged();
                                    })
                                    .addOnFailureListener(e -> {
                                        // Error adding product
                                    });
                        }
                    });
        }
    }
}
