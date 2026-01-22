package com.example.angkorriceapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

public class CartActivity extends AppCompatActivity {

    private Button btnBackToHome;
    private Button btnCheckout;
    private TextView tvSubtotal;
    private TextView tvTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        
        // Set status bar color
        getWindow().setStatusBarColor(getResources().getColor(R.color.gold, getTheme()));
        
        setContentView(R.layout.activity_cart);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initializeUI();
        setupListeners();
        displayCartItems();
        updatePrices();
    }

    private void initializeUI() {
        btnBackToHome = findViewById(R.id.btnBackToHome);
        btnCheckout = findViewById(R.id.btnCheckout);
        tvSubtotal = findViewById(R.id.tvSubtotal);
        tvTotal = findViewById(R.id.tvTotal);
    }

    private void setupListeners() {
        // Checkout button
        btnCheckout.setOnClickListener(v -> {
            List<CartItem> items = CartManager.getInstance().getCartItems();
            if (items.isEmpty()) {
                Toast.makeText(this, "Cart is empty", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(CartActivity.this, CheckoutActivity.class);
                startActivity(intent);
            }
        });

        // Back button
        btnBackToHome.setOnClickListener(v -> {
            finish();
        });
    }

    private void displayCartItems() {
        List<CartItem> cartItems = CartManager.getInstance().getCartItems();

        // Show up to 6 items in the layout
        for (int i = 0; i < Math.min(6, cartItems.size()); i++) {
            int layoutId = getLayoutIdForItem(i);
            LinearLayout itemLayout = findViewById(layoutId);
            if (itemLayout != null) {
                itemLayout.setVisibility(android.view.View.VISIBLE);
                updateItemLayout(i, cartItems.get(i));
            }
        }

        // Hide item layouts that aren't needed
        for (int i = cartItems.size(); i < 6; i++) {
            int layoutId = getLayoutIdForItem(i);
            LinearLayout itemLayout = findViewById(layoutId);
            if (itemLayout != null) {
                itemLayout.setVisibility(android.view.View.GONE);
            }
        }
    }

    private int getLayoutIdForItem(int itemIndex) {
        switch (itemIndex) {
            case 0: return R.id.item1Layout;
            case 1: return R.id.item2Layout;
            case 2: return R.id.item3Layout;
            case 3: return R.id.item4Layout;
            case 4: return R.id.item5Layout;
            case 5: return R.id.item6Layout;
            default: return -1;
        }
    }

    private void updateItemLayout(int itemIndex, CartItem item) {
        int itemNum = itemIndex + 1;
        String qtyId = "qty" + itemNum;
        String priceId = "price" + itemNum;
        String minusBtnId = "btnMinus" + itemNum;
        String plusBtnId = "btnPlus" + itemNum;
        String removeBtnId = "btnRemove" + itemNum;
        String nameId = "productName" + itemNum;
        String sizeId = "productSize" + itemNum;

        // Update product name
        TextView nameView = findViewById(getResources().getIdentifier(nameId, "id", getPackageName()));
        if (nameView != null) {
            nameView.setText(item.getName());
        }

        // Update product size
        TextView sizeView = findViewById(getResources().getIdentifier(sizeId, "id", getPackageName()));
        if (sizeView != null) {
            sizeView.setText(item.getSize());
        }

        // Update quantity display
        TextView qtyView = findViewById(getResources().getIdentifier(qtyId, "id", getPackageName()));
        if (qtyView != null) {
            qtyView.setText(String.valueOf(item.getQuantity()));
        }

        // Update price display
        TextView priceView = findViewById(getResources().getIdentifier(priceId, "id", getPackageName()));
        if (priceView != null) {
            priceView.setText("$ " + String.format("%.2f", item.getPrice() * item.getQuantity()));
        }

        // Setup minus button
        Button minusBtn = findViewById(getResources().getIdentifier(minusBtnId, "id", getPackageName()));
        if (minusBtn != null) {
            minusBtn.setOnClickListener(v -> {
                if (item.getQuantity() > 1) {
                    item.setQuantity(item.getQuantity() - 1);
                    displayCartItems();
                    updatePrices();
                }
            });
        }

        // Setup plus button
        Button plusBtn = findViewById(getResources().getIdentifier(plusBtnId, "id", getPackageName()));
        if (plusBtn != null) {
            plusBtn.setOnClickListener(v -> {
                item.setQuantity(item.getQuantity() + 1);
                displayCartItems();
                updatePrices();
            });
        }

        // Setup remove button
        Button removeBtn = findViewById(getResources().getIdentifier(removeBtnId, "id", getPackageName()));
        if (removeBtn != null) {
            removeBtn.setOnClickListener(v -> {
                CartManager.getInstance().removeItem(itemIndex);
                displayCartItems();
                updatePrices();
                Toast.makeText(this, "Item removed", Toast.LENGTH_SHORT).show();
            });
        }
    }

    private void updatePrices() {
        double subtotal = CartManager.getInstance().getTotalPrice();
        tvSubtotal.setText("$ " + String.format("%.2f", subtotal));
        tvTotal.setText("$ " + String.format("%.2f", subtotal));
    }

    @Override
    protected void onResume() {
        super.onResume();
        displayCartItems();
        updatePrices();
    }
}
