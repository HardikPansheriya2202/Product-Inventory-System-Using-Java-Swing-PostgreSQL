package com.logilite.user;

import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import com.logilite.dao.SupplierDao;
import com.logilite.email.Email;

public class SupplierRequest extends JFrame
{

	private static final long	serialVersionUID	= 1L;
	public static JPanel		contentPane;
	public static JTextField	txtLastName;
	public static JTextField	txtEmail;
	public static JTextField	txtMobile;
	public static JTextField	txtAddress1;
	public static JTextField	txtAddress2;
	public static JTextField	txtFirstName;
	public static JPanel		panel;
	public static JLabel		lblLastName;
	public static JLabel		lblEmail;
	public static JLabel		lblMobileNo;
	public static JLabel		lblAdress;
	public static JLabel		lblAddressLinecountry;
	public static JButton		btnSend;
	public static JButton		btnClear;
	public static JLabel		lblRequestWillSend;
	public static JLabel		lblFirstName;
	public static JLabel		lblSupplierRequest;
	public static JLabel		lblX;
	SupplierDao					supplier			= new SupplierDao();
	public static JLabel		lblEnterOtp;
	public static JTextField	OTPField;
	public static JButton		btnVerifyOtp;
	public static JButton		btnSendOtp;
	public static int			otp;
	public static boolean		otpVerified			= false;
	private JButton				btnResendOtp;

	/**
	 * Create the frame.
	 */
	public SupplierRequest()
	{
		initComponent();
		init();
	}

	public void initComponent()
	{
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 490, 581);
		setLocationRelativeTo(null);
		setUndecorated(true);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		panel = new JPanel();
		panel.setBounds(0, 0, 490, 581);
		contentPane.add(panel);
		panel.setLayout(null);

		txtLastName = new JTextField();
		txtLastName.setFont(new Font("DejaVu Serif Condensed", Font.PLAIN, 14));
		txtLastName.setColumns(10);
		txtLastName.setBounds(106, 154, 290, 30);
		panel.add(txtLastName);

		txtEmail = new JTextField();
		txtEmail.setFont(new Font("DejaVu Serif Condensed", Font.PLAIN, 14));
		txtEmail.setColumns(10);
		txtEmail.setBounds(106, 209, 290, 30);
		panel.add(txtEmail);

		txtMobile = new JTextField();
		txtMobile.setFont(new Font("DejaVu Serif Condensed", Font.PLAIN, 14));
		txtMobile.setColumns(10);
		txtMobile.setBounds(106, 339, 290, 30);
		panel.add(txtMobile);

		txtAddress1 = new JTextField();
		txtAddress1.setFont(new Font("DejaVu Serif Condensed", Font.PLAIN, 14));
		txtAddress1.setColumns(10);
		txtAddress1.setBounds(106, 394, 290, 30);
		panel.add(txtAddress1);

		txtAddress2 = new JTextField();
		txtAddress2.setFont(new Font("DejaVu Serif Condensed", Font.PLAIN, 14));
		txtAddress2.setColumns(10);
		txtAddress2.setBounds(106, 448, 290, 30);
		panel.add(txtAddress2);

