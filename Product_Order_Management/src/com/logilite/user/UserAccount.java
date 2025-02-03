package com.logilite.user;

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

import com.logilite.dao.UserDao;

public class UserAccount extends JPanel
{

	private static final long serialVersionUID = 1L;
	public static JPanel panel;
	public static JTextField IDField;
	public static JTextField nameField;
	public static JTextField emailField;
	public static JPasswordField passwordField;
	public static JTextField mobileField;
	public static JTextField ansField;
	public static JTextField address1Field;
	public static JTextField address2Field;
	public static JLabel lblEmail;
	public static JLabel lblPassword;
	public static JLabel lblMobileNo;
	public static JLabel lblSecurityQuestion;
	public static JLabel lblAnswer;
	public static JLabel lblAdress;
	public static JLabel lblAddressLinecountry;
	public static JButton btnUpdate;
	public static JLabel lblX;
	public static JLabel btnHide;
	public static JLabel btnShow;
	public static JButton btnDelete;
	public static JTextField seQueField;
	public static JLabel lblUsername;
	public static JLabel lblUserId;
	int xx, xy;
	private int uID;
	UserDao user = new UserDao();
	String data[] = new String[9]; 

	public UserAccount()
	{
		initComponent();
		init();
	}
	
	private void init() {
		uID = user.getUserId(UserDashboard.lblUseremail.getText());
		data = user.getUserData(uID);
		setValue();
		
		nameField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				char input = e.getKeyChar();
				if (!(input < '0' || input > '9') && input != '\b') {
					e.consume();
					JOptionPane.showMessageDialog(UserAccount.this, "Username doesn't contains any numbers!", "Warning", 2);
				}
			}
		});
		
		mobileField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				if (!Character.isDigit(e.getKeyChar())) {
					e.consume();
				}
			}
		});
		
		btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (isEmpty()) {
					if (!check()) {
						int id = Integer.parseInt(IDField.getText());
						String name = nameField.getText();
						String email = emailField.getText();
						String password = String.valueOf(passwordField.getPassword());
						String phone = mobileField.getText();
						String sque = seQueField.getText();
						String ans = ansField.getText();
						String address1 = address1Field.getText();
						String address2 = address2Field.getText();
						
						user.update(id, name, email, password, phone, sque, ans, address1, address2);
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
		
		btnDelete.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				user.delete(uID);
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
		seQueField.setText(data[5]);
		ansField.setText(data[6]);
		address1Field.setText(data[7]);
		address2Field.setText(data[8]);
	}
	
	public boolean isEmpty() {
		if (nameField.getText().isEmpty()) {
			JOptionPane.showMessageDialog(UserAccount.this, "Username is required", "Warning", 2);
			return false;
		}
		if (emailField.getText().isEmpty()) {
			JOptionPane.showMessageDialog(UserAccount.this, "Email address is required", "Warning", 2);
			return false;
		}
		if (!emailField.getText().matches("^.+@.+\\..+$")) {
			JOptionPane.showMessageDialog(UserAccount.this, "Invalid email address", "Warning", 2);
			return false;
		}
		if (String.valueOf(passwordField.getPassword()).isEmpty()) {
			JOptionPane.showMessageDialog(UserAccount.this, "Password is required", "Warning", 2);
			return false;
		}
		if (mobileField.getText().isEmpty()) {
			JOptionPane.showMessageDialog(UserAccount.this, "Phone number is required", "Warning", 2);
			return false;
		}
		if (mobileField.getText().length() > 15) {
			JOptionPane.showMessageDialog(UserAccount.this, "Phone number is to long", "Warning", 2);
			return false;
		}
		if (mobileField.getText().length() < 10) {
			JOptionPane.showMessageDialog(UserAccount.this, "Phone number is short", "Warning", 2);
			return false;
		}
		if (ansField.getText().isEmpty()) {
			JOptionPane.showMessageDialog(UserAccount.this, "Security answer is required", "Warning", 2);
			return false;
		}
		if (address1Field.getText().isEmpty()) {
			JOptionPane.showMessageDialog(UserAccount.this, "Address line 1 is required", "Warning", 2);
			return false;
		}
		if (address2Field.getText().isEmpty()) {
			JOptionPane.showMessageDialog(UserAccount.this, "Address line 2 is required", "Warning", 2);
			return false;
		}
		return true;
	}
	
	private boolean check() {
		String newEmail = emailField.getText();
		String newPhone = mobileField.getText();
		
		if (newEmail.equals(data[2]) && newPhone.equals(data[4])) {
			return false;
		}else {
			if (!newEmail.equals(data[2])) {
				boolean x = user.isEmailExists(newEmail);
				if (x) {
					JOptionPane.showMessageDialog(UserAccount.this, "This email already exists", "Warning", 2);
				}
				return x;
			}
			if (!newPhone.equals(data[4])) {
				boolean x = user.isPhoneExists(newPhone);
				if (x) {
					JOptionPane.showMessageDialog(UserAccount.this, "This phone number already exists", "Warning", 2);
				}
				return x;
			}
		}
		return false;
	}
	
	private void initComponent() {
		setLayout(null);
		
		panel = new JPanel();
		panel.setBounds(0, 0, 876, 501);
		add(panel);
		panel.setLayout(null);

		IDField = new JTextField();
		IDField.setBackground(new Color(186, 182, 182));
		IDField.setEditable(false);
		IDField.setBounds(181, 146, 290, 30);
		IDField.setFont(new Font("DejaVu Serif Condensed", Font.PLAIN, 14));
		panel.add(IDField);
		IDField.setColumns(10);
		
		nameField = new JTextField();
		nameField.setBounds(181, 203, 290, 30);
		nameField.setFont(new Font("DejaVu Serif Condensed", Font.PLAIN, 14));
		panel.add(nameField);
		nameField.setColumns(10);
		
		emailField = new JTextField();
		emailField.setBounds(181, 263, 290, 30);
		emailField.setFont(new Font("DejaVu Serif Condensed", Font.PLAIN, 14));
		panel.add(emailField);
		emailField.setColumns(10);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(181, 321, 290, 30);
		passwordField.setFont(new Font("DejaVu Serif Condensed", Font.PLAIN, 14));
		panel.add(passwordField);
		
		mobileField = new JTextField();
		mobileField.setBounds(181, 381, 290, 30);
		mobileField.setFont(new Font("DejaVu Serif Condensed", Font.PLAIN, 14));
		panel.add(mobileField);
		mobileField.setColumns(10);
		
		ansField = new JTextField();
		ansField.setBounds(532, 203, 290, 30);
		ansField.setFont(new Font("DejaVu Serif Condensed", Font.PLAIN, 14));
		panel.add(ansField);
		ansField.setColumns(10);
		
		address1Field = new JTextField();
		address1Field.setBounds(532, 263, 290, 30);
		address1Field.setFont(new Font("DejaVu Serif Condensed", Font.PLAIN, 14));
		panel.add(address1Field);
		address1Field.setColumns(10);
		
		address2Field = new JTextField();
		address2Field.setBounds(532, 321, 290, 30);
		address2Field.setFont(new Font("DejaVu Serif Condensed", Font.PLAIN, 14));
		panel.add(address2Field);
		address2Field.setColumns(10);
		
		lblUserId = new JLabel("User ID");
		lblUserId.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 14));
		lblUserId.setBounds(181, 124, 207, 15);
		panel.add(lblUserId);
		
		lblUsername = new JLabel("Username");
		lblUsername.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 14));
		lblUsername.setBounds(181, 184, 207, 15);
		panel.add(lblUsername);
		
		lblEmail = new JLabel("Email");
		lblEmail.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 14));
		lblEmail.setBounds(181, 242, 207, 15);
		panel.add(lblEmail);
		
		lblPassword = new JLabel("Password");
		lblPassword.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 14));
		lblPassword.setBounds(181, 302, 207, 15);
		panel.add(lblPassword);
		
		lblMobileNo = new JLabel("Mobile no.");
		lblMobileNo.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 14));
		lblMobileNo.setBounds(181, 360, 207, 15);
		panel.add(lblMobileNo);
		
		lblSecurityQuestion = new JLabel("Security Question");
		lblSecurityQuestion.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 14));
		lblSecurityQuestion.setBounds(532, 124, 207, 15);
		panel.add(lblSecurityQuestion);
		
		lblAnswer = new JLabel("Answer");
		lblAnswer.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 14));
		lblAnswer.setBounds(532, 184, 207, 15);
		panel.add(lblAnswer);
		
		lblAdress = new JLabel("Address Line 1(State & Area)");
		lblAdress.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 14));
		lblAdress.setBounds(532, 245, 233, 15);
		panel.add(lblAdress);
		
		lblAddressLinecountry = new JLabel("Address Line 2(Country)");
		lblAddressLinecountry.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 14));
		lblAddressLinecountry.setBounds(532, 302, 207, 15);
		panel.add(lblAddressLinecountry);
		
		btnUpdate = new JButton("Update");
		btnUpdate.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnUpdate.setIcon(new ImageIcon(getClass().getResource("/com/logilite/img/update.png")));
		btnUpdate.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 16));
		btnUpdate.setBounds(532, 381, 134, 30);
		btnUpdate.setFocusPainted(false);
		panel.add(btnUpdate);
		
		btnHide = new JLabel("");
		btnHide.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnHide.setIcon(new ImageIcon(getClass().getResource("/com/logilite/img/hide.png")));
		btnHide.setBackground(Color.BLACK);
		btnHide.setBounds(481, 321, 33, 30);
		panel.add(btnHide);
		
		btnShow = new JLabel("");
		btnShow.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnShow.setIcon(new ImageIcon(getClass().getResource("/com/logilite/img/unhide.png")));
		btnShow.setBackground(Color.BLACK);
		btnShow.setBounds(481, 321, 33, 30);
		panel.add(btnShow);
		
		btnDelete = new JButton("Delete");
		btnDelete.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnDelete.setIcon(new ImageIcon(getClass().getResource("/com/logilite/img/remove.png")));
		btnDelete.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 16));
		btnDelete.setBounds(685, 381, 137, 30);
		btnDelete.setFocusPainted(false);
		panel.add(btnDelete);
		
		seQueField = new JTextField();
		seQueField.setBackground(new Color(186, 182, 182));
		seQueField.setEditable(false);
		seQueField.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 14));
		seQueField.setBounds(532, 146, 290, 30);
		panel.add(seQueField);
		seQueField.setColumns(10);
	}
}
