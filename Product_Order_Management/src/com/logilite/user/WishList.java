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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.logilite.dao.OrderDao;
import com.logilite.dao.ProductDao;
import com.logilite.dao.PurchaseDao;
import com.logilite.dao.Statistics;
import com.logilite.dao.UserDao;

public class WishList extends JPanel
{

	private static final long serialVersionUID = 1L;
	public static JTable table;
	public static JTable table_1;
	public static JTextField searchField;
	public static JTextField IDField;
	public static JTextField nameField;
	public static JTextField quantityField;
	public static JScrollPane scrollPane;
	public static JLabel lblX;
	public static JScrollPane scrollPane1;
	public static JButton btnAdd;
	public static JButton btnClear;
	public static JLabel lblPurchaseId;
	public static JLabel lblProductName;
	public static JLabel lblQuantity;
	public static JLabel lblSearchProduct;
	public static JLabel lblTotal;
	public static JLabel lblTotalValue;
	public static JPanel panel;
	public static JLabel lblWishlist;
	public static JLabel lblCart;
	public static double totalDiscount;
	public static double grandTotal;
	public static int discount;
	public static int proId;
	int xx, xy;
	ProductDao product = new ProductDao();
	PurchaseDao purchase = new PurchaseDao();
	public double total = purchase.getTotalAmount(purchase.getId(UserDashboard.lblUseremail.getText()));
	DefaultTableModel model;
	private int quantity = 0;
	public static int existQuantity = 0;
	private double price = 0.0;
	public static int pId;
	int rowIndex;
	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
	Date date = (Date) new java.util.Date();
	Statistics statistics = new Statistics();
	UserDao user = new UserDao();
	OrderDao orders = new OrderDao();
	public static int uId;
	private JButton btnRemove;

	/**
	 * Create the panel.
	 */
	public WishList()
	{
		pId = purchase.getMaxRow();
		uId = purchase.getId(UserDashboard.lblUseremail.getText());
		initComponent();
		init();
	}
	
	@SuppressWarnings("static-access")
	private void init() {
		discount = product.getDiscount();
		IDField.setText(String.valueOf(purchase.getMaxRow()));
		totalDiscount = (total * (double) discount) / 100;
		grandTotal = total - totalDiscount;
		lblTotalValue.setText(String.format("%.2f", grandTotal));
		btnRemove.setEnabled(false);
		wishListTable();
		cartTable();
		
		quantityField.addKeyListener(new KeyAdapter() {
	    	@Override
	    	public void keyTyped(KeyEvent e) {
	    		if (!Character.isDigit(e.getKeyChar())) {
	    			e.consume();
	    		}
	    	}
	    });
		
	    searchField.addKeyListener(new KeyAdapter() {
	    	@Override
	    	public void keyReleased(KeyEvent e) {
	    		table.setModel(new DefaultTableModel(null, new Object[] {"Product ID", "Product Name", "Category", "Price"}));
	    		DefaultTableCellRenderer centerRenderer2 = new DefaultTableCellRenderer();
	    		centerRenderer2.setHorizontalAlignment(SwingConstants.CENTER);
	    		
	    		for (int i = 0; i < table.getColumnCount(); i++) {
	    			table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer2);
	    		}
	    		product.getProductData(table, searchField.getText());
	    	}
	    });
	    
