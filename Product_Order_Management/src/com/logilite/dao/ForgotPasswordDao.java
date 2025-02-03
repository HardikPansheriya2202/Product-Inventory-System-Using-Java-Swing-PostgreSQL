package com.logilite.dao;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

import com.logilite.user.ForgotPassword;
import com.logilite.user.Login;

public class ForgotPasswordDao
{
	Connection conn = Login.conn;
	private static MessageDigest md;
	
		public boolean isEmailExists(String email) {
			try
			{
				PreparedStatement pst = conn.prepareStatement("SELECT * FROM users WHERE uemail = ?");
				pst.setString(1, email);
				ResultSet rs = pst.executeQuery();
				
				if (rs.next()) {
					ForgotPassword.seQueField.setText(rs.getString(6));
					return true;
				}else {
					JOptionPane.showMessageDialog(null, "Email address doesn't exist");
				}
			}
			catch (SQLException e)
			{	
				Logger.getLogger(ForgotPasswordDao.class.getName()).log(Level.SEVERE, null, e);
			} 
			return false;
		}
		
		public boolean isAns(String email, String newAns) {
			try
			{
				PreparedStatement pst = conn.prepareStatement("SELECT * FROM users WHERE uemail = ?");
				pst.setString(1, email);
				ResultSet rs = pst.executeQuery();
				
				if (rs.next()) {
					String oldAns = rs.getString(7);
					if (oldAns.equals(newAns)) {
						return true;						
					}else {
						JOptionPane.showMessageDialog(null, "Security answer didn't match");
						return false;
					}
				}
			}
			catch (SQLException e)
			{
				Logger.getLogger(ForgotPasswordDao.class.getName()).log(Level.SEVERE, null, e);
			} 
			return false;
		}
		
		// set New Paasword
		public void setPassword(String email, String password) {
			String query = "UPDATE users SET upassword = ? WHERE uemail = ?";
			
			try
			{
				PreparedStatement pst = conn.prepareStatement(query);
				pst.setString(1, password);
				pst.setString(2, email);
				if(pst.executeUpdate() > 0) {
					JOptionPane.showMessageDialog(null, "Password successfully updated");
				}
			}
			catch (SQLException e)
			{
				Logger.getLogger(ForgotPasswordDao.class.getName()).log(Level.SEVERE, null, e);
			}
		}
		
		// make password ecrypt
		public static String encryptString(String s) {
	        try {
	            md = MessageDigest.getInstance("MD5");
	            byte[] sBytes = s.getBytes();
	            md.reset();
	            byte[] digested = md.digest(sBytes);
	            StringBuffer sb = new StringBuffer();
	            for (int i = 0; i < digested.length; i++) {
	                sb.append(Integer.toHexString(0xff & digested[i]));
	            }
	            return sb.toString();
	        } catch (NoSuchAlgorithmException ex) {
	            Logger.getLogger(ForgotPassword.class.getName()).log(Level.SEVERE, null, ex);
	        }
	        return null;
	    }
}
