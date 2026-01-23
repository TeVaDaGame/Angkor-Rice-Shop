package com.example.angkorriceapp.Model;

public class OrderItem {
    private String productName;
    private double price;
    private int quantity;
    private String size;

    // Default constructor for Firebase
    public OrderItem() {
    }

    public OrderItem(String productName, double price, int quantity, String size) {
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
        this.size = size;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public double getTotal() {
        return price * quantity;
    }
}
