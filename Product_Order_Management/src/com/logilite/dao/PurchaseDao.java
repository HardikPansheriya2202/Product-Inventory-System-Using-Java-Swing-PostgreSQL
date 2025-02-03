package com.logilite.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.logilite.user.Login;

public class PurchaseDao
{
	Connection conn = Login.conn;
	 
	// get purchase table max row
	public int getMaxRow() {
		int row = 0;
		try
		{
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("SELECT max(id) from purchase_details");
			while(rs.next()) {
				row = rs.getInt(1);
			}
		}
		catch (SQLException e)
		{
			Logger.getLogger(PurchaseDao.class.getName()).log(Level.SEVERE, null, e);
		}
		return row + 1;
	}
	
	// get purchase table max row
	public double getTotalAmount(int uId) {
		double total = 0.0;
		try
		{
			String query = "SELECT sum(total) from cart WHERE user_id = ?";
			PreparedStatement pst = conn.prepareStatement(query);
			pst.setInt(1, uId);
			ResultSet rs = pst.executeQuery();
			while(rs.next()) {
				total = rs.getDouble(1);
			}
		}
		catch (SQLException e)
		{
			Logger.getLogger(PurchaseDao.class.getName()).log(Level.SEVERE, null, e);
		}
		return total;
	}
	
	// get user value
	
	public String[] getUserValue(String email) {
		String[] data = new String[5];
		String sql = "select uid, uname, uphone, uaddress1, uaddress2 from users where uemail = '" + email +"'";
		
		try
		{
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			
			if (rs.next()) {
				data[0] = rs.getString(1);
				data[1] = rs.getString(2);
				data[2] = rs.getString(3);
				data[3] = rs.getString(4);
				data[4] = rs.getString(5);
			}
		}
		catch (SQLException e)
		{
			Logger.getLogger(PurchaseDao.class.getName()).log(Level.SEVERE, null, e);
		}
		return data;
	}
	
	// get product quantity
	public int getQty(int pid) {
		int qty = 0;
		
		try
		{
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("select pqty from product where pid = "+ pid + "");
			if (rs.next()) {
				qty = rs.getInt(1);
			}
		}
		catch (SQLException e)
		{
			Logger.getLogger(PurchaseDao.class.getName()).log(Level.SEVERE, null, e);
		}
		return qty;
	}
	
	// update product quantity
	public void qtyUpdate(int pid, int qty) {
		String sql = "update product set pqty = ? where pid = ?";
		try
		{
			PreparedStatement pst = conn.prepareStatement(sql);
			pst.setInt(1, qty);
			pst.setInt(2, pid);
			pst.executeUpdate();
		}
		catch (SQLException e)
		{
			Logger.getLogger(PurchaseDao.class.getName()).log(Level.SEVERE, null, e);
		}
	}
	
	// set supplier status
	public void setSupplierStatus(String order_id, String supplier, String status) {
		String query = "UPDATE purchase_details set supplier = ?, order_status = ? where order_id = ?";
		
		try
		{
			PreparedStatement pst = conn.prepareStatement(query);
			pst.setString(1, supplier);
			pst.setString(2, status);
			pst.setString(3, order_id);
			
			if (pst.executeUpdate() > 0) {
				JOptionPane.showMessageDialog(null, "Supplier successfully selected..");
			}
		}
		catch (SQLException e)
		{
			Logger.getLogger(PurchaseDao.class.getName()).log(Level.SEVERE, null, e);
		}
	}
	
	// set date status
		public void setDateStatus(int id, String receivedDate, String status) {
			String query = "UPDATE purchase_details set receive_date = ?, order_status = ? where id = ?";
			
			try
			{
				PreparedStatement pst = conn.prepareStatement(query);
				pst.setString(1, receivedDate);
				pst.setString(2, status);
				pst.setInt(3, id);
				
				if (pst.executeUpdate() > 0) {
					JOptionPane.showMessageDialog(null, "Product successfully delivered..");
				}
			}
			catch (SQLException e)
			{
				Logger.getLogger(PurchaseDao.class.getName()).log(Level.SEVERE, null, e);
			}
		}
	
