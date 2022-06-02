package com.playtech.model;

public class Product {
	
	private int id;
	private String image;
	private String name;
	private String desc;
	private double price;
	private int stockin;
	private int stockout;
	
	public Product() {
		super();
		this.id = 0;
		this.image = null;
		this.name = null;
		this.desc = null;
		this.price = 0.00;
		this.stockin = 0;
		this.stockout = 0;
	}
	
	public Product(int id, String image, String name, String desc, double price, int stockin, int stockout) {
		super();
		this.id = id;
		this.image = image;
		this.name = name;
		this.desc = desc;
		this.price = price;
		this.stockin = stockin;
		this.stockout = stockout;
	}
	
	public Product(int id, String image, String name, double price, int stockin) {
		super();
		this.id = id;
		this.image = image;
		this.name = name;
		this.price = price;
		this.stockin = stockin;
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

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getStockin() {
		return stockin;
	}

	public void setStockin(int stockin) {
		this.stockin = stockin;
	}

	public int getStockout() {
		return stockout;
	}

	public void setStockout(int stockout) {
		this.stockout = stockout;
	}
	
	
	
	
}
