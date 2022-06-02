package com.playtech.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.playtech.model.Report;

public class ReportDataUtil {
	
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
	
	public List<Report> getReportList(String start, String end) {
		
		List<Report> reportList = new ArrayList<>();
		
		try {
			Connection connection = get_connection();
			String sql = "Select o.order_no, o.order_date, concat(u.fname,' ',u.lname) as `customer`, group_concat(od.qty,' ',if(od.qty>1,'pcs','pc'),' ',p.name) as `purchased`, sum(od.qty*p.price) as `total` from tbl_orders o inner join tbl_users u on u.user_id = o.user_id inner join tbl_order_details od on od.order_no = o.order_no inner join tbl_products p on p.product_id = od.product_id where o.order_date between ? and ? and o.status_id = 3 group by o.order_no";
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setString(1, start);
			ps.setString(2, end);
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
				int orderno = rs.getInt("order_no");
				String date = rs.getString("order_date");
				String customer = rs.getString("customer");
				String purchased = rs.getString("purchased");
				double total = rs.getDouble("total");
				
				reportList.add(new Report(orderno,date,customer,purchased,total));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return reportList;
	}
}
