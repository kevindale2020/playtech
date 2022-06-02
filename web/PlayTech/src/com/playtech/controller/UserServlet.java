package com.playtech.controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.playtech.model.Product;
import com.playtech.model.User;
import com.playtech.dao.UserDataUtil;

/**
 * Servlet implementation class UserServlet
 */
@WebServlet("/UserServlet")
@MultipartConfig(fileSizeThreshold=1024*1024*2, // 2MB
maxFileSize=1024*1024*10,      // 10MB
maxRequestSize=1024*1024*50)   // 50MB
public class UserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static final String SAVE_DIR = "images\\users";
	private UserDataUtil udu;
	private JSONObject obj;
	private String action;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserServlet() {
        super();
        this.udu = new UserDataUtil();
        this.obj = new JSONObject();
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
		// TODO Auto-generated method stub
		
		action = request.getParameter("action");
		
		System.out.println(action);
		
		if(action==null) {
			action = "loginForm";
		}
		
		switch(action) {
		
			case "register":
				saveUser(request,response);
				break;
				
			case "login":
				loginUser(request,response);
				break;
				
			case "loginForm":
				loginForm(request,response);
				break;
				
			case "admin_profile":
				adminProfile(request,response);
				break;
				
			case "edit_profile":
				editProfile(request,response);
				break;
				
			case "change_password":
				changePassword(request,response);
				break;
				
			case "image_upload":
				imageUpload(request,response);
				break;
				
			case "user_lists":
				userLists(request,response);
				break;
					
			default:
				loginForm(request,response);
				break;
		}
	}
	
	private void loginForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher dispatcher = request.getRequestDispatcher("index.jsp");
		dispatcher.forward(request, response);
	}
	
	@SuppressWarnings("unchecked")
	private void saveUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			String image = "user_none.png";
			String fname = request.getParameter("fname");
			String lname = request.getParameter("lname");
			String address = request.getParameter("address");
			String email = request.getParameter("email");
			String contact = request.getParameter("contact");
			String username = request.getParameter("username");
			String password = request.getParameter("pass1");
			
			// username already exists
			if(udu.usernameExist(username)==true) {
				 response.setHeader("Content-Type", "text/plain");
				 response.setHeader("success", "yes");
				 PrintWriter writer = response.getWriter();
				 obj.put("response", "0");
				 obj.put("message", "Username already exists");
				 writer.write(obj.toString());
				 writer.close();
				return;
			}
			
			// email already exisits
			if(udu.emailExist(email)==true) {
				 response.setHeader("Content-Type", "text/plain");
				 response.setHeader("success", "yes");
				 PrintWriter writer = response.getWriter();
				 obj.put("response", "0");
				 obj.put("message", "Email already exists");
				 writer.write(obj.toString());
				 writer.close();
				return;
			}
			
			User user = new User(image,fname,lname,address,email,contact,username,password);
			udu.register(user);
