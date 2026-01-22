package com.example.angkorriceapp;


import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

public class AdminProductListActivity extends AppCompatActivity {

    ListView list;
    AdminProductAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_product_list);

        list = findViewById(R.id.list_products);
        adapter = new AdminProductAdapter(this);
        list.setAdapter(adapter);
    }
}
