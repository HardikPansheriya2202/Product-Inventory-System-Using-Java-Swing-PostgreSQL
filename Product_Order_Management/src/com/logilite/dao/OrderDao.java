package com.logilite.dao;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONObject;

import com.logilite.stripe.StripeCheckoutBackend;
import com.logilite.user.Login;

public class OrderDao {
    Connection conn = Login.conn;

    PurchaseDao purchase = new PurchaseDao();
    public static String payment_status = "";
    // Insert data into orders_details table
    public void insertOrdersDetails(String order_id, int uid, int pid, String pname, int qty, 
            double price, double total) {
        String sql = "insert into orders_details (order_id, uid, pid, product_name, qty, price, total) values (?,?,?,?,?,?,?)";
        try {
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, order_id);
            pst.setInt(2, uid);
            pst.setInt(3, pid);
            pst.setString(4, pname);
            pst.setInt(5, qty);
            pst.setDouble(6, price);
            pst.setDouble(7, total);
            pst.executeUpdate();
        } catch (SQLException e) {
            Logger.getLogger(OrderDao.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    // Insert data into orders_master table
    public void insertOrdersMaster(String order_id, int uid, String uname, String uphone,
            String uaddress, double total, String orderDate) {
        String sql = "insert into orders_master values (?,?,?,?,?,?,?)";
        try {
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, order_id);
            pst.setInt(2, uid);
            pst.setString(3, uname);
            pst.setString(4, uphone);
            pst.setString(5, uaddress);
            pst.setDouble(6, total);
            pst.setString(7, orderDate);
            pst.executeUpdate();
        } catch (SQLException e) {
            Logger.getLogger(OrderDao.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    // Insert data into purchase details and payment details table
    public void insertPurchaseDetails(String order_id, int uid, String uname, 
            String uphone, double total, String purchase_date, String receive_date, 
            String supplier, String order_status) {

        String userHome = System.getProperty("user.home");
        String downloadsFolder = userHome + "/Downloads";
        String paymentStatusFilePath = downloadsFolder + "/paymentStatus.json";
        Path path = Paths.get(paymentStatusFilePath);
        String paymentId = "";

        if (Files.exists(path)) {
            try {
                String fileContent = new String(Files.readAllBytes(path));
                JSONObject paymentData = new JSONObject(fileContent);

                paymentId = paymentData.getString("paymentId");
                payment_status = paymentData.getString("status");
            } catch (Exception e) {
                Logger.getLogger(OrderDao.class.getName()).log(Level.SEVERE, "Error reading or parsing paymentStatus.json", e);
            }
        } else {
            Logger.getLogger(OrderDao.class.getName()).log(Level.SEVERE, "paymentStatus.json file not found");
        }

        
        
        String sql = "insert into purchase_details (order_id, uid, uname, uphone, total, purchase_date, receive_date, supplier, order_status, payment_status) values (?,?,?,?,?,?,?,?,?,?)";
        String sql1 = "insert into payment_details values (?,?,?,?,?,?,?,?)";
        try {
            if (payment_status.equals("success")) {
            	PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1, order_id);
                pst.setInt(2, uid);
                pst.setString(3, uname);
                pst.setString(4, uphone);
                pst.setDouble(5, total);
                pst.setString(6, purchase_date);
                pst.setString(7, receive_date);
                pst.setString(8, supplier);
                pst.setString(9, order_status);
                pst.setString(10, payment_status);
                pst.executeUpdate();
            }
            
            PreparedStatement pst1 = conn.prepareStatement(sql1);
            pst1.setString(1, paymentId);
            pst1.setString(2, order_id);
            pst1.setInt(3, uid);
            pst1.setString(4, uname);
            pst1.setString(5, uphone);
            pst1.setDouble(6, total);
            pst1.setString(7, purchase_date);
            pst1.setString(8, payment_status);
            pst1.executeUpdate();
        } catch (SQLException e) {
            Logger.getLogger(OrderDao.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    
 // Insert data into purchase details and payment details table
    public void insertStripePurchaseDetails(String order_id, int uid, String uname, 
            String uphone, double total, String purchase_date, String receive_date, 
            String supplier, String order_status) {

        String paymentId = String.valueOf(System.currentTimeMillis());
        payment_status = StripeCheckoutBackend.payment_Status;

        String sql = "insert into purchase_details (order_id, uid, uname, uphone, total, purchase_date, receive_date, supplier, order_status, payment_status) values (?,?,?,?,?,?,?,?,?,?)";
        String sql1 = "insert into payment_details values (?,?,?,?,?,?,?,?)";
        try {
            if (payment_status.equals("succeeded")) {
            	PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1, order_id);
                pst.setInt(2, uid);
                pst.setString(3, uname);
                pst.setString(4, uphone);
                pst.setDouble(5, total);
                pst.setString(6, purchase_date);
                pst.setString(7, receive_date);
                pst.setString(8, supplier);
                pst.setString(9, order_status);
                pst.setString(10, "success");
                pst.executeUpdate();
                
                PreparedStatement pst1 = conn.prepareStatement(sql1);
                pst1.setString(1, paymentId);
                pst1.setString(2, order_id);
                pst1.setInt(3, uid);
                pst1.setString(4, uname);
                pst1.setString(5, uphone);
                pst1.setDouble(6, total);
                pst1.setString(7, purchase_date);
                pst1.setString(8, "success");
                pst1.executeUpdate();
            }else {
            	PreparedStatement pst1 = conn.prepareStatement(sql1);
            	pst1.setString(1, paymentId);
            	pst1.setString(2, order_id);
            	pst1.setInt(3, uid);
            	pst1.setString(4, uname);
            	pst1.setString(5, uphone);
            	pst1.setDouble(6, total);
            	pst1.setString(7, purchase_date);
            	pst1.setString(8, payment_status);
            	pst1.executeUpdate();            	
            }
            
        } catch (SQLException e) {
            Logger.getLogger(OrderDao.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    
}
