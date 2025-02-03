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
import com.logilite.user.Purchase;

public class ProductDao
{
	static Connection conn = Login.conn;

	// get product table max row
	public int getMaxRow() {
		int row = 0;
		try
		{
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("SELECT max(pid) from product");
			while(rs.next()) {
				row = rs.getInt(1);
			}
		}
		catch (SQLException e)
		{
			Logger.getLogger(ProductDao.class.getName()).log(Level.SEVERE, null, e);
		}
		return row + 1;
	}
	
	// count categories
	public int countCategories() {
		int total = 0;
		try
		{
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("SELECT count(*) as total from category");
			if (rs.next()) {
				total = rs.getInt(1);
			}
		}
		catch (SQLException e)
		{
			Logger.getLogger(ProductDao.class.getName()).log(Level.SEVERE, null, e);
		}
		return total;
	}
	
	// get categories
	public String[] getCategories() {
		String[] categories = new String[countCategories()];
		try
		{
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("SELECT * FROM category");
			int i = 0;
			while(rs.next()) {
				categories[i] = rs.getString(2);
				i++;
			}
		}
		catch (SQLException e)
		{
			Logger.getLogger(ProductDao.class.getName()).log(Level.SEVERE, null, e);
		}
		return categories;
	}
	
	// check product id already exists
	public boolean isIDExists(int id) {
		try
		{
			PreparedStatement pst = conn.prepareStatement("SELECT * FROM product WHERE pid = ?");
			pst.setInt(1, id);
			ResultSet rs = pst.executeQuery();
			
			if (rs.next()) {
				return true;
			}
		}
		catch (SQLException e)
		{
			Logger.getLogger(ProductDao.class.getName()).log(Level.SEVERE, null, e);
		} 
		return false;
	}
	
	// check product and category already exists
	public boolean isProductCategoryExists(String pro, String cat) {
		try
		{
			PreparedStatement pst = conn.prepareStatement("SELECT * FROM product WHERE pname = ? AND cname = ?");
			pst.setString(1, pro);
			pst.setString(2, cat);
			ResultSet rs = pst.executeQuery();
			
			if (rs.next()) {
				return true;
			}
		}
		catch (SQLException e)
		{
			Logger.getLogger(ProductDao.class.getName()).log(Level.SEVERE, null, e);
		} 
		return false;
	}
	
	// insert product into product table
	public void insert(int id, String pname, String cname, int quantity, double price) {
		String query = "INSERT INTO product VALUES (?, ?, ?, ?, ?)";
		
		try
		{
			PreparedStatement pst = conn.prepareStatement(query);
			pst.setInt(1, id);
			pst.setString(2, pname);
			pst.setString(3, cname);
			pst.setInt(4, quantity);
			pst.setDouble(5, price);
			if (pst.executeUpdate() > 0) {
				JOptionPane.showMessageDialog(null, "Product added successfully");
			}
		}
		catch (SQLException e)
		{
			Logger.getLogger(ProductDao.class.getName()).log(Level.SEVERE, null, e);
		}
	}
	
	// insert product into cart table
	public void insertIntoCart(int userId, int productId, String pname, int quantity, double price, double total, double total_discount, double grand_total) {
		String query = "INSERT INTO cart (user_id, product_id, product_name, quantity, price, total, total_discount, grand_total) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
		
		try
		{
			PreparedStatement pst = conn.prepareStatement(query);
			pst.setInt(1, userId);
			pst.setInt(2, productId);
			pst.setString(3, pname);
			pst.setInt(4, quantity);
			pst.setDouble(5, price);
			pst.setDouble(6, total);
			pst.setDouble(7, total_discount);
			pst.setDouble(8, grand_total);
			if (pst.executeUpdate() > 0) {
				JOptionPane.showMessageDialog(null, "Product added successfully");
			}
		}
		catch (SQLException e)
		{
			Logger.getLogger(ProductDao.class.getName()).log(Level.SEVERE, null, e);
		}
	}
	
