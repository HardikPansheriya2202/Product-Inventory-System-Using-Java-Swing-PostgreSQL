package com.logilite.admin;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.logilite.dao.Statistics;
import com.logilite.dao.SupplierDao;
import com.logilite.email.Email;

public class AddSupplier extends JPanel
{

	private static final long		serialVersionUID	= 1L;
	public static JPanel			panel;

	public static JTextField		IDField;
	public static JTextField		nameField;
	public static JTextField		emailField;
	public static JPasswordField	passwordField;
	public static JTextField		mobileField;
	public static JTextField		address1Field;
	public static JTextField		address2Field;
	public static JLabel			lblEmail;
	public static JLabel			lblPassword;
	public static JLabel			lblMobileNo;
	public static JLabel			lblAdress;
	public static JLabel			lblAddressLinecountry;
	public static JButton			btnSave;
	public static JLabel			lblX;
	public static JLabel			btnHide;
	public static JLabel			btnShow;
	public static JButton			btnClear;
	public static JLabel			lblSupplierId;
	public static JLabel			lblUsername;
	int								xx, xy;
	Color							noEditColor			= new Color(204, 204, 204);
	SupplierDao						supplier			= new SupplierDao();
	Statistics						statistics			= new Statistics();

	public AddSupplier()
	{
		initComponent();
		init();
	}

