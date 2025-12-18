package com.example.angkorriceapp;

public class CartItem {
    private int id;
    private String name;
    private String size;
    private double price;
    private int quantity;

    public CartItem(int id, String name, String size, double price, int quantity) {
        this.id = id;
        this.name = name;
        this.size = size;
        this.price = price;
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSize() {
        return size;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotal() {
        return price * quantity;
    }
}
