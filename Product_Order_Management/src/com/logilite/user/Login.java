package com.logilite.user;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import com.logilite.admin.AdminDashboard;
import com.logilite.dao.Statistics;
import com.logilite.dao.UserDao;
import com.logilite.supplier.SupplierDashboard;

public class Login extends JFrame
{

	private static final long		serialVersionUID	= 1L;
	public static JPanel			contentPane;
	public static JTextField		email;
	public static JPasswordField	passwordField;
	public static JPanel			panel;
	public static JLabel			lblImage;
	public static JLabel			lblW;
	public static JLabel			lblTo;
	public static JLabel			lblProduct;
	public static JLabel			lblInventory;
	public static JPanel			panel_1;
	public static JLabel			lblX;
	public static JLabel			lblLogin;
	public static JLabel			lblEmail;
	public static JLabel			lblPassword;
	public static JLabel			btnHide;
	public static JLabel			btnShow;
	public static JRadioButton		rdbtnUser;
	public static JRadioButton		rdbtnSupplier;
	public static JRadioButton		rdbtnAdmin;
	public static JButton			btnLogin;
	public static JLabel			lblDontHaveAnAccount;
	public static JLabel			lblSign;
	public static JLabel			lblForgotPassword;
	public static JTextField		dbConfigField;
	public static JDialog			dbConfigDialog;
	public static JLabel			lblHost;
	public static JTextField		hostField;
	public static JLabel			lblPort;
	public static JTextField		portField;
	public static JLabel			lblDb;
	public static JTextField		dbField;
	public static JLabel			lblUser;
	public static JTextField		userField;
	public static JLabel			lblPasswordDB;
	public static JPasswordField	passwordFieldDB;
	public static JButton			btnTestConnection;
	public static JLabel 			lblClickHereTo;
	public static ButtonGroup		bg					= new ButtonGroup();
	public static Connection		conn;
	public static String			host				= null;
	public static String			port				= null;
	public static String			db					= null;
	public static String			user				= null;
	public static String			password			= null;
	public static String			savedHost			= null;
	public static String			savedPort			= null;
	public static String			savedDb				= null;
	public static String			savedUsername		= null;
	public static String			savedPassword		= null;
	Statistics						statistics			= new Statistics();
	UserDao							userdao				= new UserDao();

	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable() {
			public void run()
			{
				try
				{
					Login frame = new Login();
					frame.showSplashScreen();
					frame.setVisible(true);
				}
				catch (Exception e)
				{
					Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, e);
				}
			}
		});
	}

	public Login()
	{
		initComponent();
		init();
	}

	private void showSplashScreen()
	{
		SwingUtilities.invokeLater(() -> {
			SplashScreen splashScreen = new SplashScreen();
			splashScreen.showSplash();
		});
	}

	private void initComponent()
	{
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent e)
			{
				for (double i = 0.1; i <= 1.0; i += 0.1)
				{
					String s = "" + i;
					float f = Float.parseFloat(s);
					Login.this.setOpacity(f);
					try
					{
						Thread.sleep(20);
					}
					catch (InterruptedException e1)
					{
						Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, e1);
					}
				}
			}
		});
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		contentPane = new JPanel();
		setSize(623, 455);
		setUndecorated(true);
		setLocationRelativeTo(null);
		
		contentPane.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent evt)
			{
				if (evt.getKeyCode() == KeyEvent.VK_ENTER)
				{
					btnLogin.doClick();
				}
			}
		});
		contentPane.setFocusable(true);
		contentPane.requestFocusInWindow();
		setContentPane(contentPane);
		contentPane.setLayout(null);

		panel = new JPanel();
		panel.setBackground(Color.LIGHT_GRAY);
		panel.setBounds(0, 0, 267, 455);
		contentPane.add(panel);
		panel.setLayout(null);

		lblImage = new JLabel("");
		lblImage.setIcon(new ImageIcon(getClass().getResource("/com/logilite/img/login_page_logo.png")));
		lblImage.setBounds(12, 12, 225, 205);
		panel.add(lblImage);

		lblW = new JLabel("WELCOME");
		lblW.setForeground(new Color(255, 255, 255));
		lblW.setHorizontalAlignment(SwingConstants.CENTER);
		lblW.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 35));
		lblW.setBounds(22, 229, 232, 48);
		panel.add(lblW);

		lblTo = new JLabel("TO");
		lblTo.setForeground(new Color(255, 255, 255));
		lblTo.setHorizontalAlignment(SwingConstants.CENTER);
		lblTo.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 35));
		lblTo.setBounds(32, 274, 222, 49);
		panel.add(lblTo);

		lblProduct = new JLabel("PRODUCT");
		lblProduct.setForeground(new Color(255, 255, 255));
		lblProduct.setHorizontalAlignment(SwingConstants.CENTER);
		lblProduct.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 35));
		lblProduct.setBounds(22, 323, 232, 44);
		panel.add(lblProduct);

		lblInventory = new JLabel("INVENTORY");
		lblInventory.setForeground(new Color(255, 255, 255));
		lblInventory.setHorizontalAlignment(SwingConstants.CENTER);
		lblInventory.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 35));
		lblInventory.setBounds(22, 374, 232, 44);
		panel.add(lblInventory);

		panel_1 = new JPanel();
		panel_1.setBackground(Color.LIGHT_GRAY);
		panel_1.setBounds(265, 0, 358, 455);
		contentPane.add(panel_1);
		panel_1.setLayout(null);

		lblX = new JLabel("");
		lblX.setIcon(new ImageIcon(getClass().getResource("/com/logilite/img/closewindow.png")));
		lblX.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lblX.setHorizontalAlignment(SwingConstants.CENTER);
		lblX.setFont(new Font("URW Bookman", Font.BOLD, 23));
		lblX.setBounds(307, 12, 33, 37);
		panel_1.add(lblX);

		dbConfigField = new JTextField("Click to configure DB");
		dbConfigField.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		dbConfigField.setFont(new Font("DejaVu Sans Condensed", Font.BOLD, 14));
		dbConfigField.setEditable(false);
		dbConfigField.setBounds(40, 100, 259, 30);
		panel_1.add(dbConfigField);
		dbConfigField.setColumns(10);

		if (conn != null)
		{
			dbConfigField.setText(host);
			dbConfigField.setBackground(new Color(144, 238, 144));
		}

		lblLogin = new JLabel("Login");
		lblLogin.setHorizontalAlignment(SwingConstants.CENTER);
		lblLogin.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 40));
		lblLogin.setBounds(12, 44, 320, 48);
		panel_1.add(lblLogin);

		email = new JTextField();
		email.setBounds(40, 170, 259, 30);
		panel_1.add(email);
		email.setColumns(10);

		passwordField = new JPasswordField();
		passwordField.setBounds(40, 243, 259, 30);
		panel_1.add(passwordField);

		lblEmail = new JLabel("Email");
		lblEmail.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 16));
		lblEmail.setBounds(40, 143, 84, 15);
		panel_1.add(lblEmail);

		lblPassword = new JLabel("Password");
		lblPassword.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 16));
		lblPassword.setBounds(40, 216, 96, 15);
		panel_1.add(lblPassword);

		btnHide = new JLabel("");
		btnHide.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnHide.setBackground(new Color(0, 0, 0));
		btnHide.setIcon(new ImageIcon(getClass().getResource("/com/logilite/img/hide.png")));
		btnHide.setBounds(307, 243, 33, 30);
		panel_1.add(btnHide);

		btnShow = new JLabel("");
		btnShow.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnShow.setIcon(new ImageIcon(getClass().getResource("/com/logilite/img/unhide.png")));
		btnShow.setBackground(Color.BLACK);
		btnShow.setBounds(307, 243, 33, 30);
		panel_1.add(btnShow);

		rdbtnUser = new JRadioButton("User");
		rdbtnUser.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		rdbtnUser.setBackground(new Color(192, 191, 188));
		rdbtnUser.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 14));
		rdbtnUser.setBounds(40, 285, 64, 23);
		panel_1.add(rdbtnUser);

		rdbtnSupplier = new JRadioButton("Supplier");
		rdbtnSupplier.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		rdbtnSupplier.setBackground(new Color(192, 191, 188));
		rdbtnSupplier.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 14));
		rdbtnSupplier.setBounds(121, 285, 96, 23);
		panel_1.add(rdbtnSupplier);

		rdbtnAdmin = new JRadioButton("Admin");
		rdbtnAdmin.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		rdbtnAdmin.setBackground(new Color(192, 191, 188));
		rdbtnAdmin.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 14));
		rdbtnAdmin.setBounds(226, 285, 73, 23);
		panel_1.add(rdbtnAdmin);

		btnLogin = new JButton("Login");
		btnLogin.setIcon(new ImageIcon(getClass().getResource("/com/logilite/img/login.png")));
		btnLogin.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnLogin.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 16));
		btnLogin.setBounds(49, 326, 236, 30);
		btnLogin.setFocusPainted(false);
		panel_1.add(btnLogin);

		lblDontHaveAnAccount = new JLabel("Don't have an account?");
		lblDontHaveAnAccount.setBounds(54, 379, 175, 15);
		panel_1.add(lblDontHaveAnAccount);

		lblSign = new JLabel("Sign Up");
		lblSign.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lblSign.setBounds(226, 379, 70, 15);
		panel_1.add(lblSign);

		lblForgotPassword = new JLabel("Forgot Password?");
		lblForgotPassword.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lblForgotPassword.setBounds(105, 406, 136, 15);
		panel_1.add(lblForgotPassword);
		
		lblClickHereTo = new JLabel("Click here to request for supplier.");
		lblClickHereTo.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lblClickHereTo.setIcon(new ImageIcon(Login.class.getResource("/com/logilite/img/right hand direction.png")));
		lblClickHereTo.setBounds(25, 430, 285, 18);
		panel_1.add(lblClickHereTo);

	}

	private void getDBData()
	{
		Properties p = new Properties();
		try (InputStream in = getClass().getClassLoader()
				.getResourceAsStream("com/logilite/component/db_config.properties"))
		{
			p.load(in);

			savedHost = p.getProperty("db.host");
			savedPort = p.getProperty("db.port");
			savedDb = p.getProperty("db.name");
			savedUsername = p.getProperty("db.user");
			savedPassword = p.getProperty("db.password");

			if (!savedHost.equals(null))
			{
				dbConfigField.setText(savedHost);				
			}else {
				dbConfigField.setText("Click to configure DB");
			}

		}
		catch (IOException e)
		{
			e.printStackTrace();
			dbConfigField.setText("Click to configure DB");
		}
	}

	private void init()
	{
		bg.add(rdbtnUser);
		bg.add(rdbtnSupplier);
		bg.add(rdbtnAdmin);
		rdbtnUser.setFocusPainted(false);
		rdbtnSupplier.setFocusPainted(false);
		rdbtnAdmin.setFocusPainted(false);

		rdbtnUser.setSelected(true);

		getDBData();
		createConnection();

		getRootPane().setDefaultButton(btnLogin);
		SwingUtilities.invokeLater(new Runnable() {
	        @Override
	        public void run() {
	            email.requestFocusInWindow();
	        }
	    });
		
		lblX.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e)
			{
				try
				{
					if (conn != null)
					{
						conn.close();
					}
				}
				catch (SQLException e1)
				{
					e1.printStackTrace();
					Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, e);
				}
				System.exit(0);
			}
		});

		dbConfigField.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e)
			{
				openDatabaseConfigPopup();
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

		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0)
			{
				if (isEmpty())
				{
					String email_address = email.getText();
					String password = String.valueOf(passwordField.getPassword());
					if (rdbtnUser.isSelected())
					{
						try
						{
							PreparedStatement pst = conn
									.prepareStatement("SELECT * FROM users WHERE uemail = ? AND upassword = ?");
							pst.setString(1, email_address);
							pst.setString(2, password);
							ResultSet rs = pst.executeQuery();

							if (rs.next())
							{
								UserDashboard ud = new UserDashboard();
								ud.setVisible(true);
								UserDashboard.lblUseremail.setText(email_address);
								statistics.user(rs.getInt(1));
								Login.this.dispose();
							}
							else
							{
								JOptionPane.showMessageDialog(Login.this, "Incorrect email or password", "Login Failed",
										2);
							}
						}
						catch (SQLException e)
						{
							Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, e);
						}
					}
					else if (rdbtnSupplier.isSelected())
					{
						try
						{
							PreparedStatement pst = conn
									.prepareStatement("SELECT * FROM supplier WHERE semail = ? AND spassword = ?");
							pst.setString(1, email_address);
							pst.setString(2, password);
							ResultSet rs = pst.executeQuery();

							if (rs.next())
							{
								SupplierDashboard sd = new SupplierDashboard();
								sd.setVisible(true);
								SupplierDashboard.lblSupplieremail.setText(email_address);
								statistics.supplier(rs.getString(2));
								Login.this.dispose();
							}
							else
							{
								JOptionPane.showMessageDialog(Login.this, "Incorrect email or password", "Login Failed",
										2);
							}
						}
						catch (SQLException e)
						{
							Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, e);
						}
					}
					else
					{
						try
						{
							PreparedStatement pst = Statistics.conn
									.prepareStatement("SELECT * FROM admin WHERE email = ? AND password = ?");
							pst.setString(1, email_address);
							pst.setString(2, password);
							ResultSet rs = pst.executeQuery();

							if (rs.next())
							{
								AdminDashboard ad = new AdminDashboard();
								ad.setVisible(true);
								AdminDashboard.lblAdminemail.setText(email_address);
								statistics.admin();
								Login.this.dispose();
							}
							else
							{
								JOptionPane.showMessageDialog(Login.this, "Incorrect email or password", "Login Failed",
										2);
							}
						}
						catch (SQLException e)
						{
							Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, e);
						}
					}
				}
			}
		});

		lblSign.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e)
			{
				if (conn != null)
				{
					new Signup().setVisible(true);
					Login.this.dispose();
				}
				else
				{
					JOptionPane.showMessageDialog(Login.this, "Please first configure DB", "Warning", 2);
				}
			}
		});

		lblForgotPassword.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e)
			{
				if (conn != null)
				{
					new ForgotPassword().setVisible(true);
					Login.this.dispose();
				}
				else
				{
					JOptionPane.showMessageDialog(Login.this, "Please first configure DB", "Warning", 2);
				}
			}
		});
		
		lblClickHereTo.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (conn != null)
				{
					new SupplierRequest().setVisible(true);
					Login.this.dispose();
				}
				else
				{
					JOptionPane.showMessageDialog(Login.this, "Please first configure DB", "Warning", 2);
				}
			}
		});
		
	}
	
	private void createConnection() {
		if (conn == null)
		{
			host = savedHost;
			port = savedPort;
			db = savedDb;
			user = savedUsername;
			password = savedPassword;

			boolean isConnected = testDatabaseConnection(host, port, db, user, password);
			if (isConnected)
			{
				dbConfigField.setText(host);
				dbConfigField.setBackground(new Color(144, 238, 144));
			}
			else
			{
				conn = null;
				dbConfigField.setText("Click to configure DB");
			}
		}
	}

	private void openDatabaseConfigPopup()
	{
		dbConfigDialog = new JDialog(Login.this, "Database Configuration", true);
		dbConfigDialog.setSize(400, 300);
		dbConfigDialog.setLocationRelativeTo(Login.this);

		panel = new JPanel();
		panel.setLayout(null);
		dbConfigDialog.getContentPane().add(panel);

		lblHost = new JLabel("Host:");
		lblHost.setBounds(30, 30, 100, 25);
		panel.add(lblHost);

		hostField = new JTextField();
		hostField.setBounds(120, 30, 200, 25);
		panel.add(hostField);

		lblPort = new JLabel("Port:");
		lblPort.setBounds(30, 70, 100, 25);
		panel.add(lblPort);

		portField = new JTextField("5432");
		portField.setBounds(120, 70, 200, 25);
		panel.add(portField);

		lblDb = new JLabel("Database:");
		lblDb.setBounds(30, 110, 100, 25);
		panel.add(lblDb);

		dbField = new JTextField();
		dbField.setBounds(120, 110, 200, 25);
		panel.add(dbField);

		lblUser = new JLabel("User:");
		lblUser.setBounds(30, 150, 100, 25);
		panel.add(lblUser);

		userField = new JTextField();
		userField.setBounds(120, 150, 200, 25);
		panel.add(userField);

		lblPasswordDB = new JLabel("Password:");
		lblPasswordDB.setBounds(30, 190, 100, 25);
		panel.add(lblPasswordDB);

		passwordFieldDB = new JPasswordField();
		passwordFieldDB.setBounds(120, 190, 200, 25);
		panel.add(passwordFieldDB);

		btnTestConnection = new JButton("Test Connection");
		btnTestConnection.setBounds(120, 230, 200, 30);
		btnTestConnection.setFocusPainted(false);
		panel.add(btnTestConnection);

		hostField.setText(savedHost);
		portField.setText(savedPort);
		dbField.setText(savedDb);
		userField.setText(savedUsername);
		passwordFieldDB.setText(savedPassword);

		btnTestConnection.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				host = hostField.getText();
				port = portField.getText();
				db = dbField.getText();
				user = userField.getText();
				password = new String(passwordFieldDB.getPassword());

				if (isEmptyDatabase())
				{
					boolean isConnected = testDatabaseConnection(host, port, db, user, password);
					if (isConnected)
					{
						Properties properties = new Properties();
						properties.setProperty("db.host", hostField.getText());
						properties.setProperty("db.port", portField.getText());
						properties.setProperty("db.name", dbField.getText());
						properties.setProperty("db.user", userField.getText());
						properties.setProperty("db.password", new String(passwordFieldDB.getPassword()));

						String filePath = getClass().getClassLoader()
								.getResource("com/logilite/component/db_config.properties").getPath();
						try (FileOutputStream out = new FileOutputStream(filePath))
						{
							properties.store(out, null);
							dbConfigField.setText(hostField.getText());
							dbConfigField.setBackground(new Color(144, 238, 144));
							JOptionPane.showMessageDialog(dbConfigDialog, "Connection Successful", "Success",
									JOptionPane.INFORMATION_MESSAGE);
							dbConfigDialog.dispose();
						}
						catch (IOException e1)
						{
						}
					}
					else
					{
						conn = null;
						dbConfigField.setText(host);
						dbConfigField.setBackground(new Color(246, 97, 81));
						JOptionPane.showMessageDialog(dbConfigDialog, "Connection Failed", "Error",
								JOptionPane.ERROR_MESSAGE);
						dbConfigDialog.dispose();
					}
				}
			}
		});
		dbConfigDialog.setVisible(true);
	}

	private boolean testDatabaseConnection(String host, String port, String db, String user, String password)
	{
		String url = "jdbc:postgresql://" + host + ":" + port + "/" + db;
		
		try
		{
			conn = DriverManager.getConnection(url, user, password);
			if (conn != null)
			{
				Statistics.conn = conn;
				return true;
			}
		}
		catch (SQLException e)
		{
		}

		return false;
	}

	public boolean isEmptyDatabase()
	{
		if (hostField.getText().isEmpty())
		{
			JOptionPane.showMessageDialog(Login.this, "Host is required", "Warning", 2);
			return false;
		}
		if (portField.getText().isEmpty())
		{
			JOptionPane.showMessageDialog(Login.this, "Port address is required", "Warning", 2);
			return false;
		}
		if (dbField.getText().isEmpty())
		{
			JOptionPane.showMessageDialog(Login.this, "Database name is required", "Warning", 2);
			return false;
		}
		if (userField.getText().isEmpty())
		{
			JOptionPane.showMessageDialog(Login.this, "User name is required", "Warning", 2);
			return false;
		}
		if (String.valueOf(passwordFieldDB.getPassword()).isEmpty())
		{
			JOptionPane.showMessageDialog(Login.this, "Password is required", "Warning", 2);
			return false;
		}
		return true;
	}

	private boolean isEmpty()
	{
		if (conn == null)
		{
			JOptionPane.showMessageDialog(Login.this, "Please first configure DB", "Warning", 2);
			return false;
		}
		if (email.getText().isEmpty())
		{
			JOptionPane.showMessageDialog(Login.this, "Email address is required", "Warning", 2);
			return false;
		}
		if (!email.getText().matches("^.+@.+\\..+$"))
		{
			JOptionPane.showMessageDialog(Login.this, "Invalid email address", "Warning", 2);
			return false;
		}
		if (String.valueOf(passwordField.getPassword()).isEmpty())
		{
			JOptionPane.showMessageDialog(Login.this, "Password is required", "Warning", 2);
			return false;
		}
		return true;
	}
}
