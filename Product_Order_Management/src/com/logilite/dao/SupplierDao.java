package com.logilite.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.logilite.component.SupplierReport;
import com.logilite.user.Login;

public class SupplierDao
{
	static Connection	conn				= Login.conn;
	ForgotPasswordDao	forgotPasswordDao	= new ForgotPasswordDao();

	// get supplier table max row
	public int getMaxRow()
	{
		int row = 0;
		try
		{
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("SELECT max(sid) from supplier");
			while (rs.next())
			{
				row = rs.getInt(1);
			}
		}
		catch (SQLException e)
		{
			Logger.getLogger(SupplierDao.class.getName()).log(Level.SEVERE, null, e);
		}
		return row + 1;
	}

	// check email already exists
	public boolean isEmailExists(String email)
	{
		try
		{
			PreparedStatement pst = conn.prepareStatement("SELECT * FROM supplier WHERE semail = ?");
			pst.setString(1, email);
			ResultSet rs = pst.executeQuery();

			if (rs.next())
			{
				return true;
			}
		}
		catch (SQLException e)
		{
			Logger.getLogger(SupplierDao.class.getName()).log(Level.SEVERE, null, e);
		}
		return false;
	}
	
	// check email already exists
	public boolean isSupplierRequestEmailExists(String email)
	{
		try
		{
			PreparedStatement pst = conn.prepareStatement("SELECT * FROM supplier_request WHERE email = ?");
			pst.setString(1, email);
			ResultSet rs = pst.executeQuery();

			if (rs.next())
			{
				return true;
			}
		}
		catch (SQLException e)
		{
			Logger.getLogger(SupplierDao.class.getName()).log(Level.SEVERE, null, e);
		}
		return false;
	}

	// check phone number already exists
	public boolean isPhoneExists(String phone)
	{
		try
		{
			PreparedStatement pst = conn.prepareStatement("SELECT * FROM supplier WHERE sphone = ?");
			pst.setString(1, phone);
			ResultSet rs = pst.executeQuery();

			if (rs.next())
			{
				return true;
			}
		}
		catch (SQLException e)
		{
			Logger.getLogger(SupplierDao.class.getName()).log(Level.SEVERE, null, e);
		}
		return false;
	}

	// check username already exists
	public boolean isUsernameExists(String name)
	{
		try
		{
			PreparedStatement pst = conn.prepareStatement("SELECT * FROM supplier WHERE sname = ?");
			pst.setString(1, name);
			ResultSet rs = pst.executeQuery();

			if (rs.next())
			{
				return true;
			}
		}
		catch (SQLException e)
		{
			Logger.getLogger(SupplierDao.class.getName()).log(Level.SEVERE, null, e);
		}
		return false;
	}

	// insert data into supplier table
	public void insert(int id, String name, String email, String password, String phone, String add1, String add2)
	{
		String query = "INSERT INTO supplier VALUES (?,?,?,?,?,?,?)";

		try
		{
			PreparedStatement pst = conn.prepareStatement(query);
			pst.setInt(1, id);
			pst.setString(2, name);
			pst.setString(3, email);
			pst.setString(4, password);
			pst.setString(5, phone);
			pst.setString(6, add1);
			pst.setString(7, add2);

			if (pst.executeUpdate() > 0)
			{
				JOptionPane.showMessageDialog(null, "Supplier added successfully");
			}
		}
		catch (SQLException e)
		{
			Logger.getLogger(SupplierDao.class.getName()).log(Level.SEVERE, null, e);
		}
	}
	
	// insert data into supplier request table
	public void insertSupplierRequest(String firstname, String lastname, String email, String phone, String add1, String add2)
	{
		String query = "INSERT INTO supplier_request(firstname, lastname, email, phone, address1, address2) VALUES (?,?,?,?,?,?)";

		try
		{
			PreparedStatement pst = conn.prepareStatement(query);
			pst.setString(1, firstname);
			pst.setString(2, lastname);
			pst.setString(3, email);
			pst.setString(4, phone);
			pst.setString(5, add1);
			pst.setString(6, add2);

			if (pst.executeUpdate() > 0)
			{
				JOptionPane.showMessageDialog(null, "Supplier request sent successfully");
			}
		}
		catch (SQLException e)
		{
			Logger.getLogger(SupplierDao.class.getName()).log(Level.SEVERE, null, e);
		}
	}

