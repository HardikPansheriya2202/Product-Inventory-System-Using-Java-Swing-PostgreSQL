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

import com.logilite.dao.ProductDao;
import com.logilite.dao.Statistics;

public class ManageProduct extends JPanel
{

	private static final long serialVersionUID = 1L;
	public static JPanel panel;
	private JTable table;
	private JTextField searchField;
	private JTextField IDField;
	private JTextField nameField;
	private JTextField quantityField;
	private JTextField priceField;
	public static JLabel lblX;
	public static JScrollPane scrollPane;
	public static JLabel lblProductId;
	public static JLabel lblProductName;
	public static JLabel lblQuantity;
	public static JButton btnSave;
	public static JButton btnUpdate;
	public static JButton btnDelete;
	public static JButton btnClear;
	public static JLabel lblSearch;
	public static JLabel lblCategory;
	@SuppressWarnings("rawtypes")
	public static JComboBox comboBox;
	public static JLabel lblPrice;
	int xx, xy;
	Color noEdit = new Color(204, 204, 204);
	DefaultTableModel model;
	int rowIndex;
	ProductDao product = new ProductDao();
	String[] categories;
	Statistics statistics = new Statistics();

	public ManageProduct()
	{
		initComponent();
		init();
		productTable();
	}
	
	private void init() {
		IDField.setBackground(noEdit);
		IDField.setText(String.valueOf(product.getMaxRow()));
		categories = new String[product.countCategories()];
		setCategories();
		
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				model = (DefaultTableModel) table.getModel();
				rowIndex = table.getSelectedRow();
				IDField.setText(model.getValueAt(rowIndex, 0).toString());
				nameField.setText(model.getValueAt(rowIndex, 1).toString());
				
				String category = model.getValueAt(rowIndex, 2).toString();
				for (int i = 0; i < comboBox.getItemCount(); i++) {
					if (comboBox.getItemAt(i).equals(category)) {
						comboBox.setSelectedIndex(i);
						break;
					}
				}
				
				quantityField.setText(model.getValueAt(rowIndex, 3).toString());
				priceField.setText(model.getValueAt(rowIndex, 4).toString());
			}
		});
		
		searchField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				table.setModel(new DefaultTableModel(null, new Object[] {"Product ID", "Product Name", "Category", "Quantity", "Price"}));
				
				DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
				centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
				
				for (int i = 0; i < table.getColumnCount(); i++) {
					table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
				}
				
				product.getAllProductData(table, searchField.getText());
			}
		});
		
		quantityField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				if (!Character.isDigit(e.getKeyChar())) {
					e.consume();
				}
			}
		});
		
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (isEmpty()) {
					int id = Integer.parseInt(IDField.getText());
					String pname = nameField.getText();
					String cname = comboBox.getSelectedItem().toString();
					int quantity = Integer.parseInt(quantityField.getText());
					if (isNumeric(priceField.getText())) {
						double price = Double.parseDouble(priceField.getText());
						if (!product.isIDExists(id)) {
							if (!product.isProductCategoryExists(pname, cname)) {
								product.insert(id, pname, cname, quantity, price);
								table.setModel(new DefaultTableModel(null, new Object[] {"Product ID", "Product Name", "Category", "Quantity", "Price"}));
								
								DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
								centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
								
								for (int i = 0; i < table.getColumnCount(); i++) {
									table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
								}
								
								product.getAllProductData(table, "");
								clear();
							}else {
								JOptionPane.showMessageDialog(ManageProduct.this, "This product and category already exists", "Warning", 2);
							}
						}else {
							JOptionPane.showMessageDialog(ManageProduct.this, "This product id already exists", "Warning", 2);
						}
					}
				}
			}
		});
		
		btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int id = Integer.parseInt(IDField.getText());
				if (product.isIDExists(id)) {
					if (isEmpty()) {
						String pname = nameField.getText();
						String cname = comboBox.getSelectedItem().toString();
						int quantity = Integer.parseInt(quantityField.getText());
						if (isNumeric(priceField.getText())) {
							double price = Double.parseDouble(priceField.getText());
							if (product.isProductCategoryExists(pname, cname)) {
								if (!check()) {
									product.update(id, pname, cname, quantity, price);
									table.setModel(new DefaultTableModel(null, new Object[] {"Product ID", "Product Name", "Category", "Quantity", "Price"}));
									
									DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
									centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
									
									for (int i = 0; i < table.getColumnCount(); i++) {
										table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
									}
									
									product.getAllProductData(table, "");
									clear();									
								}
							}
						}
					}
				}else {
					JOptionPane.showMessageDialog(ManageProduct.this, "This product id doesn't exists", "Warning", 2);
				}
			}
		});
		
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int id = Integer.parseInt(IDField.getText());
				if (product.isIDExists(id)) {
					product.delete(id);
					table.setModel(new DefaultTableModel(null, new Object[] {"Product ID", "Product Name", "Category", "Quantity", "Price"}));
					
					DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
					centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
					
					for (int i = 0; i < table.getColumnCount(); i++) {
						table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
					}
					
					product.getAllProductData(table, "");
					clear();
				}else {
					JOptionPane.showMessageDialog(ManageProduct.this, "Product doesn't exists", "Warning", 2);
				}
			}
		});
		
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clear();
			}
		});
	}
	
	@SuppressWarnings("unchecked")
	private void setCategories() {
		categories = product.getCategories();
		for(String s : categories) {
			comboBox.addItem(s);
		}
	}
	
	private void productTable() {
		product.getAllProductData(table, "");
		model = (DefaultTableModel) table.getModel();
		table.setRowHeight(30);
		table.setShowGrid(true);
		table.setGridColor(Color.black);
		table.setBackground(Color.white);
		table.setSelectionBackground(Color.LIGHT_GRAY);
	}
	
	private void clear() {
		searchField.setText("");
		IDField.setText(String.valueOf(product.getMaxRow()));
		nameField.setText("");
		comboBox.setSelectedIndex(0);
		quantityField.setText("0");
		priceField.setText("0.0");
		table.clearSelection();
		statistics.admin();
	}
	
	private boolean isEmpty() {
		if (nameField.getText().isEmpty()) {
			JOptionPane.showMessageDialog(ManageProduct.this, "Product name is required", "Warning", 2);
			return false;
		}
		if (quantityField.getText().isEmpty()) {
			JOptionPane.showMessageDialog(ManageProduct.this, "Product quantity is required", "Warning", 2);
			return false;
		}
		if (Integer.parseInt(quantityField.getText()) <= 0) {
			JOptionPane.showMessageDialog(ManageProduct.this, "Please, Increase the product quantity", "Warning", 2);
			return false;
		}
		if (priceField.getText().equals("0.0") || priceField.getText().isEmpty()) {
			JOptionPane.showMessageDialog(ManageProduct.this, "Product price is required", "Warning", 2);
			return false;
		}
		return true;
	}
	
	private boolean isNumeric(String s) {
		try
		{
			@SuppressWarnings("unused")
			double d = Double.parseDouble(s);
			return true;
		}
		catch (NumberFormatException e)
		{
			JOptionPane.showMessageDialog(ManageProduct.this, "" + e + "\nNumeric is required for the price field");
		}
		return false;
	}
	
	private boolean check() {
		String newProduct = nameField.getText();
		String oldProduct = model.getValueAt(rowIndex, 1).toString();
		String newCategory = comboBox.getSelectedItem().toString();
		String oldCategory = model.getValueAt(rowIndex, 2).toString();
		
		if (newProduct.equals(oldProduct) && newCategory.equals(oldCategory)) {
			return false;
		}else {
				boolean x = product.isProductCategoryExists(newProduct, newCategory);
				if (x) {
					JOptionPane.showMessageDialog(ManageProduct.this, "Product name and category already exists", "Warning", 2);
				return x;
			}
		}
		return false;
	}
	
	@SuppressWarnings({ "serial", "rawtypes" })
	private void initComponent() {
		setLayout(null);
		
		panel = new JPanel();
		panel.setBounds(0, 0, 956, 571);
		add(panel);
		panel.setLayout(null);
		
		table = new JTable();
		table.setBackground(new Color(222, 221, 218));
		table.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 14));
		table.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Product ID", "Product Name", "Category", "Quantity", "Price"
			}
		) {
			boolean[] columnEditables = new boolean[] {
				false, false, false, true, true
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});

		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        
		table.getTableHeader().setReorderingAllowed(false);
		scrollPane = new JScrollPane(table);
		scrollPane.setBounds(401, 121, 523, 410);
		panel.add(scrollPane);
		
		searchField = new JTextField();
		searchField.setFont(new Font("DejaVu Sans Condensed", Font.BOLD, 14));
		searchField.setBounds(679, 81, 245, 30);
		panel.add(searchField);
		searchField.setColumns(10);
		
		IDField = new JTextField();
		IDField.setEditable(false);
		IDField.setFont(new Font("DejaVu Sans Condensed", Font.BOLD, 14));
		IDField.setBounds(98, 141, 262, 30);
		panel.add(IDField);
		IDField.setColumns(10);
		
		nameField = new JTextField();
		nameField.setFont(new Font("DejaVu Sans Condensed", Font.BOLD, 14));
		nameField.setBounds(98, 201, 262, 30);
		panel.add(nameField);
		nameField.setColumns(10);
		
		quantityField = new JTextField();
		quantityField.setText("0");
		quantityField.setFont(new Font("DejaVu Sans Condensed", Font.BOLD, 14));
		quantityField.setBounds(98, 313, 262, 30);
		panel.add(quantityField);
		quantityField.setColumns(10);
		
		lblProductId = new JLabel("Product ID");
		lblProductId.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 14));
		lblProductId.setBounds(98, 121, 154, 15);
		panel.add(lblProductId);
		
		lblProductName = new JLabel("Product Name");
		lblProductName.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 14));
		lblProductName.setBounds(98, 180, 154, 15);
		panel.add(lblProductName);
		
		lblQuantity = new JLabel("Quantity");
		lblQuantity.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 14));
		lblQuantity.setBounds(98, 295, 154, 15);
		panel.add(lblQuantity);
		
		btnSave = new JButton("Save");
		btnSave.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnSave.setIcon(new ImageIcon(getClass().getResource("/com/logilite/img/save.png")));
		btnSave.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 16));
		btnSave.setBounds(98, 433, 120, 37);
		btnSave.setFocusPainted(false);
		panel.add(btnSave);
		
		btnUpdate = new JButton("Update");
		btnUpdate.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnUpdate.setIcon(new ImageIcon(getClass().getResource("/com/logilite/img/update.png")));
		btnUpdate.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 16));
		btnUpdate.setBounds(230, 433, 130, 37);
		btnUpdate.setFocusPainted(false);
		panel.add(btnUpdate);
		
		btnDelete = new JButton("Delete");
		btnDelete.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnDelete.setIcon(new ImageIcon(getClass().getResource("/com/logilite/img/remove.png")));
		btnDelete.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 16));
		btnDelete.setBounds(98, 494, 120, 37);
		btnDelete.setFocusPainted(false);
		panel.add(btnDelete);
		
		btnClear = new JButton("Clear");
		btnClear.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnClear.setIcon(new ImageIcon(getClass().getResource("/com/logilite/img/clear.png")));
		btnClear.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 16));
		btnClear.setBounds(230, 494, 130, 37);
		btnClear.setFocusPainted(false);
		panel.add(btnClear);
		
		lblSearch = new JLabel("Search");
		lblSearch.setFont(new Font("DejaVu Serif", Font.BOLD, 16));
		lblSearch.setBounds(591, 79, 70, 30);
		panel.add(lblSearch);
		
		lblCategory = new JLabel("Category");
		lblCategory.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 14));
		lblCategory.setBounds(98, 237, 120, 15);
		panel.add(lblCategory);
		
		comboBox = new JComboBox();
		comboBox.setFont(new Font("DejaVu Sans Condensed", Font.BOLD, 14));
		comboBox.setBounds(98, 257, 262, 30);
		panel.add(comboBox);
		
		lblPrice = new JLabel("Price");
		lblPrice.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 14));
		lblPrice.setBounds(98, 352, 90, 15);
		panel.add(lblPrice);
		
		priceField = new JTextField();
		priceField.setText("0.0");
		priceField.setFont(new Font("DejaVu Sans Condensed", Font.BOLD, 14));
		priceField.setBounds(98, 371, 262, 30);
		panel.add(priceField);
		priceField.setColumns(10);
	}

}
