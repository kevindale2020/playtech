package com.playtech.model;

public class OrderDetail {
	
	private int id;
	private String image;
	private String name;
	private double price;
	private int qty;
	
	public OrderDetail() {
		super();
		this.id = 0;
		this.image = "";
		this.name = "";
		this.price = 0.0;
		this.qty = 0;
	}
	
	public OrderDetail(int id, String image, String name, double price, int qty) {
		super();
		this.id = id;
		this.image = image;
		this.name = name;
		this.price = price;
		this.qty = qty;
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

	public int getQty() {
		return qty;
	}

	public void setQty(int qty) {
		this.qty = qty;
	}
}
