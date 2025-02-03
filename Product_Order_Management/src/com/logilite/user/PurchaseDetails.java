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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.logilite.dao.PurchaseDao;
import com.logilite.dao.Statistics;
import com.logilite.dao.UserDao;

public class PurchaseDetails extends JPanel
{

	private static final long serialVersionUID = 1L;
	public static JPanel panel;
	public static JTable table;
	public static JTextField searchField;
	public static JTextField purchaseIdField;
	public static JTextField receivedDateField;
	public static JTextField currentDateField;
	public static JLabel lblX;
	public static JScrollPane scrollPane;
	public static JButton btnRefund;
	public static JButton btnClear;
	public static JLabel lblPurchaseId;
	public static JLabel lblReceivedDate;
	public static JLabel lblCurrentDate;
	public static JLabel lblSearchProduct;
	int xx, xy;
	int rowIndex;
	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
	Date date = (Date) new java.util.Date();
	private int uId;
	UserDao user = new UserDao();
	DefaultTableModel model;
	PurchaseDao purchase = new PurchaseDao();
	Statistics statistics = new Statistics();

	public PurchaseDetails()
	{
		initComponent();
		init();
	}
	
	private void init() {
		currentDateField.setText(df.format(date));
		uId = user.getUserId(UserDashboard.lblUseremail.getText());
		productTable();
		
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				model = (DefaultTableModel) table.getModel();
				rowIndex = table.getSelectedRow();
				purchaseIdField.setText(model.getValueAt(rowIndex, 0).toString());
				if (model.getValueAt(rowIndex, 8) == null) {
					receivedDateField.setText(null);
				}else {
					receivedDateField.setText(model.getValueAt(rowIndex, 8).toString());
				}
			}
		});
		
		searchField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				table.setModel(new DefaultTableModel(null, new Object[] {"PID", "Order ID", "Pro.ID", "Pro_Name", "Quantity", "Price", "Total", "Purchase Date", "Received Date", "Sup_Name", "Order Status"}));
				table.getColumnModel().getColumn(0).setPreferredWidth(25);
				table.getColumnModel().getColumn(1).setPreferredWidth(180);
				table.getColumnModel().getColumn(2).setPreferredWidth(40);
				table.getColumnModel().getColumn(4).setPreferredWidth(55);
				table.getColumnModel().getColumn(7).setPreferredWidth(95);
				table.getColumnModel().getColumn(8).setPreferredWidth(93);
				table.getColumnModel().getColumn(10).setPreferredWidth(82);
				
				DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
				centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
				
				for (int i = 0; i < table.getColumnCount(); i++) {
					table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
				}
				
				table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
				table.getTableHeader().setReorderingAllowed(false);
				purchase.getProductData(table, searchField.getText(), uId);
			}
		});
		
		btnRefund.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (purchaseIdField.getText().isEmpty() || receivedDateField.getText().isEmpty()) {
					JOptionPane.showMessageDialog(PurchaseDetails.this, "Purchase id or received date is missing");
				}else {
					String receivedDate = receivedDateField.getText();
					String currentDate = currentDateField.getText();
					
					try
					{
						Date d1 = df.parse(receivedDate);
						Date d2 = df.parse(currentDate);
						
						long dateReceivedInMs = d1.getTime();
						long dateCurrentInMs = d2.getTime();
						
						long timeDiff = (dateCurrentInMs - dateReceivedInMs);
						long dayDiff = TimeUnit.DAYS.convert(timeDiff, TimeUnit.MILLISECONDS);
						
						if (dayDiff > 10) {
							JOptionPane.showMessageDialog(PurchaseDetails.this, "Sorry refund time is over! \nRefund applicable 10 days from the received day...");
						}else {
							int id = Integer.parseInt(purchaseIdField.getText());
							purchase.refund(id);
							table.setModel(new DefaultTableModel(null, new Object[] {"PID", "Order ID", "Pro.ID", "Pro_Name", "Quantity", "Price", "Total", "Purchase Date", "Received Date", "Sup_Name", "Order Status"}));
							table.getColumnModel().getColumn(0).setPreferredWidth(25);
							table.getColumnModel().getColumn(1).setPreferredWidth(180);
							table.getColumnModel().getColumn(2).setPreferredWidth(40);
							table.getColumnModel().getColumn(4).setPreferredWidth(55);
							table.getColumnModel().getColumn(7).setPreferredWidth(95);
							table.getColumnModel().getColumn(8).setPreferredWidth(93);
							table.getColumnModel().getColumn(10).setPreferredWidth(82);
							
							DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
							centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
							
							for (int i = 0; i < table.getColumnCount(); i++) {
								table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
							}
							
							table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
							table.getTableHeader().setReorderingAllowed(false);
							purchase.getProductData(table, "", uId);
							statistics.user(user.getUserId(UserDashboard.lblUseremail.getText()));
							clear();
						}
					}
					catch (ParseException e)
					{
						Logger.getLogger(PurchaseDetails.class.getName()).log(Level.SEVERE, null, e);
					}
				}
			}
		});
		
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				clear();
			}
		});
	}
	
	private void productTable() {
		purchase.getProductData(table, "", uId);
		model = (DefaultTableModel) table.getModel();
		table.setRowHeight(30);
		table.setShowGrid(true);
		table.setGridColor(Color.black);
		table.setBackground(Color.white);
		table.setSelectionBackground(Color.LIGHT_GRAY);
	}
	
	private void clear() {
		searchField.setText("");
		purchaseIdField.setText("");
		receivedDateField.setText("");
		table.clearSelection();
	}
	
	@SuppressWarnings("serial")
	private void initComponent() {
		setLayout(null);
		
		panel = new JPanel();
		panel.setBounds(0, 0, 1048, 591);
		add(panel);
		panel.setLayout(null);

		table = new JTable();
		table.setBackground(new Color(222, 221, 218));
		table.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 14));
		table.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"PID", "Order ID", "Pro.ID", "Pro_Name", "Quantity", "Price", "Total", "Purchase Date", "Received Date", "Sup_Name", "Order Status"
			}
		) {
			boolean[] columnEditables = new boolean[] {
				false, false, false, false, false, false, false, false, false, false, false
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		table.getColumnModel().getColumn(0).setPreferredWidth(25);
		table.getColumnModel().getColumn(1).setPreferredWidth(180);
		table.getColumnModel().getColumn(2).setPreferredWidth(40);
		table.getColumnModel().getColumn(4).setPreferredWidth(55);
		table.getColumnModel().getColumn(7).setPreferredWidth(95);
		table.getColumnModel().getColumn(8).setPreferredWidth(93);
		table.getColumnModel().getColumn(10).setPreferredWidth(82);
		
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.getTableHeader().setReorderingAllowed(false);
		scrollPane = new JScrollPane(table);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
	    scrollPane.setBounds(144, 125, 889, 448);
	    panel.add(scrollPane);
		
		searchField = new JTextField();
		searchField.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 14));
		searchField.setBounds(655, 83, 275, 30);
		panel.add(searchField);
		searchField.setColumns(10);
		
		purchaseIdField = new JTextField();
		purchaseIdField.setEditable(false);
		purchaseIdField.setBackground(new Color(171, 167, 167));
		purchaseIdField.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 14));
		purchaseIdField.setBounds(12, 153, 113, 30);
		panel.add(purchaseIdField);
		purchaseIdField.setColumns(10);
		
		receivedDateField = new JTextField();
		receivedDateField.setEditable(false);
		receivedDateField.setBackground(new Color(171, 167, 167));
		receivedDateField.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 14));
		receivedDateField.setBounds(12, 213, 113, 30);
		panel.add(receivedDateField);
		receivedDateField.setColumns(10);
		
		currentDateField = new JTextField();
		currentDateField.setBackground(new Color(171, 167, 167));
		currentDateField.setEditable(false);
		currentDateField.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 14));
		currentDateField.setBounds(12, 275, 113, 30);
		panel.add(currentDateField);
		currentDateField.setColumns(10);
		
		btnRefund = new JButton("Refund");
		btnRefund.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnRefund.setIcon(new ImageIcon(getClass().getResource("/com/logilite/img/refund.png")));
		btnRefund.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 14));
		btnRefund.setBounds(12, 374, 113, 37);
		btnRefund.setFocusPainted(false);
		panel.add(btnRefund);
		
		btnClear = new JButton("Clear");
		btnClear.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnClear.setIcon(new ImageIcon(getClass().getResource("/com/logilite/img/clear.png")));
		btnClear.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 14));
		btnClear.setBounds(12, 436, 113, 37);
		btnClear.setFocusPainted(false);
		panel.add(btnClear);
		
		lblPurchaseId = new JLabel("Purchase ID");
		lblPurchaseId.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 14));
		lblPurchaseId.setBounds(12, 131, 103, 15);
		panel.add(lblPurchaseId);
		
		lblReceivedDate = new JLabel("Received Date");
		lblReceivedDate.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 14));
		lblReceivedDate.setBounds(12, 193, 103, 15);
		panel.add(lblReceivedDate);
		
		lblCurrentDate = new JLabel("Current Date");
		lblCurrentDate.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 14));
		lblCurrentDate.setBounds(12, 253, 103, 15);
		panel.add(lblCurrentDate);
		
		lblSearchProduct = new JLabel("Search Product");
		lblSearchProduct.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 14));
		lblSearchProduct.setBounds(524, 89, 126, 15);
		panel.add(lblSearchProduct);
	}
}
