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
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import com.logilite.dao.UserDao;
import com.logilite.email.Email;

public class Signup extends JFrame
{

	private static final long	serialVersionUID	= 1L;
	public static JPanel				contentPane;
	public static JTextField IDField;
	public static JTextField usernameField;
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
	public static JButton btnSave;
	public static JLabel lblX;
	public static JLabel btnHide;
	public static JLabel btnShow;
	public static JButton btnBack;
	public static JPanel panel;
	public static JLabel lblSignUp;
	@SuppressWarnings("rawtypes")
	public static JComboBox comboBox;
	public static JLabel lblUserId;
	public static JLabel lblUsername;
	int xx, xy;
	Color noEditColor = new Color(204,204,204);
	UserDao user = new UserDao();
	public static JLabel lblEnterOtp;
	public static JTextField OTPField;
	public static JButton btnVerifyOtp;
	public static JButton btnSendOtp;
	public static int otp;
	public static boolean otpVerified = false;
	private JButton btnResendOtp;
	
	public Signup()
	{
		initComponent();
		init();
	}
	
	private void init() {
		IDField.setBackground(noEditColor);
		IDField.setText(String.valueOf(user.getMaxRow()));
		btnVerifyOtp.setEnabled(false);
		
		panel.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				xx = e.getX();
				xy = e.getY();
			}
		});
		
		panel.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				int x = e.getXOnScreen();
				int y = e.getYOnScreen();
				Signup.this.setLocation(x - xx, y - xy);
			}
		});
		
		lblX.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				otpVerified = false;
				Signup.this.setVisible(false);
				Login login = new Login();
				login.setVisible(true);
			}
		});
		
		usernameField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				char input = e.getKeyChar();
				if (!(input < '0' || input > '9') && input != '\b') {
					e.consume();
					JOptionPane.showMessageDialog(Signup.this, "Username doesn't contains any numbers!", "Warning", 2);
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
		
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (isEmpty()) {
					int id = Integer.parseInt(IDField.getText());
					String name = usernameField.getText();
					String email = emailField.getText();
					String password = String.valueOf(passwordField.getPassword());
					String phone = mobileField.getText();
					String sque = comboBox.getSelectedItem().toString();
					String ans = ansField.getText();
					String address1 = address1Field.getText();
					String address2 = address2Field.getText();
					
					if (!user.isEmailExists(email)) {
						if (!user.isPhoneExists(phone)) {
							user.insert(id, name, email, password, phone, sque, ans, address1, address2);
							new Login().setVisible(true);
							Signup.this.dispose();
						}else {
							JOptionPane.showMessageDialog(Signup.this, "This phone number already exists!", "Warning", 2);
						}
					}else {
						JOptionPane.showMessageDialog(Signup.this, "This email address already exists!", "Warning", 2);
					}
				}
			}
		});
		
		btnHide.addMouseListener(new MouseAdapter() {
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
		
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				otpVerified = false;
				new Login().setVisible(true);
				Signup.this.dispose();
			}
		});
		
		btnVerifyOtp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!OTPField.getText().isEmpty()) {
					if (OTPField.getText().equals(String.valueOf(otp))) {
						JOptionPane.showMessageDialog(null, "OTP verification successfull");
						otpVerified = true;
						btnVerifyOtp.setEnabled(false);
						btnSendOtp.setEnabled(false);
						btnResendOtp.setEnabled(false);
						emailField.setEditable(false);
						OTPField.setEditable(false);
					}else {
						JOptionPane.showMessageDialog(null, "OTP verification failed");
						otpVerified = false;
					}
				}else {
					JOptionPane.showMessageDialog(Signup.this, "Please enter OTP", "Warning", 2);
				}
			}
		});
		
		btnSendOtp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (isEmailValidate()) {
					Random random = new Random();
					otp = random.nextInt(999999);
					if (Email.send(emailField.getText(), "OTP Verification", "Your OTP is : " +otp)) {
						JOptionPane.showMessageDialog(null, "OTP sent successfully");
						btnSendOtp.setVisible(false);
						btnResendOtp.setVisible(true);
						btnVerifyOtp.setEnabled(true);
					}else {
						JOptionPane.showMessageDialog(null, "OTP not sent");
					}
				}
			}
		});
		
		btnResendOtp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (isEmailValidate()) {
					Random random = new Random();
					otp = random.nextInt(999999);
					if (Email.send(emailField.getText(), "OTP Verification", "Your OTP is : " +otp)) {
						JOptionPane.showMessageDialog(null, "OTP sent successfully");
					}else {
						JOptionPane.showMessageDialog(null, "OTP not sent");
					}
				}
			}
		});
		
		OTPField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				if (!Character.isDigit(e.getKeyChar())) {
					e.consume();
				}
			}
		});
	}
	
	public boolean isEmailValidate() {
		if (emailField.getText().isEmpty()) {
			JOptionPane.showMessageDialog(Signup.this, "Email address is required", "Warning", 2);
			return false;
		}
		if (!emailField.getText().matches("^.+@.+\\..+$")) {
			JOptionPane.showMessageDialog(Signup.this, "Invalid email address", "Warning", 2);
			return false;
		}
		if (!emailField.getText().toLowerCase().equals(emailField.getText())) {
			JOptionPane.showMessageDialog(Signup.this, "Please enter email address in lowercase", "Warning", 2);
			return false;
		}
		if (user.isEmailExists(emailField.getText())) {
			JOptionPane.showMessageDialog(Signup.this, "This email address already exists!", "Warning", 2);
			return false;
		}
		return true;
	}
	
	public boolean isEmpty() {
		if (usernameField.getText().isEmpty()) {
			JOptionPane.showMessageDialog(Signup.this, "Username is required", "Warning", 2);
			return false;
		}
		if (emailField.getText().isEmpty()) {
			JOptionPane.showMessageDialog(Signup.this, "Email address is required", "Warning", 2);
			return false;
		}
		if (!emailField.getText().matches("^.+@.+\\..+$")) {
			JOptionPane.showMessageDialog(Signup.this, "Invalid email address", "Warning", 2);
			return false;
		}
		if (!emailField.getText().toLowerCase().equals(emailField.getText())) {
			JOptionPane.showMessageDialog(Signup.this, "Please enter email address in lowercase", "Warning", 2);
			return false;
		}
		if (String.valueOf(passwordField.getPassword()).isEmpty()) {
			JOptionPane.showMessageDialog(Signup.this, "Password is required", "Warning", 2);
			return false;
		}
		if (!isPasswordValid(String.valueOf(passwordField.getPassword()))) {
		    return false;
		}

		if (mobileField.getText().isEmpty()) {
			JOptionPane.showMessageDialog(Signup.this, "Phone number is required", "Warning", 2);
			return false;
		}
		if (mobileField.getText().length() > 15) {
			JOptionPane.showMessageDialog(Signup.this, "Phone number is to long", "Warning", 2);
			return false;
		}
		if (mobileField.getText().length() < 10) {
			JOptionPane.showMessageDialog(Signup.this, "Phone number is short", "Warning", 2);
			return false;
		}
		if (comboBox.getSelectedIndex() == 0) {
			JOptionPane.showMessageDialog(Signup.this, "Security question is required", "Warning", 2);
			return false;
		}
		if (ansField.getText().isEmpty()) {
			JOptionPane.showMessageDialog(Signup.this, "Security answer is required", "Warning", 2);
			return false;
		}
		if (address1Field.getText().isEmpty()) {
			JOptionPane.showMessageDialog(Signup.this, "Address line 1 is required", "Warning", 2);
			return false;
		}
		if (address2Field.getText().isEmpty()) {
			JOptionPane.showMessageDialog(Signup.this, "Address line 2 is required", "Warning", 2);
			return false;
		}
		if (!otpVerified) {
			JOptionPane.showMessageDialog(Signup.this, "Please verify email", "Warning", 2);
		}
		return true;
	}
	
	public boolean isPasswordValid(String password) {
	    if (password.length() < 8) {
	        JOptionPane.showMessageDialog(Signup.this, "Password must be at least 8 characters long.", "Warning", 2);
	        return false;
	    }
	    if (!password.matches(".*[A-Z].*")) {
	        JOptionPane.showMessageDialog(Signup.this, "Password must contain at least one uppercase letter.", "Warning", 2);
	        return false;
	    }
	    if (!password.matches(".*[a-z].*")) {
	        JOptionPane.showMessageDialog(Signup.this, "Password must contain at least one lowercase letter.", "Warning", 2);
	        return false;
	    }
	    if (!password.matches(".*\\d.*")) {
	        JOptionPane.showMessageDialog(Signup.this, "Password must contain at least one digit.", "Warning", 2);
	        return false;
	    }
	    if (!password.matches(".*[@#$%^&+=!].*")) {
	        JOptionPane.showMessageDialog(Signup.this, "Password must contain at least one special character (@#$%^&+=!).", "Warning", 2);
	        return false;
	    }
	    return true;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void initComponent() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent e) {
				for (double i = 0.1; i <= 1.0; i += 0.1) {
					String s = "" + i;
					float f = Float.parseFloat(s);
					Signup.this.setOpacity(f);
					try
					{
						Thread.sleep(20);
					}
					catch (InterruptedException e1)
					{
						Logger.getLogger(Signup.class.getName()).log(Level.SEVERE, null, e1);
					}
				}
			}
		});
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		
		setSize(418, 735);
		setUndecorated(true);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		panel = new JPanel();
		panel.setBackground(new Color(238, 238, 238));
		panel.setBounds(0, 0, 418, 735);
		contentPane.add(panel);
		panel.setLayout(null);
		
		lblX = new JLabel("");
		lblX.setIcon(new ImageIcon(getClass().getResource("/com/logilite/img/closewindow.png")));
		lblX.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lblX.setHorizontalAlignment(SwingConstants.CENTER);
		lblX.setFont(new Font("URW Bookman", Font.BOLD, 23));
		lblX.setBounds(373, 12, 33, 42);
		panel.add(lblX);
		
		lblSignUp = new JLabel("Sign Up");
		lblSignUp.setBounds(0, 32, 418, 42);
		lblSignUp.setHorizontalAlignment(SwingConstants.CENTER);
		lblSignUp.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 30));
		panel.add(lblSignUp);
		
		IDField = new JTextField();
		IDField.setEditable(false);
		IDField.setBounds(49, 102, 290, 30);
		IDField.setFont(new Font("DejaVu Serif Condensed", Font.PLAIN, 14));
		panel.add(IDField);
		IDField.setColumns(10);
		
		usernameField = new JTextField();
		usernameField.setBounds(49, 159, 290, 30);
		usernameField.setFont(new Font("DejaVu Serif Condensed", Font.PLAIN, 14));
		panel.add(usernameField);
		usernameField.setColumns(10);
		
		emailField = new JTextField();
		emailField.setBounds(49, 219, 290, 30);
		emailField.setFont(new Font("DejaVu Serif Condensed", Font.PLAIN, 14));
		panel.add(emailField);
		emailField.setColumns(10);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(49, 340, 290, 30);
		passwordField.setFont(new Font("DejaVu Serif Condensed", Font.PLAIN, 14));
		panel.add(passwordField);
		
		mobileField = new JTextField();
		mobileField.setBounds(49, 400, 290, 30);
		mobileField.setFont(new Font("DejaVu Serif Condensed", Font.PLAIN, 14));
		panel.add(mobileField);
		mobileField.setColumns(10);
		
		comboBox = new JComboBox();
		comboBox.setBounds(49, 457, 290, 30);
		comboBox.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 14));
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"-- Select a question --", "What is your favourite color?", "What is your family name?", "What is your first school name?", "What is your pet name?", "What was your first car?"}));
		comboBox.setSelectedIndex(0);
		panel.add(comboBox);
		
		ansField = new JTextField();
		ansField.setBounds(49, 514, 290, 30);
		ansField.setFont(new Font("DejaVu Serif Condensed", Font.PLAIN, 14));
		panel.add(ansField);
		ansField.setColumns(10);
		
		address1Field = new JTextField();
		address1Field.setBounds(49, 576, 290, 30);
		address1Field.setFont(new Font("DejaVu Serif Condensed", Font.PLAIN, 14));
		panel.add(address1Field);
		address1Field.setColumns(10);
		
		address2Field = new JTextField();
		address2Field.setBounds(49, 635, 290, 30);
		address2Field.setFont(new Font("DejaVu Serif Condensed", Font.PLAIN, 14));
		panel.add(address2Field);
		address2Field.setColumns(10);
		
		lblUserId = new JLabel("User ID");
		lblUserId.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 14));
		lblUserId.setBounds(49, 80, 207, 15);
		panel.add(lblUserId);
		
		lblUsername = new JLabel("Username");
		lblUsername.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 14));
		lblUsername.setBounds(49, 140, 207, 15);
		panel.add(lblUsername);
		
		lblEmail = new JLabel("Email");
		lblEmail.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 14));
		lblEmail.setBounds(49, 198, 207, 15);
		panel.add(lblEmail);
		
		lblPassword = new JLabel("Password");
		lblPassword.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 14));
		lblPassword.setBounds(49, 321, 207, 15);
		panel.add(lblPassword);
		
		lblMobileNo = new JLabel("Mobile no.");
		lblMobileNo.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 14));
		lblMobileNo.setBounds(49, 379, 207, 15);
		panel.add(lblMobileNo);
		
		lblSecurityQuestion = new JLabel("Security Question");
		lblSecurityQuestion.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 14));
		lblSecurityQuestion.setBounds(49, 436, 207, 15);
		panel.add(lblSecurityQuestion);
		
		lblAnswer = new JLabel("Answer");
		lblAnswer.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 14));
		lblAnswer.setBounds(49, 495, 207, 15);
		panel.add(lblAnswer);
		
		lblAdress = new JLabel("Address Line 1(State & Area)");
		lblAdress.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 14));
		lblAdress.setBounds(49, 556, 233, 15);
		panel.add(lblAdress);
		
		lblAddressLinecountry = new JLabel("Address Line 2(Country)");
		lblAddressLinecountry.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 14));
		lblAddressLinecountry.setBounds(49, 615, 207, 15);
		panel.add(lblAddressLinecountry);
		
		btnSave = new JButton("Save");
		btnSave.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnSave.setIcon(new ImageIcon(getClass().getResource("/com/logilite/img/save.png")));
		btnSave.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 16));
		btnSave.setBounds(59, 682, 124, 30);
		btnSave.setFocusPainted(false);
		panel.add(btnSave);
		
		btnHide = new JLabel("");
		btnHide.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnHide.setHorizontalAlignment(SwingConstants.CENTER);
		btnHide.setIcon(new ImageIcon(getClass().getResource("/com/logilite/img/hide.png")));
		btnHide.setBackground(Color.BLACK);
		btnHide.setBounds(349, 340, 33, 30);
		panel.add(btnHide);
		
		btnShow = new JLabel("");
		btnShow.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnShow.setHorizontalAlignment(SwingConstants.CENTER);
		btnShow.setIcon(new ImageIcon(getClass().getResource("/com/logilite/img/unhide.png")));
		btnShow.setBackground(Color.BLACK);
		btnShow.setBounds(349, 340, 33, 30);
		panel.add(btnShow);
		
		btnBack = new JButton("Back");
		btnBack.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnBack.setIcon(new ImageIcon(getClass().getResource("/com/logilite/img/back.png")));
		btnBack.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 16));
		btnBack.setBounds(202, 682, 124, 30);
		btnBack.setFocusPainted(false);
		panel.add(btnBack);	
		
		lblEnterOtp = new JLabel("Enter OTP");
		lblEnterOtp.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 14));
		lblEnterOtp.setBounds(49, 261, 207, 15);
		panel.add(lblEnterOtp);
		
		OTPField = new JTextField();
		OTPField.setFont(new Font("DejaVu Serif Condensed", Font.PLAIN, 14));
		OTPField.setColumns(10);
		OTPField.setBounds(49, 282, 134, 30);
		panel.add(OTPField);
		
		btnVerifyOtp = new JButton("Verify OTP");
		btnVerifyOtp.setBounds(202, 284, 117, 25);
		btnVerifyOtp.setFocusPainted(false);
		panel.add(btnVerifyOtp);
		
		btnSendOtp = new JButton("Send OTP");
		btnSendOtp.setFont(new Font("Dialog", Font.BOLD, 12));
		btnSendOtp.setBounds(215, 249, 124, 25);
		btnSendOtp.setFocusPainted(false);
		panel.add(btnSendOtp);
		
		btnResendOtp = new JButton("Resend OTP");
		btnResendOtp.setFont(new Font("Dialog", Font.BOLD, 12));
		btnResendOtp.setBounds(215, 249, 124, 25);
		btnResendOtp.setFocusPainted(false);
		panel.add(btnResendOtp);
	}
}
