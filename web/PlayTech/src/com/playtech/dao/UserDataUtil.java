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

import com.playtech.model.Product;
//import com.playtech.dbconnection.DatabaseConnection;
import com.playtech.model.User;

public class UserDataUtil {
	
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
			ps2.setString(2, "1");
			ps2.setString(3, ld.toString());
			ps2.executeUpdate();
			
			connection.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
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
	
	public User login(String username, String password) throws SQLException {
		
		Connection connection = get_connection();
		User user = null;
		
		try {
			String sql = "Select * from tbl_users u inner join tbl_user_roles ur using(user_id) where u.username = ? and u.password = ? and ur.role_id = 1";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setString(1, username);
			ps.setString(2, getMd5(password));
			ResultSet rs = ps.executeQuery();
			
			if(rs.next()) {
				user = new User(rs.getInt("user_id"), rs.getString("username"), "Success");
			} else {
				user = new User(0, "No Current User", "Error");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return user;
	}
	
	public User getProfile(int id) throws SQLException {
		User user = new User();
		try {
			Connection connection = get_connection();
			String sql = "Select * from tbl_users where user_id = ?";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setInt(1, id);
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
				user.setPassword(rs.getString("password"));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return user;
	}
	
	public void editProfile(User user) throws SQLException {
		try {
			Connection connection = get_connection();
			String sql = "UPDATE `tbl_users` SET `fname` = ?, `lname` = ?, `address` = ?, `email` = ?, `contact` = ?, `username` = ? WHERE `tbl_users`.`user_id` = ?";
			PreparedStatement ps =	connection.prepareStatement(sql);
			ps.setString(1, user.getFname());
			ps.setString(2, user.getLname());
			ps.setString(3, user.getAddress());
			ps.setString(4, user.getEmail());
			ps.setString(5, user.getContact());
			ps.setString(6, user.getUsername());
			ps.setInt(7, user.getId());
			ps.executeUpdate();
			
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public boolean changePassword(int id, String current, String newpass) throws SQLException {
		boolean flag = false;
		try {
			Connection connection = get_connection();
			String sql = "Select * from tbl_users where password = ? and user_id = ?";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setString(1, getMd5(current));
			ps.setInt(2, id);
			ResultSet rs = ps.executeQuery();
			
			if(rs.next()) {
				String sql2 = "Update tbl_users set password = ? where user_id = ?";
				PreparedStatement ps2 = connection.prepareStatement(sql2);
				ps2.setString(1, getMd5(newpass));
				ps2.setInt(2, id);
				ps2.executeUpdate();
				connection.close();
				flag = true;
			} else {
				connection.close();
				flag = false;
			}
			
		} catch (SQLException e) {
			
		}
		return flag;
		
	}
	
	public void imageUpload(int id, String filename) throws SQLException {
		try {
			Connection connection = get_connection();
			String sql = "Update tbl_users set image = ? where user_id = ?";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setString(1,filename);
			ps.setInt(2, id);
			ps.executeUpdate();
			
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public List<User> getUsers() {
		List<User> users = new ArrayList<>();
		try {
			Connection connection = get_connection();
			String sql = "Select u.user_id, u.image, concat(u.fname,' ',u.lname) as `fullname`, u.address, u.email, u.contact, u.username, ur.date_created from tbl_users u inner join tbl_user_roles ur using(user_id) where ur.role_id = 2 order by fname asc";
			PreparedStatement ps = connection.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				int id = rs.getInt("user_id");
				String image = rs.getString("image");
				String fullname = rs.getString("fullname");
				String address = rs.getString("address");
				String email = rs.getString("email");
				String contact = rs.getString("contact");
				String username = rs.getString("username");
				String date = rs.getString("date_created");
				users.add(new User(id,image,fullname,address,email,contact,username,date));
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		return users;
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
}