	    searchField.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 14));
	    
	    btnAdd.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		if (nameField.getText().isEmpty()) {
	    			JOptionPane.showMessageDialog(WishList.this, "Please select a product", "Warning", 2);
	    		}if (quantityField.getText().isEmpty())
	    		{
	    			JOptionPane.showMessageDialog(WishList.this, "Product quantity is required", "Warning", 2);
	    		}
	    		else {
	    			int[] t1selectedRows = table.getSelectedRows();
		    		int t1rowCount = t1selectedRows.length;
		    		
		    		int[] t2selectedRows = table_1.getSelectedRows();
		    		int t2rowCount = t2selectedRows.length;
		    		
		    		if (t1rowCount > 0) {
		    			model = (DefaultTableModel) table.getModel();
		    			int proId = Integer.parseInt(model.getValueAt(rowIndex, 0).toString());
		    			if (!product.isproductExists(proId, uId)) {
		    				if (!(quantity <= 0)) {
		    					int newQuantity = Integer.parseInt(quantityField.getText());
		    					if (newQuantity != 0) {
		    						if (!(newQuantity > quantity)) {
		    							String pname = nameField.getText();
		    							double t = Double.parseDouble(String.format("%.2f", price * (double) newQuantity));
		    							double productTotal = price * (double) newQuantity;
		    				    		double productdiscount = (productTotal * (double) Purchase.discount) / 100;
		    				    		double productGrandTotal = productTotal - productdiscount;
		    				    		product.insertIntoCart(uId, proId, pname, newQuantity, price, t, productdiscount, productGrandTotal);
		    							table_1.setModel(new DefaultTableModel(null, new Object[] {"Purchase ID", "Product ID", "Product Name", "Quantity", "Price", "Total"}));
		    							DefaultTableCellRenderer centerRenderer2 = new DefaultTableCellRenderer();
		    				    		centerRenderer2.setHorizontalAlignment(SwingConstants.CENTER);
		    				    		
		    				    		for (int i = 0; i < table_1.getColumnCount(); i++) {
		    				    			table_1.getColumnModel().getColumn(i).setCellRenderer(centerRenderer2);
		    				    		}
		    							total += price * (double) newQuantity;
		    							totalDiscount = (total * (double) discount) / 100;
		    							grandTotal = total - totalDiscount;
		    							lblTotalValue.setText(String.format("%.2f", grandTotal));
		    							product.getCartData(table_1, uId, "");
		    							clear();
		    						}else {
		    							JOptionPane.showMessageDialog(WishList.this, "Not enough stock", "Warning", 2);
		    						}								
		    					}else {
		    						JOptionPane.showMessageDialog(WishList.this, "Please increase the product quantity", "Warning", 2);
		    					}
		    				}else {
		    					JOptionPane.showMessageDialog(WishList.this, "Stock is empty", "Warning", 2);
		    				}
		    			}else {
		    				if (!(quantity <= 0)) {
		    					int newQuantity = Integer.parseInt(quantityField.getText());
		    					if (newQuantity != 0) {
		    						if (!(newQuantity > quantity)) {
		    							table_1.setModel(new DefaultTableModel(null, new Object[] {"Purchase ID", "Product ID", "Product Name", "Quantity", "Price", "Total"}));
		    							DefaultTableCellRenderer centerRenderer2 = new DefaultTableCellRenderer();
		    				    		centerRenderer2.setHorizontalAlignment(SwingConstants.CENTER);
		    				    		
		    				    		for (int i = 0; i < table_1.getColumnCount(); i++) {
		    				    			table_1.getColumnModel().getColumn(i).setCellRenderer(centerRenderer2);
		    				    		}
		    				    		existQuantity = product.getQuantity(uId, WishList.proId);
		    				    		double productTotal = price * (double) (newQuantity + existQuantity);
		    				    		double productdiscount = (productTotal * (double) discount) / 100;
		    				    		double productGrandTotal = productTotal - productdiscount;
		    				    		
		    				    		total += price * (double) newQuantity;
		    				    		totalDiscount = (total * (double) discount) / 100;
		    							grandTotal = total - totalDiscount;
		    							lblTotalValue.setText(String.format("%.2f", grandTotal));
		    							product.updateCartProduct(proId, uId, newQuantity, productdiscount, productGrandTotal);
		    							product.getCartData(table_1, uId, "");
		    							clear();
		    						}else {
		    							JOptionPane.showMessageDialog(WishList.this, "Not enough stock", "Warning", 2);
		    						}
		    					}else {
		    						JOptionPane.showMessageDialog(WishList.this, "Please increase the product quantity", "Warning", 2);
		    					}
		    				}else {
		    					JOptionPane.showMessageDialog(WishList.this, "Stock is empty", "Warning", 2);
		    				}
						}
		    		}
	    			else if (t2rowCount > 0){
	    				model = (DefaultTableModel) table_1.getModel();
		    			int proId = Integer.parseInt(model.getValueAt(rowIndex, 1).toString());
	    				if (!(quantity <= 0)) {
	    					int newQuantity = Integer.parseInt(quantityField.getText());
	    					if (newQuantity != 0) {
	    						if (!(newQuantity > quantity)) {
	    							table_1.setModel(new DefaultTableModel(null, new Object[] {"Purchase ID", "Product ID", "Product Name", "Quantity", "Price", "Total"}));
	    							DefaultTableCellRenderer centerRenderer2 = new DefaultTableCellRenderer();
	    				    		centerRenderer2.setHorizontalAlignment(SwingConstants.CENTER);
	    				    		
	    				    		for (int i = 0; i < table_1.getColumnCount(); i++) {
	    				    			table_1.getColumnModel().getColumn(i).setCellRenderer(centerRenderer2);
	    				    		}
	    				    		double productTotal = price * (double) (newQuantity + existQuantity);
	    				    		double productdiscount = (productTotal * (double) discount) / 100;
	    				    		double productGrandTotal = productTotal - productdiscount;
	    				    		
	    							
	    							total += price * (double) newQuantity;
	    							totalDiscount = (total * (double) discount) / 100;
	    							grandTotal = total - totalDiscount;
	    							lblTotalValue.setText(String.format("%.2f", grandTotal));
	    							product.updateCartProduct(proId, uId, newQuantity, productdiscount, productGrandTotal);
	    							product.getCartData(table_1, uId, "");
	    							clear();
	    						}else {
	    							JOptionPane.showMessageDialog(WishList.this, "Not enough stock", "Warning", 2);
	    						}
	    					}else {
	    						JOptionPane.showMessageDialog(WishList.this, "Please increase the product quantity", "Warning", 2);
	    					}
	    				}else {
	    					JOptionPane.showMessageDialog(WishList.this, "Stock is empty", "Warning", 2);
	    				}
	    			}
	    			else {
	    				JOptionPane.showMessageDialog(WishList.this, "Please select wishlist or cart product", "Warning", 2);
					}
	    		}
	    	}
	    });
	    
	    btnClear.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent arg0) {
	    		clear();
	    	}
	    });
	    
	    btnRemove.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		int[] t1selectedRows = table.getSelectedRows();
	    		int t1rowCount = t1selectedRows.length;
	    		
	    		int[] t2selectedRows = table_1.getSelectedRows();
	    		int t2rowCount = t2selectedRows.length;
	    		
	    		if (t1rowCount > 0) {
	    			if (nameField.getText().isEmpty()) {
		    			JOptionPane.showMessageDialog(WishList.this, "Please select a product", "Warning", 2);
		    		}else {
		    			model = (DefaultTableModel) table.getModel();
		    			int proId = Integer.parseInt(model.getValueAt(rowIndex, 0).toString());
		    			product.deleteSpecificWishlistItem(uId, proId);
		    			total = purchase.getTotalAmount(purchase.getId(UserDashboard.lblUseremail.getText()));
		    			totalDiscount = (total * (double) discount) / 100;
						grandTotal = total - totalDiscount;
						
		    			lblTotalValue.setText(String.format("%.2f", grandTotal));
		    			table.setModel(new DefaultTableModel(null, new Object[] {"Product ID", "Product Name", "Category", "Price"}));
		    			DefaultTableCellRenderer centerRenderer2 = new DefaultTableCellRenderer();
			    		centerRenderer2.setHorizontalAlignment(SwingConstants.CENTER);
			    		
			    		for (int i = 0; i < table.getColumnCount(); i++) {
			    			table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer2);
			    		}
		    			product.getWishListData(table, uId, "");
						clear();
		    		}
	    		}
	    		else if (t2rowCount > 0) {
            		if (nameField.getText().isEmpty()) {
    	    			JOptionPane.showMessageDialog(WishList.this, "Please select a product", "Warning", 2);
    	    		}else {
    	    			model = (DefaultTableModel) table_1.getModel();
    	    			int proId = Integer.parseInt(model.getValueAt(rowIndex, 1).toString());
    	    			product.deleteSpecificCartItem(uId, proId);
    	    			total = purchase.getTotalAmount(purchase.getId(UserDashboard.lblUseremail.getText()));
    	    			lblTotalValue.setText(String.format("%.2f", total));
    	    			table_1.setModel(new DefaultTableModel(null, new Object[] {"Purchase ID", "Product ID", "Product Name", "Quantity", "Price", "Total"}));
    	    			DefaultTableCellRenderer centerRenderer2 = new DefaultTableCellRenderer();
    		    		centerRenderer2.setHorizontalAlignment(SwingConstants.CENTER);
    		    		
    		    		for (int i = 0; i < table_1.getColumnCount(); i++) {
    		    			table_1.getColumnModel().getColumn(i).setCellRenderer(centerRenderer2);
    		    		}
    	    			product.getCartData(table_1, uId, "");
    					clear();
    	    		}
            	}
	    		else {
	    			JOptionPane.showMessageDialog(WishList.this, "Please select wishlist or cart product", "Warning", 2);
				}
	    	}
	    });
	    
	    
	    table.addMouseListener(new MouseAdapter() {
	    	@Override
	    	public void mouseClicked(MouseEvent e) {
	    		btnRemove.setEnabled(true);
	    		table_1.clearSelection();
	    		model = (DefaultTableModel) table.getModel();
				rowIndex = table.getSelectedRow();
				nameField.setText(model.getValueAt(rowIndex, 1).toString());
				String s1 = model.getValueAt(rowIndex, 3).toString();
				String s2 = model.getValueAt(rowIndex, 4).toString();
				quantity = Integer.parseInt(s1);
				price = Double.parseDouble(s2);
				proId = Integer.parseInt(model.getValueAt(rowIndex, 0).toString());
	    	}
	    });
	    
	    table_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				btnRemove.setEnabled(true);
				table.clearSelection();
				model = (DefaultTableModel) table_1.getModel();
				rowIndex = table_1.getSelectedRow();
				nameField.setText(model.getValueAt(rowIndex, 2).toString());
				String s1 = model.getValueAt(rowIndex, 1).toString();
				String s2 = model.getValueAt(rowIndex, 4).toString();
				quantity = product.getProductQuantity(Integer.parseInt(s1));
				price = Double.parseDouble(s2);
				existQuantity = Integer.parseInt(model.getValueAt(rowIndex, 3).toString());
			}
		});
	    
	}
	
	private void wishListTable() {
		product.getWishListData(table, uId, "");
		model = (DefaultTableModel) table.getModel();
		table.setRowHeight(30);
		table.setShowGrid(true);
		table.setGridColor(Color.black);
		table.setBackground(Color.white);
		table.setSelectionBackground(Color.LIGHT_GRAY);
	}
	
	private void cartTable() {
		product.getCartData(table_1, uId, "");
		model = (DefaultTableModel) table_1.getModel();
		table_1.setRowHeight(30);
		table_1.setShowGrid(true);
		table_1.setGridColor(Color.black);
		table_1.setBackground(Color.white);
		table_1.setSelectionBackground(Color.LIGHT_GRAY);
	}
	
	private void clear() {
		IDField.setText(String.valueOf(purchase.getMaxRow()));
		nameField.setText("");
		quantityField.setText("0");
		searchField.setText("");
		table.clearSelection();
		price = 0.0;
		quantity = 0;
	}
	
	@SuppressWarnings("serial")
	private void initComponent() {
		setLayout(null);
		
		panel = new JPanel();
		panel.setBounds(0, 0, 995, 610);
		add(panel);
		panel.setLayout(null);
		
		lblPurchaseId = new JLabel("Purchase ID");
		lblPurchaseId.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 14));
		lblPurchaseId.setBounds(94, 128, 103, 15);
		panel.add(lblPurchaseId);
		
		IDField = new JTextField();
		IDField.setEditable(false);
		IDField.setText("0");
		IDField.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 14));
		IDField.setColumns(10);
		IDField.setBackground(new Color(171, 167, 167));
		IDField.setBounds(94, 150, 259, 30);
		panel.add(IDField);
		
		lblProductName = new JLabel("Product Name");
		lblProductName.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 14));
		lblProductName.setBounds(94, 190, 103, 15);
		panel.add(lblProductName);
		
		nameField = new JTextField();
		nameField.setEditable(false);
		nameField.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 14));
		nameField.setColumns(10);
		nameField.setBackground(new Color(171, 167, 167));
		nameField.setBounds(94, 210, 259, 30);
		panel.add(nameField);
		
		lblQuantity = new JLabel("Quantity");
		lblQuantity.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 14));
		lblQuantity.setBounds(94, 250, 70, 15);
		panel.add(lblQuantity);
		
		quantityField = new JTextField();
		quantityField.setText("0");
		quantityField.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 14));
		quantityField.setColumns(10);
		quantityField.setBounds(94, 272, 259, 30);
		panel.add(quantityField);
		
		lblSearchProduct = new JLabel("Search Product");
		lblSearchProduct.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 14));
		lblSearchProduct.setBounds(553, 65, 126, 15);
		panel.add(lblSearchProduct);
		
		searchField = new JTextField();
		searchField.setColumns(10);
		searchField.setBounds(684, 59, 275, 30);
		panel.add(searchField);
		
		lblTotal = new JLabel("Total : ");
		lblTotal.setHorizontalAlignment(SwingConstants.CENTER);
		lblTotal.setFont(new Font("DejaVu Sans Condensed", Font.BOLD, 16));
		lblTotal.setBounds(666, 355, 56, 15);
		panel.add(lblTotal);
		
		table_1 = new JTable();
		table_1.setBackground(new Color(222, 221, 218));
		table_1.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 14));
		table_1.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Purchase ID", "Product ID", "Product Name", "Quantity", "Price", "Total"
			}
		) {
			boolean[] columnEditables = new boolean[] {
				false, false, false, false, false, false
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        
        for (int i = 0; i < table_1.getColumnCount(); i++) {
            table_1.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
		table_1.getTableHeader().setReorderingAllowed(false);
		scrollPane1 = new JScrollPane(table_1);
		scrollPane1.setBounds(399, 382, 560, 216);
	    panel.add(scrollPane1);
		
		btnAdd = new JButton("Add");
		btnAdd.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		btnAdd.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnAdd.setIcon(new ImageIcon(getClass().getResource("/com/logilite/img/save.png")));
		btnAdd.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 14));
		btnAdd.setBounds(94, 387, 111, 37);
		btnAdd.setFocusPainted(false);
		panel.add(btnAdd);
		
		btnClear = new JButton("Clear");
		btnClear.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		btnClear.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnClear.setIcon(new ImageIcon(getClass().getResource("/com/logilite/img/clear.png")));
		btnClear.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 14));
		btnClear.setBounds(217, 387, 136, 37);
		btnClear.setFocusPainted(false);
		panel.add(btnClear);
		
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
		    		false, false, false, false, true
		    	};
		    	public boolean isCellEditable(int row, int column) {
		    		return columnEditables[column];
		    	}
		    });
	    DefaultTableCellRenderer centerRenderer2 = new DefaultTableCellRenderer();
        centerRenderer2.setHorizontalAlignment(SwingConstants.CENTER);
        
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer2);
        }
	    table.getTableHeader().setReorderingAllowed(false);
	    
	    scrollPane = new JScrollPane(table);
	    scrollPane.setBounds(399, 113, 560, 216);
	    panel.add(scrollPane);
	    
	    lblTotalValue = new JLabel("");
	    lblTotalValue.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 14));
	    lblTotalValue.setBounds(721, 356, 140, 15);
	    panel.add(lblTotalValue);
	    
	    lblWishlist = new JLabel("Wishlist :");
	    lblWishlist.setFont(new Font("DejaVu Sans Condensed", Font.BOLD, 19));
	    lblWishlist.setBounds(399, 82, 103, 19);
	    panel.add(lblWishlist);
	    
	    lblCart = new JLabel("Cart :");
	    lblCart.setFont(new Font("DejaVu Sans Condensed", Font.BOLD, 19));
	    lblCart.setBounds(399, 356, 70, 15);
	    panel.add(lblCart);
	    
	    btnRemove = new JButton("Remove");
	    btnRemove.setIcon(new ImageIcon(WishList.class.getResource("/com/logilite/img/remove.png")));
	    btnRemove.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 14));
	    btnRemove.setBounds(154, 445, 117, 37);
	    panel.add(btnRemove);
	}
}
