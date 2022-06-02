package com.example.playtech;

public class Product {

    private int id;
    private String image;
    private String name;
    private double price;
    private int stock;

    public Product() {
        this.id = 0;
        this.image = "";
        this.name = "";
        this.price = 0.0;
        this.stock = 0;
    }

    public Product(int id, String image, String name, double price, int stock) {
        this.id = id;
        this.image = image;
        this.name = name;
        this.price = price;
        this.stock = stock;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }
}
