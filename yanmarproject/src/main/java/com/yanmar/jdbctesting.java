package com.yanmar;

import java.beans.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class jdbctesting {
	 public static void main(String[] args) {
		 LocalDate currentDate = LocalDate.now();
	        //LocalTime currentTime = LocalTime.now();
	        LocalTime time = LocalTime.now();
	       // String hour = String.valueOf(time.getHour());
	        //String minute = String.valueOf(time.getMinute());
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm");
	        String time12 = time.format(formatter);
	        System.out.println(time12);
		 
	        String url = "jdbc:sqlserver://den1.mssql7.gear.host:1433;databaseName=sample23;encrypt=true;trustServerCertificate=true";
	        String user = "sample23";
	        String password = "Ou8BJ63-st!B";
		 try {
	            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

	            Connection conn = DriverManager.getConnection(url, user, password);
	            System.out.println("✅ Database connected successfully!");

	            String sql="Select* from core_external_user";
	            PreparedStatement pst=conn.prepareStatement(sql);
	            ResultSet res=pst.executeQuery();
	            while(res.next()) {
	            	System.out.println(res.getString("vendorcode"));
	            	System.out.println(res.getString("email"));
	            	System.out.println(res.getString("username"));
	            	
	            }
	            conn.close();
	        } catch (ClassNotFoundException e) {
	            System.out.println("❌ JDBC Driver not found.");
	            e.printStackTrace();
	        } catch (SQLException e) {
	            System.out.println("❌ Failed to connect to database.");
	            e.printStackTrace();
	        }
	    }
}