	// get Supplier ID
	public int getSupplierId(String email)
	{
		int id = 0;
		try
		{
			PreparedStatement pst = conn.prepareStatement("SELECT sid FROM supplier WHERE semail = ?");
			pst.setString(1, email);
			ResultSet rs = pst.executeQuery();

			if (rs.next())
			{
				id = rs.getInt(1);
			}
		}
		catch (SQLException e)
		{
			Logger.getLogger(SupplierDao.class.getName()).log(Level.SEVERE, null, e);
		}
		return id;
	}

	// get Supplier username
	public String getSupplierName(String email)
	{
		String supplierName = "";
		try
		{
			PreparedStatement pst = conn.prepareStatement("SELECT sname FROM supplier WHERE semail = ?");
			pst.setString(1, email);
			ResultSet rs = pst.executeQuery();

			if (rs.next())
			{
				supplierName = rs.getString(1);
			}
		}
		catch (SQLException e)
		{
			Logger.getLogger(SupplierDao.class.getName()).log(Level.SEVERE, null, e);
		}
		return supplierName;
	}

	// get Supplier Data
	public String[] getSupplierData(int id)
	{
		String data[] = new String[7];
		try
		{
			PreparedStatement pst = conn.prepareStatement("SELECT * FROM supplier WHERE sid = ?");
			pst.setInt(1, id);
			ResultSet rs = pst.executeQuery();

			if (rs.next())
			{
				data[0] = rs.getString(1);
				data[1] = rs.getString(2);
				data[2] = rs.getString(3);
				data[3] = rs.getString(4);
				data[4] = rs.getString(5);
				data[5] = rs.getString(6);
				data[6] = rs.getString(7);
			}
		}
		catch (SQLException e)
		{
			Logger.getLogger(SupplierDao.class.getName()).log(Level.SEVERE, null, e);
		}
		return data;
	}
	
	// get Supplier request Data
	public void getSupplierRequestData(JTable table)
	{
		try
		{
			PreparedStatement pst = conn.prepareStatement("SELECT * FROM supplier_request");
			ResultSet rs = pst.executeQuery();

			DefaultTableModel model = (DefaultTableModel) table.getModel();
			Object [] row;
			while (rs.next())
			{
				row = new Object[6];
				row[0] = rs.getString(2);
				row[1] = rs.getString(3);
				row[2] = rs.getString(4);
				row[3] = rs.getString(5);
				row[4] = rs.getString(6);
				row[5] = rs.getString(7);
				model.addRow(row);
			}
		}
		catch (SQLException e)
		{
			Logger.getLogger(SupplierDao.class.getName()).log(Level.SEVERE, null, e);
		}
	}