	// insert product into wishlist table
	public void insertIntoWishlist(int userId, int productId, String pname, int quantity, double price) {
		String query = "INSERT INTO wishlist (user_id, product_id, product_name, quantity, price) VALUES (?, ?, ?, ?, ?)";
		
		try
		{
			PreparedStatement pst = conn.prepareStatement(query);
			pst.setInt(1, userId);
			pst.setInt(2, productId);
			pst.setString(3, pname);
			pst.setInt(4, quantity);
			pst.setDouble(5, price);
			if (pst.executeUpdate() > 0) {
				JOptionPane.showMessageDialog(null, "Product added in wishlist");
			}
		}
		catch (SQLException e)
		{
			Logger.getLogger(ProductDao.class.getName()).log(Level.SEVERE, null, e);
		}
	}
	
	// get product data
	public void getProductData(JTable table, String search) {
		String query = "SELECT * FROM product WHERE CONCAT(pid, pname, cname) like ? order by pid asc";
		try
		{
			PreparedStatement pst = conn.prepareStatement(query);
			pst.setString(1, "%"+search+"%");
			
			ResultSet rs = pst.executeQuery();
			
			DefaultTableModel model = (DefaultTableModel) table.getModel();
			Object [] row;
			while(rs.next()) {
				row = new Object[4];
				row[0] = rs.getInt(1);
				row[1] = rs.getString(2);
				row[2] = rs.getString(3);
				row[3] = rs.getDouble(5);
				model.addRow(row);
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	// get product data
	public void getAllProductData(JTable table, String search) {
		String query = "SELECT * FROM product WHERE CONCAT(pid, pname, cname) like ? order by pid asc";
		try
		{
			PreparedStatement pst = conn.prepareStatement(query);
			pst.setString(1, "%"+search+"%");
			
			ResultSet rs = pst.executeQuery();
			
			DefaultTableModel model = (DefaultTableModel) table.getModel();
			Object [] row;
			while(rs.next()) {
				row = new Object[5];
				row[0] = rs.getInt(1);
				row[1] = rs.getString(2);
				row[2] = rs.getString(3);
				row[3] = rs.getInt(4);
				row[4] = rs.getDouble(5);
				model.addRow(row);
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	// get product data
	public void getCartData(JTable table, int uId, String search) {
		String query = "SELECT * FROM cart WHERE CONCAT(id, user_id, product_id, product_name) like ? AND user_id = ? order by user_id asc";
		try
		{
			PreparedStatement pst = conn.prepareStatement(query);
			pst.setString(1, "%"+search+"%");
			pst.setInt(2, uId);
			
			ResultSet rs = pst.executeQuery();
			
			DefaultTableModel model = (DefaultTableModel) table.getModel();
			Object [] row;
			while(rs.next()) {
				row = new Object[6];
				row[0] = Purchase.pId;
				row[1] = rs.getInt("product_id");
				row[2] = rs.getString("product_name");
				row[3] = rs.getInt("quantity");
				row[4] = rs.getDouble("price");
				row[5] = rs.getDouble("grand_total");
				model.addRow(row);
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	// get wishlist data
	public void getWishListData(JTable table, int uid, String search) {
		String query = "SELECT w.product_id, w.product_name, p.cname, w.quantity, w.price FROM wishlist w JOIN product p ON w.product_id = p.pid WHERE CONCAT(id, product_id, user_id, product_name) like ? AND w.user_id = ? order by w.product_id asc";
		try
		{
			PreparedStatement pst = conn.prepareStatement(query);
			pst.setString(1, "%"+search+"%");
			pst.setInt(2, uid);
			
			ResultSet rs = pst.executeQuery();
			
			DefaultTableModel model = (DefaultTableModel) table.getModel();
			Object [] row;
			while(rs.next()) {
				row = new Object[5];
				row[0] = rs.getInt("product_id");
				row[1] = rs.getString("product_name");
				row[2] = rs.getString("cname");
				row[3] = rs.getInt("quantity");
				row[4] = rs.getDouble("price");
				model.addRow(row);
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	
		
	// update category data
	public void update(int id, String pname, String cname, int quantity, double price) {
		String query = "UPDATE product SET pname = ?, cname = ?, pqty = ?, pprice = ? WHERE pid = ?";
		
		try
		{
			PreparedStatement pst = conn.prepareStatement(query);
			pst.setString(1, pname);
			pst.setString(2, cname);
			pst.setInt(3, quantity);
			pst.setDouble(4, price);
			pst.setInt(5, id);
			if(pst.executeUpdate() > 0) {
				JOptionPane.showMessageDialog(null, "Product data successfully updated");
			}
		}
		catch (SQLException e)
		{
			Logger.getLogger(ProductDao.class.getName()).log(Level.SEVERE, null, e);		
		}
	}
	
	// delete product data
	public void delete(int id) {
		int x = JOptionPane.showConfirmDialog(null, "Are you sure to delete this product?", "Delete Product", JOptionPane.OK_CANCEL_OPTION, 0);
		if (x == JOptionPane.OK_OPTION) {
			try
			{
				PreparedStatement pst = conn.prepareStatement("DELETE FROM product WHERE pid = ?");
				pst.setInt(1, id);
				
				if (pst.executeUpdate() > 0) {
					JOptionPane.showMessageDialog(null, "Product deleted");
				}
			}
			catch (SQLException e)
			{
				Logger.getLogger(ProductDao.class.getName()).log(Level.SEVERE, null, e);
			}
		}
	}
	
	// delete cart data
	public static void deleteCart(int id) {
		String query = "DELETE FROM cart WHERE user_id = ?";
		try
		{
			PreparedStatement pst = conn.prepareStatement(query);
			pst.setInt(1, id);
			
			if (pst.executeUpdate() > 0) {
				JOptionPane.showMessageDialog(null, "cart deleted");
			}
		}
		catch (SQLException e)
		{
			Logger.getLogger(ProductDao.class.getName()).log(Level.SEVERE, null, e);
		}
	}
	
	// check cart product exist or not
	public static boolean isproductExists(int proId, int uId) {
		String query = "SELECT product_id from cart WHERE user_id = ?";
		try
		{
			PreparedStatement pst = conn.prepareStatement(query);
			pst.setInt(1, uId);
			
			ResultSet rs = pst.executeQuery();
			while(rs.next()) {
				if (rs.getInt(1) == proId) {
					return true;
				}
			}
		}
		catch (SQLException e)
		{
			Logger.getLogger(ProductDao.class.getName()).log(Level.SEVERE, null, e);
		}
		return false;
	}
	
	// check wishlist product exist or not
	public static boolean isWishlistproductExists(int proId, int uId) {
		String query = "SELECT product_id from wishlist WHERE user_id = ?";
		try
		{
			PreparedStatement pst = conn.prepareStatement(query);
			pst.setInt(1, uId);
			
			ResultSet rs = pst.executeQuery();
			while(rs.next()) {
				if (rs.getInt(1) == proId) {
					return true;
				}
			}
		}
		catch (SQLException e)
		{
			Logger.getLogger(ProductDao.class.getName()).log(Level.SEVERE, null, e);
		}
		return false;
	}
	
	// update cart product
	public static void updateCartProduct(int proId, int uId, int quantity, Double total_discount, Double grand_total) {
		String query = "UPDATE cart SET quantity = quantity + ?, total = ?, total_discount = ?, grand_total = ? WHERE user_id = ? AND product_id = ?";
		String query2 = "SELECT quantity, price FROM cart WHERE user_id = ? AND product_id = ?";
		try
		{
			int qty = 0;
			double price = 0.0;
			PreparedStatement pst1 = conn.prepareStatement(query2);
			pst1.setInt(1, uId);
			pst1.setInt(2, proId);
			ResultSet rs = pst1.executeQuery();
			if (rs.next()) {
				qty = rs.getInt(1);
				price = rs.getDouble(2);
			}
			qty += quantity;
			double t = price * (double) qty;
			
			PreparedStatement pst = conn.prepareStatement(query);
			pst.setInt(1, quantity);
			pst.setDouble(2, t);
			pst.setDouble(3, total_discount);
			pst.setDouble(4, grand_total);
			pst.setInt(5, uId);
			pst.setInt(6, proId);
			if(pst.executeUpdate() > 0) {
				JOptionPane.showMessageDialog(null, "Product added successfully");
			}
		}
		catch (SQLException e)
		{
			Logger.getLogger(ProductDao.class.getName()).log(Level.SEVERE, null, e);		
		}
	}
	
	// delete cart data
	public static void deleteSpecificCartItem(int id, int proId) {
		String query = "DELETE FROM cart WHERE user_id = ? AND product_id = ?";
		try
		{
			PreparedStatement pst = conn.prepareStatement(query);
			pst.setInt(1, id);
			pst.setInt(2, proId);
			
			if (pst.executeUpdate() > 0) {
				JOptionPane.showMessageDialog(null, "Cart Item deleted");
			}
		}
		catch (SQLException e)
		{
			Logger.getLogger(ProductDao.class.getName()).log(Level.SEVERE, null, e);
		}
	}
	
	// delete wishlist data
	public static void deleteSpecificWishlistItem(int id, int proId) {
		String query = "DELETE FROM wishlist WHERE user_id = ? AND product_id = ?";
		try
		{
			PreparedStatement pst = conn.prepareStatement(query);
			pst.setInt(1, id);
			pst.setInt(2, proId);
			
			if (pst.executeUpdate() > 0) {
				JOptionPane.showMessageDialog(null, "Wishlist Item deleted");
			}
		}
		catch (SQLException e)
		{
			Logger.getLogger(ProductDao.class.getName()).log(Level.SEVERE, null, e);
		}
	}
	
	//get product actual quantity
	public static int getProductQuantity(int id) {
		String query = "SELECT pqty from product where pid = ?";
		try
		{
			PreparedStatement pst = conn.prepareStatement(query);
			pst.setInt(1, id);
			
			ResultSet rs = pst.executeQuery();
			if (rs.next()) {
				return rs.getInt(1);
			}
		}
		catch (SQLException e)
		{
			Logger.getLogger(ProductDao.class.getName()).log(Level.SEVERE, null, e);
		}
		return 0;
	}
	
	// set Discount
	public static void setDiscount(int discount) {
		String query = "UPDATE admin SET discount = ?";
		try
		{
			PreparedStatement pst = conn.prepareStatement(query);
			pst.setInt(1, discount);
			
			if (pst.executeUpdate() > 0) {
				JOptionPane.showMessageDialog(null, "Discount set successfully");
			}
		}
		catch (SQLException e)
		{
			Logger.getLogger(ProductDao.class.getName()).log(Level.SEVERE, null, e);
		}
	}
	
	//get Discount
	public static int getDiscount() {
		String query = "SELECT discount FROM admin";
		try
		{
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(query);
			
			if (rs.next()) {
				return rs.getInt(1);
			}
		}
		catch (SQLException e)
		{
			Logger.getLogger(ProductDao.class.getName()).log(Level.SEVERE, null, e);
		}
		return 0;
	}
	
	// update cart respect to discount value
	public static void updatecartUsingDiscount(int discount) {
		String selectCart = "SELECT * FROM cart";
		String updateCart = "UPDATE cart SET total_discount = ?, grand_total = ? WHERE user_id = ? AND product_id = ?";
		
		try
		{
			Statement st = conn.createStatement();
			PreparedStatement pst = conn.prepareStatement(updateCart);
			ResultSet rs = st.executeQuery(selectCart);
			while(rs.next()) {
				int user_id = rs.getInt("user_id");
				int product_id = rs.getInt("product_id");
				Double total = rs.getDouble("total");
				Double totalDiscount = (total * (double) discount) / 100;
				Double grandTotal = total - totalDiscount;
				
				pst.setDouble(1, totalDiscount);
				pst.setDouble(2, grandTotal);
				pst.setInt(3, user_id);
				pst.setInt(4, product_id);
				
				pst.execute();
			}
		}
		catch (SQLException e)
		{
			Logger.getLogger(ProductDao.class.getName()).log(Level.SEVERE, null, e);
		}
	}
	
	// select qunatity from cart
	public static int getQuantity(int user_id, int productId) {
		String query = "SELECT quantity FROM cart WHERE user_id = ? AND product_id = ?";
		
		try
		{
			PreparedStatement pst = conn.prepareStatement(query);
			pst.setInt(1, user_id);
			pst.setInt(2, productId);
			
			ResultSet rSet = pst.executeQuery();
			if (rSet.next()) {
				return rSet.getInt(1);
			}
		}
		catch (SQLException e)
		{
			Logger.getLogger(ProductDao.class.getName()).log(Level.SEVERE, null, e);
		}
		return 0;
	}
}
