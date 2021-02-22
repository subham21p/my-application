package com.example.onlineshoping.ModelClasses;

public class OrdersClass {

    private String image;
    private int item_id;
    private int nos_of_products;
    private String category;
    private String name;
    private String brand;
    private String order_date;
    private String delivery_date;
    private String quantity;
    private int price;

    public OrdersClass(String image, int item_id, int nos_of_products, String category, String name, String brand, String order_date, String delivery_date, String quantity, int price) {
        this.image = image;
        this.item_id = item_id;
        this.nos_of_products = nos_of_products;
        this.category = category;
        this.name = name;
        this.brand = brand;
        this.order_date = order_date;
        this.delivery_date = delivery_date;
        this.quantity = quantity;
        this.price = price;
    }

    public String getImage() {
        return image;
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

    public String getOrder_date() {
        return order_date;
    }

    public String getDelivery_date() {
        return delivery_date;
    }

    public String getQuantity() {
        return quantity;
    }

    public int getPrice() {
        return price;
    }
}
