package com.logilite.supplier;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
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
import com.logilite.dao.SupplierDao;

public class Delivery extends JPanel
{

	private static final long serialVersionUID = 1L;
	public static JPanel panel;
	public static JTable table;
	public static JTextField searchField;
	public static JLabel lblX;
	public static JScrollPane scrollPane;
	public static JLabel lblProductSearch;
	public static JDialog dialog;
	public static JPanel buttonPanel;
	public static JButton confirmButton;
	public static JButton cancelButton;
	int xx, xy;
	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
	Date date = (Date) new java.util.Date();
	DefaultTableModel model;
	private String supllierName;
	SupplierDao supplier = new SupplierDao();
	PurchaseDao purchase = new PurchaseDao();
	int rowIndex = 0;
	Statistics statistics = new Statistics();

	public Delivery()
	{
		initComponent();
		init();
	}
	
	private void init() {
		supllierName = supplier.getSupplierName(SupplierDashboard.lblSupplieremail.getText());
		purchaseTable();
		
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				model = (DefaultTableModel) table.getModel();
				rowIndex = table.getSelectedRow();
				openDeliveryDetailsPopup(rowIndex);
			}
		});
		
		searchField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				table.setModel(new DefaultTableModel(null, new Object[] {"PID", "Order ID", "UID", "User Name", "User Phone", "Total", "Purchase Date", "Address", "Received Date", "Supplier Name", "Order Status"}));
				table.getColumnModel().getColumn(0).setPreferredWidth(25);
				table.getColumnModel().getColumn(1).setPreferredWidth(170);
				table.getColumnModel().getColumn(2).setPreferredWidth(28);
				table.getColumnModel().getColumn(4).setPreferredWidth(95);
				table.getColumnModel().getColumn(6).setPreferredWidth(95);
				table.getColumnModel().getColumn(7).setPreferredWidth(175);
				table.getColumnModel().getColumn(8).setPreferredWidth(92);
				table.getColumnModel().getColumn(9).setPreferredWidth(93);
				table.getColumnModel().getColumn(10).setPreferredWidth(85);
				DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		        
		        for (int i = 0; i < table.getColumnCount(); i++) {
		            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
		        }
				table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
				table.getTableHeader().setReorderingAllowed(false);
				purchase.getOnTheWayPurchaseProduct(table, searchField.getText(), supllierName);
			}
		});
		
	}
	
	
	private void openDeliveryDetailsPopup(int rowIndex) {
	    model = (DefaultTableModel) table.getModel();
	    int id = Integer.parseInt(model.getValueAt(rowIndex, 0).toString());
	    String orderId = model.getValueAt(rowIndex, 1).toString();
	    String userName = model.getValueAt(rowIndex, 3).toString();
	    String userPhone = model.getValueAt(rowIndex, 4).toString();
	    String total = model.getValueAt(rowIndex, 5).toString();
	    String purchaseDate = model.getValueAt(rowIndex, 6).toString();
	    String address = model.getValueAt(rowIndex, 7).toString();
	    String receivedDate = model.getValueAt(rowIndex, 8).toString();
	    String orderStatus = model.getValueAt(rowIndex, 10).toString();

	    dialog = new JDialog();
	    dialog.setTitle("Delivery Details");
	    dialog.setSize(400, 400);
	    dialog.setLayout(new BoxLayout(dialog.getContentPane(), BoxLayout.Y_AXIS));
	    
	    addLabelWithSpacing(dialog, "Order ID:", String.valueOf(orderId));
	    addLabelWithSpacing(dialog, "User Name:", userName);
	    addLabelWithSpacing(dialog, "User Phone:", userPhone);
	    addLabelWithSpacing(dialog, "Total:", total);
	    addLabelWithSpacing(dialog, "Purchase Date:", purchaseDate);
	    addLabelWithSpacing(dialog, "Address:", address);
	    addLabelWithSpacing(dialog, "Received Date:", receivedDate);
	    addLabelWithSpacing(dialog, "Order Status:", orderStatus);

	    buttonPanel = new JPanel();
	    confirmButton = new JButton("Confirm");
	    cancelButton = new JButton("Cancel");

	    confirmButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String receivedDate = df.format(new Date());
				String status = "Received";
				purchase.setDateStatus(id, receivedDate, status);
				table.setModel(new DefaultTableModel(null, new Object[] {"PID", "Order ID", "UID", "User Name", "User Phone", "Total", "Purchase Date", "Address", "Received Date", "Supplier Name", "Order Status"}));
				table.getColumnModel().getColumn(0).setPreferredWidth(25);
				table.getColumnModel().getColumn(1).setPreferredWidth(170);
				table.getColumnModel().getColumn(2).setPreferredWidth(28);
				table.getColumnModel().getColumn(4).setPreferredWidth(95);
				table.getColumnModel().getColumn(6).setPreferredWidth(95);
				table.getColumnModel().getColumn(7).setPreferredWidth(175);
				table.getColumnModel().getColumn(8).setPreferredWidth(92);
				table.getColumnModel().getColumn(9).setPreferredWidth(93);
				table.getColumnModel().getColumn(10).setPreferredWidth(85);
				DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
				centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
				
				for (int i = 0; i < table.getColumnCount(); i++) {
					table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
				}
				table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
				table.getTableHeader().setReorderingAllowed(false);
				purchase.getOnTheWayPurchaseProduct(table, "", supllierName);
				statistics.supplier(supplier.getSupplierName(SupplierDashboard.lblSupplieremail.getText()));
				dialog.dispose();
			}
		});

	    cancelButton.addActionListener(e -> dialog.dispose());

	    buttonPanel.add(confirmButton);
	    buttonPanel.add(cancelButton);
	    dialog.add(buttonPanel);

	    dialog.setLocationRelativeTo(null);
	    
	    dialog.setVisible(true);
	}

	private void addLabelWithSpacing(JDialog dialog, String labelText, String valueText) {
	    JPanel panel = new JPanel();
	    panel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
	    
	    JLabel label = new JLabel(labelText);
	    label.setPreferredSize(new Dimension(120, 30));
	    JLabel value = new JLabel(valueText);
	    value.setPreferredSize(new Dimension(250, 30));
	    
	    panel.add(label);
	    panel.add(value);

	    dialog.add(panel);
	}
	
	private void purchaseTable() {
		purchase.getOnTheWayPurchaseProduct(table, "", supllierName);
		model = (DefaultTableModel) table.getModel();
		table.setRowHeight(30);
		table.setShowGrid(true);
		table.setGridColor(Color.black);
		table.setBackground(Color.white);
		table.setSelectionBackground(Color.LIGHT_GRAY);
	}
	
	@SuppressWarnings("serial")
	private void initComponent() {
		setLayout(null);
		
		panel = new JPanel();
		panel.setBounds(0, 0, 1039, 553);
		add(panel);
		panel.setLayout(null);

		table = new JTable();
		table.setBackground(new Color(222, 221, 218));
		table.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 14));
		table.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"PID", "Order ID", "UID", "User Name", "User Phone", "Total", "Purchase Date", "Address", "Received Date", "Supplier Name", "Order Status"
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
		table.getColumnModel().getColumn(1).setPreferredWidth(170);
		table.getColumnModel().getColumn(2).setPreferredWidth(28);
		table.getColumnModel().getColumn(4).setPreferredWidth(95);
		table.getColumnModel().getColumn(6).setPreferredWidth(95);
		table.getColumnModel().getColumn(7).setPreferredWidth(175);
		table.getColumnModel().getColumn(8).setPreferredWidth(92);
		table.getColumnModel().getColumn(9).setPreferredWidth(93);
		table.getColumnModel().getColumn(10).setPreferredWidth(85);
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.getTableHeader().setReorderingAllowed(false);
		scrollPane = new JScrollPane(table);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setBounds(0, 125, 1027, 416);
		panel.add(scrollPane);
		
		searchField = new JTextField();
		searchField.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 14));
		searchField.setBounds(677, 83, 278, 30);
		panel.add(searchField);
		searchField.setColumns(10);
		
		lblProductSearch = new JLabel("Product Search");
		lblProductSearch.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 14));
		lblProductSearch.setBounds(547, 83, 134, 30);
		panel.add(lblProductSearch);
	}

}
