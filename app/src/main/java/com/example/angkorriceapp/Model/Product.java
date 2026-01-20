package com.example.angkorriceapp.Model;


public class Product {
    private String name;
    private String price;
    private String weight;
    private String description;
    private String image;

    public Product(String name, String price, String weight, String description, String image) {
        this.name = name;
        this.price = price;
        this.weight = weight;
        this.description = description;
        this.image = image;
    }

    public String getName() { return name; }
    public String getPrice() { return price; }
    public String getWeight() { return weight; }
    public String getDescription() { return description; }
    public String getImage() { return image; }
}
