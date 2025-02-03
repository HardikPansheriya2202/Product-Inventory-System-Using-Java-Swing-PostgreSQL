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
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.logilite.dao.PurchaseDao;
import com.logilite.dao.SupplierDao;

public class SelectSupplier extends JPanel
{

	private static final long serialVersionUID = 1L;
	public static JPanel panel;
	public static JTable table;
	public static JTextField searchField;
	public static JLabel lblX;
	@SuppressWarnings("rawtypes")
	public static JComboBox comboBox;
	public static JLabel lblSupplier;
	public static JButton btnSelect;
	public static JButton btnClear;
	public static JScrollPane scrollPane;
	public static JLabel lblSearch;
	int xx, xy;
	String[] suppliers;
	SupplierDao supplier = new SupplierDao();
	DefaultTableModel model;
	PurchaseDao purchase = new PurchaseDao();
	int rowIndex = 0;

	public SelectSupplier()
	{
		initComponent();
		init();
	}
	
	private void init() {
		suppliers = new String[supplier.countSuppliers()];
		setSupplier();
		supplierTable();
		
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				model = (DefaultTableModel) table.getModel();
				rowIndex = table.getSelectedRow();
			}
		});
		
		searchField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				table.setModel(new DefaultTableModel(null, new Object[] {"Order ID", "User ID", "User Name", "User Phone", "Total", "Purchase Date", "Received Date", "Supplier Name", "Order Status", "Payment Status"}));
				table.getColumnModel().getColumn(0).setPreferredWidth(185);
				table.getColumnModel().getColumn(1).setPreferredWidth(50);
				table.getColumnModel().getColumn(3).setPreferredWidth(100);
				table.getColumnModel().getColumn(5).setPreferredWidth(100);
				table.getColumnModel().getColumn(6).setPreferredWidth(100);
				table.getColumnModel().getColumn(7).setPreferredWidth(100);
				table.getColumnModel().getColumn(8).setPreferredWidth(85);
				table.getColumnModel().getColumn(9).setPreferredWidth(105);
				DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
				centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
				
				for (int i = 0; i < table.getColumnCount(); i++) {
					table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
				}
				table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
				table.getTableHeader().setReorderingAllowed(false);
				purchase.getProductData(table, searchField.getText());
			}
		});
		
		btnSelect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				model = (DefaultTableModel) table.getModel();
				if (table.getSelectedRow() >= 0) {
					rowIndex = table.getSelectedRow();
					String order_id = model.getValueAt(rowIndex, 0).toString();
					String supplier = comboBox.getSelectedItem().toString();
					String status = "On the way";
					purchase.setSupplierStatus(order_id, supplier, status);
					table.setModel(new DefaultTableModel(null, new Object[] {"Order ID", "User ID", "User Name", "User Phone", "Total", "Purchase Date", "Received Date", "Supplier Name", "Order Status", "Payment Status"}));
					table.getColumnModel().getColumn(0).setPreferredWidth(185);
					table.getColumnModel().getColumn(1).setPreferredWidth(50);
					table.getColumnModel().getColumn(3).setPreferredWidth(100);
					table.getColumnModel().getColumn(5).setPreferredWidth(100);
					table.getColumnModel().getColumn(6).setPreferredWidth(100);
					table.getColumnModel().getColumn(7).setPreferredWidth(100);
					table.getColumnModel().getColumn(8).setPreferredWidth(85);
					table.getColumnModel().getColumn(9).setPreferredWidth(105);
					DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
					centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
					
					for (int i = 0; i < table.getColumnCount(); i++) {
						table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
					}
					table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
					table.getTableHeader().setReorderingAllowed(false);
					purchase.getProductData(table, "");
				}else {
					JOptionPane.showMessageDialog(SelectSupplier.this, "No product has been selected", "Warning", 2 );
				}
			}
		});
		
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				searchField.setText("");
				table.clearSelection();
			}
		});
	}
	
	@SuppressWarnings("unchecked")
	private void setSupplier() {
		suppliers = supplier.getSupplier();
		for (String s : suppliers) {
			comboBox.addItem(s);
		}
	}
	
	private void supplierTable() {
		purchase.getProductData(table, "");
		model = (DefaultTableModel) table.getModel();
		table.setRowHeight(30);
		table.setShowGrid(true);
		table.setGridColor(Color.black);
		table.setBackground(Color.white);
		table.setSelectionBackground(Color.LIGHT_GRAY);
	}
	
	@SuppressWarnings({ "serial", "rawtypes" })
	private void initComponent() {
		setLayout(null);
		
		panel = new JPanel();
		panel.setBounds(0, 0, 1063, 597);
		add(panel);
		panel.setLayout(null);
		
		table = new JTable();
		table.setBackground(new Color(222, 221, 218));
		table.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 14));
		table.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Order ID", "User ID", "User Name", "User Phone", "Total", "Purchase Date", "Received Date", "Supplier Name", "Order Status", "Payment Status"
			}
		) {
			boolean[] columnEditables = new boolean[] {
				false, false, false, false, false, false, false, false, false, false
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		table.getColumnModel().getColumn(0).setPreferredWidth(185);
		table.getColumnModel().getColumn(1).setPreferredWidth(50);
		table.getColumnModel().getColumn(3).setPreferredWidth(100);
		table.getColumnModel().getColumn(5).setPreferredWidth(100);
		table.getColumnModel().getColumn(6).setPreferredWidth(100);
		table.getColumnModel().getColumn(7).setPreferredWidth(100);
		table.getColumnModel().getColumn(8).setPreferredWidth(85);
		table.getColumnModel().getColumn(9).setPreferredWidth(105);
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.getTableHeader().setReorderingAllowed(false);
		scrollPane = new JScrollPane(table);
		scrollPane.setBounds(25, 235, 994, 337);
		panel.add(scrollPane);
		
		searchField = new JTextField();
		searchField.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 14));
		searchField.setBounds(741, 189, 278, 30);
		panel.add(searchField);
		searchField.setColumns(10);
		
		lblSearch = new JLabel("Search");
		lblSearch.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 14));
		lblSearch.setBounds(663, 189, 80, 30);
		panel.add(lblSearch);
		
		comboBox = new JComboBox();
		comboBox.setFont(new Font("DejaVu Sans Condensed", Font.BOLD, 14));
		comboBox.setBounds(42, 100, 278, 30);
		panel.add(comboBox);
		
		lblSupplier = new JLabel("Supplier");
		lblSupplier.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 14));
		lblSupplier.setBounds(42, 73, 70, 15);
		panel.add(lblSupplier);
		
		btnSelect = new JButton("Select");
		btnSelect.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnSelect.setIcon(new ImageIcon(getClass().getResource("/com/logilite/img/select.png")));
		btnSelect.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 16));
		btnSelect.setBounds(352, 100, 122, 28);
		btnSelect.setFocusPainted(false);
		panel.add(btnSelect);
		
		btnClear = new JButton("Clear");
		btnClear.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnClear.setIcon(new ImageIcon(getClass().getResource("/com/logilite/img/clear.png")));
		btnClear.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 16));
		btnClear.setBounds(499, 100, 117, 28);
		btnClear.setFocusPainted(false);
		panel.add(btnClear);
	}
}
