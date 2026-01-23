package com.example.angkorriceapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;

public class AdminEditProductActivity extends AppCompatActivity {

    private EditText etProductName, etProductPrice, etProductOrigin;
    private Button btnSave, btnCancel;
    private FirebaseFirestore db;
    private String originalProductName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_edit_product);

        db = FirebaseFirestore.getInstance();

        // Initialize views
        etProductName = findViewById(R.id.et_product_name);
        etProductPrice = findViewById(R.id.et_product_price);
        etProductOrigin = findViewById(R.id.et_product_origin);
        btnSave = findViewById(R.id.btn_save);
        btnCancel = findViewById(R.id.btn_cancel);

        // Get data from intent
        if (getIntent().hasExtra("productName")) {
            originalProductName = getIntent().getStringExtra("productName");
            String productPrice = getIntent().getStringExtra("productPrice");
            String productOrigin = getIntent().getStringExtra("productOrigin");

            // Set values in edit fields
            etProductName.setText(originalProductName);
            etProductPrice.setText(productPrice);
            etProductOrigin.setText(productOrigin);
        }

        // Save button functionality
        btnSave.setOnClickListener(v -> saveProductChanges());

        // Cancel button functionality
        btnCancel.setOnClickListener(v -> finish());
    }

    private void saveProductChanges() {
        String newName = etProductName.getText().toString().trim();
        String newPrice = etProductPrice.getText().toString().trim();
        String newOrigin = etProductOrigin.getText().toString().trim();

        // Validate inputs
        if (newName.isEmpty() || newPrice.isEmpty() || newOrigin.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Update product in Firestore
        db.collection("products")
                .whereEqualTo("name", originalProductName)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        for (com.google.firebase.firestore.QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            db.collection("products")
                                    .document(document.getId())
                                    .update(
                                            "name", newName,
                                            "price", newPrice,
                                            "origin", newOrigin
                                    )
                                    .addOnSuccessListener(aVoid -> {
                                        Toast.makeText(AdminEditProductActivity.this, "Product updated successfully", Toast.LENGTH_SHORT).show();
                                        finish();
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(AdminEditProductActivity.this, "Error updating product", Toast.LENGTH_SHORT).show();
                                    });
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error finding product", Toast.LENGTH_SHORT).show();
                });
    }
}
