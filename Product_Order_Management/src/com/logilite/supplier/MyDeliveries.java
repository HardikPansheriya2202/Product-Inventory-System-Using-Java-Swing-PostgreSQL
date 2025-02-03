package com.logilite.supplier;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

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
import com.logilite.dao.SupplierDao;

public class MyDeliveries extends JPanel
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
	public static JButton okButton;
	int xx, xy;
	DefaultTableModel model;
	private String supplierName;
	SupplierDao supplier = new SupplierDao();
	PurchaseDao purchase = new PurchaseDao();

	public MyDeliveries()
	{
		initComponent();
		init();
	}
	
	private void init() {
		supplierName = supplier.getSupplierName(SupplierDashboard.lblSupplieremail.getText());
		myDeliveredTable();
		
		table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
            	model = (DefaultTableModel) table.getModel();
                int rowIndex = table.getSelectedRow();
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
				purchase.getSupplierDeliveredProducts(table, searchField.getText(), supplierName);
			}
		});
	}
	
	private void myDeliveredTable() {
		purchase.getSupplierDeliveredProducts(table, "", supplierName);
		model = (DefaultTableModel) table.getModel();
		table.setRowHeight(30);
		table.setShowGrid(true);
		table.setGridColor(Color.black);
		table.setBackground(Color.white);
		table.setSelectionBackground(Color.LIGHT_GRAY);
	}
	
	private void openDeliveryDetailsPopup(int rowIndex) {
	    model = (DefaultTableModel) table.getModel();

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
	    
	    addLabelWithSpacing(dialog, "Order ID:", orderId);
	    addLabelWithSpacing(dialog, "User Name:", userName);
	    addLabelWithSpacing(dialog, "User Phone:", userPhone);
	    addLabelWithSpacing(dialog, "Total:", total);
	    addLabelWithSpacing(dialog, "Purchase Date:", purchaseDate);
	    addLabelWithSpacing(dialog, "Address:", address);
	    addLabelWithSpacing(dialog, "Received Date:", receivedDate);
	    addLabelWithSpacing(dialog, "Order Status:", orderStatus);

	    buttonPanel = new JPanel();
	    okButton = new JButton("OK");

	    okButton.addActionListener(e -> dialog.dispose());

	    buttonPanel.add(okButton);
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
		searchField.setBounds(676, 86, 278, 30);
		panel.add(searchField);
		searchField.setColumns(10);
		
		lblProductSearch = new JLabel("Product Search");
		lblProductSearch.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 14));
		lblProductSearch.setBounds(543, 86, 134, 30);
		panel.add(lblProductSearch);
	}
}
