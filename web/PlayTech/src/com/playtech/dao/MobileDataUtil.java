package com.playtech.dao;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.playtech.model.Cart;
import com.playtech.model.Product;
import com.playtech.model.User;
import com.playtech.model.Order;
import com.playtech.model.OrderDetail;

public class MobileDataUtil {
	
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
	
	public void register(User user) throws SQLException {
		
		Connection connection = get_connection();
		int userid = 0;
		
		try {
			String sql = "INSERT INTO `tbl_users` (`image`, `fname`, `lname`, `address`, `email`, `contact`, `username`, `password`) VALUES (?,?,?,?,?,?,?,?)";
			PreparedStatement ps = connection.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, user.getImage());
			ps.setString(2, user.getFname());
			ps.setString(3, user.getLname());
			ps.setString(4, user.getAddress());
			ps.setString(5, user.getEmail());
			ps.setString(6, user.getContact());
			ps.setString(7, user.getUsername());
			ps.setString(8, getMd5(user.getPassword()));
			ps.executeUpdate();
			
			ResultSet rs = ps.getGeneratedKeys();
			if(rs.next()) {
				userid = rs.getInt(1);
			}
			
			LocalDate ld = LocalDate.now();
			String sql2 = "INSERT INTO `tbl_user_roles` (`user_id`, `role_id`, `date_created`) VALUES (?,?,?)";
			PreparedStatement ps2 = connection.prepareStatement(sql2);
			ps2.setString(1, String.valueOf(userid));
			ps2.setString(2, "2");
			ps2.setString(3, ld.toString());
			ps2.executeUpdate();
			
			connection.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public boolean usernameExist(String username) {
		boolean flag = false;
		try {
			Connection connection = get_connection();
			String sql = "Select * from tbl_users where username = ?";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setString(1, username);
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
	
	public boolean emailExist(String email) {
		boolean flag = false;
		try {
			Connection connection = get_connection();
			String sql = "Select * from tbl_users where email = ?";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setString(1, email);
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
	
	public boolean productExist(int uid, int pid) {
		boolean flag = false;
		try {
			Connection connection = get_connection();
			String sql = "Select * from tbl_cart where product_id = ? and user_id = ?";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setInt(1, pid);
			ps.setInt(2, uid);
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
	
	public void productUpdate(int uid, int pid, int qty) {
		
		try {
			Connection connection = get_connection();
			String sql = "Update tbl_cart set qty = qty + ? where product_id = ? and user_id = ?";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setInt(1, qty);
			ps.setInt(2, pid);
			ps.setInt(3, uid);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public User login(String username,String password) throws SQLException {
		
		Connection connection = get_connection();
		
		User user = new User();
		
		try {
			String sql = "Select * from tbl_users u inner join tbl_user_roles ur using(user_id) where u.username = ? and u.password = ? and ur.role_id = 2";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setString(1, username);
			ps.setString(2, getMd5(password));
			ResultSet rs = ps.executeQuery();
			
			if(rs.next()) {
				user.setId(rs.getInt("user_id"));
				user.setImage(rs.getString("image"));
				user.setFname(rs.getString("fname"));
				user.setLname(rs.getString("lname"));
				user.setAddress(rs.getString("address"));
				user.setEmail(rs.getString("email"));
				user.setContact(rs.getString("contact"));
				user.setUsername(rs.getString("username"));
				user.setSuccess("1");
			} else {
				user.setSuccess("0");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return user;
	}
	
	public User getUserById(int id) throws SQLException {
		
		Connection connection = get_connection();
		
		User user = new User();
		
		try {
			String sql = "Select * from tbl_users where user_id = ?";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			
			if(rs.next()) {
				user.setImage(rs.getString("image"));
				user.setFname(rs.getString("fname"));
				user.setLname(rs.getString("lname"));
				user.setAddress(rs.getString("address"));
				user.setEmail(rs.getString("email"));
				user.setContact(rs.getString("contact"));
				user.setUsername(rs.getString("username"));
				user.setSuccess("1");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return user;
	}
	
	public void saveProfile(User user) throws SQLException {
		
		try {
			
			Connection connection = get_connection();
			
			String password = "";
			
			if(user.getPassword().equals("empty")) {
				
				String sql = "Select password from tbl_users where user_id = ?";
				PreparedStatement ps = connection.prepareStatement(sql);
				ps.setInt(1, user.getId());
				ResultSet rs = ps.executeQuery();
				
				if(rs.next()) {
					password = rs.getString("password");
				} 
			} else {
				password = getMd5(user.getPassword());
			}
			
			if(user.getImage().equals("empty")) {
				String sql2 = "UPDATE `tbl_users` SET `fname` = ?, `lname` = ?, `address` = ?, `email` = ?, `contact` = ?, `username` = ?, `password` = ? WHERE `tbl_users`.`user_id` = ?";
				PreparedStatement ps2 = connection.prepareStatement(sql2);
				ps2.setString(1, user.getFname());
				ps2.setString(2, user.getLname());
				ps2.setString(3, user.getAddress());
				ps2.setString(4, user.getEmail());
				ps2.setString(5, user.getContact());
				ps2.setString(6, user.getUsername());
				ps2.setString(7, password);
				ps2.setInt(8, user.getId());
				ps2.executeUpdate();
				
			} else {
				String sql2 = "UPDATE `tbl_users` SET `image` = ?, `fname` = ?, `lname` = ?, `address` = ?, `email` = ?, `contact` = ?, `username` = ?, `password` = ? WHERE `tbl_users`.`user_id` = ?";
				PreparedStatement ps2 = connection.prepareStatement(sql2);
				ps2.setString(1, user.getImage());
				ps2.setString(2, user.getFname());
				ps2.setString(3, user.getLname());
				ps2.setString(4, user.getAddress());
				ps2.setString(5, user.getEmail());
				ps2.setString(6, user.getContact());
				ps2.setString(7, user.getUsername());
				ps2.setString(8, password);
				ps2.setInt(9, user.getId());
				ps2.executeUpdate();
			}
			
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public List<Product> getProducts() throws SQLException {
		
		List<Product> productList = new ArrayList<>();
		
		try {
			
			Connection connection = get_connection();
			
			String sql = "Select * from tbl_products order by name asc";
			PreparedStatement ps = connection.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
				
				int id = rs.getInt("product_id");
				String image = rs.getString("image");
				String name = rs.getString("name");
				double price = rs.getDouble("price");
				int stock = rs.getInt("stockin");
				
				productList.add(new Product(id,image,name,price,stock));
			}
			
			for(int i=0; i<productList.size(); i++) {
				System.out.print(productList.get(i).getName());
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return productList;
	}
	
	public Product getProductById(int id) throws SQLException {
		
		Connection connection = get_connection();
		Product product = new Product();
		
		try {
			
			String sql = "Select * from tbl_products where product_id = ?";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			
			if(rs.next()) {
				product.setId(rs.getInt("product_id"));
				product.setImage(rs.getString("image"));
				product.setName(rs.getString("name"));
				product.setDesc(rs.getString("description"));
				product.setPrice(rs.getDouble("price"));
				product.setStockin(rs.getInt("stockin"));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return product;
	}
	
	public void addToCart(int uid, int pid, int qty) throws SQLException {
		
		Connection connection = get_connection();
		
		try {
			
			String sql = "INSERT INTO `tbl_cart` (`user_id`, `product_id`, `qty`) VALUES (?,?,?)";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setInt(1, uid);
			ps.setInt(2, pid);
			ps.setInt(3, qty);
			ps.executeUpdate();
			
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public List<Cart> getCartById(int id) throws SQLException {
		
		List<Cart> cartList = new ArrayList<>();
		
		try {
			
			Connection connection = get_connection();
				
			String sql = "Select c.cart_id, p.image, p.name, p.price, c.qty from tbl_cart c inner join tbl_products p on c.product_id = p.product_id where c.user_id = ? order by p.name";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
				
				int cid = rs.getInt("cart_id");
				String image = rs.getString("image");
				String name = rs.getString("name");
				double price = rs.getDouble("price");
				int qty = rs.getInt("qty");
				
				cartList.add(new Cart(cid,image,name,price,qty));
			}
			
			for(int i=0; i<cartList.size(); i++) {
				System.out.print(cartList.get(i).getName());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return cartList;
	}
	
	public void placeOrder(int id) throws SQLException {
		
		LocalDate ld = LocalDate.now();
		
		int orderno = 0;
		
		Connection connection = get_connection();
		
		try {
			String reason = "";
			// insert to order table
			String sql = "INSERT INTO `tbl_orders` (`user_id`, `order_date`, `status_id`, `cancelled_date`, `cancelled_reason`, `confirmed_date`, `closed_date`) VALUES (?,?,?,NULL,?,NULL,NULL)";
			PreparedStatement ps = connection.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS);
			ps.setInt(1, id);
			ps.setString(2, ld.toString());
			ps.setInt(3, 1);
			ps.setString(4, reason);
			ps.executeUpdate();
			
			ResultSet rs = ps.getGeneratedKeys();
			if(rs.next()) {
				orderno = rs.getInt(1);
			}
			
			// get cart list
			String sql2 = "Select * from tbl_cart where user_id = ?";
			PreparedStatement ps2 = connection.prepareStatement(sql2);
			ps2.setInt(1, id);
			ResultSet rs2 = ps2.executeQuery();
			
			while(rs2.next()) {
				// insert to order details
				int pid = rs2.getInt("product_id");
				int qty = rs2.getInt("qty");
				String sql3 = "INSERT INTO `tbl_order_details` (`order_no`, `product_id`, `qty`) VALUES (?,?,?)";
				PreparedStatement ps3 = connection.prepareStatement(sql3);
				ps3.setInt(1, orderno);
				ps3.setInt(2, pid);
				ps3.setInt(3, qty);
				ps3.executeUpdate();
				
				// update product stock and sold field
//				String sql4 = "UPDATE `tbl_products` SET `stockin` = stockin - ?, `stockout` = stockout + ? WHERE `tbl_products`.`product_id` = ?";
//				PreparedStatement ps4 = connection.prepareStatement(sql4);
//				ps4.setInt(1, qty);
//				ps4.setInt(2, qty);
//				ps4.setInt(3, pid);
//				ps4.executeUpdate();
				
				// clear out cart
				String sql5 = "Delete from tbl_cart where product_id = ? and user_id = ?";
				PreparedStatement ps5 = connection.prepareStatement(sql5);
				ps5.setInt(1, pid);
				ps5.setInt(2, id);
				ps5.executeUpdate();
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public List<Order> getOrderList(int id) throws SQLException {
		
		List<Order> orderList = new ArrayList<>();
		
		try {
			
			Connection connection = get_connection();
			
			String sql = "select o.order_no, o.order_date, sum(p.price * od.qty) as total,  s.status_desc as status from tbl_orders o inner join tbl_status s on o.status_id = s.status_id inner join tbl_order_details od on o.order_no = od.order_no inner join tbl_products p on od.product_id = p.product_id where o.user_id = ? group by o.order_no order by o.order_date desc";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
				
				int orderno = rs.getInt("order_no");
				String date = rs.getString("order_date");
				double total = rs.getDouble("total");
				String status = rs.getString("status");
				
				orderList.add(new Order(orderno,date,total,status));
			}
		
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return orderList;
	}
	
	public List<OrderDetail> getOrderDetailList(int orderno) throws SQLException {
		
		Connection connection = get_connection();
		
		List<OrderDetail> orderDetailList = new ArrayList<>();
		
		try {
			String sql = "Select od.order_details_id, p.image, p.name, p.price, od.qty from tbl_orders o inner join tbl_order_details od on o.order_no = od.order_no inner join tbl_products p on od.product_id = p.product_id where o.order_no = ?";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setInt(1, orderno);
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
				
				OrderDetail orderDetail = new OrderDetail(rs.getInt("order_details_id"), rs.getString("image"), rs.getString("name"), rs.getDouble("price"), rs.getInt("qty"));
				
				orderDetailList.add(orderDetail);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return orderDetailList;
	}
	
	public void removeCart(int id) throws SQLException {
		
		try {
			Connection connection = get_connection();
			String sql = "Delete from tbl_cart where cart_id = ?";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setInt(1, id);
			ps.executeUpdate();
			connection.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void cancelOrder(int orderno, String reason) throws SQLException {
		try {
			LocalDate ld = LocalDate.now();
			Connection connection = get_connection();
			String sql = "UPDATE `tbl_orders` SET `cancelled_date` = ?, `cancelled_reason` = ?, `status_id` = ? WHERE `tbl_orders`.`order_no` = ?";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setString(1, ld.toString());
			ps.setString(2, reason);
			ps.setInt(3, 4);
			ps.setInt(4, orderno);
			ps.executeUpdate();
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void receiveOrder(int orderno) throws SQLException {
		try {
			LocalDate ld = LocalDate.now();
			Connection connection = get_connection();
			String sql = "UPDATE `tbl_orders` SET `closed_date` = ?, `status_id` = ? WHERE `tbl_orders`.`order_no` = ?";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setString(1, ld.toString());
			ps.setInt(2, 3);
			ps.setInt(3, orderno);
			ps.executeUpdate();
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public String getEstimateDate() {
		String finalDate = "";
		try {
			Connection connection = get_connection();
			String sql = "SELECT concat(date_format(date_add(curdate(), interval 15 day), '%d %b'),' - ',date_format(date_add(curdate(), interval 24 day), '%d %b')) as date";
			PreparedStatement ps = connection.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			
			if(rs.next()) {
				finalDate = rs.getString("date");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return finalDate;
	}
	
	public String getArrivalDate(int id) {
		String finalDate = "";
		try {
			Connection connection = get_connection();
			String sql = "SELECT concat(date_format(date_add(order_date, interval 15 day), '%d %b'),' - ',date_format(date_add(order_date, interval 24 day), '%d %b')) as date FROM `tbl_orders` WHERE order_no = ?";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			
			if(rs.next()) {
				finalDate = rs.getString("date");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return finalDate;
	}

	public String getMd5(String input) {
		try {
			  
	        // Static getInstance method is called with hashing MD5
	        MessageDigest md = MessageDigest.getInstance("MD5");
	
	        // digest() method is called to calculate message digest
	        //  of an input digest() return array of byte
	        byte[] messageDigest = md.digest(input.getBytes());
	
	        // Convert byte array into signum representation
	        BigInteger no = new BigInteger(1, messageDigest);
	
	        // Convert message digest into hex value
	        String hashtext = no.toString(16);
	        while (hashtext.length() < 32) {
	            hashtext = "0" + hashtext;
	        }
	        return hashtext;
	    } 
	
	    // For specifying wrong message digest algorithms
	    catch (NoSuchAlgorithmException e) {
	        throw new RuntimeException(e);
	    }
	}
}
