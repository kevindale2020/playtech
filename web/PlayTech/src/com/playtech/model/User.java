package com.playtech.model;

public class User {
	
	private int id;
	private String image;
	private String fname;
	private String lname;
	private String fullname;
	private String address;
	private String email;
	private String contact;
	private String username;
	private String password;
	private String success;
	private String date;
	
	
	public User() {
		super();
		this.id = 0;
		this.image = null;
		this.fname = null;
		this.lname = null;
		this.address = null;
		this.email = null;
		this.contact = null;
		this.password = null;
	}

	public User(int id, String image, String fname, String lname, String address, String email, String contact, String username,
			String password) {
		super();
		this.id = id;
		this.image = image;
		this.fname = fname;
		this.lname = lname;
		this.address = address;
		this.email = email;
		this.contact = contact;
		this.username = username;
		this.password = password;
	}
	
	

	public User(String image, String fname, String lname, String address, String email, String contact, String username,
			String password) {
		super();
		this.image = image;
		this.fname = fname;
		this.lname = lname;
		this.address = address;
		this.email = email;
		this.contact = contact;
		this.username = username;
		this.password = password;
	}
	
	public User(int id, String username, String success) {
		super();
		this.id = id;
		this.success = success;
		this.username = username;
	}
	
	

	public User(int id, String image, String fullname, String address, String email, String contact,
			String username, String date) {
		super();
		this.id = id;
		this.image = image;
		this.fullname = fullname;
		this.address = address;
		this.email = email;
		this.contact = contact;
		this.username = username;
		this.date = date;
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

	public String getFname() {
		return fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}

	public String getLname() {
		return lname;
	}

	public void setLname(String lname) {
		this.lname = lname;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSuccess() {
		return success;
	}

	public void setSuccess(String success) {
		this.success = success;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}
	
	

}
