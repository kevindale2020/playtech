package com.playtech.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.playtech.dao.OrderDataUtil;
import com.playtech.dao.ProductDataUtil;
import com.playtech.dao.ReportDataUtil;
import com.playtech.model.Order;
import com.playtech.model.OrderDetail;
import com.playtech.model.Product;
import com.playtech.model.Report;

import javax.servlet.http.Part;

/**
 * Servlet implementation class AdminServlet
 */
@WebServlet("/AdminServlet")
@MultipartConfig(fileSizeThreshold=1024*1024*2, // 2MB
maxFileSize=1024*1024*10,      // 10MB
maxRequestSize=1024*1024*50)   // 50MB
public class AdminServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static final String SAVE_DIR = "images\\products";
	private String action;
	private ProductDataUtil pdu;
	private JSONObject obj;
	private String fileName;
	private OrderDataUtil odu;
	private ReportDataUtil rdu;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AdminServlet() {
        super();
        // TODO Auto-generated constructor stub
        this.pdu = new ProductDataUtil();
        this.action = null;
        this.obj = new JSONObject();
        this.fileName = "";
        this.odu = new OrderDataUtil();
        this.rdu = new ReportDataUtil();
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
		
		// debugging purposes
		System.out.println(action);
		
		if(action==null) {
			action = "home";
		}
		
		switch(action) {
			
			case "home":
				homePage(request,response);
				break;
				
			case "new_product":
				newProduct(request,response);
				break;
				
			case "product_list":
				productList(request,response);
				break;
				
			case "product_details":
				productDetails(request,response);
				break;
				
			case "save_product":
				saveProduct(request,response);
				break;
				
			case "delete_product":
				deleteProduct(request,response);
				break;
				
			case "admin_orders":
				adminOrders(request,response);
				break;
				
			case "admin_order_details":
				adminOrderDetails(request,response);
				break;
				
			case "admin_cancel_orders":
				adminCancelOrders(request,response);
				break;
				
			case "admin_confirm_orders":
				adminConfirmOrders(request,response);
				break;
				
			case "admin_closed_orders":
				adminClosedOrders(request,response);
				break;
				
			case "admin_confirm_order":
				adminConfirmOrder(request,response);
				break;
				
			case "admin_set_delivery":
				adminSetDelivery(request,response);
				break;
				
			case "admin_generate_report":
				adminGenerateReport(request,response);
				break;
				
			case "logout":
				logoutUser(request,response);
				break;
				
			default:
				homePage(request,response);
				break;
		}
		
	}
	
	private void homePage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		RequestDispatcher dispatcher = request.getRequestDispatcher("admin/index.jsp");
		dispatcher.forward(request, response);
	}
	
