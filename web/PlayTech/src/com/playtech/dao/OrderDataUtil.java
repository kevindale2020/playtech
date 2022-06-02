package com.playtech.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.sql.SQLException;
import java.time.LocalDate;

import com.playtech.model.Order;
import com.playtech.model.OrderDetail;

public class OrderDataUtil {
	
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
	
	public List<Order> getOrderList() {
		
		List<Order> orderList = new ArrayList<>();
		
		try {
			Connection connection = get_connection();
			String sql = "Select o.order_no as orderno, concat(u.fname,' ',u.lname) as customer, u.contact, u.address, o.order_date as date, concat(date_format(date_add(curdate(), interval 15 day), '%d %b'),' - ',date_format(date_add(curdate(), interval 24 day), '%d %b')) as edate, s.status_desc as status from tbl_orders o inner join tbl_users u on o.user_id = u.user_id inner join tbl_status s on o.status_id = s.status_id where o.status_id = 1 order by o.order_no desc";
			PreparedStatement ps = connection.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
				int orderno = rs.getInt("orderno");
				String customer = rs.getString("customer");
				String contact = rs.getString("contact");
				String address = rs.getString("address");
				String date = rs.getString("date");
				String edate = rs.getString("edate");
				String status = rs.getString("status");
				orderList.add(new Order(orderno,customer,contact,address,date,edate,status));
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		return orderList;
	}
	
	public List<Order> getCancelOrderList() {
		
		List<Order> orderList = new ArrayList<>();
		
		try {
			Connection connection = get_connection();
			String sql = "Select o.order_no as orderno, concat(u.fname,' ',u.lname) as customer, u.contact, u.address, o.order_date as date, concat(date_format(date_add(o.order_date, interval 15 day), '%d %b'),' - ',date_format(date_add(o.order_date, interval 24 day), '%d %b')) as edate, s.status_desc as status, o.cancelled_date, o.cancelled_reason from tbl_orders o inner join tbl_users u on o.user_id = u.user_id inner join tbl_status s on o.status_id = s.status_id where o.status_id = 4 order by o.order_no desc";
			PreparedStatement ps = connection.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
				int orderno = rs.getInt("orderno");
				String customer = rs.getString("customer");
				String contact = rs.getString("contact");
				String address = rs.getString("address");
				String date = rs.getString("date");
				String edate = rs.getString("edate");
				String status = rs.getString("status");
				String date2 = rs.getString("cancelled_date");
				String reason = rs.getString("cancelled_reason");
				orderList.add(new Order(orderno,customer,contact,address,date,edate,status,date2,reason));
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		return orderList;
	}
	
	public List<Order> getConfirmOrderList() {
		
		List<Order> orderList = new ArrayList<>();
		
		try {
			Connection connection = get_connection();
			String sql = "Select o.order_no as orderno, concat(u.fname,' ',u.lname) as customer, u.contact, u.address, o.order_date as date, concat(date_format(date_add(o.order_date, interval 15 day), '%d %b'),' - ',date_format(date_add(o.order_date, interval 24 day), '%d %b')) as edate, s.status_desc as status, o.confirmed_date from tbl_orders o inner join tbl_users u on o.user_id = u.user_id inner join tbl_status s on o.status_id = s.status_id where o.status_id = 2 or o.status_id = 5 order by o.order_no desc";
			PreparedStatement ps = connection.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
				int orderno = rs.getInt("orderno");
				String customer = rs.getString("customer");
				String contact = rs.getString("contact");
				String address = rs.getString("address");
				String date = rs.getString("date");
				String edate = rs.getString("edate");
				String status = rs.getString("status");
				String date2 = rs.getString("confirmed_date");
				orderList.add(new Order(orderno,customer,contact,address,date,edate,status,date2));
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		return orderList;
	}
	
public List<Order> getClosedOrderList() {
		
		List<Order> orderList = new ArrayList<>();
		
		try {
			Connection connection = get_connection();
			String sql = "Select o.order_no as orderno, concat(u.fname,' ',u.lname) as customer, u.contact, u.address, o.order_date as date, concat(date_format(date_add(o.order_date, interval 15 day), '%d %b'),' - ',date_format(date_add(o.order_date, interval 24 day), '%d %b')) as edate, s.status_desc as status, o.closed_date from tbl_orders o inner join tbl_users u on o.user_id = u.user_id inner join tbl_status s on o.status_id = s.status_id where o.status_id = 3 order by o.order_no desc";
			PreparedStatement ps = connection.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
				int orderno = rs.getInt("orderno");
				String customer = rs.getString("customer");
				String contact = rs.getString("contact");
				String address = rs.getString("address");
				String date = rs.getString("date");
				String edate = rs.getString("edate");
				String status = rs.getString("status");
				String date2 = rs.getString("closed_date");
				orderList.add(new Order(orderno,customer,contact,address,date,edate,status,date2));
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		return orderList;
	}
	
	public List<OrderDetail> getOrderDetailsById(int id) {
		
		List<OrderDetail> orderDetailList = new ArrayList<>();
		
		try {
			Connection connection = get_connection();
			String sql = "Select od.order_details_id, p.image, p.name, p.price, od.qty from tbl_order_details od inner join tbl_products p using(product_id) where od.order_no = ? order by od.order_details_id asc";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
				int oid = rs.getInt("order_details_id");
				String image = rs.getString("image");
				String name = rs.getString("name");
				double price = rs.getDouble("price");
				int qty = rs.getInt("qty");
				
				orderDetailList.add(new OrderDetail(oid,image,name,price,qty));
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		return orderDetailList;
	}
	
	public void confirmOrder(int id) throws SQLException {
		LocalDate ld = LocalDate.now();
		try {
			Connection connection = get_connection();
			String sql = "Update tbl_orders set status_id = ?, confirmed_date = ? where order_no = ?";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setInt(1, 2);
			ps.setString(2, ld.toString());
			ps.setInt(3, id);
			ps.executeUpdate();
			
			String sql2 = "Select * from tbl_order_details where order_no = ?";
			PreparedStatement ps2 = connection.prepareStatement(sql2);
			ps2.setInt(1, id);
			ResultSet rs = ps2.executeQuery();
			
			while(rs.next()) {
				int pid = rs.getInt("product_id");
				int qty = rs.getInt("qty");
				
				String sql3 = "UPDATE `tbl_products` SET `stockin` = stockin - ?, `stockout` = stockout + ? WHERE `tbl_products`.`product_id` = ?";
				PreparedStatement ps3 = connection.prepareStatement(sql3);
				ps3.setInt(1, qty);
				ps3.setInt(2, qty);
				ps3.setInt(3, pid);
				ps3.executeUpdate();
			}
			connection.close();
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void setDelivery(int id) throws SQLException {
		LocalDate ld = LocalDate.now();
		try {
			Connection connection = get_connection();
			String sql = "Update tbl_orders set status_id = ? where order_no = ?";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setInt(1, 5);
			ps.setInt(2, id);
			ps.executeUpdate();
			connection.close();
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
}
