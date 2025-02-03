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

public class CategoryDao
{
	Connection conn = Login.conn;
	 
	// get category table max row
	public int getMaxRow() {
		int row = 0;
		try
		{
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("SELECT max(cid) from category");
			while(rs.next()) {
				row = rs.getInt(1);
			}
		}
		catch (SQLException e)
		{
			Logger.getLogger(CategoryDao.class.getName()).log(Level.SEVERE, null, e);
		}
		return row + 1;
	}
	
	// check category name already exists
	public boolean isIDExists(int id) {
		try
		{
			PreparedStatement pst = conn.prepareStatement("SELECT * FROM category WHERE cid = ?");
			pst.setInt(1, id);
			ResultSet rs = pst.executeQuery();
			
			if (rs.next()) {
				return true;
			}
		}
		catch (SQLException e)
		{
			Logger.getLogger(CategoryDao.class.getName()).log(Level.SEVERE, null, e);
		} 
		return false;
	}
	
	// check category name already exists
	public boolean isCategoryNameExists(String cname) {
		try
		{
			PreparedStatement pst = conn.prepareStatement("SELECT * FROM category WHERE cname = ?");
			pst.setString(1, cname);
			ResultSet rs = pst.executeQuery();
			
			if (rs.next()) {
				return true;
			}
		}
		catch (SQLException e)
		{
			Logger.getLogger(CategoryDao.class.getName()).log(Level.SEVERE, null, e);
		} 
		return false;
	}
	
	// insert data into category table
	public void insert(int id, String cname, String desc) {
		String query = "INSERT INTO category VALUES (?,?,?)";
		
		try
		{
			PreparedStatement pst = conn.prepareStatement(query);
			pst.setInt(1, id);
			pst.setString(2, cname);
			pst.setString(3, desc);
			if (pst.executeUpdate() > 0) {
				JOptionPane.showMessageDialog(null, "Category added successfully");
			}
		}
		catch (SQLException e)
		{
			Logger.getLogger(CategoryDao.class.getName()).log(Level.SEVERE, null, e);
		}
	}
	
	// get category data
	public void getCategoryData(JTable table, String search) {
		String query = "SELECT * FROM category WHERE CONCAT(cid, cname) like ? order by cid asc";
		try
		{
			PreparedStatement pst = conn.prepareStatement(query);
			pst.setString(1, "%"+search+"%");
			
			ResultSet rs = pst.executeQuery();
			
			DefaultTableModel model = (DefaultTableModel) table.getModel();
			Object [] row;
			while(rs.next()) {
				row = new Object[3];
				row[0] = rs.getInt(1);
				row[1] = rs.getString(2);
				row[2] = rs.getString(3);
				model.addRow(row);
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	// update category data
	public void update(int id, String cname, String cdesc) {
		String query = "UPDATE category SET cname = ?, cdesc = ? WHERE cid = ?";
		
		try
		{
			PreparedStatement pst = conn.prepareStatement(query);
			pst.setString(1, cname);
			pst.setString(2, cdesc);
			pst.setInt(3, id);
			if(pst.executeUpdate() > 0) {
				JOptionPane.showMessageDialog(null, "Category data successfully updated");
			}
		}
		catch (SQLException e)
		{
			Logger.getLogger(CategoryDao.class.getName()).log(Level.SEVERE, null, e);		
		}
	}
	
	// delete category data
	public void delete(int id) {
		int x = JOptionPane.showConfirmDialog(null, "Are you sure to delete this category?", "Delete Category", JOptionPane.OK_CANCEL_OPTION, 0);
		if (x == JOptionPane.OK_OPTION) {
			try
			{
				PreparedStatement pst = conn.prepareStatement("DELETE FROM category WHERE cid = ?");
				pst.setInt(1, id);
				
				if (pst.executeUpdate() > 0) {
					JOptionPane.showMessageDialog(null, "Category deleted");
				}
			}
			catch (SQLException e)
			{
				Logger.getLogger(CategoryDao.class.getName()).log(Level.SEVERE, null, e);
			}
		}
	}
}
