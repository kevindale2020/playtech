package com.playtech.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.playtech.model.Product;

public class ProductDataUtil {
	
	protected Connection get_connection() {
		Connection connection = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/play_tech_db?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", "root", "");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return connection;
	}
	
	public void addProduct(Product product) throws SQLException {
		try {
			Connection connection = get_connection();
			String sql = "INSERT INTO `tbl_products` (`image`, `name`, `description`, `price`, `stockin`) VALUES (?,?,?,?,?)";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setString(1, product.getImage());
			ps.setString(2, product.getName());
			ps.setString(3, product.getDesc());
			ps.setString(4, String.valueOf(product.getPrice()));
			ps.setString(5, String.valueOf(product.getStockin()));
			ps.executeUpdate();
			connection.close();
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public List<Product> getProducts() {
		List<Product> products = new ArrayList<>();
		try {
			Connection connection = get_connection();
			String sql = "Select product_id, image, name, description, price, stockin, stockout from tbl_products order by name asc";
			PreparedStatement ps = connection.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				int id = rs.getInt("product_id");
				String image = rs.getString("image");
				String name = rs.getString("name");
				String desc = rs.getString("description");
				double price = rs.getDouble("price");
				int stockin = rs.getInt("stockin");
				int stockout = rs.getInt("stockout");
				products.add(new Product(id,image,name,desc,price,stockin,stockout));
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		return products;
	}
	
	public Product details(int id) {
		Product product = new Product();
		try {
			Connection connection = get_connection();
			String sql = "Select * from tbl_products where product_id = ?";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				int pid = rs.getInt("product_id");
				String image = rs.getString("image");
				String name = rs.getString("name");
				String desc = rs.getString("description");
				double price = rs.getDouble("price");
				int stockin = rs.getInt("stockin");
				product.setId(pid);
				product.setImage(image);
				product.setName(name);
				product.setDesc(desc);
				product.setPrice(price);
				product.setStockin(stockin);
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return product;
	}
	
	public void saveProduct(Product product) throws SQLException {
		try {
			Connection connection = get_connection();
			String sql = "UPDATE `tbl_products` SET `image` = ?, `name` = ?, `description` = ?, `price` = ?, `stockin` = ? WHERE `tbl_products`.`product_id` = ?";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setString(1, product.getImage());
			ps.setString(2, product.getName());
			ps.setString(3, product.getDesc());
			ps.setString(4, String.valueOf(product.getPrice()));
			ps.setString(5, String.valueOf(product.getStockin()));
			ps.setString(6, String.valueOf(product.getId()));
			ps.executeUpdate();
			connection.close();
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public boolean nameExist(String name) {
		boolean flag = false;
		try {
			Connection connection = get_connection();
			String sql = "Select * from tbl_products where name = ?";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setString(1, name);
			ResultSet rs = ps.executeQuery();
			
			if(rs.next()) {
				flag = true;
			} else {
				flag = false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return flag;
	}
	
	public void deleteProduct(String id) throws SQLException {
		
		try {
			Connection connection = get_connection();
			String sql = "Delete from tbl_products where product_id = ?";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setString(1, id);
			ps.executeUpdate();
			connection.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