	// get specific user purchased product
	public void getProductData(JTable table, String search, int uId) {
		String query = "select pd.id, om.order_id, od.pid, od.product_name, od.qty, od.price, od.total, pd.purchase_date, "
				+ "pd.receive_date, pd.supplier, pd.order_status FROM "
				+ "purchase_details pd JOIN "
				+ "orders_master om ON pd.order_id = om.order_id JOIN "
				+ "orders_details od ON om.order_id = od.order_id WHERE "
				+ "pd.order_id = om.order_id AND CONCAT(pd.id, pd.uid, pd.uname) like ? AND od.uid = ? order by id asc";
		try
		{
			PreparedStatement pst = conn.prepareStatement(query);
			pst.setString(1, "%"+search+"%");
			pst.setInt(2, uId);
			
			ResultSet rs = pst.executeQuery();
			
			DefaultTableModel model = (DefaultTableModel) table.getModel();
			Object [] row;
			while(rs.next()) {
				row = new Object[11];
				row[0] = rs.getInt("id");
				row[1] = rs.getString("order_id");
				row[2] = rs.getInt("pid");
				row[3] = rs.getString("product_name");
				row[4] = rs.getInt("qty");
				row[5] = rs.getDouble("price");
				row[6] = rs.getDouble("total");
				row[7] = rs.getString("purchase_date");
				row[8] = rs.getString("receive_date");
				row[9] = rs.getString("supplier");
				row[10] = rs.getString("order_status");
				model.addRow(row);
			}
		}
		catch (SQLException e)
		{
			Logger.getLogger(PurchaseDao.class.getName()).log(Level.SEVERE, null, e);
		}
	}
	
	// get specific user purchased product
	public void getProductData(JTable table, String search) {
		String query = "SELECT * FROM purchase_details WHERE CONCAT(id, uid, uname) like ? AND order_status = 'Pending' AND payment_status = 'success' order by id asc";
		try
		{
			PreparedStatement pst = conn.prepareStatement(query);
			pst.setString(1, "%"+search+"%");				
			ResultSet rs = pst.executeQuery();
			
			DefaultTableModel model = (DefaultTableModel) table.getModel();
			Object [] row;
			while(rs.next()) {
				row = new Object[10];
				row[0] = rs.getString(2);
				row[1] = rs.getInt(3);
				row[2] = rs.getString(4);
				row[3] = rs.getString(5);
				row[4] = rs.getDouble(6);
				row[5] = rs.getString(7);
				row[6] = rs.getString(8);
				row[7] = rs.getString(9);
				row[8] = rs.getString(10);
				row[9] = rs.getString(11);
				model.addRow(row);
			}
		}
		catch (SQLException e)
		{
			Logger.getLogger(PurchaseDao.class.getName()).log(Level.SEVERE, null, e);
		}
	}
	
	// get all on the way purchase products
	public void getOnTheWayPurchaseProduct(JTable table, String search, String supplier) {
		String query = "select pd.id, om.order_id, u.uid, u.uname, u.uphone, pd.total, pd.purchase_date, "
				+ "u.uaddress1 || ' ' || u.uaddress2 AS Address, pd.receive_date, "
				+ "pd.supplier, pd.order_status FROM "
				+ "purchase_details pd JOIN "
				+ "orders_master om ON pd.order_id = om.order_id JOIN "
				+ "users u ON pd.uid = u.uid WHERE "
				+ "pd.order_id = om.order_id AND CONCAT(pd.id, pd.uid, pd.uname) like ? AND pd.supplier = ? AND pd.order_status = 'On the way' order by id asc";
		try
		{
			PreparedStatement pst = conn.prepareStatement(query);
			pst.setString(1, "%"+search+"%");
			pst.setString(2, supplier);
			ResultSet rs = pst.executeQuery();
			
			DefaultTableModel model = (DefaultTableModel) table.getModel();
			Object [] row;
			while(rs.next()) {
				row = new Object[11];
				row[0] = rs.getInt("id");
				row[1] = rs.getString("order_id");
				row[2] = rs.getInt("uid");
				row[3] = rs.getString("uname");
				row[4] = rs.getString("uphone");
				row[5] = rs.getDouble("total");
				row[6] = rs.getString("purchase_date");
				row[7] = rs.getString("Address");
				row[8] = rs.getString("receive_date");
				row[9] = rs.getString("supplier");
				row[10] = rs.getString("order_status");
				model.addRow(row);
			}
		}
		catch (SQLException e)
		{
			Logger.getLogger(PurchaseDao.class.getName()).log(Level.SEVERE, null, e);
		}
	}
	
