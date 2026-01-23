package com.example.angkorriceapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AdminAddProductActivity extends AppCompatActivity {

    EditText name, price, weight, description;
    Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_product);

        name = findViewById(R.id.input_name);
        price = findViewById(R.id.input_price);
        weight = findViewById(R.id.input_weight);
        description = findViewById(R.id.input_description);
        btnSave = findViewById(R.id.btn_save_product);

        btnSave.setOnClickListener(v -> saveProduct());
    }

    private void saveProduct() {
        String n = name.getText().toString();
        String p = price.getText().toString();
        String w = weight.getText().toString();
        String d = description.getText().toString();

        if (n.isEmpty() || p.isEmpty() || w.isEmpty()) {
            Toast.makeText(this, "Please fill required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences prefs = getSharedPreferences("products", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        String productData = p + ";" + w + ";" + d + ";";
        editor.putString(n, productData);
        editor.apply();

        Toast.makeText(this, "Product Saved!", Toast.LENGTH_SHORT).show();
        finish();
    }
}

