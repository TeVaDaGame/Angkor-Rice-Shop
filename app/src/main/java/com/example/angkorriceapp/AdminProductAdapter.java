package com.example.angkorriceapp;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.angkorriceapp.Model.Product;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class AdminProductAdapter extends BaseAdapter {

    Context context;
    List<Product> productList = new ArrayList<>();
    private FirebaseFirestore firestore;

    public AdminProductAdapter(Context context) {
        this.context = context;
        this.firestore = FirebaseFirestore.getInstance();
        loadProducts();
    }

    private void loadProducts() {
        // Fetch all products from Firestore
        firestore.collection("products")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    productList.clear();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        String id = document.getId();
                        String name = document.getString("name");
                        String price = document.getString("price");
                        String weight = document.getString("weight");
                        String image = document.getString("image");
                        String origin = document.getString("origin");

                        if (name != null && price != null && weight != null) {
                            Product product = new Product(name, price, weight, origin != null ? origin : "", image != null ? image : "");
                            productList.add(product);
                        }
                    }
                    notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    // If Firestore fails, fallback to SharedPreferences
                    loadProductsFromSharedPreferences();
                });
    }

    private void loadProductsFromSharedPreferences() {
        android.content.SharedPreferences prefs = context.getSharedPreferences("products", Context.MODE_PRIVATE);
        java.util.Map<String, ?> all = prefs.getAll();

        for (String key : all.keySet()) {
            String data = prefs.getString(key, "");
            String[] parts = data.split(";");

            if (parts.length >= 4) {
                productList.add(new Product(
                        key,
                        parts[0],
                        parts[1],
                        parts[2],
                        parts[3]
                ));
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return productList.size();
    }

    @Override
    public Object getItem(int i) {
        return productList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup parent) {
        if (view == null) {
            view = LayoutInflater.from(context)
                    .inflate(R.layout.item_admin_product, parent, false);
        }

        Product p = productList.get(i);
        final int position = i;

        TextView name = view.findViewById(R.id.txt_name);
        TextView price = view.findViewById(R.id.txt_price);
        TextView origin = view.findViewById(R.id.txt_origin);
        android.widget.Button btnEdit = view.findViewById(R.id.btn_edit);
        android.widget.Button btnDelete = view.findViewById(R.id.btn_delete);

        name.setText(p.getName());
        price.setText("$" + p.getPrice());
        origin.setText("From: " + p.getDescription());

        // Edit button functionality
        btnEdit.setOnClickListener(v -> {
            // Open AdminEditProductActivity with product data
            android.content.Intent intent = new android.content.Intent(context, AdminEditProductActivity.class);
            intent.putExtra("productName", p.getName());
            intent.putExtra("productPrice", p.getPrice());
            intent.putExtra("productOrigin", p.getDescription());
            context.startActivity(intent);
        });

        // Delete button functionality
        btnDelete.setOnClickListener(v -> {
            // Show confirmation dialog before deleting with themed design
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
            builder.setTitle("Delete Product")
                    .setMessage("Are you sure you want to delete " + p.getName() + "?")
                    .setPositiveButton("Delete", (dialog, which) -> {
                        // Delete from Firestore
                        firestore.collection("products")
                                .whereEqualTo("name", p.getName())
                                .get()
                                .addOnSuccessListener(queryDocumentSnapshots -> {
                                    for (com.google.firebase.firestore.QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                        firestore.collection("products")
                                                .document(document.getId())
                                                .delete()
                                                .addOnSuccessListener(aVoid -> {
                                                    productList.remove(position);
                                                    notifyDataSetChanged();
                                                    android.widget.Toast.makeText(context, "Product deleted successfully", android.widget.Toast.LENGTH_SHORT).show();
                                                })
                                                .addOnFailureListener(e -> {
                                                    android.widget.Toast.makeText(context, "Error deleting product", android.widget.Toast.LENGTH_SHORT).show();
                                                });
                                    }
                                });
                    })
                    .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

            android.app.AlertDialog dialog = builder.create();
            
            // Style the dialog to match the store theme
            dialog.setOnShowListener(dialogInterface -> {
                // Style positive button (Delete) - Red for delete action
                dialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE)
                        .setTextColor(context.getResources().getColor(android.R.color.holo_red_dark));
                dialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE)
                        .setTextSize(android.util.TypedValue.COMPLEX_UNIT_SP, 14);
                
                // Style negative button (Cancel) - Gold for cancel action
                dialog.getButton(android.app.AlertDialog.BUTTON_NEGATIVE)
                        .setTextColor(context.getResources().getColor(android.R.color.holo_green_dark));
                dialog.getButton(android.app.AlertDialog.BUTTON_NEGATIVE)
                        .setTextSize(android.util.TypedValue.COMPLEX_UNIT_SP, 14);
            });
            
            dialog.show();
        });

        return view;
    }
}