	private void init()
	{
		IDField.setBackground(noEditColor);
		IDField.setText(String.valueOf(supplier.getMaxRow()));

		nameField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e)
			{
				char input = e.getKeyChar();
				if (!(input < '0' || input > '9') && input != '\b')
				{
					e.consume();
					JOptionPane.showMessageDialog(AddSupplier.this, "Username doesn't contains any numbers!", "Warning",
							2);
				}
			}
		});

		mobileField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e)
			{
				if (!Character.isDigit(e.getKeyChar()))
				{
					e.consume();
				}
				if (mobileField.getText().length() >= 10)
				{
					e.consume();
				}
			}
		});

		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0)
			{
				if (isEmpty())
				{
					int id = Integer.parseInt(IDField.getText());
					String name = nameField.getText();
					String email = emailField.getText();
					String password = String.valueOf(passwordField.getPassword());
					String phone = mobileField.getText();
					String address1 = address1Field.getText();
					String address2 = address2Field.getText();
					if (!supplier.isUsernameExists(name))
					{
						if (!supplier.isEmailExists(email))
						{
							if (!supplier.isPhoneExists(phone))
							{
								supplier.insert(id, name, email, password, phone, address1, address2);
								String massage = "Your email address is : " + email + "\n Your Password is : " + password;
								Email.send(email, "Your Request Accepted.", massage);
								clear();
								supplier.deleteSupplierRequest(email);
							}
							else
							{
								JOptionPane.showMessageDialog(AddSupplier.this, "This phone number already exists!");
							}
						}
						else
						{
							JOptionPane.showMessageDialog(AddSupplier.this, "This email already exists!");
						}
					}
					else
					{
						JOptionPane.showMessageDialog(AddSupplier.this, "This username already exists!");
					}
				}
			}
		});

		btnHide.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e)
			{
				passwordField.setEchoChar((char) 0);
				btnHide.setVisible(false);
				btnShow.setVisible(true);
			}
		});

		btnShow.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e)
			{
				passwordField.setEchoChar('*');
				btnHide.setVisible(true);
				btnShow.setVisible(false);
			}
		});

		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				clear();
			}
		});

	}

	private void clear()
	{
		IDField.setText(String.valueOf(supplier.getMaxRow()));
		nameField.setText("");
		emailField.setText("");
		passwordField.setText("");
		mobileField.setText("");
		address1Field.setText("");
		address2Field.setText("");
		statistics.admin();
	}

	public boolean isEmpty()
	{
		if (nameField.getText().isEmpty())
		{
			JOptionPane.showMessageDialog(AddSupplier.this, "Username is required", "Warning", 2);
			return false;
		}
		if (emailField.getText().isEmpty())
		{
			JOptionPane.showMessageDialog(AddSupplier.this, "Email address is required", "Warning", 2);
			return false;
		}
		if (!emailField.getText().matches("^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
				+ "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$"))
		{
			JOptionPane.showMessageDialog(AddSupplier.this, "Invalid email address", "Warning", 2);
			return false;
		}
		if (String.valueOf(passwordField.getPassword()).isEmpty())
		{
			JOptionPane.showMessageDialog(AddSupplier.this, "Password is required", "Warning", 2);
			return false;
		}
		if (!isPasswordValid(String.valueOf(passwordField.getPassword()))) {
		    return false;
		}
		if (mobileField.getText().isEmpty())
		{
			JOptionPane.showMessageDialog(AddSupplier.this, "Phone number is required", "Warning", 2);
			return false;
		}
		if (mobileField.getText().length() > 15)
		{
			JOptionPane.showMessageDialog(AddSupplier.this, "Phone number is to long", "Warning", 2);
			return false;
		}
		if (mobileField.getText().length() < 10)
		{
			JOptionPane.showMessageDialog(AddSupplier.this, "Phone number is short", "Warning", 2);
			return false;
		}
		if (address1Field.getText().isEmpty())
		{
			JOptionPane.showMessageDialog(AddSupplier.this, "Address line 1 is required", "Warning", 2);
			return false;
		}
		if (address2Field.getText().isEmpty())
		{
			JOptionPane.showMessageDialog(AddSupplier.this, "Address line 2 is required", "Warning", 2);
			return false;
		}
		return true;
	}
	
	public boolean isPasswordValid(String password) {
	    if (password.length() < 8) {
	        JOptionPane.showMessageDialog(AddSupplier.this, "Password must be at least 8 characters long.", "Warning", 2);
	        return false;
	    }
	    if (!password.matches(".*[A-Z].*")) {
	        JOptionPane.showMessageDialog(AddSupplier.this, "Password must contain at least one uppercase letter.", "Warning", 2);
	        return false;
	    }
	    if (!password.matches(".*[a-z].*")) {
	        JOptionPane.showMessageDialog(AddSupplier.this, "Password must contain at least one lowercase letter.", "Warning", 2);
	        return false;
	    }
	    if (!password.matches(".*\\d.*")) {
	        JOptionPane.showMessageDialog(AddSupplier.this, "Password must contain at least one digit.", "Warning", 2);
	        return false;
	    }
	    if (!password.matches(".*[@#$%^&+=!].*")) {
	        JOptionPane.showMessageDialog(AddSupplier.this, "Password must contain at least one special character (@#$%^&+=!).", "Warning", 2);
	        return false;
	    }
	    return true;
	}

	private void initComponent()
	{
		setLayout(null);

		panel = new JPanel();
		panel.setBounds(0, 0, 885, 576);
		add(panel);
		panel.setLayout(null);

		IDField = new JTextField();
		IDField.setEditable(false);
		IDField.setBounds(337, 106, 290, 30);
		IDField.setFont(new Font("DejaVu Serif Condensed", Font.PLAIN, 14));
		panel.add(IDField);
		IDField.setColumns(10);

		nameField = new JTextField();
		nameField.setBounds(337, 161, 290, 30);
		nameField.setFont(new Font("DejaVu Serif Condensed", Font.PLAIN, 14));
		panel.add(nameField);
		nameField.setColumns(10);

		emailField = new JTextField();
		emailField.setBounds(337, 216, 290, 30);
		emailField.setFont(new Font("DejaVu Serif Condensed", Font.PLAIN, 14));
		panel.add(emailField);
		emailField.setColumns(10);

		passwordField = new JPasswordField();
		passwordField.setBounds(337, 270, 290, 30);
		passwordField.setFont(new Font("DejaVu Serif Condensed", Font.PLAIN, 14));
		panel.add(passwordField);

		mobileField = new JTextField();
		mobileField.setBounds(337, 326, 290, 30);
		mobileField.setFont(new Font("DejaVu Serif Condensed", Font.PLAIN, 14));
		panel.add(mobileField);
		mobileField.setColumns(10);

		address1Field = new JTextField();
		address1Field.setBounds(337, 381, 290, 30);
		address1Field.setFont(new Font("DejaVu Serif Condensed", Font.PLAIN, 14));
		panel.add(address1Field);
		address1Field.setColumns(10);

		address2Field = new JTextField();
		address2Field.setBounds(337, 435, 290, 30);
		address2Field.setFont(new Font("DejaVu Serif Condensed", Font.PLAIN, 14));
		panel.add(address2Field);
		address2Field.setColumns(10);

		lblSupplierId = new JLabel("Supplier ID");
		lblSupplierId.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 14));
		lblSupplierId.setBounds(337, 88, 207, 15);
		panel.add(lblSupplierId);

		lblUsername = new JLabel("Username");
		lblUsername.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 14));
		lblUsername.setBounds(337, 144, 207, 15);
		panel.add(lblUsername);

		lblEmail = new JLabel("Email");
		lblEmail.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 14));
		lblEmail.setBounds(337, 199, 207, 15);
		panel.add(lblEmail);

		lblPassword = new JLabel("Password");
		lblPassword.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 14));
		lblPassword.setBounds(337, 253, 207, 15);
		panel.add(lblPassword);

		lblMobileNo = new JLabel("Mobile no.");
		lblMobileNo.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 14));
		lblMobileNo.setBounds(337, 309, 207, 15);
		panel.add(lblMobileNo);

		lblAdress = new JLabel("Address Line 1(State & Area)");
		lblAdress.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 14));
		lblAdress.setBounds(337, 364, 233, 15);
		panel.add(lblAdress);

		lblAddressLinecountry = new JLabel("Address Line 2(Country)");
		lblAddressLinecountry.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 14));
		lblAddressLinecountry.setBounds(337, 418, 207, 15);
		panel.add(lblAddressLinecountry);

		btnSave = new JButton("Save");
		btnSave.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnSave.setIcon(new ImageIcon(getClass().getResource("/com/logilite/img/save.png")));
		btnSave.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 16));
		btnSave.setBounds(337, 488, 130, 30);
		btnSave.setFocusPainted(false);
		panel.add(btnSave);

		btnHide = new JLabel("");
		btnHide.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnHide.setBackground(Color.BLACK);
		btnHide.setBounds(637, 270, 33, 30);
		panel.add(btnHide);

		btnShow = new JLabel("");
		btnShow.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnHide.setIcon(new ImageIcon(getClass().getResource("/com/logilite/img/hide.png")));
		btnShow.setIcon(new ImageIcon(getClass().getResource("/com/logilite/img/unhide.png")));
		btnShow.setBackground(Color.BLACK);
		btnShow.setBounds(637, 270, 33, 30);
		panel.add(btnShow);

		btnClear = new JButton("Clear");
		btnClear.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnClear.setIcon(new ImageIcon(getClass().getResource("/com/logilite/img/clear.png")));
		btnClear.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 16));
		btnClear.setBounds(497, 488, 130, 30);
		btnClear.setFocusPainted(false);
		panel.add(btnClear);
	}

}
