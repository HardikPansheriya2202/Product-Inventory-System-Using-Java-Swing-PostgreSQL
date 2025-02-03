package com.logilite.supplier;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.logilite.dao.SupplierDao;

public class SupplierAccount extends JPanel
{

	private static final long serialVersionUID = 1L;
	public static JPanel panel;
	public static JTextField IDField;
	public static JTextField nameField;
	public static JTextField emailField;
	public static JPasswordField passwordField;
	public static JTextField mobileField;
	public static JTextField address1Field;
	public static JTextField address2Field;
	public static JLabel lblEmail;
	public static JLabel lblPassword;
	public static JLabel lblMobileNo;
	public static JLabel lblAdress;
	public static JLabel lblAddressLinecountry;
	public static JButton btnUpdate;
	public static JLabel lblX;
	public static JLabel btnHide;
	public static JLabel btnShow;
	public static JButton btnDelete;
	public static JLabel lblUserId;
	public static JLabel lblUsername;
	int xx, xy;
	int sId;
	String data[] = new String[7]; 
	SupplierDao supplier = new SupplierDao();

	public SupplierAccount()
	{
		initComponent();
		init();
	}
	
	private void init() {
		sId = supplier.getSupplierId(SupplierDashboard.lblSupplieremail.getText());
		data = supplier.getSupplierData(sId);
		setValue();
		
		btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (isEmpty()) {
					if (!check()) {
						int id = Integer.parseInt(IDField.getText());
						String name = nameField.getText();
						String email = emailField.getText();
						String password = String.valueOf(passwordField.getPassword());
						String phone = mobileField.getText();
						String address1 = address1Field.getText();
						String address2 = address2Field.getText();
						
						supplier.update(id, name, email, password, phone, address1, address2);
					}
				}
			}
		});
		
		btnHide.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				passwordField.setEchoChar((char) 0);
				btnHide.setVisible(false);
				btnShow.setVisible(true);
			}
		});
		
		btnShow.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				passwordField.setEchoChar('*');
				btnHide.setVisible(true);
				btnShow.setVisible(false);
			}
		});
		
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				supplier.delete(sId);
				System.exit(0);
			}
		});
	}
	
	private void setValue() {
		IDField.setText(data[0]);
		nameField.setText(data[1]);
		emailField.setText(data[2]);
		passwordField.setText(data[3]);
		mobileField.setText(data[4]);
		address1Field.setText(data[5]);
		address2Field.setText(data[6]);
	}
	
	public boolean isEmpty() {
		if (nameField.getText().isEmpty()) {
			JOptionPane.showMessageDialog(SupplierAccount.this, "Username is required", "Warning", 2);
			return false;
		}
		if (emailField.getText().isEmpty()) {
			JOptionPane.showMessageDialog(SupplierAccount.this, "Email address is required", "Warning", 2);
			return false;
		}
		if (!emailField.getText().matches("^.+@.+\\..+$")) {
			JOptionPane.showMessageDialog(SupplierAccount.this, "Invalid email address", "Warning", 2);
			return false;
		}
		if (String.valueOf(passwordField.getPassword()).isEmpty()) {
			JOptionPane.showMessageDialog(SupplierAccount.this, "Password is required", "Warning", 2);
			return false;
		}
		if (mobileField.getText().isEmpty()) {
			JOptionPane.showMessageDialog(SupplierAccount.this, "Phone number is required", "Warning", 2);
			return false;
		}
		if (mobileField.getText().length() > 15) {
			JOptionPane.showMessageDialog(SupplierAccount.this, "Phone number is to long", "Warning", 2);
			return false;
		}
		if (mobileField.getText().length() < 10) {
			JOptionPane.showMessageDialog(SupplierAccount.this, "Phone number is short", "Warning", 2);
			return false;
		}
		if (address1Field.getText().isEmpty()) {
			JOptionPane.showMessageDialog(SupplierAccount.this, "Address line 1 is required", "Warning", 2);
			return false;
		}
		if (address2Field.getText().isEmpty()) {
			JOptionPane.showMessageDialog(SupplierAccount.this, "Address line 2 is required", "Warning", 2);
			return false;
		}
		return true;
	}
	
	private boolean check() {
		String newUsername = nameField.getText();
		String newEmail = emailField.getText();
		String newPhone = mobileField.getText();
		String oldUsername = data[1];
		String oldEmail = data[2];
		String oldPhone = data[4];
		
		if (newEmail.equals(oldEmail) && newPhone.equals(oldPhone) && newUsername.equals(oldUsername)) {
			return false;
		}else {
			if (!newEmail.equals(oldEmail)) {
				boolean x = supplier.isEmailExists(newEmail);
				if (x) {
					JOptionPane.showMessageDialog(SupplierAccount.this, "This email already exists", "Warning", 2);
				}
				return x;
			}
			if (!newPhone.equals(oldPhone)) {
				boolean x = supplier.isPhoneExists(newPhone);
				if (x) {
					JOptionPane.showMessageDialog(SupplierAccount.this, "This phone number already exists", "Warning", 2);
				}
				return x;
			}
			if (!newUsername.equals(oldUsername)) {
				boolean x = supplier.isUsernameExists(newUsername);
				if (x) {
					JOptionPane.showMessageDialog(SupplierAccount.this, "This Username already exists", "Warning", 2);
				}
				return x;
			}
		}
		return false;
	}
	
	private void initComponent() {
		setLayout(null);
		
		panel = new JPanel();
		panel.setBounds(0, 0, 877, 557);
		add(panel);
		panel.setLayout(null);
		
		IDField = new JTextField();
		IDField.setBackground(new Color(186, 182, 182));
		IDField.setEditable(false);
		IDField.setBounds(175, 156, 290, 30);
		IDField.setFont(new Font("DejaVu Serif Condensed", Font.PLAIN, 14));
		panel.add(IDField);
		IDField.setColumns(10);
		
		nameField = new JTextField();
		nameField.setBounds(175, 213, 290, 30);
		nameField.setFont(new Font("DejaVu Serif Condensed", Font.PLAIN, 14));
		panel.add(nameField);
		nameField.setColumns(10);
		
		emailField = new JTextField();
		emailField.setBounds(175, 273, 290, 30);
		emailField.setFont(new Font("DejaVu Serif Condensed", Font.PLAIN, 14));
		panel.add(emailField);
		emailField.setColumns(10);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(175, 331, 290, 30);
		passwordField.setFont(new Font("DejaVu Serif Condensed", Font.PLAIN, 14));
		panel.add(passwordField);
		
		mobileField = new JTextField();
		mobileField.setBounds(526, 156, 290, 30);
		mobileField.setFont(new Font("DejaVu Serif Condensed", Font.PLAIN, 14));
		panel.add(mobileField);
		mobileField.setColumns(10);
		
		address1Field = new JTextField();
		address1Field.setBounds(526, 213, 290, 30);
		address1Field.setFont(new Font("DejaVu Serif Condensed", Font.PLAIN, 14));
		panel.add(address1Field);
		address1Field.setColumns(10);
		
		address2Field = new JTextField();
		address2Field.setBounds(526, 273, 290, 30);
		address2Field.setFont(new Font("DejaVu Serif Condensed", Font.PLAIN, 14));
		panel.add(address2Field);
		address2Field.setColumns(10);
		
		lblUserId = new JLabel("User ID");
		lblUserId.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 14));
		lblUserId.setBounds(175, 134, 207, 15);
		panel.add(lblUserId);
		
		lblUsername = new JLabel("Username");
		lblUsername.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 14));
		lblUsername.setBounds(175, 194, 207, 15);
		panel.add(lblUsername);
		
		lblEmail = new JLabel("Email");
		lblEmail.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 14));
		lblEmail.setBounds(175, 252, 207, 15);
		panel.add(lblEmail);
		
		lblPassword = new JLabel("Password");
		lblPassword.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 14));
		lblPassword.setBounds(175, 312, 207, 15);
		panel.add(lblPassword);
		
		lblMobileNo = new JLabel("Mobile no.");
		lblMobileNo.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 14));
		lblMobileNo.setBounds(526, 134, 207, 15);
		panel.add(lblMobileNo);
		
		lblAdress = new JLabel("Address Line 1(State & Area)");
		lblAdress.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 14));
		lblAdress.setBounds(526, 194, 233, 15);
		panel.add(lblAdress);
		
		lblAddressLinecountry = new JLabel("Address Line 2(Country)");
		lblAddressLinecountry.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 14));
		lblAddressLinecountry.setBounds(526, 252, 207, 15);
		panel.add(lblAddressLinecountry);
		
		btnUpdate = new JButton("Update");
		btnUpdate.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnUpdate.setIcon(new ImageIcon(getClass().getResource("/com/logilite/img/update.png")));
		btnUpdate.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 16));
		btnUpdate.setBounds(526, 329, 135, 30);
		btnUpdate.setFocusPainted(false);
		panel.add(btnUpdate);
		
		btnHide = new JLabel("");
		btnHide.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnHide.setIcon(new ImageIcon(getClass().getResource("/com/logilite/img/hide.png")));
		btnHide.setBackground(Color.BLACK);
		btnHide.setBounds(475, 331, 33, 30);
		panel.add(btnHide);
		
		btnShow = new JLabel("");
		btnShow.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnShow.setIcon(new ImageIcon(getClass().getResource("/com/logilite/img/unhide.png")));
		btnShow.setBackground(Color.BLACK);
		btnShow.setBounds(475, 331, 33, 30);
		panel.add(btnShow);
		
		btnDelete = new JButton("Delete");
		btnDelete.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnDelete.setIcon(new ImageIcon(getClass().getResource("/com/logilite/img/remove.png")));
		btnDelete.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 16));
		btnDelete.setBounds(686, 329, 130, 30);
		btnDelete.setFocusPainted(false);
		panel.add(btnDelete);
	}
}
