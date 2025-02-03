package com.logilite.supplier;

import java.awt.Font;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;

import com.logilite.component.SupplierReport;
import com.logilite.dao.PurchaseDao;
import com.logilite.dao.SupplierDao;

public class PrintSupplierReport extends JPanel
{

	private static final long serialVersionUID = 1L;
	public static JPanel panel;
	public static JTextField nameField;
	public static JTextField emailField;
	public static JPasswordField passwordField;
	public static JTextField address1Field;
	public static JTextField address2Field;
	public static JLabel lblEmail;
	public static JLabel lblPassword;
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
	public static JLabel lblMobileNo;
	public static JLabel lblChooseDateFor;
	int xx, xy;
	private int sID;
	SupplierDao supplier = new SupplierDao();
	String data[] = new String[9]; 
	public static JTextField mobileField;
	public static JTextField IDField;
	public static JButton btnPrint;
	@SuppressWarnings("rawtypes")
	public static JComboBox comboBox;
	PurchaseDao purchase = new PurchaseDao();

	/**
	 * Create the panel.
	 */
	public PrintSupplierReport()
	{
		initComponent();
		init();
	}

	private void init() {
		sID = supplier.getSupplierId(SupplierDashboard.lblSupplieremail.getText());
		data = supplier.getSupplierData(sID);
		setValue();
		
		btnPrint.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try
				{
					SupplierReport.reportPrint(comboBox.getSelectedItem().toString());
				}
				catch (Exception e)
				{
					Logger.getLogger(SupplierReport.class.getName()).log(Level.SEVERE, null, e);
				}
			}
		});
	}
	
	private void setValue() {
		IDField.setText(data[0]);
		nameField.setText(data[1]);
		emailField.setText(data[2]);
		mobileField.setText(data[4]);
		address1Field.setText(data[5]);
		address2Field.setText(data[6]);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void initComponent() {
		setLayout(null);
		
		panel = new JPanel();
		panel.setBounds(30, 12, 890, 577);
		add(panel);
		panel.setLayout(null);
		
		nameField = new JTextField();
		nameField.setEditable(false);
		nameField.setBounds(142, 217, 290, 30);
		nameField.setFont(new Font("DejaVu Sans Condensed", Font.PLAIN, 14));
		panel.add(nameField);
		nameField.setColumns(10);
		
		emailField = new JTextField();
		emailField.setEditable(false);
		emailField.setBounds(142, 277, 290, 30);
		emailField.setFont(new Font("DejaVu Sans Condensed", Font.PLAIN, 14));
		panel.add(emailField);
		emailField.setColumns(10);
		
		address1Field = new JTextField();
		address1Field.setEditable(false);
		address1Field.setBounds(460, 216, 290, 30);
		address1Field.setFont(new Font("DejaVu Sans Condensed", Font.PLAIN, 14));
		panel.add(address1Field);
		address1Field.setColumns(10);
		
		address2Field = new JTextField();
		address2Field.setEditable(false);
		address2Field.setBounds(460, 274, 290, 30);
		address2Field.setFont(new Font("DejaVu Sans Condensed", Font.PLAIN, 14));
		panel.add(address2Field);
		address2Field.setColumns(10);
		
		lblUserId = new JLabel("User ID");
		lblUserId.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 14));
		lblUserId.setBounds(142, 138, 207, 15);
		panel.add(lblUserId);
		
		lblUsername = new JLabel("Username");
		lblUsername.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 14));
		lblUsername.setBounds(142, 198, 207, 15);
		panel.add(lblUsername);
		
		lblEmail = new JLabel("Email");
		lblEmail.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 14));
		lblEmail.setBounds(142, 256, 207, 15);
		panel.add(lblEmail);
	
		
		lblAdress = new JLabel("Address Line 1(State & Area)");
		lblAdress.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 14));
		lblAdress.setBounds(460, 198, 233, 15);
		panel.add(lblAdress);
		
		lblAddressLinecountry = new JLabel("Address Line 2(Country)");
		lblAddressLinecountry.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 14));
		lblAddressLinecountry.setBounds(460, 255, 207, 15);
		panel.add(lblAddressLinecountry);
		
		lblMobileNo = new JLabel("Mobile no.");
		lblMobileNo.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 14));
		lblMobileNo.setBounds(463, 138, 207, 15);
		panel.add(lblMobileNo);
		
		mobileField = new JTextField();
		mobileField.setEditable(false);
		mobileField.setText((String) null);
		mobileField.setFont(new Font("DejaVu Sans Condensed", Font.PLAIN, 14));
		mobileField.setColumns(10);
		mobileField.setBounds(463, 159, 290, 30);
		panel.add(mobileField);
		
		IDField = new JTextField();
		IDField.setText((String) null);
		IDField.setFont(new Font("DejaVu Sans Condensed", Font.PLAIN, 14));
		IDField.setEditable(false);
		IDField.setColumns(10);
		IDField.setBounds(142, 159, 290, 30);
		panel.add(IDField);
		
		lblChooseDateFor = new JLabel("Choose Order Status Which You Print :");
		lblChooseDateFor.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 20));
		lblChooseDateFor.setBounds(142, 331, 407, 24);
		panel.add(lblChooseDateFor);
		
		btnPrint = new JButton("Print");
		btnPrint.setIcon(new ImageIcon(getClass().getResource("/com/logilite/img/print.png")));
		btnPrint.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 14));
		btnPrint.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		btnPrint.setBounds(597, 439, 153, 37);
		btnPrint.setFocusPainted(false);
		panel.add(btnPrint);
		
		comboBox = new JComboBox();
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"ALL", "On the way", "Received"}));
		comboBox.setBounds(272, 384, 207, 30);
		panel.add(comboBox);
		
		JLabel lblOrderStatus = new JLabel("Order Status :");
		lblOrderStatus.setFont(new Font("DejaVu Sans Condensed", Font.BOLD, 16));
		lblOrderStatus.setBounds(142, 391, 122, 15);
		panel.add(lblOrderStatus);
	}
}