//	private void userPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		RequestDispatcher dispatcher = request.getRequestDispatcher("admin/users.jsp");
//		dispatcher.forward(request, response);
//	}
	@SuppressWarnings("unchecked")
	private void newProduct(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			boolean flag = false;
//			String image = "product_none.png";
			String name = request.getParameter("name");
			String desc = request.getParameter("desc");
			String price = request.getParameter("price");
			String qty = request.getParameter("qty");
			
			if(pdu.nameExist(name)==true) {
				response.setHeader("Content-Type", "text/plain");
				response.setHeader("success", "yes");
				PrintWriter writer = response.getWriter();
				obj.put("response", "0");
				obj.put("message", "Product already exists");
				writer.write(obj.toJSONString());
				writer.close();
				return;
			}
			
			if(request.getPart("image").getSize()>0) {
				flag = true;
			}
			
			if(request.getPart("image").getSize()<=0) {
				flag = false;
			}
			
			if(flag==true) {
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
		        
		        fileName = extractFileName(part);
		        part.write(savePath + File.separator + fileName);
		        
		        System.out.println(savePath);
		        
		        Product product = new Product();
				product.setImage(fileName);
				product.setName(name);
				product.setDesc(desc);
				product.setPrice(Double.parseDouble(price));
				product.setStockin(Integer.parseInt(qty));
				pdu.addProduct(product);
				
				response.setHeader("Content-Type", "text/plain");
				response.setHeader("success", "yes");
				PrintWriter writer = response.getWriter();
				obj.put("response", "1");
				obj.put("message", "New product has been added");
				writer.write(obj.toJSONString());
				writer.close();
		        
			} else {
				fileName = "product_none.png";
				Product product = new Product();
				product.setImage(fileName);
				product.setName(name);
				product.setDesc(desc);
				product.setPrice(Double.parseDouble(price));
				product.setStockin(Integer.parseInt(qty));
				pdu.addProduct(product);
				
				response.setHeader("Content-Type", "text/plain");
				response.setHeader("success", "yes");
				PrintWriter writer = response.getWriter();
				obj.put("response", "1");
				obj.put("message", "New product has been added");
				writer.write(obj.toJSONString());
				writer.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void productList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		List<Product> productList = pdu.getProducts();
		PrintWriter writer = response.getWriter();
		StringBuilder sb = new StringBuilder();
		sb.append("<div class='card'>"
				+ "<div class='card-body'>"
				+ "<h5 class='card-title'>List of Products</h5>"
				+ "<table class='table' id='dataTable'>"
				+ "<thead>"
				+ "<tr>"
				+ "<th scope='col'>#</th>"
				+ "<th scope='col'>Image</th>"
				+ "<th scope='col'>Name</th>"
				+ "<th scope='col'>Description</th>"
				+ "<th scope='col'>Price</th>"
				+ "<th scope='col'>Stock</th>"
				+ "<th scope='col'>Sold</th>"
				+ "<th scope='col'></th>"
				+ "<th scope='col'></th>"
				+ "</tr>"
				+ "</thead>"
				+ "<tbody>");
		
		for(int i=0; i<productList.size(); i++) {
			sb.append("<tr>"
					+ "<td>" + productList.get(i).getId()+"</td>"
					+ "<td><img src='../images/products/" + productList.get(i).getImage()+"' width='100' height='100'/></td>"
					+ "<td>" + productList.get(i).getName()+"</td>"
					+ "<td>" + productList.get(i).getDesc()+"</td>"
					+ "<td>" + productList.get(i).getPrice()+"</td>"
					+ "<td>" + productList.get(i).getStockin()+"</td>"
					+ "<td>" + productList.get(i).getStockout()+"</td>"
					+ "<td><button name='edit_product' id='"+productList.get(i).getId()+"' class='btn btn-primary btn-sm edit_product'><i class='fa fa-pencil' aria-hidden='true'></i></button>"
					+ "<td><button name='delete_product' id='"+productList.get(i).getId()+"' class='btn btn-danger btn-sm delete_product'><i class='fa fa-trash' aria-hidden='true'></i></button></td>"
					+ "</tr>");
		}
		sb.append("</tbody>"
				+ "</table>"
				+ "</div>"
				+ "</div>");
		writer.write(sb.toString());
		writer.close();
	}
	
    @SuppressWarnings("unchecked")
	private void productDetails(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		int pid = Integer.parseInt(request.getParameter("pid"));
		
		if(pid==0) {
			return;
		}
//		
		Product product = pdu.details(pid);
		System.out.println(product.getName());
		PrintWriter writer = response.getWriter();
		
		obj.put("pid", product.getId());
		obj.put("image", product.getImage());
		obj.put("name", product.getName());
		obj.put("desc", product.getDesc());
		obj.put("price", product.getPrice());
		obj.put("stockin", product.getStockin());
		
		writer.write(obj.toJSONString());
		writer.close();
		
		System.out.println("Product ID: " + pid);
		
	}
    
    @SuppressWarnings("unchecked")
	private void saveProduct(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
    	try {
    		boolean flag = false;
    		String id = request.getParameter("pid");
    		String name = request.getParameter("cname");
    		String desc = request.getParameter("cdesc");
    		String stockin = request.getParameter("cstock");
    		String price = request.getParameter("cprice");
    		String old_image = request.getParameter("product_old_image");
    		
    		if(request.getPart("image").getSize()>0) {
				flag = true;
				System.out.println("File not empty");
    		}
			
			if(request.getPart("image").getSize()<=0) {
				flag = false;
				System.out.println("File empty");
			}
			
			if(flag==true) {
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
		        
		        fileName = extractFileName(part);
		        part.write(savePath + File.separator + fileName);
		        
		        System.out.println(savePath);
		        
		        // delete current image
		        File deleteFile = new File(savePath + File.separator + old_image);
		        if(deleteFile.exists()) {
		        	deleteFile.delete();
		        	System.out.println(old_image + " was deleted");
		        }
		        
		        Product product = new Product();
	    		product.setId(Integer.parseInt(id));
	    		product.setImage(fileName);
	    		product.setName(name);
	    		product.setDesc(desc);
	    		product.setStockin(Integer.parseInt(stockin));
	    		product.setPrice(Double.parseDouble(price));
	    		
	    		pdu.saveProduct(product);
	    		
	    		PrintWriter writer = response.getWriter();
				obj.put("response", "1");
				obj.put("message", "Successfully updated");
				writer.write(obj.toJSONString());
				writer.close();
			} else {
				fileName = old_image;
				Product product = new Product();
	    		product.setId(Integer.parseInt(id));
	    		product.setImage(fileName);
	    		product.setName(name);
	    		product.setDesc(desc);
	    		product.setStockin(Integer.parseInt(stockin));
	    		product.setPrice(Double.parseDouble(price));
	    		
	    		pdu.saveProduct(product);
	    		
	    		PrintWriter writer = response.getWriter();
				obj.put("response", "1");
				obj.put("message", "Successfully updated");
				writer.write(obj.toJSONString());
				writer.close();
			}
			
    	} catch(SQLException e) {
    		e.printStackTrace();
    	}
	}
    
    @SuppressWarnings("unchecked")
    private void deleteProduct(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
    	try {
    		String id = request.getParameter("pid");
    		pdu.deleteProduct(id);
    		
    		PrintWriter writer = response.getWriter();
//    		JSONObject obj = new JSONObject();
			obj.put("response", "1");
			obj.put("message", "Successfully deleted");
			writer.write(obj.toJSONString());
			writer.close();
			
    	} catch (SQLException e) {
    		e.printStackTrace();
    	}
	}
    
    private void adminOrders(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    	List<Order> orderList = odu.getOrderList();
		PrintWriter writer = response.getWriter();
		StringBuilder sb = new StringBuilder();
		sb.append("<div class='card'>"
				+ "<div class='card-body'>"
				+ "<h5 class='card-title'>Pending Orders</h5>"
				+ "<table class='table' id='dataTable3'>"
				+ "<thead>"
				+ "<tr>"
				+ "<th scope='col'>#</th>"
				+ "<th scope='col'>Customer</th>"
				+ "<th scope='col'>Contact</th>"
				+ "<th scope='col'>DeliveryAddress</th>"
				+ "<th scope='col'>OrderDate</th>"
				+ "<th scope='col'>Delivery Period</th>"
				+ "<th scope='col'>Status</th>"
				+ "<th scope='col'></th>"
				+ "<th scope='col'></th>"
				+ "</tr>"
				+ "</thead>"
				+ "<tbody>");
		
		for(int i=0; i<orderList.size(); i++) {
			sb.append("<tr>"
					+ "<td>" + orderList.get(i).getOrderno() + "</td>"
					+ "<td>" + orderList.get(i).getCustomer()+"</td>"
					+ "<td>" + orderList.get(i).getContact()+"</td>"
					+ "<td>" + orderList.get(i).getAddress()+"</td>"
					+ "<td>" + orderList.get(i).getDate()+"</td>"
					+ "<td>" + orderList.get(i).getDate3()+"</td>"
					+ "<td>" + orderList.get(i).getStatus()+"</td>"
					+ "<td><button name='confirm_order' id='"+orderList.get(i).getOrderno()+"' class='btn btn-success btn-sm confirm_order'>Confirm</button></td>"
					+ "<td><button name='order_details' id='"+orderList.get(i).getOrderno()+"' class='btn btn-info btn-sm order_details'>Details</button></td>"
					+ "</tr>");
		}
		sb.append("</tbody>"
				+ "</table>"
				+ "</div>"
				+ "</div>");
		writer.write(sb.toString());
		writer.close();
		
	}
    
    private void adminCancelOrders(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
    	List<Order> orderList = odu.getCancelOrderList();
		PrintWriter writer = response.getWriter();
		StringBuilder sb = new StringBuilder();
		sb.append("<div class='card'>"
				+ "<div class='card-body'>"
				+ "<h5 class='card-title'>Cancelled Orders</h5>"
				+ "<table class='table' id='dataTable4'>"
				+ "<thead>"
				+ "<tr>"
				+ "<th scope='col'>#</th>"
				+ "<th scope='col'>Customer</th>"
				+ "<th scope='col'>Contact</th>"
				+ "<th scope='col'>DeliveryAddress</th>"
				+ "<th scope='col'>OrderDate</th>"
				+ "<th scope='col'>DeliveryPeriod</th>"
				+ "<th scope='col'>Status</th>"
				+ "<th scope='col'>CancelledDate</th>"
				+ "<th scope='col'>Reason</th>"
				+ "<th scope='col'></th>"
				+ "</tr>"
				+ "</thead>"
				+ "<tbody>");
		
		for(int i=0; i<orderList.size(); i++) {
			sb.append("<tr>"
					+ "<td>" + orderList.get(i).getOrderno() + "</td>"
					+ "<td>" + orderList.get(i).getCustomer()+"</td>"
					+ "<td>" + orderList.get(i).getContact()+"</td>"
					+ "<td>" + orderList.get(i).getAddress()+"</td>"
					+ "<td>" + orderList.get(i).getDate()+"</td>"
					+ "<td>" + orderList.get(i).getDate3()+"</td>"
					+ "<td>" + orderList.get(i).getStatus()+"</td>"
					+ "<td>" + orderList.get(i).getDate2()+"</td>"
					+ "<td>" + orderList.get(i).getReason()+"</td>"
					+ "<td><button name='order_details' id='"+orderList.get(i).getOrderno()+"' class='btn btn-info btn-sm order_details'>Details</button></td>"
					+ "</tr>");
		}
		sb.append("</tbody>"
				+ "</table>"
				+ "</div>"
				+ "</div>");
		writer.write(sb.toString());
		writer.close();
	}
    
    private void adminConfirmOrders(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
    	List<Order> orderList = odu.getConfirmOrderList();
		PrintWriter writer = response.getWriter();
		StringBuilder sb = new StringBuilder();
		sb.append("<div class='card'>"
				+ "<div class='card-body'>"
				+ "<h5 class='card-title'>Confirmed Orders</h5>"
				+ "<table class='table' id='dataTable5'>"
				+ "<thead>"
				+ "<tr>"
				+ "<th scope='col'>#</th>"
				+ "<th scope='col'>Customer</th>"
				+ "<th scope='col'>Contact</th>"
				+ "<th scope='col'>DeliveryAddress</th>"
				+ "<th scope='col'>OrderDate</th>"
				+ "<th scope='col'>DeliveryPeriod</th>"
				+ "<th scope='col'>Status</th>"
				+ "<th scope='col'>ConfirmedDate</th>"
				+ "<th scope='col'></th>"
				+ "<th scope='col'></th>"
				+ "</tr>"
				+ "</thead>"
				+ "<tbody>");
		
		for(int i=0; i<orderList.size(); i++) {
			sb.append("<tr>"
					+ "<td>" + orderList.get(i).getOrderno() + "</td>"
					+ "<td>" + orderList.get(i).getCustomer()+"</td>"
					+ "<td>" + orderList.get(i).getContact()+"</td>"
					+ "<td>" + orderList.get(i).getAddress()+"</td>"
					+ "<td>" + orderList.get(i).getDate()+"</td>"
					+ "<td>" + orderList.get(i).getDate3()+"</td>"
					+ "<td>" + orderList.get(i).getStatus()+"</td>"
					+ "<td>" + orderList.get(i).getDate2()+"</td>");
					if(orderList.get(i).getStatus().equals("Confirmed")) {
						sb.append("<td><button name='set_delivery' id='"+orderList.get(i).getOrderno()+"' class='btn btn-success btn-sm set_delivery'>Deliver</button></td>");
					}
					sb.append("<td><button name='order_details' id='"+orderList.get(i).getOrderno()+"' class='btn btn-info btn-sm order_details'>Details</button></td>"
					+ "</tr>");
		}
		sb.append("</tbody>"
				+ "</table>"
				+ "</div>"
				+ "</div>");
		writer.write(sb.toString());
		writer.close();
	}
    
    private void adminClosedOrders(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
    	List<Order> orderList = odu.getClosedOrderList();
		PrintWriter writer = response.getWriter();
		StringBuilder sb = new StringBuilder();
		sb.append("<div class='card'>"
				+ "<div class='card-body'>"
				+ "<h5 class='card-title'>Closed Orders</h5>"
				+ "<table class='table' id='dataTable6'>"
				+ "<thead>"
				+ "<tr>"
				+ "<th scope='col'>#</th>"
				+ "<th scope='col'>Customer</th>"
				+ "<th scope='col'>Contact</th>"
				+ "<th scope='col'>DeliveryAddress</th>"
				+ "<th scope='col'>OrderDate</th>"
				+ "<th scope='col'>DeliveryPeriod</th>"
				+ "<th scope='col'>Status</th>"
				+ "<th scope='col'>ClosedDate</th>"
				+ "<th scope='col'></th>"
				+ "</tr>"
				+ "</thead>"
				+ "<tbody>");
		
		for(int i=0; i<orderList.size(); i++) {
			sb.append("<tr>"
					+ "<td>" + orderList.get(i).getOrderno() + "</td>"
					+ "<td>" + orderList.get(i).getCustomer()+"</td>"
					+ "<td>" + orderList.get(i).getContact()+"</td>"
					+ "<td>" + orderList.get(i).getAddress()+"</td>"
					+ "<td>" + orderList.get(i).getDate()+"</td>"
					+ "<td>" + orderList.get(i).getDate3()+"</td>"
					+ "<td>" + orderList.get(i).getStatus()+"</td>"
					+ "<td>" + orderList.get(i).getDate2()+"</td>"
					+ "<td><button name='order_details' id='"+orderList.get(i).getOrderno()+"' class='btn btn-info btn-sm order_details'>Details</button></td>"
					+ "</tr>");
		}
		sb.append("</tbody>"
				+ "</table>"
				+ "</div>"
				+ "</div>");
		writer.write(sb.toString());
		writer.close();
	}
    
    private void adminOrderDetails(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	
    	String id = request.getParameter("orderno");
    	
    	double total = 0;
    	double subtotal = 0;
    	
    	List<OrderDetail> orderDetailList = odu.getOrderDetailsById(Integer.parseInt(id));
		PrintWriter writer = response.getWriter();
		StringBuilder sb = new StringBuilder();
		sb.append("<div class='card'>"
				+ "<div class='card-body'>"
				+ "<h5 class='card-title'>Order Details</h5>"
				+ "<table class='table'>"
				+ "<thead>"
				+ "<tr>"
				+ "<th scope='col'>#</th>"
				+ "<th scope='col'>Image</th>"
				+ "<th scope='col'>Name</th>"
				+ "<th scope='col'>Price</th>"
				+ "<th scope='col'>Quantity</th>"
				+ "<th scope='col'>Subtotal</th>"
				+ "</tr>"
				+ "</thead>"
				+ "<tbody>");
		
		for(int i=0; i<orderDetailList.size(); i++) {
			subtotal = orderDetailList.get(i).getPrice() * orderDetailList.get(i).getQty();
			total += subtotal;
			sb.append("<tr>"
					+ "<td>" + orderDetailList.get(i).getId() + "</td>"
					+ "<td><img src='../images/products/" + orderDetailList.get(i).getImage()+"' width='100' height='100'/></td>"
					+ "<td>" + orderDetailList.get(i).getName()+"</td>"
					+ "<td>" + orderDetailList.get(i).getPrice()+"</td>"
					+ "<td>" + orderDetailList.get(i).getQty()+"</td>"
					+ "<td>" + subtotal + "</td>"
					+ "</tr>");
		}
		sb.append("</tbody>"
				+ "</table>"
				+ "</div>"
				+ "<div class='card-footer'><div class='pull-right' style='margin: 5px;'>Total: <b>" + total + "</b></div></div>"
				+ "</div>");
		writer.write(sb.toString());
		writer.close();
		
		System.out.println(sb.toString());
	}
    
    @SuppressWarnings("unchecked")
	private void adminConfirmOrder(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException  {
    	
    	try {
    		
    		String id = request.getParameter("orderno");
    		odu.confirmOrder(Integer.parseInt(id));
    		
    		PrintWriter writer = response.getWriter();
    		JSONObject obj = new JSONObject();
			obj.put("response", "1");
			obj.put("message", "Order confirmed");
			writer.write(obj.toJSONString());
			writer.close();
    	} catch (SQLException e) {
    		e.printStackTrace();
    	}
		
	}
    
    @SuppressWarnings("unchecked")
    private void adminSetDelivery(HttpServletRequest request, HttpServletResponse response)  throws ServletException, IOException {
		
    	try {
    		
    		String id = request.getParameter("orderno");
    		odu.setDelivery(Integer.parseInt(id));
    		
    		PrintWriter writer = response.getWriter();
    		JSONObject obj = new JSONObject();
			obj.put("response", "1");
			obj.put("message", "Order#"+id+" is now out for delivery");
			writer.write(obj.toJSONString());
			writer.close();
    	} catch (SQLException e) {
    		e.printStackTrace();
    	}
		
	}
    
    private void adminGenerateReport(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
    	String start_date = request.getParameter("start_date");
		String end_date = request.getParameter("end_date");
		
		double total = 0;
		
		List<Report> reportList = rdu.getReportList(start_date, end_date);
		PrintWriter writer = response.getWriter();
		StringBuilder sb = new StringBuilder();
		sb.append("<div class='card'>"
				+ "<div class='card-body'>"
				+ "<h5 style='text-align: center;'class='card-title'>List of Sales between " + start_date+ " and " + end_date +"</h5>"
				+ "<table class='table'>"
				+ "<thead>"
				+ "<tr>"
				+ "<th scope='col'>#</th>"
				+ "<th scope='col'>Date</th>"
				+ "<th scope='col'>Customer</th>"
				+ "<th scope='col'>Purchased</th>"
				+ "<th scope='col'>Total</th>"
				+ "</tr>"
				+ "</thead>"
				+ "<tbody>");
		
		for(int i=0; i<reportList.size(); i++) {
			total += reportList.get(i).getTotal();
			sb.append("<tr>"
					+ "<td>" + reportList.get(i).getOrderno() + "</td>"
					+ "<td>" + reportList.get(i).getDate()+"</td>"
					+ "<td>" + reportList.get(i).getCustomer()+"</td>"
					+ "<td>" + reportList.get(i).getPurchased()+"</td>"
					+ "<td>" + reportList.get(i).getTotal()+"</td>"
					+ "</tr>");
		}
		sb.append("</tbody>"
				+ "</table>"
				+ "</div>"
				+ "<div class='card-footer'><div class='pull-right' style='margin: 5px;'>Total Sales: <b>" + total + "</b></div></div>"
				+ "</div>");
		System.out.println(sb.toString());
		writer.write(sb.toString());
		writer.close();
	}
	
	private void logoutUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		session.invalidate();
		response.sendRedirect("index.jsp");
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