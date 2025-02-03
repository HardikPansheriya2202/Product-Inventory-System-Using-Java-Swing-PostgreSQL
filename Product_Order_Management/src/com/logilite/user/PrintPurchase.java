package com.logilite.user;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;

import com.logilite.dao.PurchaseDao;
import com.logilite.dao.UserDao;
import com.toedter.calendar.JDateChooser;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

public class PrintPurchase extends JPanel
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
	public static JDateChooser dateChooser;
	public static JDateChooser dateChooser_1;
	public static JLabel lblFrom;
	public static JLabel lblTo;
	public static JLabel lblMobileNo;
	public static JLabel lblChooseDateFor;
	int xx, xy;
	private int uID;
	UserDao user = new UserDao();
	String data[] = new String[9]; 
	private JTextField mobileField;
	private JTextField IDField;
	private JButton btnPrint;
	PurchaseDao purchase = new PurchaseDao();
	Date date = (Date) new java.util.Date();

	public PrintPurchase()
	{
		initComponent();
		init();
	}
	
	private void init() {
		uID = user.getUserId(UserDashboard.lblUseremail.getText());
		data = user.getUserData(uID);
		setValue();
		
		btnPrint.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				 Date fromDate = dateChooser.getDate();
		         Date toDate = dateChooser_1.getDate();
		         
		         if (fromDate == null) {
		        	 int uid = purchase.getId(UserDashboard.lblUseremail.getText());
		        	 generateReport(uid, null, null);
		         }else {
		        	 if (toDate == null) {
		        		 toDate = date;
		        	 }
		         SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		         String formattedFromDate = dateFormat.format(fromDate);
		         String formattedToDate = dateFormat.format(toDate);
		            
				int uid = purchase.getId(UserDashboard.lblUseremail.getText());
				generateReport(uid, formattedFromDate, formattedToDate);
		        }
			}
		});
	}
	
	private void setValue() {
		IDField.setText(data[0]);
		nameField.setText(data[1]);
		emailField.setText(data[2]);
		mobileField.setText(data[4]);
		address1Field.setText(data[7]);
		address2Field.setText(data[8]);
		dateChooser_1.setDate(date);
	}
	
	@SuppressWarnings("unchecked")
	private void generateReport(int uid, String dateFrom, String dateTo) {
		try
		{
			URL imageUrl = getClass().getResource("/com/logilite/component/login_page_logo.png");
	        if (imageUrl == null) {
	        	JOptionPane.showMessageDialog(PrintPurchase.this, "Image file not found", "Warning", 2);
	        }
			InputStream jasperStream = getClass().getResourceAsStream("/com/logilite/component/PurchaseOrder.jasper");
			JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperStream);
			@SuppressWarnings("rawtypes")
			HashMap a = new HashMap();
			a.put("inputId", uid);
			
			if (dateFrom != null && dateTo != null) {
	            a.put("dateFrom", dateFrom);
	            a.put("dateTo", dateTo);
	        }
			a.put("imagePath", imageUrl);
			
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, a, Login.conn);
			JasperViewer jasperViewer = new JasperViewer(jasperPrint, false);
			jasperViewer.setDefaultCloseOperation(JasperViewer.DISPOSE_ON_CLOSE);
			jasperViewer.setVisible(true);
		}
		catch (JRException e)
		{
            JOptionPane.showMessageDialog(PrintPurchase.this, "Failed to open URL: " + e.getMessage());
		}
	}
	
	private void initComponent() {
		setLayout(null);
		
		panel = new JPanel();
		panel.setBounds(0, 0, 890, 577);
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
		
		lblFrom = new JLabel("From");
		lblFrom.setHorizontalAlignment(SwingConstants.CENTER);
		lblFrom.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 18));
		lblFrom.setBounds(142, 381, 70, 30);
		panel.add(lblFrom);
		
		lblTo = new JLabel("To");
		lblTo.setHorizontalAlignment(SwingConstants.CENTER);
		lblTo.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 18));
		lblTo.setBounds(476, 381, 50, 30);
		panel.add(lblTo);
		
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
		
		lblChooseDateFor = new JLabel("Choose Date For Print Purchase Order :");
		lblChooseDateFor.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 20));
		lblChooseDateFor.setBounds(142, 331, 407, 24);
		panel.add(lblChooseDateFor);
		
		dateChooser = new JDateChooser();
		dateChooser.setDateFormatString("yyyy-MM-dd");
		dateChooser.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 14));
		dateChooser.setBounds(220, 381, 212, 30);
		panel.add(dateChooser);
		
		dateChooser_1 = new JDateChooser();
		dateChooser_1.setDateFormatString("yyyy-MM-dd");
		dateChooser_1.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 14));
		dateChooser_1.setBounds(529, 381, 221, 30);
		panel.add(dateChooser_1);
		
		btnPrint = new JButton("Print");
		btnPrint.setIcon(new ImageIcon(getClass().getResource("/com/logilite/img/print.png")));
		btnPrint.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 14));
		btnPrint.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		btnPrint.setBounds(597, 439, 153, 37);
		btnPrint.setFocusPainted(false);
		panel.add(btnPrint);
	}
}
