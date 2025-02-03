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
import javax.swing.border.EtchedBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.logilite.dao.Statistics;
import com.logilite.dao.SupplierDao;

public class ManageSuppliers extends JPanel
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
	public static JLabel label;
	public static JTable table;
	public static JTextField searchField;
	public static JLabel lblSearch;
	public static JLabel lblSupplierId;
	public static JLabel lblUsername;
	public static JButton btnClear;
	public static JScrollPane scrollPane;
	int xx, xy;
	SupplierDao supplier = new SupplierDao();
	DefaultTableModel model;
	int rowIndex;
	Statistics statistics = new Statistics();

	public ManageSuppliers()
	{
		initComponent();
		init();
		supplierTable();
	}
	
	private void init() {
		btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
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
						table.setModel(new DefaultTableModel(null, new Object[] {"Supplier ID", "Username", "Email", "Password", "Phone", "Address Line 1", "Address Line 2"}));
						table.getColumnModel().getColumn(2).setPreferredWidth(200);
						table.getColumnModel().getColumn(4).setPreferredWidth(100);
						table.getColumnModel().getColumn(5).setPreferredWidth(100);
						table.getColumnModel().getColumn(6).setPreferredWidth(100);
						
						DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
						centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
						
						for (int i = 0; i < table.getColumnCount(); i++) {
							table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
						}
						
						table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
						table.getTableHeader().setReorderingAllowed(false);
						supplier.getSupplierData(table, "");
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
				if (IDField.getText().isEmpty()) {
					JOptionPane.showMessageDialog(ManageSuppliers.this, "Please select user", "Warning", 2);
				}else {
					int id = Integer.valueOf(IDField.getText());
					supplier.delete(id);
					table.setModel(new DefaultTableModel(null, new Object[] {"Supplier ID", "Username", "Email", "Password", "Phone", "Address Line 1", "Address Line 2"}));
					table.getColumnModel().getColumn(2).setPreferredWidth(200);
					table.getColumnModel().getColumn(4).setPreferredWidth(100);
					table.getColumnModel().getColumn(5).setPreferredWidth(100);
					table.getColumnModel().getColumn(6).setPreferredWidth(100);
					
					DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
					centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
					
					for (int i = 0; i < table.getColumnCount(); i++) {
						table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
					}
					
					table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
					table.getTableHeader().setReorderingAllowed(false);
					supplier.getSupplierData(table, "");
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
				address1Field.setText(model.getValueAt(rowIndex, 5).toString());
				address2Field.setText(model.getValueAt(rowIndex, 6).toString());
			}
		});
		
		searchField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				table.setModel(new DefaultTableModel(null, new Object[] {"Supplier ID", "Username", "Email", "Password", "Phone", "Address Line 1", "Address Line 2"}));
				table.getColumnModel().getColumn(2).setPreferredWidth(200);
				table.getColumnModel().getColumn(4).setPreferredWidth(100);
				table.getColumnModel().getColumn(5).setPreferredWidth(100);
				table.getColumnModel().getColumn(6).setPreferredWidth(100);

				DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		        
		        for (int i = 0; i < table.getColumnCount(); i++) {
		            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
		        }
		        
				table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
				table.getTableHeader().setReorderingAllowed(false);
				supplier.getSupplierData(table, searchField.getText());
			}
		});
	}
	
	private void supplierTable() {
		supplier.getSupplierData(table, "");
		model = (DefaultTableModel) table.getModel();
		table.setRowHeight(30);
		table.setShowGrid(true);
		table.setGridColor(Color.black);
		table.setBackground(Color.white);
		table.setSelectionBackground(Color.LIGHT_GRAY);
	}
	
	private void clear() {
		IDField.setText("");
		nameField.setText("");
		emailField.setText("");
		passwordField.setText("");
		mobileField.setText("");
		address1Field.setText("");
		address2Field.setText("");
		table.clearSelection();
		statistics.admin();
	}
	
	public boolean isEmpty() {
		if (nameField.getText().isEmpty()) {
			JOptionPane.showMessageDialog(ManageSuppliers.this, "Please select user", "Warning", 2);
			return false;
		}
		if (emailField.getText().isEmpty()) {
			JOptionPane.showMessageDialog(ManageSuppliers.this, "Email address is required", "Warning", 2);
			return false;
		}
		if (!emailField.getText().matches("^.+@.+\\..+$")) {
			JOptionPane.showMessageDialog(ManageSuppliers.this, "Invalid email address", "Warning", 2);
			return false;
		}
		if (String.valueOf(passwordField.getPassword()).isEmpty()) {
			JOptionPane.showMessageDialog(ManageSuppliers.this, "Password is required", "Warning", 2);
			return false;
		}
		if (mobileField.getText().isEmpty()) {
			JOptionPane.showMessageDialog(ManageSuppliers.this, "Phone number is required", "Warning", 2);
			return false;
		}
		if (mobileField.getText().length() > 15) {
			JOptionPane.showMessageDialog(ManageSuppliers.this, "Phone number is to long", "Warning", 2);
			return false;
		}
		if (mobileField.getText().length() < 10) {
			JOptionPane.showMessageDialog(ManageSuppliers.this, "Phone number is short", "Warning", 2);
			return false;
		}
		if (address1Field.getText().isEmpty()) {
			JOptionPane.showMessageDialog(ManageSuppliers.this, "Address line 1 is required", "Warning", 2);
			return false;
		}
		if (address2Field.getText().isEmpty()) {
			JOptionPane.showMessageDialog(ManageSuppliers.this, "Address line 2 is required", "Warning", 2);
			return false;
		}
		return true;
	}
	
	private boolean check() {
		String newUsername = nameField.getText();
		String newEmail = emailField.getText();
		String newPhone = mobileField.getText();
		String oldUsername = model.getValueAt(rowIndex, 1).toString();
		String oldEmail = model.getValueAt(rowIndex, 2).toString();
		String oldPhone = model.getValueAt(rowIndex, 4).toString();
		
		if (newEmail.equals(oldEmail) && newPhone.equals(oldPhone) && newUsername.equals(oldUsername)) {
			return false;
		}else {
			if (!newEmail.equals(oldEmail)) {
				boolean x = supplier.isEmailExists(newEmail);
				if (x) {
					JOptionPane.showMessageDialog(ManageSuppliers.this, "This email already exists", "Warning", 2);
				}
				return x;
			}
			if (!newPhone.equals(oldPhone)) {
				boolean x = supplier.isPhoneExists(newPhone);
				if (x) {
					JOptionPane.showMessageDialog(ManageSuppliers.this, "This phone number already exists", "Warning", 2);
				}
				return x;
			}
			if (!newUsername.equals(oldUsername)) {
				boolean x = supplier.isUsernameExists(newUsername);
				if (x) {
					JOptionPane.showMessageDialog(ManageSuppliers.this, "This Username already exists", "Warning", 2);
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
		panel.setBounds(0, 0, 883, 640);
		add(panel);
		panel.setLayout(null);
		
		IDField = new JTextField();
		IDField.setBackground(new Color(186, 182, 182));
		IDField.setEditable(false);
		IDField.setBounds(147, 34, 290, 30);
		IDField.setFont(new Font("DejaVu Serif Condensed", Font.PLAIN, 14));
		panel.add(IDField);
		IDField.setColumns(10);
		
		nameField = new JTextField();
		nameField.setBounds(147, 91, 290, 30);
		nameField.setFont(new Font("DejaVu Serif Condensed", Font.PLAIN, 14));
		panel.add(nameField);
		nameField.setColumns(10);
		
		emailField = new JTextField();
		emailField.setBounds(147, 151, 290, 30);
		emailField.setFont(new Font("DejaVu Serif Condensed", Font.PLAIN, 14));
		panel.add(emailField);
		emailField.setColumns(10);
		
		passwordField = new JPasswordField();
		passwordField.setEditable(false);
		passwordField.setBounds(147, 209, 290, 30);
		passwordField.setFont(new Font("DejaVu Serif Condensed", Font.PLAIN, 14));
		panel.add(passwordField);
		
		mobileField = new JTextField();
		mobileField.setBounds(554, 34, 290, 30);
		mobileField.setFont(new Font("DejaVu Serif Condensed", Font.PLAIN, 14));
		panel.add(mobileField);
		mobileField.setColumns(10);
		
		address1Field = new JTextField();
		address1Field.setBounds(554, 91, 290, 30);
		address1Field.setFont(new Font("DejaVu Serif Condensed", Font.PLAIN, 14));
		panel.add(address1Field);
		address1Field.setColumns(10);
		
		address2Field = new JTextField();
		address2Field.setBounds(554, 151, 290, 30);
		address2Field.setFont(new Font("DejaVu Serif Condensed", Font.PLAIN, 14));
		panel.add(address2Field);
		address2Field.setColumns(10);
		
		lblSupplierId = new JLabel("Supplier ID");
		lblSupplierId.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 14));
		lblSupplierId.setBounds(147, 12, 207, 15);
		panel.add(lblSupplierId);
		
		lblUsername = new JLabel("Username");
		lblUsername.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 14));
		lblUsername.setBounds(147, 72, 207, 15);
		panel.add(lblUsername);
		
		lblEmail = new JLabel("Email");
		lblEmail.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 14));
		lblEmail.setBounds(147, 130, 207, 15);
		panel.add(lblEmail);
		
		lblPassword = new JLabel("Password");
		lblPassword.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 14));
		lblPassword.setBounds(147, 190, 207, 15);
		panel.add(lblPassword);
		
		lblMobileNo = new JLabel("Mobile no.");
		lblMobileNo.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 14));
		lblMobileNo.setBounds(554, 12, 207, 15);
		panel.add(lblMobileNo);
		
		lblAdress = new JLabel("Address Line 1(State & Area)");
		lblAdress.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 14));
		lblAdress.setBounds(554, 72, 253, 15);
		panel.add(lblAdress);
		
		lblAddressLinecountry = new JLabel("Address Line 2(Country)");
		lblAddressLinecountry.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 14));
		lblAddressLinecountry.setBounds(554, 130, 207, 15);
		panel.add(lblAddressLinecountry);
		
		btnUpdate = new JButton("Update");
		btnUpdate.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnUpdate.setIcon(new ImageIcon(getClass().getResource("/com/logilite/img/update.png")));
		btnUpdate.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 16));
		btnUpdate.setBounds(554, 207, 132, 30);
		btnUpdate.setFocusPainted(false);
		panel.add(btnUpdate);
		
		btnHide = new JLabel("");
		btnHide.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnHide.setIcon(new ImageIcon(getClass().getResource("/com/logilite/img/hide.png")));
		btnHide.setBackground(Color.BLACK);
		btnHide.setBounds(447, 209, 33, 30);
		panel.add(btnHide);
		
		btnShow = new JLabel("");
		btnShow.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnShow.setIcon(new ImageIcon(getClass().getResource("/com/logilite/img/unhide.png")));
		btnShow.setBackground(Color.BLACK);
		btnShow.setBounds(447, 209, 33, 30);
		panel.add(btnShow);
		
		btnDelete = new JButton("Delete");
		btnDelete.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnDelete.setIcon(new ImageIcon(getClass().getResource("/com/logilite/img/remove.png")));
		btnDelete.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 16));
		btnDelete.setBounds(712, 207, 132, 30);
		btnDelete.setFocusPainted(false);
		panel.add(btnDelete);
		
		btnClear = new JButton("Clear");
		btnClear.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnClear.setIcon(new ImageIcon(getClass().getResource("/com/logilite/img/clear.png")));
		btnClear.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 16));
		btnClear.setBounds(554, 265, 290, 30);
		btnClear.setFocusPainted(false);
		panel.add(btnClear);
		
		label = new JLabel("___________________________________________________________________________________________________________________");
		label.setBounds(147, 305, 697, 15);
		panel.add(label);
		
		table = new JTable();
		table.setBackground(new Color(222, 221, 218));
		table.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 14));
		table.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Supplier ID", "Username", "Email", "Password", "Phone", "Address Line 1", "Address Line 2"
			}
		) {
			boolean[] columnEditables = new boolean[] {
				false, false, false, false, false, false, false
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		table.getColumnModel().getColumn(2).setPreferredWidth(200);
		table.getColumnModel().getColumn(4).setPreferredWidth(100);
		table.getColumnModel().getColumn(5).setPreferredWidth(100);
		table.getColumnModel().getColumn(6).setPreferredWidth(100);

		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.getTableHeader().setReorderingAllowed(false);
		scrollPane = new JScrollPane(table);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setViewportBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		scrollPane.setBounds(120, 385, 747, 231);
		panel.add(scrollPane);
		
		searchField = new JTextField();
		searchField.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 14));
		searchField.setBounds(570, 335, 274, 30);
		panel.add(searchField);
		searchField.setColumns(10);
		
		lblSearch = new JLabel("Search");
		lblSearch.setFont(new Font("DejaVu Serif", Font.BOLD, 16));
		lblSearch.setBounds(482, 334, 70, 30);
		panel.add(lblSearch);
	}

}
