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
public class UserDao
{
	Connection conn = Login.conn;
	ForgotPasswordDao forgotPasswordDao = new ForgotPasswordDao();
	
	// get user table max row
	public int getMaxRow() {
		int row = 0;
		try
		{
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("SELECT max(uid) from users");
			while(rs.next()) {
				row = rs.getInt(1);
			}
		}
		catch (SQLException e)
		{
			Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, e);
		}
		return row + 1;
	}
	
	// check email already exists
	public boolean isEmailExists(String email) {
		try
		{
			PreparedStatement pst = conn.prepareStatement("SELECT * FROM users WHERE uemail = ?");
			pst.setString(1, email);
			ResultSet rs = pst.executeQuery();
			
			if (rs.next()) {
				return true;
			}
		}
		catch (SQLException e)
		{
			Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, e);
		} 
		return false;
	}
	
	// check phone number already exists
	public boolean isPhoneExists(String phone) {
		try
		{
			PreparedStatement pst = conn.prepareStatement("SELECT * FROM users WHERE uphone = ?");
			pst.setString(1, phone);
			ResultSet rs = pst.executeQuery();
			
			if (rs.next()) {
				return true;
			}
		}
		catch (SQLException e)
		{
			Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, e);
		} 
		return false;
	}
	
	// insert data into user table
	public void insert(int id, String name, String email, String password, String phone, String sque, String ans, String add1, String add2) {
		String query = "INSERT INTO users VALUES (?,?,?,?,?,?,?,?,?)";
		
		try
		{
			PreparedStatement pst = conn.prepareStatement(query);
			pst.setInt(1, id);
			pst.setString(2, name);
			pst.setString(3, email);
			pst.setString(4, password);
			pst.setString(5, phone);
			pst.setString(6, sque);
			pst.setString(7, ans);
			pst.setString(8, add1);
			pst.setString(9, add2);
			
			if (pst.executeUpdate() > 0) {
				JOptionPane.showMessageDialog(null, "User added successfully");
			}
		}
		catch (SQLException e)
		{
			Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, e);
		}
	}
	
	// get User Data
	public String[] getUserData(int id) {
		String data[] = new String[9];
		try
		{
			PreparedStatement pst = conn.prepareStatement("SELECT * FROM users WHERE uid = ?");
			pst.setInt(1, id);
			ResultSet rs = pst.executeQuery();
			
			if (rs.next()) {
				data[0] = rs.getString(1);
				data[1] = rs.getString(2);
				data[2] = rs.getString(3);
				data[3] = rs.getString(4);
				data[4] = rs.getString(5);
				data[5] = rs.getString(6);
				data[6] = rs.getString(7);
				data[7] = rs.getString(8);
				data[8] = rs.getString(9);
			}
		}
		catch (SQLException e)
		{
			Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, e);
		} 
		return data;
	}
	
	// get User ID
	public int getUserId(String email) {
		int id = 0;
		try
		{
			PreparedStatement pst = conn.prepareStatement("SELECT uid FROM users WHERE uemail = ?");
			pst.setString(1, email);
			ResultSet rs = pst.executeQuery();
			
			if (rs.next()) {
				id = rs.getInt(1);
			}
		}
		catch (SQLException e)
		{
			Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, e);
		} 
		return id;
	}
	
	// update user data
	public void update(int id, String name, String email, String password, String phone, String sque, String ans, String add1, String add2) {
		String query = "UPDATE users SET uname = ?, uemail = ?, upassword = ?, uphone = ?, usecqus = ?, uans = ?, uaddress1 = ?, uaddress2 = ? WHERE uid = ?";
		
		try
		{
			PreparedStatement pst = conn.prepareStatement(query);
			pst.setString(1, name);
			pst.setString(2, email);
			pst.setString(3, password);
			pst.setString(4, phone);
			pst.setString(5, sque);
			pst.setString(6, ans);
			pst.setString(7, add1);
			pst.setString(8, add2);
			pst.setInt(9, id);
			if(pst.executeUpdate() > 0) {
				JOptionPane.showMessageDialog(null, "User data successfully updated");
			}
		}
		catch (SQLException e)
		{
			Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, e);		
		}
	}
	
	// delete user data
	public void delete(int id) {
		int x = JOptionPane.showConfirmDialog(null, "Are you sure to delete this account?", "Delete Account", JOptionPane.OK_CANCEL_OPTION, 0);
		if (x == JOptionPane.OK_OPTION) {
			try
			{
				PreparedStatement pst = conn.prepareStatement("DELETE FROM users WHERE uid = ?");
				pst.setInt(1, id);
				
				if (pst.executeUpdate() > 0) {
					JOptionPane.showMessageDialog(null, "Account deleted");
				}
			}
			catch (SQLException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	// get user data
	@SuppressWarnings("static-access")
	public void getUserData(JTable table, String search) {
		String query = "SELECT * FROM users WHERE CONCAT(uid, uname, uemail, uphone) like ? order by uid desc";
		try
		{
			PreparedStatement pst = conn.prepareStatement(query);
			pst.setString(1, "%"+search+"%");
			
			ResultSet rs = pst.executeQuery();
			
			DefaultTableModel model = (DefaultTableModel) table.getModel();
			Object [] row;
			while(rs.next()) {
				row = new Object[9];
				row[0] = rs.getInt(1);
				row[1] = rs.getString(2);
				row[2] = rs.getString(3);
				row[3] = forgotPasswordDao.encryptString(rs.getString(4));
				row[4] = rs.getString(5);
				row[5] = rs.getString(6);
				row[6] = rs.getString(7);
				row[7] = rs.getString(8);
				row[8] = rs.getString(9);
				model.addRow(row);
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		
	}
}
