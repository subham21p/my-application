package com.example.onlineshoping.ModelClasses;

public class AddCartClass {

    private String imageUrl;
    private int item_id;
    private int nos_of_products;
    private String category;
    private String name;
    private String brand;
    private String quantity;
    private double price;
    private double rating;



    public AddCartClass(String imageUrl, int item_id, int nos_of_products, String category, String name, String brand, String quantity, double price, double rating) {
        this.imageUrl = imageUrl;
        this.item_id = item_id;
        this.nos_of_products = nos_of_products;
        this.category = category;
        this.name = name;
        this.brand = brand;
        this.quantity = quantity;
        this.price = price;
        this.rating = rating;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public int getItem_id() {
        return item_id;
    }

    public int getNos_of_products() {
        return nos_of_products;
    }

    public String getCategory() {
        return category;
    }

    public String getName() {
        return name;
    }

    public String getBrand() {
        return brand;
    }

    public String getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }

    public double getRating() {
        return rating;
    }
}
