package com.playtech.model;

public class Report {
	
	private int orderno;
	private String date;
	private String customer;
	private String purchased;
	private double total;
	
	public Report() {
		super();
		this.orderno = 0;
		this.date = "";
		this.customer = "";
		this.purchased = "";
		this.total = 0.0;
	}
	
	public Report(int orderno, String date, String customer, String purchased, double total) {
		super();
		this.orderno = orderno;
		this.date = date;
		this.customer = customer;
		this.purchased = purchased;
		this.total = total;
	}
	
	public int getOrderno() {
		return orderno;
	}

	public void setOrderno(int orderno) {
		this.orderno = orderno;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getCustomer() {
		return customer;
	}

	public void setCustomer(String customer) {
		this.customer = customer;
	}

	public String getPurchased() {
		return purchased;
	}

	public void setPurchased(String purchased) {
		this.purchased = purchased;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}
}