		lblLastName = new JLabel("Last Name");
		lblLastName.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 14));
		lblLastName.setBounds(106, 137, 207, 15);
		panel.add(lblLastName);

		lblEmail = new JLabel("Email");
		lblEmail.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 14));
		lblEmail.setBounds(106, 192, 207, 15);
		panel.add(lblEmail);

		lblMobileNo = new JLabel("Mobile no.");
		lblMobileNo.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 14));
		lblMobileNo.setBounds(106, 322, 207, 15);
		panel.add(lblMobileNo);

		lblAdress = new JLabel("Address Line 1(State & Area)");
		lblAdress.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 14));
		lblAdress.setBounds(106, 377, 233, 15);
		panel.add(lblAdress);

		lblAddressLinecountry = new JLabel("Address Line 2(Country)");
		lblAddressLinecountry.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 14));
		lblAddressLinecountry.setBounds(106, 431, 207, 15);
		panel.add(lblAddressLinecountry);

		btnSend = new JButton("Send");
		btnSend.setIcon(new ImageIcon(SupplierRequest.class.getResource("/com/logilite/img/save.png")));
		btnSend.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnSend.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 16));
		btnSend.setFocusPainted(false);
		btnSend.setBounds(106, 500, 130, 30);
		panel.add(btnSend);

		btnClear = new JButton("Clear");
		btnClear.setIcon(new ImageIcon(SupplierRequest.class.getResource("/com/logilite/img/clear.png")));
		btnClear.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnClear.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 16));
		btnClear.setFocusPainted(false);
		btnClear.setBounds(266, 500, 130, 30);
		panel.add(btnClear);

		lblRequestWillSend = new JLabel("Request will send to productinventory@management.com");
		lblRequestWillSend.setBounds(40, 554, 409, 15);
		panel.add(lblRequestWillSend);

		lblFirstName = new JLabel("First Name");
		lblFirstName.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 14));
		lblFirstName.setBounds(106, 78, 207, 15);
		panel.add(lblFirstName);

		txtFirstName = new JTextField();
		txtFirstName.setFont(new Font("DejaVu Serif Condensed", Font.PLAIN, 14));
		txtFirstName.setColumns(10);
		txtFirstName.setBounds(106, 95, 290, 30);
		panel.add(txtFirstName);

		lblSupplierRequest = new JLabel("Supplier Request");
		lblSupplierRequest.setHorizontalAlignment(SwingConstants.CENTER);
		lblSupplierRequest.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 30));
		lblSupplierRequest.setBounds(53, 24, 396, 42);
		panel.add(lblSupplierRequest);

		lblX = new JLabel("");
		lblX.setIcon(new ImageIcon(SupplierRequest.class.getResource("/com/logilite/img/closewindow.png")));
		lblX.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lblX.setHorizontalAlignment(SwingConstants.CENTER);
		lblX.setFont(new Font("URW Bookman", Font.BOLD, 23));
		lblX.setBounds(447, 12, 33, 37);
		panel.add(lblX);

		btnSendOtp = new JButton("Send OTP");
		btnSendOtp.setFont(new Font("Dialog", Font.BOLD, 12));
		btnSendOtp.setFocusPainted(false);
		btnSendOtp.setBounds(272, 239, 124, 25);
		panel.add(btnSendOtp);

		lblEnterOtp = new JLabel("Enter OTP");
		lblEnterOtp.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 14));
		lblEnterOtp.setBounds(106, 262, 158, 15);
		panel.add(lblEnterOtp);

		OTPField = new JTextField();
		OTPField.setFont(new Font("DejaVu Serif Condensed", Font.PLAIN, 14));
		OTPField.setColumns(10);
		OTPField.setBounds(106, 283, 134, 30);
		panel.add(OTPField);

		btnVerifyOtp = new JButton("Verify OTP");
		btnVerifyOtp.setFocusPainted(false);
		btnVerifyOtp.setEnabled(false);
		btnVerifyOtp.setBounds(259, 285, 117, 25);
		panel.add(btnVerifyOtp);

		btnResendOtp = new JButton("Resend OTP");
		btnResendOtp.setFont(new Font("Dialog", Font.BOLD, 12));
		btnResendOtp.setFocusPainted(false);
		btnResendOtp.setBounds(272, 239, 124, 25);
		panel.add(btnResendOtp);
	}

	public void init()
	{
		lblX.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e)
			{
				SupplierRequest.this.setVisible(false);
				Login login = new Login();
				login.setVisible(true);
			}
		});

		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				clear();
			}
		});

		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0)
			{
				if (isEmpty())
				{
					String firstName = txtFirstName.getText();
					String lastName = txtLastName.getText();
					String email = txtEmail.getText();
					String phone = txtMobile.getText();
					String address1 = txtAddress1.getText();
					String address2 = txtAddress2.getText();

					if (!supplier.isEmailExists(email))
					{
						if (!supplier.isPhoneExists(phone))
						{
							supplier.insertSupplierRequest(firstName, lastName, email, phone, address1, address2);
							clear();
							SupplierRequest.this.setVisible(false);
							Login login = new Login();
							login.setVisible(true);
						}
						else
						{
							JOptionPane.showMessageDialog(SupplierRequest.this, "This phone number already exists!");
						}
					}
					else
					{
						JOptionPane.showMessageDialog(SupplierRequest.this, "This email already exists!");
					}
				}
			}
		});

		btnVerifyOtp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				if (!OTPField.getText().isEmpty())
				{
					if (OTPField.getText().equals(String.valueOf(otp)))
					{
						JOptionPane.showMessageDialog(null, "OTP verification successfull");
						otpVerified = true;
						btnVerifyOtp.setEnabled(false);
						btnSendOtp.setEnabled(false);
						btnResendOtp.setEnabled(false);
						txtEmail.setEditable(false);
						OTPField.setEditable(false);
					}
					else
					{
						JOptionPane.showMessageDialog(null, "OTP verification failed");
						otpVerified = false;
					}
				}
				else
				{
					JOptionPane.showMessageDialog(SupplierRequest.this, "Please enter OTP", "Warning", 2);
				}
			}
		});

		btnSendOtp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				if (isEmailValidate())
				{
					Random random = new Random();
					otp = random.nextInt(999999);
					if (Email.send(txtEmail.getText(), "OTP Verification", "Your OTP is : " + otp))
					{
						JOptionPane.showMessageDialog(null, "OTP sent successfully");
						btnSendOtp.setVisible(false);
						btnResendOtp.setVisible(true);
						btnVerifyOtp.setEnabled(true);
					}
					else
					{
						JOptionPane.showMessageDialog(null, "OTP not sent");
					}
				}
			}
		});

		btnResendOtp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				if (isEmailValidate())
				{
					Random random = new Random();
					otp = random.nextInt(999999);
					if (Email.send(txtEmail.getText(), "OTP Verification", "Your OTP is : " + otp))
					{
						JOptionPane.showMessageDialog(null, "OTP sent successfully");
					}
					else
					{
						JOptionPane.showMessageDialog(null, "OTP not sent");
					}
				}
			}
		});

		OTPField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e)
			{
				if (!Character.isDigit(e.getKeyChar()))
				{
					e.consume();
				}
			}
		});
	}

	public boolean isEmailValidate()
	{
		if (txtEmail.getText().isEmpty())
		{
			JOptionPane.showMessageDialog(SupplierRequest.this, "Email address is required", "Warning", 2);
			return false;
		}
		if (!txtEmail.getText().matches("^.+@.+\\..+$"))
		{
			JOptionPane.showMessageDialog(SupplierRequest.this, "Invalid email address", "Warning", 2);
			return false;
		}
		if (!txtEmail.getText().toLowerCase().equals(txtEmail.getText()))
		{
			JOptionPane.showMessageDialog(SupplierRequest.this, "Please enter email address in lowercase", "Warning",
					2);
			return false;
		}
		if (supplier.isEmailExists(txtEmail.getText()))
		{
			JOptionPane.showMessageDialog(SupplierRequest.this, "This email address already exists!", "Warning", 2);
			return false;
		}
		return true;
	}

	public boolean isEmpty()
	{
		if (txtFirstName.getText().isEmpty())
		{
			JOptionPane.showMessageDialog(SupplierRequest.this, "First name is required", "Warning", 2);
			return false;
		}
		if (txtLastName.getText().isEmpty())
		{
			JOptionPane.showMessageDialog(SupplierRequest.this, "Last name is required", "Warning", 2);
			return false;
		}
		if (txtEmail.getText().isEmpty())
		{
			JOptionPane.showMessageDialog(SupplierRequest.this, "Email address is required", "Warning", 2);
			return false;
		}
		if (!txtEmail.getText().matches("^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
				+ "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$"))
		{
			JOptionPane.showMessageDialog(SupplierRequest.this, "Invalid email address", "Warning", 2);
			return false;
		}
		if (txtMobile.getText().isEmpty())
		{
			JOptionPane.showMessageDialog(SupplierRequest.this, "Phone number is required", "Warning", 2);
			return false;
		}
		if (txtMobile.getText().length() != 10)
		{
			JOptionPane.showMessageDialog(SupplierRequest.this, "Phone number is incorrect", "Warning", 2);
			return false;
		}
		if (txtAddress1.getText().isEmpty())
		{
			JOptionPane.showMessageDialog(SupplierRequest.this, "Address line 1 is required", "Warning", 2);
			return false;
		}
		if (txtAddress2.getText().isEmpty())
		{
			JOptionPane.showMessageDialog(SupplierRequest.this, "Address line 2 is required", "Warning", 2);
			return false;
		}
		if (!otpVerified)
		{
			JOptionPane.showMessageDialog(SupplierRequest.this, "Please verify email", "Warning", 2);
		}
		return true;
	}

	private void clear()
	{
		txtFirstName.setText("");
		txtLastName.setText("");
		txtEmail.setText("");
		txtMobile.setText("");
		txtAddress1.setText("");
		txtAddress2.setText("");
	}
}
