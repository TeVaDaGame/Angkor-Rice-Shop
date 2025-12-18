package com.example.angkorriceapp;

import java.util.ArrayList;
import java.util.List;

public class CartManager {
    private static CartManager instance;
    private List<CartItem> cartItems;

    private CartManager() {
        cartItems = new ArrayList<>();
    }

    public static CartManager getInstance() {
        if (instance == null) {
            instance = new CartManager();
        }
        return instance;
    }

    public void addItem(CartItem item) {
        // Check if item already exists
        for (CartItem existingItem : cartItems) {
            if (existingItem.getName().equals(item.getName()) && 
                existingItem.getSize().equals(item.getSize())) {
                // Item already exists, increase quantity
                existingItem.setQuantity(existingItem.getQuantity() + item.getQuantity());
                return;
            }
        }
        // New item, add to cart
        cartItems.add(item);
    }

    public void removeItem(int index) {
        if (index >= 0 && index < cartItems.size()) {
            cartItems.remove(index);
        }
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public void clearCart() {
        cartItems.clear();
    }

    public double getTotalPrice() {
        double total = 0;
        for (CartItem item : cartItems) {
            total += item.getPrice() * item.getQuantity();
        }
        return total;
    }

    public int getTotalItems() {
        int total = 0;
        for (CartItem item : cartItems) {
            total += item.getQuantity();
        }
        return total;
    }
}