	// get supplier delivered products
	public void getSupplierDeliveredProducts(JTable table, String search, String supplier) {
		String query = "select pd.id, om.order_id, u.uid, u.uname, u.uphone, pd.total, pd.purchase_date, "
				+ "u.uaddress1 || ' ' || u.uaddress2 AS Address, pd.receive_date, "
				+ "pd.supplier, pd.order_status FROM "
				+ "purchase_details pd JOIN "
				+ "orders_master om ON pd.order_id = om.order_id JOIN "
				+ "users u ON pd.uid = u.uid WHERE "
				+ "pd.order_id = om.order_id AND CONCAT(pd.id, pd.uid, pd.uname) like ? AND pd.supplier = ? AND pd.order_status = 'Received' order by id asc";
		try
		{
			PreparedStatement pst = conn.prepareStatement(query);
			pst.setString(1, "%"+search+"%");
			pst.setString(2, supplier);
			ResultSet rs = pst.executeQuery();
			
			DefaultTableModel model = (DefaultTableModel) table.getModel();
			Object [] row;
			while(rs.next()) {
				row = new Object[11];
				row[0] = rs.getInt("id");
				row[1] = rs.getString("order_id");
				row[2] = rs.getInt("uid");
				row[3] = rs.getString("uname");
				row[4] = rs.getString("uphone");
				row[5] = rs.getDouble("total");
				row[6] = rs.getString("purchase_date");
				row[7] = rs.getString("Address");
				row[8] = rs.getString("receive_date");
				row[9] = rs.getString("supplier");
				row[10] = rs.getString("order_status");
				model.addRow(row);
			}
		}
		catch (SQLException e)
		{
			Logger.getLogger(PurchaseDao.class.getName()).log(Level.SEVERE, null, e);
		}
	}
	
	// get transactions
	public void transaction(JTable table, String search) {
		String query = "SELECT * FROM payment_details WHERE CONCAT(payment_id, uid, uname) like ? AND payment_status = 'success' order by payment_date desc";
		try
		{
			PreparedStatement pst = conn.prepareStatement(query);
			pst.setString(1, "%"+search+"%");
			ResultSet rs = pst.executeQuery();
			
			DefaultTableModel model = (DefaultTableModel) table.getModel();
			Object [] row;
			while(rs.next()) {
				row = new Object[8];
				row[0] = rs.getString(1);
				row[1] = rs.getString(2);
				row[2] = rs.getInt(3);
				row[3] = rs.getString(4);
				row[4] = rs.getString(5);
				row[5] = rs.getDouble(6);
				row[6] = rs.getString(7);
				row[7] = rs.getString(8);
				model.addRow(row);
			}
		}
		catch (SQLException e)
		{
			Logger.getLogger(PurchaseDao.class.getName()).log(Level.SEVERE, null, e);
		}
	}

	
	// refund product
	public void refund(int id) {
		int x = JOptionPane.showConfirmDialog(null, "Are you sure to refund this product?", "Refund Account", JOptionPane.OK_CANCEL_OPTION, 0);
		if (x == JOptionPane.OK_OPTION) {
			try
			{
				PreparedStatement pst = conn.prepareStatement("DELETE FROM purchase_details WHERE id = ?");
				pst.setInt(1, id);
				
				if (pst.executeUpdate() > 0) {
					JOptionPane.showMessageDialog(null, "Product refund succeed");
				}
			}
			catch (SQLException e)
			{
				Logger.getLogger(PurchaseDao.class.getName()).log(Level.SEVERE, null, e);
			}
		}
	}
	
	// get User id
	public int getId(String email) {
		String query = "SELECT uid from users where uemail = ?";
		
		try
		{
			PreparedStatement pst = conn.prepareStatement(query);
			pst.setString(1, email);
						
			ResultSet rs = pst.executeQuery();
			if (rs.next()) {
				return rs.getInt(1);
			}
		}
		catch (SQLException e)
		{
			Logger.getLogger(PurchaseDao.class.getName()).log(Level.SEVERE, null, e);
		}
		return 0;
	}
}
