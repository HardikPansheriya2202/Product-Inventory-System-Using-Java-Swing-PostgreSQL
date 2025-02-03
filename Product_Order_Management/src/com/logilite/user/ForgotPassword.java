package com.logilite.user;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import com.logilite.dao.ForgotPasswordDao;

public class ForgotPassword extends JFrame
{
	private static final long	serialVersionUID	= 1L;
	public static JPanel				contentPane;
	public static JTextField emailSearchField;
	public static JPasswordField passwordField;
	public static JTextField ansField;
	public static JLabel lblEmail;
	public static JLabel lblPassword;
	public static JLabel lblSecurityQuestion;
	public static JLabel lblAnswer;
	public static JButton btnSave;
	public static JLabel lblX;
	public static JLabel btnHide;
	public static JLabel btnShow;
	public static JButton btnBack;
	public static JTextField seQueField;
	public static JLabel lblSearch;
	public static JPanel panel;
	public static JLabel lblSignUp;
	int xx, xy;
	Color noEditColor = new Color(204,204,204);
	Color edit = new Color(255,255,255);
	ForgotPasswordDao fg = new ForgotPasswordDao();

	public ForgotPassword()
	{
		initComponent();
		init();
	}
	
	private void init() {
		seQueField.setBackground(noEditColor);
		ansField.setBackground(noEditColor);
		passwordField.setBackground(noEditColor);
		seQueField.setEditable(false);
		ansField.setEditable(false);
		passwordField.setEditable(false);
		btnSave.setEnabled(false);
		
		panel.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				int x = e.getXOnScreen();
				int y = e.getYOnScreen();
				ForgotPassword.this.setLocation(x - xx, y - xy);
			}
		});
		
		panel.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				xx = e.getX();
				xy = e.getY();
			}
		});
		
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (isEmpty()) {
					String email = emailSearchField.getText();
					String ans = ansField.getText();
					
					if (fg.isAns(email, ans)) {
						String password = String.valueOf(passwordField.getPassword());
						fg.setPassword(email, password);
						new Login().setVisible(true);
						ForgotPassword.this.dispose();
					}
				}
			}
		});
		
		lblX.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				ForgotPassword.this.setVisible(false);
				Login login = new Login();
				login.setVisible(true);
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
		
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new Login().setVisible(true);
				ForgotPassword.this.dispose();
			}
		});
		
		lblSearch.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (emailValidation()) {
					if (fg.isEmailExists(emailSearchField.getText())) {
						emailSearchField.setBackground(noEditColor);
						emailSearchField.setEditable(false);
						ansField.setBackground(edit);
						ansField.setEditable(true);
						passwordField.setBackground(edit);
						passwordField.setEditable(true);
						btnSave.setEnabled(true);
					}
				}
			}
		});
	}
	
	private boolean isEmpty() {
		if (ansField.getText().isEmpty()) {
			JOptionPane.showMessageDialog(ForgotPassword.this, "Security answer is required", "Warning", 2);
			return false;
		}
		if (String.valueOf(passwordField.getPassword()).isEmpty()) {
			JOptionPane.showMessageDialog(ForgotPassword.this, "Please enter new password", "Warning", 2);
			return false;
		}
		return true;
	}
	
	private boolean emailValidation() {
		if (emailSearchField.getText().isEmpty()) {
			JOptionPane.showMessageDialog(ForgotPassword.this, "Please enter your email address", "Warning", 2);
			return false;
		}
		if (!emailSearchField.getText().matches("^.+@.+\\..+$")) {
			JOptionPane.showMessageDialog(ForgotPassword.this, "Invalid email address", "Warning", 2);
			return false;
		}
		return true;
	}
	
	public void initComponent() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent e) {
				for (double i = 0.1; i <= 1.0; i += 0.1) {
					String s = "" + i;
					float f = Float.parseFloat(s);
					ForgotPassword.this.setOpacity(f);
					try
					{
						Thread.sleep(20);
					}
					catch (InterruptedException e1)
					{
						Logger.getLogger(ForgotPassword.class.getName()).log(Level.SEVERE, null, e1);
					}
				}
			}
		});
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(418, 429);
		setUndecorated(true);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		panel = new JPanel();
		panel.setBackground(new Color(238, 238, 238));
		panel.setBounds(0, 0, 418, 429);
		contentPane.add(panel);
		panel.setLayout(null);
		
		lblSignUp = new JLabel("Forgot Password");
		lblSignUp.setBounds(12, 33, 396, 42);
		lblSignUp.setHorizontalAlignment(SwingConstants.CENTER);
		lblSignUp.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 30));
		panel.add(lblSignUp);
		
		emailSearchField = new JTextField();
		emailSearchField.setBounds(49, 120, 290, 30);
		emailSearchField.setFont(new Font("DejaVu Serif Condensed", Font.PLAIN, 14));
		panel.add(emailSearchField);
		emailSearchField.setColumns(10);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(49, 323, 290, 30);
		passwordField.setFont(new Font("DejaVu Serif Condensed", Font.PLAIN, 14));
		panel.add(passwordField);
		
		ansField = new JTextField();
		ansField.setBounds(49, 258, 290, 30);
		ansField.setFont(new Font("DejaVu Serif Condensed", Font.PLAIN, 14));
		panel.add(ansField);
		ansField.setColumns(10);
		
		lblEmail = new JLabel("Enter Email Address");
		lblEmail.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 14));
		lblEmail.setBounds(49, 93, 207, 15);
		panel.add(lblEmail);
		
		lblPassword = new JLabel("New Password");
		lblPassword.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 14));
		lblPassword.setBounds(49, 300, 207, 15);
		panel.add(lblPassword);
		
		lblSecurityQuestion = new JLabel("Security Question");
		lblSecurityQuestion.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 14));
		lblSecurityQuestion.setBounds(49, 162, 207, 15);
		panel.add(lblSecurityQuestion);
		
		lblAnswer = new JLabel("Answer");
		lblAnswer.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 14));
		lblAnswer.setBounds(49, 231, 207, 15);
		panel.add(lblAnswer);
		
		btnSave = new JButton("Save");
		btnSave.setIcon(new ImageIcon(getClass().getResource("/com/logilite/img/save.png")));
		btnSave.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnSave.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 16));
		btnSave.setBounds(61, 377, 124, 30);
		btnSave.setFocusPainted(false);
		panel.add(btnSave);
		
		lblX = new JLabel("");
		lblX.setIcon(new ImageIcon(getClass().getResource("/com/logilite/img/closewindow.png")));
		lblX.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lblX.setHorizontalAlignment(SwingConstants.CENTER);
		lblX.setFont(new Font("URW Bookman", Font.BOLD, 23));
		lblX.setBounds(373, 8, 33, 37);
		panel.add(lblX);
		
		btnHide = new JLabel("");
		btnHide.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnHide.setIcon(new ImageIcon(getClass().getResource("/com/logilite/img/hide.png")));
		btnHide.setBackground(Color.BLACK);
		btnHide.setBounds(353, 323, 33, 30);
		panel.add(btnHide);
		
		btnShow = new JLabel("");
		btnShow.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnShow.setIcon(new ImageIcon(getClass().getResource("/com/logilite/img/unhide.png")));
		btnShow.setBackground(Color.BLACK);
		btnShow.setBounds(353, 323, 33, 30);
		panel.add(btnShow);
		
		btnBack = new JButton("Back");
		btnBack.setIcon(new ImageIcon(getClass().getResource("/com/logilite/img/back.png")));
		btnBack.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnBack.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 16));
		btnBack.setBounds(204, 377, 124, 30);
		btnBack.setFocusPainted(false);
		panel.add(btnBack);
		
		seQueField = new JTextField();
		seQueField.setBounds(49, 189, 290, 30);
		panel.add(seQueField);
		seQueField.setColumns(10);
		
		lblSearch = new JLabel("");
		lblSearch.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lblSearch.setIcon(new ImageIcon(getClass().getResource("/com/logilite/img/search.png")));
		lblSearch.setBounds(352, 120, 33, 30);
		panel.add(lblSearch);
	}

}
