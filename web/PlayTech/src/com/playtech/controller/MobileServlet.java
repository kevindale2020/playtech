package com.playtech.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Base64;
import java.util.List;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.xml.bind.DatatypeConverter;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.playtech.dao.MobileDataUtil;
import com.playtech.model.Cart;
import com.playtech.model.Order;
import com.playtech.model.OrderDetail;
import com.playtech.model.Product;
import com.playtech.model.User;

/**
 * Servlet implementation class MobileServlet
 */
@WebServlet("/MobileServlet")
public class MobileServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static final String SAVE_DIR = "images\\users";
	private MobileDataUtil mdu;
	private String action;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MobileServlet() {
        super();
        this.mdu = new MobileDataUtil();
        this.action = null;
    }
    
    /**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		action = request.getParameter("action");
		
		System.out.println(action);
		
		if(action==null) {
			return;
		}
		
		switch(action) {
			case "mobile_register":
				mobileRegister(request,response);
				break;
				
			case "mobile_login":
				mobileLogin(request,response);
				break;
				
			case "mobile_get_profile":
				mobileGetProfile(request,response);
				break;
				
			case "mobile_save_profile":
				mobileSaveProfile(request,response);
				break;
				
			case "mobile_product":
				mobileProdut(request,response);
				break;
				
			case "mobile_product_details":
				mobileProductDetails(request,response);
				break;
			
			case "mobile_add_cart":
				mobileAddCart(request,response);
				break;
				
			case "mobile_get_cart":
				mobileGetCart(request,response);
				break;
				
			case "mobile_place_order":
				mobilePlaceOrder(request,response);
				break;
				
			case "mobile_order_list":
				mobileOrderList(request,response);
				break;
				
			case "mobile_order_details_list":
				mobileOrderDetailsList(request,response);
				break;
				
			case "mobile_remove_cart":
				mobileRemoveCart(request,response);
				break;
				
			case "mobile_cancel_order":
				mobileCancelOrder(request,response);
				break;
				
			case "mobile_estimate_date":
				mobileEstimateDate(request,response);
				break;
				
			case "mobile_arrival_date":
				mobileArrivalDate(request,response);
				break;
				
			case "mobile_receive_order":
				mobileReceiveOrder(request,response);
				break;
				
			default:
				System.out.println("Opps something went wrong");
				break;
		}
	}
	
	@SuppressWarnings("unchecked")
	private void mobileLogin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			
			String username = request.getParameter("username");
			String password = request.getParameter("password");
			
			User user = mdu.login(username, password);
			
			 PrintWriter writer = response.getWriter();
			 JSONObject obj = new JSONObject();
			 JSONArray json = new JSONArray();
			 JSONObject userObj = new JSONObject();
			 
			if(user.getSuccess().equals("1")) {
				
				userObj.put("id", user.getId());
				userObj.put("image", user.getImage());
				userObj.put("fname", user.getFname());
				userObj.put("lname", user.getLname());
				userObj.put("address", user.getAddress());
				userObj.put("email", user.getEmail());
				userObj.put("contact", user.getContact());
				userObj.put("username", user.getUsername());
				
				json.add(userObj);
				
				obj.put("success", "1");
				obj.put("message", "Logged in");
				obj.put("data", json);
				
				writer.write(obj.toString());
				writer.close();
			} else {
				obj.put("success", "0");
				obj.put("message", "Invalid username or password");
				
				writer.write(obj.toString());
				writer.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	private void mobileRegister(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			String image = "user_none.png";
			String fname = request.getParameter("fname");
			String lname = request.getParameter("lname");
			String address = request.getParameter("address");
			String email = request.getParameter("email");
			String contact = request.getParameter("contact");
			String username = request.getParameter("username");
			String password = request.getParameter("password");
			
			// username already exists
			if(mdu.usernameExist(username)==true) {
				 response.setHeader("Content-Type", "text/plain");
				 response.setHeader("success", "yes");
				 PrintWriter writer = response.getWriter();
				 JSONObject obj = new JSONObject();
				 obj.put("success", "0");
				 obj.put("message", "Username already exists");
				 writer.write(obj.toString());
				 writer.close();
				 return;
			}
			
			// email already exisits
			if(mdu.emailExist(email)==true) {
				 response.setHeader("Content-Type", "text/plain");
				 response.setHeader("success", "yes");
				 PrintWriter writer = response.getWriter();
				 JSONObject obj = new JSONObject();
				 obj.put("success", "2");
				 obj.put("message", "Email already exists");
				 writer.write(obj.toString());
			     writer.close();
				 return;
			}
			
			User user = new User(image,fname,lname,address,email,contact,username,password);
			mdu.register(user);
//			response.sendRedirect("users");

			 PrintWriter writer = response.getWriter();
			 JSONObject obj = new JSONObject();
			 obj.put("success", "1");
			 obj.put("message", "You are registered");
			 writer.write(obj.toString());
			 writer.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	private void mobileGetProfile(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
			String id = request.getParameter("id");
			User user = mdu.getUserById(Integer.parseInt(id));
			
			PrintWriter writer = response.getWriter();
			JSONObject obj = new JSONObject();
			JSONArray json = new JSONArray();
			JSONObject userObj = new JSONObject();
			
			if(user.getSuccess().equals("1")) {
				
				userObj.put("id", user.getId());
				userObj.put("image", user.getImage());
				userObj.put("fname", user.getFname());
				userObj.put("lname", user.getLname());
				userObj.put("address", user.getAddress());
				userObj.put("email", user.getEmail());
				userObj.put("contact", user.getContact());
				userObj.put("username", user.getUsername());
				
				json.add(userObj);
				
				obj.put("success", "1");
				obj.put("data", json);
				
				// Set standard HTTP/1.1 no-cache headers.
				response.setHeader("Cache-Control", "private, no-store, no-cache, must-revalidate");

				// Set standard HTTP/1.0 no-cache header.
				response.setHeader("Pragma", "no-cache");
				
				response.setDateHeader("Expires", 0);
				
				writer.write(obj.toString());
				writer.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	@SuppressWarnings("unchecked")
	private void mobileSaveProfile(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
			
			String id = request.getParameter("id");
			String fname = request.getParameter("fname");
			String lname = request.getParameter("lname");
			String address = request.getParameter("address");
			String email = request.getParameter("email");
			String contact = request.getParameter("contact");
			String username = request.getParameter("username");
			String password = request.getParameter("password");
			String image = request.getParameter("image");
			
			User user = new User();
			
			PrintWriter writer = response.getWriter();
			JSONObject obj = new JSONObject();
			
			
			if(image.equals("empty")) {
				
				user.setId(Integer.parseInt(id));			
				user.setImage(image);
				user.setFname(fname);
				user.setLname(lname);
				user.setAddress(address);
				user.setEmail(email);
				user.setContact(contact);
				user.setUsername(username);
				user.setPassword(password);
				
				mdu.saveProfile(user);
				
				obj.put("success", "1");
				obj.put("message", "Successfully saved");
				
				writer.write(obj.toString());
				writer.close();
				
			} else {
				
				String filename = id + randomStr() + ".png";
				user.setId(Integer.parseInt(id));
				user.setImage(filename);
				user.setFname(fname);
				user.setLname(lname);
				user.setAddress(address);
				user.setEmail(email);
				user.setContact(contact);
				user.setUsername(username);
				user.setPassword(password);
				
				// move image to save path
				byte[] decodedBytes = Base64.getDecoder().decode(image);
				
				String appPath = request.getServletContext().getRealPath("");
				String savePath = appPath + File.separator + SAVE_DIR;
				
				
				String path = savePath + File.separator + filename;
				File file = new File(path);
				
				try (OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file))) {
		            outputStream.write(decodedBytes);
		        } catch (IOException e) {
		            e.printStackTrace();
		        }
				
				mdu.saveProfile(user);
				
				obj.put("success", "1");
				obj.put("message", "Successfully saved");
				
				writer.write(obj.toString());
				writer.close();
			}
				
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		
	}
	
	@SuppressWarnings("unchecked")
	private void mobileProdut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
			
			List<Product> productList = mdu.getProducts();
			
			 PrintWriter writer = response.getWriter();
			 JSONObject obj = new JSONObject();
			 JSONArray json = new JSONArray();
			 
			 for(int i=0; i < productList.size(); i++) {
				 
				 JSONObject prodObj = new JSONObject();
				 
				 prodObj.put("id", productList.get(i).getId());
				 prodObj.put("image", productList.get(i).getImage());
				 prodObj.put("name", productList.get(i).getName());
				 prodObj.put("price", productList.get(i).getPrice());
				 prodObj.put("stock", productList.get(i).getStockin());
				 
				 json.add(prodObj);
			 }
			 
			 obj.put("success", "1");
			 obj.put("data", json);
			 
			 System.out.print(obj.toJSONString());
			 
			 writer.write(obj.toString());
			 writer.close();
				
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	private void mobileProductDetails(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
			
			String pid = request.getParameter("pid");
			
			Product product = mdu.getProductById(Integer.parseInt(pid));
			
			PrintWriter writer = response.getWriter();
			JSONObject obj = new JSONObject();
			JSONArray json = new JSONArray();
			
			JSONObject prodObj = new JSONObject();
			
			prodObj.put("id", product.getId());
			prodObj.put("image", product.getImage());
			prodObj.put("name", product.getName());
			prodObj.put("description", product.getDesc());
			prodObj.put("price", product.getPrice());
			prodObj.put("stock", product.getStockin());
			
			json.add(prodObj);
			
			obj.put("success", "1");
			obj.put("data", json);
			
			System.out.println(obj.toJSONString());
			
			writer.write(obj.toJSONString());
			writer.close();
			 
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	private void mobileAddCart(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
			String uid = request.getParameter("uid");
			String pid = request.getParameter("pid");
			String qty = request.getParameter("qty");
			
			PrintWriter writer = response.getWriter();
			JSONObject obj = new JSONObject();
			
			if(mdu.productExist(Integer.parseInt(uid), Integer.parseInt(pid))==true) {
				mdu.productUpdate(Integer.parseInt(uid), Integer.parseInt(pid),Integer.parseInt(qty));
				obj.put("success", "1");
				obj.put("message", "Successfully added");
				writer.write(obj.toString());
				writer.close();
				return;
			}
			
			mdu.addToCart(Integer.parseInt(uid), Integer.parseInt(pid), Integer.parseInt(qty));
			
			obj.put("success", "1");
			obj.put("message", "Successfully added");
			
			writer.write(obj.toString());
			writer.close();
		}  catch(SQLException e) {
			e.printStackTrace();
		}
  	}
	
	@SuppressWarnings("unchecked")
	private void mobileGetCart(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		try {
			
			String uid = request.getParameter("uid");
			List<Cart> cartList = mdu.getCartById(Integer.parseInt(uid));
			
			PrintWriter writer = response.getWriter();
			JSONObject obj = new JSONObject();
			
			if(cartList.isEmpty()) {
				obj.put("success", "0");
			} else {
				JSONArray json = new JSONArray();
				
				for(int i=0; i<cartList.size(); i++) {
					
					JSONObject cartObj = new JSONObject();
					
					cartObj.put("id", cartList.get(i).getId());
					cartObj.put("image", cartList.get(i).getImage());
					cartObj.put("name", cartList.get(i).getName());
					cartObj.put("price", cartList.get(i).getPrice());
					cartObj.put("qty", cartList.get(i).getQty());
					
					json.add(cartObj);
				}
				
				obj.put("success", "1");
				obj.put("data", json);
			}
			
			System.out.println(obj.toJSONString());
			
			writer.write(obj.toString());
			writer.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	@SuppressWarnings("unchecked")
	private void mobilePlaceOrder(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String uid = request.getParameter("uid");
		
		try {
			
			mdu.placeOrder(Integer.parseInt(uid));
			
			PrintWriter writer = response.getWriter();
			JSONObject obj = new JSONObject();
			
			obj.put("success", "1");
			obj.put("message", "You have now placed your order. Go to Order History for more info.");
			
			writer.write(obj.toString());
			writer.close();
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	private void mobileOrderList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		String uid = request.getParameter("uid");
		
		 try {
			
			List<Order> orderList = mdu.getOrderList(Integer.parseInt(uid));
			
			PrintWriter writer = response.getWriter();
			JSONObject obj = new JSONObject();
			
			if(orderList.isEmpty()) {
				obj.put("success", "0");
			} else {
				JSONArray json = new JSONArray();
				
				for(int i=0; i<orderList.size(); i++) {
					
					JSONObject orderObj = new JSONObject();
					
					orderObj.put("orderno", orderList.get(i).getOrderno());
					orderObj.put("date", orderList.get(i).getDate());
					orderObj.put("total", orderList.get(i).getTotal());
					orderObj.put("status", orderList.get(i).getStatus());
					
					json.add(orderObj);
				}
				
				obj.put("success", "1");
				obj.put("data", json);
			}
			
			System.out.println(obj.toJSONString());
			
			writer.write(obj.toString());
			writer.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	private void mobileOrderDetailsList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
			
			String orderno = request.getParameter("orderno");
			List<OrderDetail> orderDetailList = mdu.getOrderDetailList(Integer.parseInt(orderno));
			
			PrintWriter writer = response.getWriter();
			JSONObject obj = new JSONObject();
			
			if(orderDetailList.isEmpty()) {
				obj.put("success", "0");
			} else {
				JSONArray json = new JSONArray();
				
				for(int i=0; i<orderDetailList.size(); i++) {
					
					JSONObject cartObj = new JSONObject();
					
					cartObj.put("id", orderDetailList.get(i).getId());
					cartObj.put("image", orderDetailList.get(i).getImage());
					cartObj.put("name", orderDetailList.get(i).getName());
					cartObj.put("price", orderDetailList.get(i).getPrice());
					cartObj.put("qty", orderDetailList.get(i).getQty());
					
					json.add(cartObj);
				}
				
				obj.put("success", "1");
				obj.put("data", json);
			}
			
			System.out.println(obj.toJSONString());
			
			writer.write(obj.toString());
			writer.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	@SuppressWarnings("unchecked")
	private void mobileRemoveCart(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
			String cid = request.getParameter("cid");
			
			mdu.removeCart(Integer.parseInt(cid));
			
			PrintWriter writer = response.getWriter();
			JSONObject obj = new JSONObject();
			
			obj.put("success", "1");
			obj.put("message", "Removed successfuly");
			
			writer.write(obj.toString());
			writer.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	private void mobileCancelOrder(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
			String orderno = request.getParameter("orderno");
			String reason = request.getParameter("reason");
			
			mdu.cancelOrder(Integer.parseInt(orderno),reason);
			
			PrintWriter writer = response.getWriter();
			JSONObject obj = new JSONObject();
			
			obj.put("success", "1");
			obj.put("message", "You have now cancelled your order");
			
			writer.write(obj.toString());
			writer.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	private void mobileReceiveOrder(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
			String orderno = request.getParameter("orderno");
			
			mdu.receiveOrder(Integer.parseInt(orderno));
			
			PrintWriter writer = response.getWriter();
			JSONObject obj = new JSONObject();
			
			obj.put("success", "1");
			obj.put("message", "You have received your order");
			
			writer.write(obj.toString());
			writer.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	private void mobileEstimateDate(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String finalDate = mdu.getEstimateDate();
		
		PrintWriter writer = response.getWriter();
		JSONObject obj = new JSONObject();
		
		obj.put("success", "1");
		obj.put("finalDate", finalDate);
		
		System.out.println("Estimate Date: " + finalDate);
		
		writer.write(obj.toString());
		writer.close();
	}
	
	@SuppressWarnings("unchecked")
	private void mobileArrivalDate(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String orderno = request.getParameter("orderno");
		String finalDate = mdu.getArrivalDate(Integer.parseInt(orderno));
		
		PrintWriter writer = response.getWriter();
		JSONObject obj = new JSONObject();
		
		obj.put("success", "1");
		obj.put("finalDate", finalDate);
		
		System.out.println("Final Date: " + finalDate);
		
		writer.write(obj.toString());
		writer.close();
	}
	
	@SuppressWarnings("unused")
	private String extractFileName(Part part) {//This method will print the file name.
        String contentDisp = part.getHeader("content-disposition");
        String[] items = contentDisp.split(";");
        for (String s : items) {
            if (s.trim().startsWith("filename")) {
                return s.substring(s.indexOf("=") + 2, s.length() - 1);
            }
        }
        return "";
    }
	
	private String randomStr() {

	    // create a string of uppercase and lowercase characters and numbers
	    String upperAlphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	    String lowerAlphabet = "abcdefghijklmnopqrstuvwxyz";
	    String numbers = "0123456789";

	    // combine all strings
	    String alphaNumeric = upperAlphabet + lowerAlphabet + numbers;

	    // create random string builder
	    StringBuilder sb = new StringBuilder();

	    // create an object of Random class
	    Random random = new Random();

	    // specify length of random string
	    int length = 10;

	    for(int i = 0; i < length; i++) {

	      // generate random index number
	      int index = random.nextInt(alphaNumeric.length());

	      // get character specified by index
	      // from the string
	      char randomChar = alphaNumeric.charAt(index);

	      // append the character to string builder
	      sb.append(randomChar);
	    }

	    String randomString = sb.toString();
	    
	    return randomString;

	}
}
