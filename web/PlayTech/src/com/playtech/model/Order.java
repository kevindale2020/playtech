package com.playtech.model;

public class Order {
	
	private int orderno;
	private String customer;
	private String date;
	private double total;
	private String status;
	private String address;
	private String date2;
	private String reason;
	private String contact;
	private String date3;
	
	public Order() {
		super();
		this.orderno = 0;
		this.customer = "";
		this.date = "";
		this.total = 0.0;
		this.status = "";
		this.address = "";
		this.date2 = "";
		this.reason = "";
		this.contact = "";
		this.date3 = "";
	}

	public Order(int orderno, String date, double total, String status) {
		super();
		this.orderno = orderno;
		this.date = date;
		this.total = total;
		this.status = status;
	}
	
	

	public Order(int orderno, String customer, String contact, String address, String date, String date3, String status) {
		super();
		this.orderno = orderno;
		this.customer = customer;
		this.contact = contact;
		this.address = address;
		this.date = date;
		this.date3 = date3;
		this.status = status;
	}
	
	public Order(int orderno, String customer, String contact, String address, String date, String date3, String status, String date2, String reason) {
		super();
		this.orderno = orderno;
		this.customer = customer;
		this.contact = contact;
		this.address = address;
		this.date = date;
		this.status = status;
		this.date2 = date2;
		this.date3 = date3;
		this.reason = reason;
	}
	
	public Order(int orderno, String customer, String contact, String address, String date, String date3, String status, String date2) {
		super();
		this.orderno = orderno;
		this.customer = customer;
		this.contact = contact;
		this.address = address;
		this.date = date;
		this.status = status;
		this.date2 = date2;
		this.date3 = date3;
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

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCustomer() {
		return customer;
	}

	public void setCustomer(String customer) {
		this.customer = customer;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getDate2() {
		return date2;
	}

	public void setDate2(String date2) {
		this.date2 = date2;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getDate3() {
		return date3;
	}

	public void setDate3(String date3) {
		this.date3 = date3;
	}
}
