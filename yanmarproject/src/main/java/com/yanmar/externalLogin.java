package com.yanmar;
import java.time.LocalDate;
import java.time.LocalTime;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;


@WebServlet("/externalLogin")
public class externalLogin extends HttpServlet {
	
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		res.setHeader("Access-Control-Allow-Origin", "http://localhost:5173");
    	res.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS");
    	res.setHeader("Access-Control-Allow-Headers", "Content-Type");
    	res.setHeader("Access-Control-Allow-Credentials", "true");
    	if ("OPTIONS".equalsIgnoreCase(req.getMethod())) {
    	    res.setStatus(HttpServletResponse.SC_OK);
    	    return;
    	}
    	
    	
    	 res.setContentType("application/json");
         PrintWriter out = res.getWriter();
         JSONObject jsonResponse = new JSONObject();

         // Get parameters
         String vendor_code = req.getParameter("VendorId");
         String username = req.getParameter("loginId");
         String vend_pass=req.getParameter("password");

         LocalDate currentDate = LocalDate.now();
         LocalTime time = LocalTime.now();
         String hour = String.valueOf(time.getHour());
         String minute = String.valueOf(time.getMinute());
         
         
         System.out.println("Login attempt for: " + vendor_code);

         String url = "jdbc:sqlserver://192.168.1.4:1433;databaseName=YANMAR;encrypt=true;trustServerCertificate=true";
         String dbUser = "yanmar_user1";
         String dbPassword = "Yanmar@123";
         
         try {
             System.out.println("Loading driver...");
             Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
             //Class.forName("com.mysql.cj.jdbc.Driver");

             
             System.out.println("Connecting to database...");
             try (Connection conn = DriverManager.getConnection(url, dbUser, dbPassword)) {
                 System.out.println("Database connected!");
                 
                 String sql = "SELECT * FROM core_external_user WHERE vendorcode = ? AND username = ? AND password = ?";
                 System.out.println("Executing: " + sql);
                 //System.out.println(vendor_code+username+vend_pass);
                 String times="update core_external_user set lastlogindate =? , lastlogintime= ? where vendorcode= ?";
                 
                 try (PreparedStatement ps = conn.prepareStatement(sql)
                		) {
                     ps.setString(1, vendor_code);
                     ps.setString(2, username);
                     ps.setString(3, vend_pass);
                     
                     try (ResultSet rs = ps.executeQuery();
                    		 PreparedStatement ps1 = conn.prepareStatement(times)) {
                         if (rs.next()) {
                        	 int active=rs.getInt("isactive");
                        	 if(active==1) {
                        		 System.out.println("Login successful for: " + vendor_code);
                        		 System.out.println("Localtime" + hour+minute+"LocalDate"+currentDate);
                        		 ps1.setString(1, String.valueOf(currentDate));
                        		 ps1.setString(2,(hour+":"+minute));
                        		 ps1.setString(3,vendor_code);
                        		 ps1.executeUpdate();
                                 jsonResponse.put("status", "success");
                        	 }
                        	 else {
                        		 System.out.println("Invalid credentials for: " + vendor_code);
                        		 jsonResponse.put("status", "error");
                        		 jsonResponse.put("message","inactive");
                        	 }
                             
                         } else {
                             System.out.println("Invalid credentials for: " + vendor_code);
                             jsonResponse.put("status", "error");
                             jsonResponse.put("message", "Invalid username or password");
                         }
                     }
                 }
             }
         }
         catch (Exception e) {
             System.err.println("Error in servlet: " + e.getMessage());
             e.printStackTrace();
             jsonResponse.put("status", "error");
             jsonResponse.put("message", "Server error: " + e.getMessage());
             //res.setStatus(HttpServletResponse.SC_INTERNAL_SERV4ER_ERROR);
         }
         
         System.out.println("Sending response: " + jsonResponse.toString());
         out.print(jsonResponse.toString());
         out.flush();


	}

}