	// get supplier data
	@SuppressWarnings("static-access")
	public void getSupplierData(JTable table, String search)
	{
		String query = "SELECT * FROM supplier WHERE CONCAT(sid, sname, semail, sphone) like ? order by sid desc";
		try
		{
			PreparedStatement pst = conn.prepareStatement(query);
			pst.setString(1, "%" + search + "%");

			ResultSet rs = pst.executeQuery();

			DefaultTableModel model = (DefaultTableModel) table.getModel();
			Object[] row;
			while (rs.next())
			{
				row = new Object[7];
				row[0] = rs.getInt(1);
				row[1] = rs.getString(2);
				row[2] = rs.getString(3);
				row[3] = forgotPasswordDao.encryptString(rs.getString(4));
				row[4] = rs.getString(5);
				row[5] = rs.getString(6);
				row[6] = rs.getString(7);
				model.addRow(row);
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}

	}

	// update supplier data
	public void update(int id, String name, String email, String password, String phone, String add1, String add2)
	{
		String query = "UPDATE supplier SET sname = ?, semail = ?, spassword = ?, sphone = ?, saddress1 = ?, saddress2 = ? WHERE sid = ?";

		try
		{
			PreparedStatement pst = conn.prepareStatement(query);
			pst.setString(1, name);
			pst.setString(2, email);
			pst.setString(3, password);
			pst.setString(4, phone);
			pst.setString(5, add1);
			pst.setString(6, add2);
			pst.setInt(7, id);
			if (pst.executeUpdate() > 0)
			{
				JOptionPane.showMessageDialog(null, "Supplier data successfully updated");
			}
		}
		catch (SQLException e)
		{
			Logger.getLogger(SupplierDao.class.getName()).log(Level.SEVERE, null, e);
		}
	}

	// delete Supplier data
	public void delete(int id)
	{
		int x = JOptionPane.showConfirmDialog(null, "Are you sure to delete this account?", "Delete Account",
				JOptionPane.OK_CANCEL_OPTION, 0);
		if (x == JOptionPane.OK_OPTION)
		{
			try
			{
				PreparedStatement pst = conn.prepareStatement("DELETE FROM supplier WHERE sid = ?");
				pst.setInt(1, id);

				if (pst.executeUpdate() > 0)
				{
					JOptionPane.showMessageDialog(null, "Account deleted");
				}
			}
			catch (SQLException e)
			{
				Logger.getLogger(SupplierDao.class.getName()).log(Level.SEVERE, null, e);
			}
		}
	}
	
	// delete Supplier data
	public void deleteSupplierRequest(String email)
	{
		try
		{
			PreparedStatement pst = conn.prepareStatement("DELETE FROM supplier_request WHERE email = ?");
			pst.setString(1, email);

			pst.execute();
		}
		catch (SQLException e)
		{
			Logger.getLogger(SupplierDao.class.getName()).log(Level.SEVERE, null, e);
		}
	}

	// count suppliers
	public int countSuppliers()
	{
		int total = 0;
		try
		{
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("SELECT count(*) as total from supplier");
			if (rs.next())
			{
				total = rs.getInt(1);
			}
		}
		catch (SQLException e)
		{
			Logger.getLogger(SupplierDao.class.getName()).log(Level.SEVERE, null, e);
		}
		return total;
	}

	// get suppliers
	public String[] getSupplier()
	{
		String[] suppliers = new String[countSuppliers()];
		try
		{
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("SELECT * FROM supplier");
			int i = 0;
			while (rs.next())
			{
				suppliers[i] = rs.getString(2);
				i++;
			}
		}
		catch (SQLException e)
		{
			Logger.getLogger(SupplierDao.class.getName()).log(Level.SEVERE, null, e);
		}
		return suppliers;
	}

	// get Supplier Data
	public static String[] getSupplierDataReport(String email)
	{
		String data[] = SupplierReport.supplierDetails;
		try
		{
			PreparedStatement pst = conn.prepareStatement("SELECT * FROM supplier WHERE semail = ?");
			pst.setString(1, email);
			ResultSet rs = pst.executeQuery();

			if (rs.next())
			{
				data[0] = rs.getString(2);
				data[1] = rs.getString(3);
				data[2] = rs.getString(5);
				data[3] = rs.getString(6) + ", " + rs.getString(7);
			}
		}
		catch (SQLException e)
		{
			Logger.getLogger(SupplierDao.class.getName()).log(Level.SEVERE, null, e);
		}
		return data;
	}

	// get Supplier Data for report
	public static Map<Integer, String[]> getAllSupplierDataReport(String name, String order_status)
	{
		SupplierReport.UserSupplierData = new HashMap<Integer, String[]>();
		Map<Integer, String[]> data = SupplierReport.UserSupplierData;

		String query = "SELECT p.order_id, p.uid, p.uname, p.uphone, p.total, p.purchase_date, p.receive_date "
				+ "FROM supplier s " + "JOIN purchase_details p ON s.sname = p.supplier " + "WHERE s.sname = ?";

		if (!"ALL".equals(order_status))
		{
			query += " AND p.order_status = ?";
		}
		else
		{
			query += " AND (p.order_status = 'Received' OR p.order_status = 'On the way')";
		}
		try
		{
			PreparedStatement pst = conn.prepareStatement(query);
			pst.setString(1, name);
			if (!"ALL".equals(order_status)) {
	            pst.setString(2, order_status);
	        }
			ResultSet rs = pst.executeQuery();
			int index = 0;
			while (rs.next())
			{
				String[] row = new String[7];
				row[0] = rs.getString("order_id");
				row[1] = rs.getString("uid");
				row[2] = rs.getString("uname");
				row[3] = rs.getString("uphone");
				row[4] = rs.getString("total");
				row[5] = rs.getString("purchase_date");
				row[6] = rs.getString("receive_date");
				data.put(index++, row);
			}
		}
		catch (SQLException e)
		{
			Logger.getLogger(SupplierDao.class.getName()).log(Level.SEVERE, null, e);
		}

		return data;
	}
}
