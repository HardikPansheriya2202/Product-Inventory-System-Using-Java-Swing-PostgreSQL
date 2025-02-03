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
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.logilite.dao.Statistics;
import com.logilite.dao.UserDao;

public class ManageUsers extends JPanel
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
	public static JLabel label;
	public static JTable table;
	public static JTextField searchField;
	public static JLabel lblSearch;
	public static JScrollPane scrollPane;
	public static JLabel lblUserId;
	public static JLabel lblUsername;
	public static JButton btnClear;
	int xx, xy;
	UserDao user = new UserDao();
	DefaultTableModel model;
	int rowIndex;
	Statistics statistics = new Statistics();

	public ManageUsers()
	{
		initComponent();
		init();
		usersTable();
	}
	
	private void init() {
		nameField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				char input = e.getKeyChar();
				if (!(input < '0' || input > '9') && input != '\b') {
					e.consume();
					JOptionPane.showMessageDialog(ManageUsers.this, "Username doesn't contains any numbers!", "Warning", 2);
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
						table.setModel(new DefaultTableModel(null, new Object[] {"User ID", "Username", "Email", "Password", "Mobile no.", "Security Question", "Answer", "Address Line 1(State & Area)", "Address Line 2(Country)"}));
						
						table.getColumnModel().getColumn(0).setPreferredWidth(50);
						table.getColumnModel().getColumn(1).setPreferredWidth(95);
						table.getColumnModel().getColumn(2).setPreferredWidth(158);
						table.getColumnModel().getColumn(4).setPreferredWidth(100);
						table.getColumnModel().getColumn(5).setPreferredWidth(210);
						table.getColumnModel().getColumn(7).setPreferredWidth(100);
						table.getColumnModel().getColumn(8).setPreferredWidth(100);
						
						DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
						centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
						
						for (int i = 0; i < table.getColumnCount(); i++) {
							table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
						}
						
						table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
						table.getTableHeader().setReorderingAllowed(false);
						
						user.getUserData(table, "");
						clear();
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
				if (isEmpty()) {
					int id = Integer.parseInt(IDField.getText());
					user.delete(id);
					table.setModel(new DefaultTableModel(null, new Object[] {"User ID", "User Name", "Email", "Password", "Phone", "Security Question", "Answer", "Address Line 1", "Address Line 2"}));
					table.getColumnModel().getColumn(0).setPreferredWidth(50);
					table.getColumnModel().getColumn(1).setPreferredWidth(95);
					table.getColumnModel().getColumn(2).setPreferredWidth(158);
					table.getColumnModel().getColumn(4).setPreferredWidth(100);
					table.getColumnModel().getColumn(5).setPreferredWidth(210);
					table.getColumnModel().getColumn(7).setPreferredWidth(100);
					table.getColumnModel().getColumn(8).setPreferredWidth(100);
					
					DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
					centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
					
					for (int i = 0; i < table.getColumnCount(); i++) {
						table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
					}
					
					table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
					table.getTableHeader().setReorderingAllowed(false);
					user.getUserData(table, "");
					clear();
				}
			}
		});
		
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clear();
			}
		});
		
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				model = (DefaultTableModel) table.getModel();
				rowIndex = table.getSelectedRow();
				IDField.setText(model.getValueAt(rowIndex, 0).toString());
				nameField.setText(model.getValueAt(rowIndex, 1).toString());
				emailField.setText(model.getValueAt(rowIndex, 2).toString());
				passwordField.setText(model.getValueAt(rowIndex, 3).toString());
				mobileField.setText(model.getValueAt(rowIndex, 4).toString());
				seQueField.setText(model.getValueAt(rowIndex, 5).toString());
				ansField.setText(model.getValueAt(rowIndex, 6).toString());
				address1Field.setText(model.getValueAt(rowIndex, 7).toString());
				address2Field.setText(model.getValueAt(rowIndex, 8).toString());
			}
		});
		
		searchField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				table.setModel(new DefaultTableModel(null, new Object[] {"User ID", "User Name", "Email", "Password", "Phone", "Security Question", "Answer", "Address Line 1", "Address Line 2"}));
				table.getColumnModel().getColumn(0).setPreferredWidth(50);
				table.getColumnModel().getColumn(1).setPreferredWidth(95);
				table.getColumnModel().getColumn(2).setPreferredWidth(158);
				table.getColumnModel().getColumn(4).setPreferredWidth(100);
				table.getColumnModel().getColumn(5).setPreferredWidth(210);
				table.getColumnModel().getColumn(7).setPreferredWidth(100);
				table.getColumnModel().getColumn(8).setPreferredWidth(100);
				DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		        
		        for (int i = 0; i < table.getColumnCount(); i++) {
		            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
		        }
				table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
				table.getTableHeader().setReorderingAllowed(false);
				user.getUserData(table, searchField.getText());
			}
		});
	}
	
	private void usersTable() {
		user.getUserData(table, "");
		model = (DefaultTableModel) table.getModel();
		table.setRowHeight(30);
		table.setShowGrid(true);
		table.setGridColor(Color.black);
		table.setBackground(Color.white);
		table.setSelectionBackground(Color.LIGHT_GRAY);
	}
	
	public boolean isEmpty() {
		if (nameField.getText().isEmpty()) {
			JOptionPane.showMessageDialog(ManageUsers.this, "Please select user", "Warning", 2);
			return false;
		}
		if (emailField.getText().isEmpty()) {
			JOptionPane.showMessageDialog(ManageUsers.this, "Email address is required", "Warning", 2);
			return false;
		}
		if (!emailField.getText().matches("^.+@.+\\..+$")) {
			JOptionPane.showMessageDialog(ManageUsers.this, "Invalid email address", "Warning", 2);
			return false;
		}
		if (String.valueOf(passwordField.getPassword()).isEmpty()) {
			JOptionPane.showMessageDialog(ManageUsers.this, "Password is required", "Warning", 2);
			return false;
		}
		if (mobileField.getText().isEmpty()) {
			JOptionPane.showMessageDialog(ManageUsers.this, "Phone number is required", "Warning", 2);
			return false;
		}
		if (mobileField.getText().length() > 15) {
			JOptionPane.showMessageDialog(ManageUsers.this, "Phone number is to long", "Warning", 2);
			return false;
		}
		if (mobileField.getText().length() < 10) {
			JOptionPane.showMessageDialog(ManageUsers.this, "Phone number is short", "Warning", 2);
			return false;
		}
		if (seQueField.getText().isEmpty()) {
			JOptionPane.showMessageDialog(ManageUsers.this, "Security question is required", "Warning", 2);
			return false;
		}
		if (ansField.getText().isEmpty()) {
			JOptionPane.showMessageDialog(ManageUsers.this, "Security answer is required", "Warning", 2);
			return false;
		}
		if (address1Field.getText().isEmpty()) {
			JOptionPane.showMessageDialog(ManageUsers.this, "Address line 1 is required", "Warning", 2);
			return false;
		}
		if (address2Field.getText().isEmpty()) {
			JOptionPane.showMessageDialog(ManageUsers.this, "Address line 2 is required", "Warning", 2);
			return false;
		}
		return true;
	}
	
	public void clear() {
		IDField.setText("");
		nameField.setText("");
		emailField.setText("");
		passwordField.setText("");
		mobileField.setText("");
		seQueField.setText("");
		ansField.setText("");
		address1Field.setText("");
		address2Field.setText("");
		table.clearSelection();
		statistics.admin();
	}
	
	private boolean check() {
		String newEmail = emailField.getText();
		String newPhone = mobileField.getText();
		String oldEmail = model.getValueAt(rowIndex, 2).toString();
		String oldPhone = model.getValueAt(rowIndex, 4).toString();
		
		if (newEmail.equals(oldEmail) && newPhone.equals(oldPhone)) {
			return false;
		}else {
			if (!newEmail.equals(oldEmail)) {
				boolean x = user.isEmailExists(newEmail);
				if (x) {
					JOptionPane.showMessageDialog(ManageUsers.this, "This email already exists", "Warning", 2);
				}
				return x;
			}
			if (!newPhone.equals(oldPhone)) {
				boolean x = user.isPhoneExists(newPhone);
				if (x) {
					JOptionPane.showMessageDialog(ManageUsers.this, "This phone number already exists", "Warning", 2);
				}
				return x;
			}
		}
		return false;
	}
	
	@SuppressWarnings("serial")
	private void initComponent() {
		setLayout(null);
		
		panel = new JPanel();
		panel.setBounds(0, 0, 1031, 645);
		add(panel);
		panel.setLayout(null);
		
		IDField = new JTextField();
		IDField.setBackground(new Color(186, 182, 182));
		IDField.setEditable(false);
		IDField.setBounds(146, 34, 290, 30);
		IDField.setFont(new Font("DejaVu Serif Condensed", Font.PLAIN, 14));
		panel.add(IDField);
		IDField.setColumns(10);
		
		nameField = new JTextField();
		nameField.setBounds(146, 91, 290, 30);
		nameField.setFont(new Font("DejaVu Serif Condensed", Font.PLAIN, 14));
		panel.add(nameField);
		nameField.setColumns(10);
		
		emailField = new JTextField();
		emailField.setBounds(146, 151, 290, 30);
		emailField.setFont(new Font("DejaVu Serif Condensed", Font.PLAIN, 14));
		panel.add(emailField);
		emailField.setColumns(10);
		
		passwordField = new JPasswordField();
		passwordField.setEditable(false);
		passwordField.setBounds(146, 209, 290, 30);
		passwordField.setFont(new Font("DejaVu Serif Condensed", Font.PLAIN, 14));
		panel.add(passwordField);
		
		mobileField = new JTextField();
		mobileField.setBounds(146, 269, 290, 30);
		mobileField.setFont(new Font("DejaVu Serif Condensed", Font.PLAIN, 14));
		panel.add(mobileField);
		mobileField.setColumns(10);
		
		ansField = new JTextField();
		ansField.setBounds(549, 91, 290, 30);
		ansField.setFont(new Font("DejaVu Serif Condensed", Font.PLAIN, 14));
		panel.add(ansField);
		ansField.setColumns(10);
		
		address1Field = new JTextField();
		address1Field.setBounds(549, 151, 290, 30);
		address1Field.setFont(new Font("DejaVu Serif Condensed", Font.PLAIN, 14));
		panel.add(address1Field);
		address1Field.setColumns(10);
		
		address2Field = new JTextField();
		address2Field.setBounds(549, 209, 290, 30);
		address2Field.setFont(new Font("DejaVu Serif Condensed", Font.PLAIN, 14));
		panel.add(address2Field);
		address2Field.setColumns(10);
		
		lblUserId = new JLabel("User ID");
		lblUserId.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 14));
		lblUserId.setBounds(146, 12, 207, 15);
		panel.add(lblUserId);
		
		lblUsername = new JLabel("Username");
		lblUsername.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 14));
		lblUsername.setBounds(146, 72, 207, 15);
		panel.add(lblUsername);
		
		lblEmail = new JLabel("Email");
		lblEmail.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 14));
		lblEmail.setBounds(146, 130, 207, 15);
		panel.add(lblEmail);
		
		lblPassword = new JLabel("Password");
		lblPassword.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 14));
		lblPassword.setBounds(146, 190, 207, 15);
		panel.add(lblPassword);
		
		lblMobileNo = new JLabel("Mobile no.");
		lblMobileNo.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 14));
		lblMobileNo.setBounds(146, 248, 207, 15);
		panel.add(lblMobileNo);
		
		lblSecurityQuestion = new JLabel("Security Question");
		lblSecurityQuestion.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 14));
		lblSecurityQuestion.setBounds(549, 12, 207, 15);
		panel.add(lblSecurityQuestion);
		
		lblAnswer = new JLabel("Answer");
		lblAnswer.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 14));
		lblAnswer.setBounds(549, 72, 207, 15);
		panel.add(lblAnswer);
		
		lblAdress = new JLabel("Address Line 1(State & Area)");
		lblAdress.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 14));
		lblAdress.setBounds(549, 133, 233, 15);
		panel.add(lblAdress);
		
		lblAddressLinecountry = new JLabel("Address Line 2(Country)");
		lblAddressLinecountry.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 14));
		lblAddressLinecountry.setBounds(549, 190, 207, 15);
		panel.add(lblAddressLinecountry);
		
		btnUpdate = new JButton("Update");
		btnUpdate.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnUpdate.setIcon(new ImageIcon(getClass().getResource("/com/logilite/img/update.png")));
		btnUpdate.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 16));
		btnUpdate.setBounds(549, 267, 132, 30);
		btnUpdate.setFocusPainted(false);
		panel.add(btnUpdate);
		
		btnHide = new JLabel("");
		btnHide.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnHide.setIcon(new ImageIcon(getClass().getResource("/com/logilite/img/hide.png")));
		btnHide.setBackground(Color.BLACK);
		btnHide.setBounds(446, 209, 33, 30);
		panel.add(btnHide);
		
		btnShow = new JLabel("");
		btnShow.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnShow.setIcon(new ImageIcon(getClass().getResource("/com/logilite/img/unhide.png")));
		btnShow.setBackground(Color.BLACK);
		btnShow.setBounds(446, 209, 33, 30);
		panel.add(btnShow);
		
		btnDelete = new JButton("Delete");
		btnDelete.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnDelete.setIcon(new ImageIcon(getClass().getResource("/com/logilite/img/remove.png")));
		btnDelete.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 16));
		btnDelete.setBounds(707, 267, 132, 30);
		btnDelete.setFocusPainted(false);
		panel.add(btnDelete);
		
		seQueField = new JTextField();
		seQueField.setBackground(new Color(186, 182, 182));
		seQueField.setEditable(false);
		seQueField.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 14));
		seQueField.setBounds(549, 34, 290, 30);
		panel.add(seQueField);
		seQueField.setColumns(10);
		
		btnClear = new JButton("Clear");
		btnClear.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnClear.setIcon(new ImageIcon(getClass().getResource("/com/logilite/img/clear.png")));
		btnClear.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 16));
		btnClear.setBounds(549, 321, 290, 30);
		btnClear.setFocusPainted(false);
		panel.add(btnClear);
		
		label = new JLabel("____________________________________________________________________________________________________________________");
		label.setBounds(146, 349, 696, 15);
		panel.add(label);
		
		table = new JTable();
		table.setBackground(new Color(222, 221, 218));
		table.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 14));
		table.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"User ID", "User Name", "Email", "Password", "Phone", "Security Question", "Answer", "Address Line 1", "Address Line 2"
			}
		) {
			boolean[] columnEditables = new boolean[] {
				false, false, false, false, false, false, false, false, false
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		table.getColumnModel().getColumn(0).setPreferredWidth(50);
		table.getColumnModel().getColumn(1).setPreferredWidth(95);
		table.getColumnModel().getColumn(2).setPreferredWidth(158);
		table.getColumnModel().getColumn(4).setPreferredWidth(100);
		table.getColumnModel().getColumn(5).setPreferredWidth(210);
		table.getColumnModel().getColumn(7).setPreferredWidth(100);
		table.getColumnModel().getColumn(8).setPreferredWidth(100);

		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.getTableHeader().setReorderingAllowed(false);
		scrollPane = new JScrollPane(table);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setBounds(22, 412, 983, 231);
		panel.add(scrollPane);
		
		searchField = new JTextField();
		searchField.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 14));
		searchField.setBounds(565, 376, 274, 30);
		panel.add(searchField);
		searchField.setColumns(10);
		
		lblSearch = new JLabel("Search");
		lblSearch.setFont(new Font("DejaVu Serif", Font.BOLD, 16));
		lblSearch.setBounds(477, 378, 70, 30);
		panel.add(lblSearch);
	}

}
