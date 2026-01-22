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

                        if (name != null && price != null && weight != null) {
                            productList.add(new Product(id, name, price, weight, image != null ? image : ""));
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

        TextView name = view.findViewById(R.id.txt_name);
        TextView price = view.findViewById(R.id.txt_price);
        TextView weight = view.findViewById(R.id.txt_weight);

        name.setText("Name: " + p.getName());
        price.setText("Price: $" + p.getPrice());
        weight.setText("Weight: " + p.getWeight());

        return view;
    }
}

