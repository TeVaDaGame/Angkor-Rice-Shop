package com.example.angkorriceapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View; // Needed for icon visibility flags
import android.view.Window; // Needed for status bar access
import android.view.WindowManager; // Needed for system bar background flags
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat; // Safer way to get colors
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    // ==================== Product Data ====================
    private static final String[] PRODUCT_NAMES = {
        "Jasmine Rice", "White Rice", "Brown Rice", 
        "Sticky Rice", "Red Rice", "Fragrant Rice"
    };
    
    private static final double[] PRODUCT_PRICES = {
        25.0, 22.0, 28.0, 20.0, 24.0, 26.0
    };
    
    private static final String[] PRODUCT_DESCRIPTIONS = {
        "Premium jasmine rice with a delicate aroma",
        "Pure white rice perfect for everyday cooking",
        "Nutritious brown rice rich in fiber and vitamins",
        "Sticky rice for traditional dishes",
        "Red rice with nutty flavor",
        "Fragrant rice with aromatic essence"
    };
    
    private static final String[] PRODUCT_ORIGINS = {
        "Battambang Province", "Battambang Province", "Siem Reap Province",
        "Pursat Province", "Battambang Province", "Kampong Thom Province"
    };
    
    private static final String[] PRODUCT_QUALITIES = {
        "Premium Quality", "Standard Quality", "Organic Quality",
        "Premium Quality", "Organic Quality", "Premium Quality"
    };

    // ==================== UI Components ====================
    private EditText searchBar;
    private Button btnHome, btnCart, btnProfile;
    
    // Product controls arrays
    private Button[] btnMinus = new Button[6];
    private Button[] btnPlus = new Button[6];
    private TextView[] quantities = new TextView[6];
    private Button[] addToCartBtns = new Button[6];
    private TextView[] priceTexts = new TextView[6];
    
    // Size buttons arrays
    private Button[][] sizeButtons = new Button[6][3]; // 6 products, 3 sizes each (10kg, 25kg, 50kg)
    private int[] selectedSizes = {10, 10, 10, 10, 10, 10}; // Default to 10kg
    
    // Quantity trackers
    private int[] productQuantities = {1, 1, 1, 1, 1, 1};
    private int totalCartItems = 0;

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        
        
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        
        
        int goldColor = ContextCompat.getColor(this, R.color.header_color);

        
        window.setStatusBarColor(goldColor);
        window.setNavigationBarColor(goldColor);

        
        window.setStatusBarColor(goldColor);
        window.setNavigationBarColor(goldColor);

        window.getDecorView().setSystemUiVisibility(0);
        
        
        setContentView(R.layout.activity_main);
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initializeUI();
        setupListeners();
    }

    // ==================== UI Initialization ====================
    private void initializeUI() {
        // Initialize search bar and navigation
        searchBar = findViewById(R.id.searchBar);
        btnHome = findViewById(R.id.btnHome);
        btnCart = findViewById(R.id.btnCart);
        btnProfile = findViewById(R.id.btnProfile);

        // Initialize product showcase items (clickable)
        setupProductShowcaseListeners();
        
        // Initialize product list controls
        setupProductControls();
    }

    private void setupProductShowcaseListeners() {
        int[] showcaseIds = {R.id.showcaseItem1, R.id.showcaseItem2, R.id.showcaseItem3, R.id.showcaseItem4, R.id.showcaseItem5, R.id.showcaseItem6};
        for (int i = 0; i < showcaseIds.length; i++) {
            final int index = i;
            findViewById(showcaseIds[i]).setOnClickListener(v -> 
                openProductDetail(index)
            );
        }
    }

    private void setupProductControls() {
        // Map button IDs to arrays
        int[] minusIds = {R.id.btnMinus1, R.id.btnMinus2, R.id.btnMinus3, R.id.btnMinus4, R.id.btnMinus5, R.id.btnMinus6};
        int[] plusIds = {R.id.btnPlus1, R.id.btnPlus2, R.id.btnPlus3, R.id.btnPlus4, R.id.btnPlus5, R.id.btnPlus6};
        int[] quantityIds = {R.id.quantity1, R.id.quantity2, R.id.quantity3, R.id.quantity4, R.id.quantity5, R.id.quantity6};
        int[] addCartIds = {R.id.addToCart1, R.id.addToCart2, R.id.addToCart3, R.id.addToCart4, R.id.addToCart5, R.id.addToCart6};
        int[] priceIds = {R.id.price1, R.id.price2, R.id.price3, R.id.price4, R.id.price5, R.id.price6};
        
        // Size button IDs for each product
        int[][] sizeButtonIds = {
            {R.id.sizeBtn10kg1, R.id.sizeBtn25kg1, R.id.sizeBtn50kg1},
            {R.id.sizeBtn10kg2, R.id.sizeBtn25kg2, R.id.sizeBtn50kg2},
            {R.id.sizeBtn10kg3, R.id.sizeBtn25kg3, R.id.sizeBtn50kg3},
            {R.id.sizeBtn10kg4, R.id.sizeBtn25kg4, R.id.sizeBtn50kg4},
            {R.id.sizeBtn10kg5, R.id.sizeBtn25kg5, R.id.sizeBtn50kg5},
            {R.id.sizeBtn10kg6, R.id.sizeBtn25kg6, R.id.sizeBtn50kg6}
        };
        
        // Initialize arrays
        for (int i = 0; i < 6; i++) {
            btnMinus[i] = findViewById(minusIds[i]);
            btnPlus[i] = findViewById(plusIds[i]);
            quantities[i] = findViewById(quantityIds[i]);
            addToCartBtns[i] = findViewById(addCartIds[i]);
            priceTexts[i] = findViewById(priceIds[i]);
            
            // Initialize size buttons
            for (int j = 0; j < 3; j++) {
                sizeButtons[i][j] = findViewById(sizeButtonIds[i][j]);
            }
        }
    }

    // ==================== Event Listeners ====================
    private void setupListeners() {
        // Setup product quantity controls
        for (int i = 0; i < 6; i++) {
            final int productIndex = i;
            btnMinus[i].setOnClickListener(v -> updateQuantity(productIndex, -1));
            btnPlus[i].setOnClickListener(v -> updateQuantity(productIndex, 1));
            addToCartBtns[i].setOnClickListener(v -> addToCart(productIndex));
            
            // Setup size buttons
            final int[] sizes = {10, 25, 50};
            for (int j = 0; j < 3; j++) {
                final int sizeIndex = j;
                sizeButtons[i][j].setOnClickListener(v -> updateProductSize(productIndex, sizes[sizeIndex]));
            }
        }

        // Setup navigation buttons
        setupNavigationListeners();
        
        // Setup search bar
        setupSearchListener();
    }

    private void setupNavigationListeners() {
        btnHome.setOnClickListener(v -> 
            Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show()
        );

        btnCart.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CartActivity.class);
            startActivity(intent);
        });

        btnProfile.setOnClickListener(v -> 
            Toast.makeText(this, "Profile", Toast.LENGTH_SHORT).show()
        );
    }

    private void setupSearchListener() {
        searchBar.setOnEditorActionListener((v, actionId, event) -> {
            String searchQuery = searchBar.getText().toString();
            if (!searchQuery.isEmpty()) {
                Toast.makeText(this, "Searching for: " + searchQuery, Toast.LENGTH_SHORT).show();
            }
            return true;
        });
    }

    // ==================== Product Management ====================
    private void updateProductSize(int productIndex, int selectedSize) {
        selectedSizes[productIndex] = selectedSize;
        updatePriceDisplay(productIndex);
    }
    
    private void updatePriceDisplay(int productIndex) {
        double basePrice = PRODUCT_PRICES[productIndex];
        double sizeMultiplier = selectedSizes[productIndex] / 10.0; // 10kg = 1x, 25kg = 2.5x, 50kg = 5x
        int quantity = productQuantities[productIndex];
        double scaledPrice = basePrice * sizeMultiplier * quantity;
        priceTexts[productIndex].setText(String.format("$ %.2f", scaledPrice));
    }
    
    private void updateQuantity(int productIndex, int change) {
        int newQuantity = productQuantities[productIndex] + change;
        if (newQuantity > 0) {
            productQuantities[productIndex] = newQuantity;
            quantities[productIndex].setText(String.valueOf(newQuantity));
            updatePriceDisplay(productIndex);
        }
    }

    private void addToCart(int productIndex) {
        String productName = PRODUCT_NAMES[productIndex];
        double basePrice = PRODUCT_PRICES[productIndex];
        int quantity = productQuantities[productIndex];
        int selectedSize = selectedSizes[productIndex];
        
        // Calculate scaled price based on size
        double multiplier = selectedSize / 10.0;
        double scaledPrice = basePrice * multiplier;
        double totalPrice = scaledPrice * quantity;
        
        // Add to CartManager
        CartItem cartItem = new CartItem(productIndex, productName, selectedSize + "kg", scaledPrice, quantity);
        CartManager.getInstance().addItem(cartItem);
        
        totalCartItems += quantity;
        
        Toast.makeText(this, 
            quantity + "x " + productName + " (" + selectedSize + "kg) ($" + String.format("%.2f", totalPrice) + ") added to cart!", 
            Toast.LENGTH_SHORT).show();
    }

    private void openProductDetail(int productIndex) {
        Intent intent = new Intent(MainActivity.this, ProductDetailActivity.class);
        intent.putExtra("productName", PRODUCT_NAMES[productIndex]);
        intent.putExtra("price", PRODUCT_PRICES[productIndex]);
        intent.putExtra("description", PRODUCT_DESCRIPTIONS[productIndex]);
        intent.putExtra("origin", PRODUCT_ORIGINS[productIndex]);
        intent.putExtra("quality", PRODUCT_QUALITIES[productIndex]);
        startActivity(intent);
    }
}