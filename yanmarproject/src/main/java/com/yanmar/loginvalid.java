package com.yanmar;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import org.json.JSONObject;

@WebServlet("/login")
public class loginvalid extends HttpServlet {
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        // CORS Configuration
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
        String username = req.getParameter("loginId");
        String password = req.getParameter("password");
        LocalDate currentDate = LocalDate.now();
        LocalTime time = LocalTime.now();
        String hour = String.valueOf(time.getHour());
        String minute = String.valueOf(time.getMinute());
   

        
        
        System.out.println("Login attempt for: " + username);

        // Database configuration
        String url = "jdbc:sqlserver://192.168.1.4:1433;databaseName=YANMAR;encrypt=true;trustServerCertificate=true";
        String dbUser = "yanmar_user1";
        String dbPassword = "Yanmar@123";


        try {
            System.out.println("Loading driver...");
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            
            System.out.println("Connecting to database...");
            try (Connection conn = DriverManager.getConnection(url, dbUser, dbPassword)) {
                System.out.println("Database connected!");
                
                String sql = "SELECT * FROM core_usermaster WHERE name = ? AND password = ?";
                String times="update core_usermaster set lastlogindate =? , lastlogintime= ? where name= ?";
                System.out.println("Executing: " + sql);
                
                try (PreparedStatement ps = conn.prepareStatement(sql); 
                		PreparedStatement ps1 = conn.prepareStatement(times)) {
                	
                    ps.setString(1, username);
                    ps.setString(2, password);
                    
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            System.out.println("Login successful for: " + username);
                            System.out.println("Localtime" + (hour+":"+minute)+"LocalDate"+currentDate);
                   		 ps1.setString(1, String.valueOf(currentDate));
                   		 ps1.setString(2,(hour+":"+minute));
                   		 ps1.setString(3,username);
                   		 ps1.executeUpdate();
                            jsonResponse.put("role", rs.getInt("roleid"));
                            jsonResponse.put("message","successfull");

                             jsonResponse.put("user_id", rs.getInt("id"));

                        } else {
                            System.out.println("Invalid credentials for: " + username);
                            jsonResponse.put("status", "error");
                            jsonResponse.put("message", "Invalid username or password");
                
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error in servlet: " + e.getMessage());
            e.printStackTrace();
            jsonResponse.put("status", "error");
            jsonResponse.put("message", "Server error: " + e.getMessage());
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

        System.out.println("Sending response: " + jsonResponse.toString());
        out.print(jsonResponse.toString());
        out.flush();
    }
}