//			response.sendRedirect("users");

			 response.setHeader("Content-Type", "text/plain");
			 response.setHeader("success", "yes");
			 PrintWriter writer = response.getWriter();
			 obj.put("response", "1");
			 obj.put("message", "You are registered");
			 writer.write(obj.toString());
			 writer.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void userLists(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		List<User> userList = udu.getUsers();
		PrintWriter writer = response.getWriter();
		StringBuilder sb = new StringBuilder();
		sb.append("<div class='card'>"
				+ "<div class='card-body'>"
				+ "<h5 class='card-title'>List of Users</h5>"
				+ "<table class='table' id='dataTable2'>"
				+ "<thead>"
				+ "<tr>"
				+ "<th scope='col'>#</th>"
				+ "<th scope='col'>Image</th>"
				+ "<th scope='col'>Fullname</th>"
				+ "<th scope='col'>Address</th>"
				+ "<th scope='col'>Email</th>"
				+ "<th scope='col'>Contact</th>"
				+ "<th scope='col'>Username</th>"
				+ "<th scope='col'>DateCreated</th>"
				+ "</tr>"
				+ "</thead>"
				+ "<tbody>");
		
		for(int i=0; i<userList.size(); i++) {
			sb.append("<tr>"
					+ "<td>" + userList.get(i).getId()+"</td>"
					+ "<td><img src='../images/users/" + userList.get(i).getImage()+"' width='100' height='100'/></td>"
					+ "<td>" + userList.get(i).getFullname()+"</td>"
					+ "<td>" + userList.get(i).getAddress()+"</td>"
					+ "<td>" + userList.get(i).getEmail()+"</td>"
					+ "<td>" + userList.get(i).getContact()+"</td>"
					+ "<td>" + userList.get(i).getUsername()+"</td>"
					+ "<td>" + userList.get(i).getDate()+"</td>"
					+ "</tr>");
//					+ "<td><button name='active' id='"+userList.get(i).getId()+"' class='btn btn-primary btn-sm edit_product'><i class='fa fa-pencil' aria-hidden='true'></i></button>");
		}
		sb.append("</tbody>"
				+ "</table>"
				+ "</div>"
				+ "</div>");
		writer.write(sb.toString());
		writer.close();
	}
	
	@SuppressWarnings("unchecked")
	private void loginUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			String username = request.getParameter("username");
			String password = request.getParameter("password");
			User user = udu.login(username, password);

			if(user.getSuccess().equals("Success")) {
				 HttpSession session = request.getSession();
				 session.setAttribute("user_id", user.getId());
				 session.setAttribute("username", user.getUsername());
				 response.setHeader("Content-Type", "text/plain");
				 response.setHeader("success", "yes");
				 PrintWriter writer = response.getWriter();
				 obj.put("response", "1");
				 obj.put("message", "Logged in");
				 writer.write(obj.toString());
				 writer.close();
			} else {
				 response.setHeader("Content-Type", "text/plain");
				 response.setHeader("success", "yes");
				 PrintWriter writer = response.getWriter();
				 obj.put("response", "0");
				 obj.put("message", "Invalid username or password");
				 writer.write(obj.toString());
				 writer.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	private void adminProfile(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		try {
			User user = udu.getProfile((int) session.getAttribute("user_id"));
			response.setHeader("Content-Type", "text/plain");
			response.setHeader("success", "yes");
			PrintWriter writer = response.getWriter();
			JSONObject json = new JSONObject();
			json.put("id", user.getId());
			json.put("image", user.getImage());
			json.put("fname", user.getFname());
			json.put("lname", user.getLname());
			json.put("address", user.getAddress());
			json.put("email", user.getEmail());
			json.put("contact", user.getContact());
			json.put("username", user.getUsername());
			System.out.println(json);
			writer.write(json.toString());
			writer.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	private void editProfile(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		try {
			String image = "user_none.png";
			String fname = request.getParameter("fname");
			String lname = request.getParameter("lname");
			String address = request.getParameter("address");
			String email = request.getParameter("email");
			String contact = request.getParameter("contact");
			String username = request.getParameter("username");
			
			User user = new User();
//			session.setAttribute("username", username);
			user.setId((int) session.getAttribute("user_id"));
			user.setFname(fname);
			user.setLname(lname);
			user.setAddress(address);
			user.setEmail(email);
			user.setContact(contact);
			user.setUsername(username);
			udu.editProfile(user);
			
			 PrintWriter writer = response.getWriter();
			 JSONObject json = new JSONObject();
			 json.put("response", "1");
			 json.put("message", "Successfully updated");
			 writer.write(json.toString());
		     writer.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	private void changePassword(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		HttpSession session = request.getSession();
		try {
			String current = request.getParameter("current");
			String newpass = request.getParameter("newpass");
			
			boolean flag = udu.changePassword((int) session.getAttribute("user_id"),current,newpass);
			
			PrintWriter writer = response.getWriter();
			JSONObject json = new JSONObject();
			
			if(flag==true) {
				 json.put("response", "1");
				 json.put("message", "Password successfully updated");
				 writer.write(json.toString());
			     writer.close();
			} else {
				 json.put("response", "0");
				 json.put("message", "This is not your current password");
				 writer.write(json.toString());
			     writer.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}	
	}
	
	@SuppressWarnings("unchecked") 
	private void imageUpload(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		HttpSession session = request.getSession();
		try {
			String old_image = request.getParameter("user_old_image");
			Part part = request.getPart("image");
			// gets absolute path of the web application
	        String appPath = request.getServletContext().getRealPath("");
	        // constructs path of the directory to save uploaded file
	        String savePath = appPath + File.separator + SAVE_DIR;

	        // creates the save directory if it does not exists
	        File fileSaveDir = new File(savePath);
	        if (!fileSaveDir.exists()) {
	            fileSaveDir.mkdir();
	        }
	        
	        // delete current image
	        File deleteFile = new File(savePath + File.separator + old_image);
	        if(!old_image.equals("user_none.png")) {
	        	if(deleteFile.exists()) {
		        	deleteFile.delete();
		        	System.out.println(old_image + " was deleted");
		        }
	        }
	        
	        String fileName = extractFileName(part);
	        part.write(savePath + File.separator + fileName);
	        
	        udu.imageUpload((int) session.getAttribute("user_id"), fileName);
	        
	        PrintWriter writer = response.getWriter();
			JSONObject json = new JSONObject();
			json.put("response", "1");
			json.put("message", "Image successfully updated");
			writer.write(json.toString());
		    writer.close();
	        
	        System.out.println(savePath);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
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

}
