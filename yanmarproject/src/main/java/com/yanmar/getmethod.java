//package com.yanmar;
//
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//
//import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.json.JSONObject;
//
///**
// * Servlet implementation class getmethod
// */
//@WebServlet("/login")
//public class getmethod extends HttpServlet {
//    protected void doGet(HttpServletRequest req, HttpServletResponse res)
//            throws ServletException, IOException {
//
//        res.setHeader("Access-Control-Allow-Origin", "http://localhost:5173");
//        res.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
//        res.setHeader("Access-Control-Allow-Headers", "Content-Type");
//        res.setHeader("Access-Control-Allow-Credentials", "true");
//
//        res.setContentType("application/json");
//        PrintWriter out = res.getWriter();
//
//        String url = "jdbc:mysql://localhost:3306/student";
//        String dbUser = "root";
//        String dbPassword = "Dinesh@8285";
//
//        JSONObject responseJson = new JSONObject();
//        try {
//            Class.forName("com.mysql.cj.jdbc.Driver");
//            Connection conn = DriverManager.getConnection(url, dbUser, dbPassword);
//
//            String sql = "SELECT * FROM information";
//            PreparedStatement ps = conn.prepareStatement(sql);
//            ResultSet rs = ps.executeQuery();
//
//            //org.json.JSONArray users = new org.json.JSONArray();
//
//            while (rs.next()) {
//                JSONObject user = new JSONObject();
//                user.put("id", rs.getInt("id"));
//                user.put("name", rs.getString("name"));
//                user.put("pass", rs.getString("pass")); // Avoid sending password in production!
//                responseJson.put("data", user);
//                out.print(responseJson.toString());
//                //users.put(user);
//            }
//
//            //responseJson.put("status", "success");
//            responseJson.put("data", users);
//
//        } catch (Exception e) {
//            //responseJson.put("status", "error");
//            responseJson.put("message", e.getMessage());
//            e.printStackTrace();
//        }
//        System.out.println("sended data");
//        
//        out.flush();
//    }
//}
