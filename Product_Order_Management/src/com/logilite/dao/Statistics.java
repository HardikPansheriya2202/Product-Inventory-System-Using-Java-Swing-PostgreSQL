package com.logilite.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.logilite.admin.AdminDashboard;
import com.logilite.supplier.SupplierDashboard;
import com.logilite.user.UserDashboard;

public class Statistics
{
	public static Connection conn;

	// count suppliers
	public static int total(String tableName) {
		int total = 0;
		try
		{
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("SELECT count(*) as total from "+ tableName);
			if (rs.next()) {
				total = rs.getInt(1);
			}
		}
		catch (SQLException e)
		{
			Logger.getLogger(Statistics.class.getName()).log(Level.SEVERE, null, e);
		}
		return total;
	}
	
	public static double totalSales() {
		double total = 0.0;
		try
		{
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("SELECT sum(total) as total from purchase_details");
			if (rs.next()) {
				total = rs.getDouble(1);
			}
		}
		catch (SQLException e)
		{
			Logger.getLogger(Statistics.class.getName()).log(Level.SEVERE, null, e);
		}
		return total;
	}
	
	public static double todaySales() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
		Date date = (Date) new java.util.Date();
		String today = df.format(date);
		
		double total = 0.0;
		try
		{
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("SELECT sum(total) as total from purchase_details where purchase_date = '" + today + "'");
			if (rs.next()) {
				total = rs.getDouble(1);
			}
		}
		catch (SQLException e)
		{
			Logger.getLogger(Statistics.class.getName()).log(Level.SEVERE, null, e);
		}
		return total;
	}
	
	public static double totalPurchase(int uid) {
		double total = 0.0;
		try
		{
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("SELECT sum(total) as total from purchase_details WHERE uid = '" + uid + "'");
			if (rs.next()) {
				total = rs.getDouble(1);
			}
		}
		catch (SQLException e)
		{
			Logger.getLogger(Statistics.class.getName()).log(Level.SEVERE, null, e);
		}
		return total;
	}
	
	public static int totalDeliveries(String name) {
		int total = 0;
		try
		{
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("SELECT count(*) as total from purchase_details WHERE supplier = '" + name + "' AND order_status = 'Received'");
			if (rs.next()) {
				total = rs.getInt(1);
			}
		}
		catch (SQLException e)
		{
			Logger.getLogger(Statistics.class.getName()).log(Level.SEVERE, null, e);
		}
		return total;
	}
	
	public static int pendingDeliveries(String name) {
		int total = 0;
		try
		{
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("SELECT count(*) as total from purchase_details WHERE supplier = '" + name + "' AND order_status = 'On the way'");
			if (rs.next()) {
				total = rs.getInt(1);
			}
		}
		catch (SQLException e)
		{
			Logger.getLogger(Statistics.class.getName()).log(Level.SEVERE, null, e);
		}
		return total;
	}
	
	public void admin() {
		AdminDashboard.lblCategoryValue.setText(String.valueOf(total("category")));
		AdminDashboard.lblProductValue.setText(String.valueOf(total("product")));
		AdminDashboard.lblUsersValue.setText(String.valueOf(total("users")));
		AdminDashboard.lblSupplierValue.setText(String.valueOf(total("supplier")));
		AdminDashboard.lblTotalSalesValue.setText(String.valueOf(totalSales()));
		AdminDashboard.lblSalesValue.setText(String.valueOf(todaySales()));
	}
	
	public void user(int id) {
		UserDashboard.lblcategoryValue.setText(String.valueOf(total("category")));
		UserDashboard.lblProductValue.setText(String.valueOf(total("product")));
		UserDashboard.lblPurchaseValue.setText(String.valueOf(totalPurchase(id)));
	}
	
	public void supplier(String name) {
		SupplierDashboard.lblDeliveriesValue.setText(String.valueOf(totalDeliveries(name)));
	}
}
