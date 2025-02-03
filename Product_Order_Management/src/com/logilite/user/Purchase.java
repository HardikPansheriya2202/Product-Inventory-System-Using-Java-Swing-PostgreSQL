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
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.DefaultComboBoxModel;
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
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.border.BevelBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import org.json.JSONObject;

import com.logilite.dao.OrderDao;
import com.logilite.dao.ProductDao;
import com.logilite.dao.PurchaseDao;
import com.logilite.dao.Statistics;
import com.logilite.dao.UserDao;
import com.logilite.email.Email;
import com.logilite.stripe.StripeCheckout;
import com.logilite.stripe.StripeCheckoutBackend;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;

public class Purchase extends JPanel
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
	public static JButton btnPurchase;
	public static JButton btnClear;
	public static JLabel lblPurchaseId;
	public static JLabel lblProductName;
	public static JLabel lblQuantity;
	public static JLabel lblSearchProduct;
	public static JLabel lblTotal;
	public static JLabel lblTotalValue;
	public static JPanel panel;
	public static JButton btnAddWishlist;
	public static JLabel lblCart;
	@SuppressWarnings("rawtypes")
	public static JComboBox comboBox;
	int xx, xy;
	ProductDao product = new ProductDao();
	PurchaseDao purchase = new PurchaseDao();
	DefaultTableModel model;
	public static int quantity = 0;
	public static int existQuantity = 0;
	public static double price = 0.0;
	public double total = purchase.getTotalAmount(purchase.getId(UserDashboard.lblUseremail.getText()));
	public static int pId;
	public static int proId;
	public static int uId;
	public static int discount;
	public static double totalDiscount;
	public static double grandTotal;
	int rowIndex;
	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
	Date date = (Date) new java.util.Date();
	Statistics statistics = new Statistics();
	UserDao user = new UserDao();
	OrderDao orders = new OrderDao();
	String userHome = System.getProperty("user.home");
    String downloadsFolder = userHome + "/Downloads";
    String paymentStatusFilePath = downloadsFolder + "/paymentStatus.json";
    Path path = Paths.get(paymentStatusFilePath);
    WishList wishList = new WishList();
    public static JButton btnRemove;
    public static JLabel lblDiscountValue;
    public static JLabel lblTotalAmount;
    public static JTextField txtTotalAmount;
    public static JLabel lblTotalDiscount;
    public static JTextField txtDiscountAmount;
    public static JLabel lblGrandTotal;
    public static JTextField txtGrandTotal;
    

	@SuppressWarnings("static-access")
	public Purchase()
	{
		uId = purchase.getId(UserDashboard.lblUseremail.getText());
		pId = purchase.getMaxRow();
		discount = product.getDiscount();
		initComponent();
		init();
	}
	
	@SuppressWarnings("static-access")
	private void init() {
		btnRemove.setEnabled(false);
		IDField.setText(String.valueOf(purchase.getMaxRow()));
		txtTotalAmount.setText(String.format("%.2f", total));
		totalDiscount = (total * (double) discount) / 100;
		txtDiscountAmount.setText(String.valueOf(totalDiscount));
		grandTotal = total - totalDiscount;
		txtGrandTotal.setText(String.format("%.2f", grandTotal));
		
		lblTotalValue.setText(String.format("%.2f", grandTotal));
		btnPurchase.setText("Pay  ₹ "+ String.valueOf(grandTotal));
		lblDiscountValue.setText(String.valueOf(discount));
		
		product.updatecartUsingDiscount(discount);
		productsTable();
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
	    
	    btnPurchase.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent arg0) {
	    		if (comboBox.getSelectedItem().toString().equals("Using Stripe")) {
	    			model = (DefaultTableModel) table_1.getModel();
	    			if (model.getRowCount() > 0) {
	    				String[] data = new String[5];
	    				String email = UserDashboard.lblUseremail.getText();
	    				data = purchase.getUserValue(email);
	    				int uid = Integer.parseInt(data[0]);
	    				String uname = data[1];
	    				String uphone = data[2];
	    				String address = data[3] + ", " + data[4];
	    				String orderDate = df.format(date);
	    				
	    				JOptionPane.showMessageDialog(Purchase.this, "Successfully purchased");
		    			try {
		    				new StripeCheckout().start();
		    				
				    			new SwingWorker<Void, Void>() {
				    				
			    					@Override
			    					protected Void doInBackground() throws Exception {
			    						String userHome = System.getProperty("user.home");

			    				        String downloadsFolder;
			    				        if (System.getProperty("os.name").toLowerCase().contains("win")) {
			    				            downloadsFolder = userHome + "\\Downloads";
			    				        } else {
			    				            downloadsFolder = userHome + "/Downloads";
			    				        }

			    				        String paymentStatusFilePath = downloadsFolder + File.separator + "paymentStatus.json";
			    				        Path path = Paths.get(paymentStatusFilePath);
			    				        
			    						while (!Files.exists(path)) {
			    							try {
			    								Thread.sleep(1000);
			    							} catch (InterruptedException e) {
			    								e.printStackTrace();
			    							}
			    						}
			    						
			    						if (StripeCheckoutBackend.payment_Status.equalsIgnoreCase("succeeded")) {
		    								for (int i = 0; i < model.getRowCount(); i++) {
		    									int pid = Integer.parseInt(model.getValueAt(i, 1).toString());
		    									String pname = model.getValueAt(i, 2).toString();
		    									int quantity = Integer.parseInt(model.getValueAt(i, 3).toString());
		    									double price = Double.parseDouble(model.getValueAt(i, 4).toString());
		    									double total = Double.parseDouble(model.getValueAt(i, 5).toString());
		    									orders.insertOrdersDetails(StripeCheckoutBackend.payment_id, uid, pid, pname, quantity, price, total);
		    									
		    									int newQuantity = purchase.getQty(pid) - quantity;
		    									purchase.qtyUpdate(pid, newQuantity);
		    								}
		    								
		    								orders.insertOrdersMaster(StripeCheckoutBackend.payment_id, uid, uname, uphone, address,
		    										Double.parseDouble(lblTotalValue.getText()), orderDate);
		    								
		    								orders.insertStripePurchaseDetails(StripeCheckoutBackend.payment_id, uid, uname, uphone, Double.parseDouble(lblTotalValue.getText()), orderDate, "", "", "Pending");
		    								try
			    							{
			    								File reportImage = generateInvoiceReport(StripeCheckoutBackend.payment_id);
			    								Email.send(UserDashboard.lblUseremail.getText(), "Invoice Order", "Please Find Your Invoice Below :", reportImage);
				    							pId++;
			    								reportImage.delete();
			    								product.deleteCart(uId);
			    							}
			    							catch (Exception e)
			    							{
			    								e.printStackTrace();
			    								JOptionPane.showMessageDialog(Purchase.this, "Failed to send email: " + e.getMessage());
			    							}
			    							table_1.setModel(new DefaultTableModel(null, new Object[] {"Purchase ID", "Product ID", "Product Name", "Quantity", "Price", "Total"}));
			    							table.setModel(new DefaultTableModel(null, new Object[] {"Product ID", "Product Name", "Category", "Price"}));
			    				    		DefaultTableCellRenderer centerRenderer2 = new DefaultTableCellRenderer();
			    				    		centerRenderer2.setHorizontalAlignment(SwingConstants.CENTER);
			    				    		
			    				    		for (int i = 0; i < table.getColumnCount(); i++) {
			    				    			table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer2);
			    				    		}
			    				    		
			    				    		product.getProductData(table, "");
			    							product.getCartData(table_1, uId, "");
			    							Files.delete(path);
		    							}
										return null;
			    						
			    					}
			    					@Override
			    					protected void done() {
			    						statistics.user(user.getUserId(email));
			    						IDField.setText(String.valueOf(purchase.getMaxRow()));
			    						lblTotalValue.setText("0.0");
		    				    		txtTotalAmount.setText("0.0");
		    				    		txtDiscountAmount.setText("0.0");
		    				    		txtGrandTotal.setText("0.0");
		    				    		btnPurchase.setText("Pay  ₹ 0");
			    						JOptionPane.showMessageDialog(Purchase.this, "Purchase completed successfully!");
			    					}
			    				}.execute();
		    			} catch (IOException e) {
		    				JOptionPane.showMessageDialog(Purchase.this,e.getMessage());
		    			}				
	    			}else {
	    				JOptionPane.showMessageDialog(Purchase.this, "You havn't purchased any product", "Warning", 2);
	    			}
	    		}else {
	    			model = (DefaultTableModel) table_1.getModel();
		    		if (model.getRowCount() > 0) {
		    			String[] data = new String[5];
		    			String email = UserDashboard.lblUseremail.getText();
		    			data = purchase.getUserValue(email);
		    			int uid = Integer.parseInt(data[0]);
		    			String uname = data[1];
		    			String uphone = data[2];
		    			String address = data[3] + ", " + data[4];
		    			String orderDate = df.format(date);
		    			
		    			JOptionPane.showMessageDialog(Purchase.this, "Successfully purchased");
		    			
		    			try {
		    				String apiKey = "rzp_test_HxKUwQGInE2deL";
		    				String apiSecret = "2QtlOquq9XbMD5y8ot8x6y8n";
		    				
		    				RazorpayClient razorpay = new RazorpayClient(apiKey, apiSecret);
		    				double totalAmount = Double.parseDouble(lblTotalValue.getText()) * 100;
		    				
		    				JSONObject orderRequest = new JSONObject();
		    				orderRequest.put("amount", (int) totalAmount);
		    				orderRequest.put("currency", "INR");
		    				orderRequest.put("receipt", "txn_" + System.currentTimeMillis());
		    				
		    				Order order = razorpay.orders.create(orderRequest);
		    				String orderId = order.get("id");
		    				
		    				JOptionPane.showMessageDialog(Purchase.this,
		    						"Order Created Successfully. Order ID: " + orderId + "\nRedirecting to payment...");
		    				
		    				String htmlPage = com.logilite.razorpay.RazorpayClient.generateHtmlForPayment(totalAmount, orderId);
		    				
		    				File tempFile = File.createTempFile("razorpay_checkout_" + System.currentTimeMillis(), ".html");
		    				tempFile.deleteOnExit();
		    				try (FileWriter writer = new FileWriter(tempFile)) {
		    					writer.write(htmlPage);
		    				}
		    				
		    				String wrapperHtml = com.logilite.razorpay.RazorpayClient.generateWrapperHtml(tempFile);
		    				
		    				File wrapperFile = File.createTempFile("razorpay_wrapper_" + System.currentTimeMillis(), ".html");
		    				wrapperFile.deleteOnExit();
		    				
		    				try (FileWriter writer = new FileWriter(wrapperFile)) {
		    					writer.write(wrapperHtml);
		    				}
		    				
		    				String wrapperFilePath = wrapperFile.getAbsolutePath();
		    				SwingUtilities.invokeLater(() -> {
		    				    try {
		    				        File htmlFile = new File(wrapperFilePath);
		    				        String filePath = htmlFile.getAbsolutePath();

		    				        if (System.getProperty("os.name").toLowerCase().contains("win")) {
		    				            new ProcessBuilder("cmd", "/c", "start", "", filePath)
		    				                .redirectOutput(ProcessBuilder.Redirect.DISCARD)
		    				                .redirectError(ProcessBuilder.Redirect.DISCARD)
		    				                .start();
		    				        } else if (System.getProperty("os.name").toLowerCase().contains("mac")) {
		    				            new ProcessBuilder("open", filePath)
		    				                .redirectOutput(ProcessBuilder.Redirect.DISCARD)
		    				                .redirectError(ProcessBuilder.Redirect.DISCARD)
		    				                .start();
		    				        } else {
		    				            new ProcessBuilder("xdg-open", filePath)
		    				                .redirectOutput(ProcessBuilder.Redirect.DISCARD)
		    				                .redirectError(ProcessBuilder.Redirect.DISCARD)
		    				                .start();
		    				        }
		    				    } catch (IOException e) {
		    				        JOptionPane.showMessageDialog(Purchase.this,
		    				                "Failed to open the payment page: " + e.getMessage(),
		    				                "Error",
		    				                JOptionPane.ERROR_MESSAGE);
		    				    }
		    				});

		    				
		    				new SwingWorker<Void, Void>() {
		    					@Override
		    					protected Void doInBackground() throws Exception {
		    						String userHome = System.getProperty("user.home");

		    				        String downloadsFolder;
		    				        if (System.getProperty("os.name").toLowerCase().contains("win")) {
		    				            downloadsFolder = userHome + "\\Downloads";
		    				            } else {
		    				            downloadsFolder = userHome + "/Downloads";
		    				            }

		    				        String paymentStatusFilePath = downloadsFolder + File.separator + "paymentStatus.json";
		    				        Path path = Paths.get(paymentStatusFilePath);
		    						
		    						while (!Files.exists(path)) {
		    							try {
		    								Thread.sleep(1000);
		    							} catch (InterruptedException e) {
		    								e.printStackTrace();
		    							}
		    						}
		    						
		    						if (Files.exists(path)) {
		    							String fileContent = new String(Files.readAllBytes(path));
		    							JSONObject paymentData = new JSONObject(fileContent);
		    							String paymentStatus = paymentData.getString("status");
		    							
		    							if ("success".equals(paymentStatus)) {
		    								for (int i = 0; i < model.getRowCount(); i++) {
		    									int pid = Integer.parseInt(model.getValueAt(i, 1).toString());
		    									String pname = model.getValueAt(i, 2).toString();
		    									int quantity = Integer.parseInt(model.getValueAt(i, 3).toString());
		    									double price = Double.parseDouble(model.getValueAt(i, 4).toString());
		    									double total = Double.parseDouble(model.getValueAt(i, 5).toString());
		    									orders.insertOrdersDetails(orderId, uid, pid, pname, quantity, price, total);
		    									
		    									int newQuantity = purchase.getQty(pid) - quantity;
		    									purchase.qtyUpdate(pid, newQuantity);
		    								}
		    								
		    								orders.insertOrdersMaster(orderId, uid, uname, uphone, address,
		    										Double.parseDouble(lblTotalValue.getText()), orderDate);
		    							}
		    							orders.insertPurchaseDetails(orderId, uid, uname, uphone, Double.parseDouble(lblTotalValue.getText()), orderDate, "", "", "Pending");
		    							try
		    							{
		    								product.deleteCart(uId);
		    								File reportImage = generateInvoiceReport(orderId);
		    								Email.send(UserDashboard.lblUseremail.getText(), "Invoice Order", "Please Find Your Invoice Below :", reportImage);
			    							pId++;
		    								reportImage.delete();
		    							}
		    							catch (Exception e)
		    							{
		    								e.printStackTrace();
		    								JOptionPane.showMessageDialog(Purchase.this, "Failed to send email: " + e.getMessage());
		    							}
		    							table_1.setModel(new DefaultTableModel(null, new Object[] {"Purchase ID", "Product ID", "Product Name", "Quantity", "Price", "Total"}));
		    							table.setModel(new DefaultTableModel(null, new Object[] {"Product ID", "Product Name", "Category", "Price"}));
		    				    		DefaultTableCellRenderer centerRenderer2 = new DefaultTableCellRenderer();
		    				    		centerRenderer2.setHorizontalAlignment(SwingConstants.CENTER);
		    				    		
		    				    		for (int i = 0; i < table.getColumnCount(); i++) {
		    				    			table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer2);
		    				    		}
		    				    		
		    				    		product.getProductData(table, "");
		    							product.getCartData(table_1, uId, "");
		    							Files.delete(path);
		    							
		    						} else {
		    							Logger.getLogger(OrderDao.class.getName()).log(Level.SEVERE, "paymentStatus.json file not found");
		    						}
		    						return null;
		    					}
		    					
		    					@Override
		    					protected void done() {
		    						statistics.user(user.getUserId(email));
		    						IDField.setText(String.valueOf(purchase.getMaxRow()));
		    						lblTotalValue.setText("0.0");
	    				    		txtTotalAmount.setText("0.0");
	    				    		txtDiscountAmount.setText("0.0");
	    				    		txtGrandTotal.setText("0.0");
	    				    		btnPurchase.setText("Pay  ₹ 0");
		    						JOptionPane.showMessageDialog(Purchase.this, "Purchase completed successfully!");
		    					}
		    				}.execute();
		    				
		    			} catch (Exception e) {
		    				e.printStackTrace();
		    				JOptionPane.showMessageDialog(Purchase.this, "Failed to process payment: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		    			}
		    		}else {
		    			JOptionPane.showMessageDialog(Purchase.this, "You havn't purchased any product", "Warning", 2);
		    		}

				}
	    		
	    }
	    });
	    
	    btnAdd.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    		if (nameField.getText().isEmpty()) {
	    			JOptionPane.showMessageDialog(Purchase.this, "Please select a product", "Warning", 2);
	    		}if (quantityField.getText().isEmpty())
	    		{
	    			JOptionPane.showMessageDialog(Purchase.this, "Product quantity is required", "Warning", 2);
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
		    							table_1.setModel(new DefaultTableModel(null, new Object[] {"Purchase ID", "Product ID", "Product Name", "Quantity", "Price", "Total"}));
		    							DefaultTableCellRenderer centerRenderer2 = new DefaultTableCellRenderer();
		    				    		centerRenderer2.setHorizontalAlignment(SwingConstants.CENTER);
		    				    		
		    				    		for (int i = 0; i < table_1.getColumnCount(); i++) {
		    				    			table_1.getColumnModel().getColumn(i).setCellRenderer(centerRenderer2);
		    				    		}
		    				    		double productTotal = price * (double) newQuantity;
		    				    		double productdiscount = (productTotal * (double) discount) / 100;
		    				    		double productGrandTotal = productTotal - productdiscount;
		    				    		
		    							total += price * (double) newQuantity;
		    							txtTotalAmount.setText(String.format("%.2f", total));
		    							totalDiscount = (total * (double) discount) / 100;
		    							txtDiscountAmount.setText(String.valueOf(totalDiscount));
		    							grandTotal = total - totalDiscount;
		    							txtGrandTotal.setText(String.format("%.2f", grandTotal));
		    							
		    							lblTotalValue.setText(String.format("%.2f", grandTotal));
		    							btnPurchase.setText("Pay  ₹ "+ String.valueOf(grandTotal));
		    							product.insertIntoCart(uId, proId, pname, newQuantity, price, t, productdiscount, productGrandTotal);
		    							product.getCartData(table_1, uId, "");
		    							clear();
		    						}else {
		    							JOptionPane.showMessageDialog(Purchase.this, "Not enough stock", "Warning", 2);
		    						}								
		    					}else {
		    						JOptionPane.showMessageDialog(Purchase.this, "Please increase the product quantity", "Warning", 2);
		    					}
		    				}else {
		    					JOptionPane.showMessageDialog(Purchase.this, "Stock is empty", "Warning", 2);
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
		    				    		existQuantity = product.getQuantity(uId, Purchase.proId);
		    				    		double productTotal = price * (double) (newQuantity + existQuantity);
		    				    		double productdiscount = (productTotal * (double) discount) / 100;
		    				    		double productGrandTotal = productTotal - productdiscount;
		    				    		
		    				    		total += price * (double) newQuantity;
		    				    		txtTotalAmount.setText(String.format("%.2f", total));
		    							totalDiscount = (total * (double) discount) / 100;
		    							txtDiscountAmount.setText(String.valueOf(totalDiscount));
		    							grandTotal = total - totalDiscount;
		    							txtGrandTotal.setText(String.format("%.2f", grandTotal));
		    							
		    							lblTotalValue.setText(String.format("%.2f", grandTotal));
		    							btnPurchase.setText("Pay  ₹ "+ String.valueOf(grandTotal));
		    							product.updateCartProduct(proId, uId, newQuantity, productdiscount, productGrandTotal);
		    							product.getCartData(table_1, uId, "");
		    							clear();
		    						}else {
		    							JOptionPane.showMessageDialog(Purchase.this, "Not enough stock", "Warning", 2);
		    						}
		    					}else {
		    						JOptionPane.showMessageDialog(Purchase.this, "Please increase the product quantity", "Warning", 2);
		    					}
		    				}else {
		    					JOptionPane.showMessageDialog(Purchase.this, "Stock is empty", "Warning", 2);
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
	    							txtTotalAmount.setText(String.format("%.2f", total));
	    							totalDiscount = (total * (double) discount) / 100;
	    							txtDiscountAmount.setText(String.valueOf(totalDiscount));
	    							grandTotal = total - totalDiscount;
	    							txtGrandTotal.setText(String.format("%.2f", grandTotal));
	    							
	    							lblTotalValue.setText(String.format("%.2f", grandTotal));
	    							btnPurchase.setText("Pay  ₹ "+ String.valueOf(grandTotal));
	    							product.updateCartProduct(proId, uId, newQuantity, productdiscount, productGrandTotal);
	    							product.getCartData(table_1, uId, "");
	    							clear();
	    						}else {
	    							JOptionPane.showMessageDialog(Purchase.this, "Not enough stock", "Warning", 2);
	    						}
	    					}else {
	    						JOptionPane.showMessageDialog(Purchase.this, "Please increase the product quantity", "Warning", 2);
	    					}
	    				}else {
	    					JOptionPane.showMessageDialog(Purchase.this, "Stock is empty", "Warning", 2);
	    				}
	    			}
	    			else {
	    				JOptionPane.showMessageDialog(Purchase.this, "Please select wishlist or cart product", "Warning", 2);
					}
	    		}
	    	}
	    });
	    
	    btnAddWishlist.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent arg0) {
	    		if (nameField.getText().isEmpty()) {
	    			JOptionPane.showMessageDialog(Purchase.this, "Please select a product", "Warning", 2);
	    		}else {
	    			model = (DefaultTableModel) table.getModel();
	    			int proId = Integer.parseInt(model.getValueAt(rowIndex, 0).toString());
	    			if (!product.isWishlistproductExists(proId, uId)) {
	    				String pname = nameField.getText();
						product.insertIntoWishlist(uId, proId, pname, quantity, price);
						wishList.table.setModel(new DefaultTableModel(null, new Object[] {"Product ID", "Product Name", "Category", "Price"}));
						product.getWishListData(wishList.table, uId, "");
						clear();
    				}else {
	    				JOptionPane.showMessageDialog(Purchase.this, "Product already in wishlist", "Warning", 2);
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
	    	public void actionPerformed(ActionEvent arg0) {
	    		if (nameField.getText().isEmpty()) {
	    			JOptionPane.showMessageDialog(Purchase.this, "Please select a product", "Warning", 2);
	    		}else {
	    			model = (DefaultTableModel) table_1.getModel();
	    			int proId = Integer.parseInt(model.getValueAt(rowIndex, 1).toString());
	    			product.deleteSpecificCartItem(uId, proId);
	    			total = purchase.getTotalAmount(purchase.getId(UserDashboard.lblUseremail.getText()));
	    			txtTotalAmount.setText(String.format("%.2f", total));
					totalDiscount = (total * (double) discount) / 100;
					txtDiscountAmount.setText(String.valueOf(totalDiscount));
					grandTotal = total - totalDiscount;
					txtGrandTotal.setText(String.format("%.2f", grandTotal));
					
	    			lblTotalValue.setText(String.format("%.2f", grandTotal));
	    			btnPurchase.setText("Pay  ₹ "+ String.valueOf(grandTotal));
	    			table_1.setModel(new DefaultTableModel(null, new Object[] {"Purchase ID", "Product ID", "Product Name", "Quantity", "Price", "Total"}));
	    			table.setModel(new DefaultTableModel(null, new Object[] {"Product ID", "Product Name", "Category", "Price"}));
		    		DefaultTableCellRenderer centerRenderer2 = new DefaultTableCellRenderer();
		    		centerRenderer2.setHorizontalAlignment(SwingConstants.CENTER);
		    		
		    		for (int i = 0; i < table.getColumnCount(); i++) {
		    			table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer2);
		    		}
		    		DefaultTableCellRenderer centerRenderer3 = new DefaultTableCellRenderer();
		    		centerRenderer3.setHorizontalAlignment(SwingConstants.CENTER);
		    		
		    		for (int i = 0; i < table_1.getColumnCount(); i++) {
		    			table_1.getColumnModel().getColumn(i).setCellRenderer(centerRenderer3);
		    		}
		    		product.getProductData(table, "");
	    			product.getCartData(table_1, uId, "");
					clear();
	    		}
	    	}
	    });
	    
	    
	    table.addMouseListener(new MouseAdapter() {
	    	@Override
	    	public void mouseClicked(MouseEvent e) {
	    		btnRemove.setEnabled(false);
	    		table_1.clearSelection();
	    		btnAddWishlist.setEnabled(true);
	    		model = (DefaultTableModel) table.getModel();
				rowIndex = table.getSelectedRow();
				nameField.setText(model.getValueAt(rowIndex, 1).toString());
				String s1 = model.getValueAt(rowIndex, 0).toString();
				String s2 = model.getValueAt(rowIndex, 3).toString();
				quantity = product.getProductQuantity(Integer.parseInt(s1));
				price = Double.parseDouble(s2);
				proId = Integer.parseInt(model.getValueAt(rowIndex, 0).toString());
	    	}
	    });
	    
	    table_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				btnRemove.setEnabled(true);
				table.clearSelection();
				btnAddWishlist.setEnabled(false);
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
	
	private void productsTable() {
		product.getProductData(table, "");
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
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private File generateInvoiceReport(String order_id) throws JRException, IOException {
		InputStream jasperStream = getClass().getResourceAsStream("/com/logilite/component/InvoiceReport.jasper");
		JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperStream);
		URL imageUrl = getClass().getResource("/com/logilite/component/tick.png");
		if (imageUrl == null) {
			JOptionPane.showMessageDialog(Purchase.this, "Image file not found", "Warning", 2);
		}
		String imagePath = imageUrl.getPath();
		HashMap a = new HashMap();
		a.put("orderId", order_id);
		a.put("imagePath", imagePath);
		
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, a, Login.conn);
		File pdfFile = new File("invoice_report.pdf");
		
		JasperExportManager.exportReportToPdfFile(jasperPrint, pdfFile.getAbsolutePath());
		return pdfFile;
	}
	
	
	@SuppressWarnings({ "serial", "rawtypes", "unchecked" })
	private void initComponent() {
		setLayout(null);
		
		panel = new JPanel();
		panel.setBounds(0, 0, 995, 610);
		add(panel);
		panel.setLayout(null);
		
		lblPurchaseId = new JLabel("Purchase ID");
		lblPurchaseId.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 14));
		lblPurchaseId.setBounds(94, 79, 103, 15);
		panel.add(lblPurchaseId);
		
		IDField = new JTextField();
		IDField.setEditable(false);
		IDField.setText("0");
		IDField.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 14));
		IDField.setColumns(10);
		IDField.setBackground(new Color(171, 167, 167));
		IDField.setBounds(94, 95, 259, 30);
		panel.add(IDField);
		
		lblProductName = new JLabel("Product Name");
		lblProductName.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 14));
		lblProductName.setBounds(94, 133, 103, 15);
		panel.add(lblProductName);
		
		nameField = new JTextField();
		nameField.setEditable(false);
		nameField.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 14));
		nameField.setColumns(10);
		nameField.setBackground(new Color(171, 167, 167));
		nameField.setBounds(94, 150, 259, 30);
		panel.add(nameField);
		
		lblQuantity = new JLabel("Quantity");
		lblQuantity.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 14));
		lblQuantity.setBounds(94, 185, 70, 15);
		panel.add(lblQuantity);
		
		quantityField = new JTextField();
		quantityField.setText("0");
		quantityField.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 14));
		quantityField.setColumns(10);
		quantityField.setBounds(94, 202, 259, 30);
		panel.add(quantityField);
		
		lblSearchProduct = new JLabel("Search Product");
		lblSearchProduct.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 14));
		lblSearchProduct.setBounds(553, 77, 126, 15);
		panel.add(lblSearchProduct);
		
		searchField = new JTextField();
		searchField.setColumns(10);
		searchField.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 14));
		searchField.setBounds(684, 71, 275, 30);
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
		
		btnPurchase = new JButton("Pay  ₹ 0");
		btnPurchase.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		btnPurchase.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnPurchase.setIcon(new ImageIcon(getClass().getResource("/com/logilite/img/paynow.png")));
		btnPurchase.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 14));
		btnPurchase.setBounds(114, 548, 215, 37);
		btnPurchase.setFocusPainted(false);
		panel.add(btnPurchase);
		
		btnAdd = new JButton("Add");
		btnAdd.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		btnAdd.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnAdd.setIcon(new ImageIcon(getClass().getResource("/com/logilite/img/save.png")));
		btnAdd.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 14));
		btnAdd.setBounds(94, 394, 111, 37);
		btnAdd.setFocusPainted(false);
		panel.add(btnAdd);
		
		btnClear = new JButton("Clear");
		btnClear.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		btnClear.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnClear.setIcon(new ImageIcon(getClass().getResource("/com/logilite/img/clear.png")));
		btnClear.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 14));
		btnClear.setBounds(217, 394, 136, 37);
		btnClear.setFocusPainted(false);
		panel.add(btnClear);
		
		table = new JTable();
		table.setBackground(new Color(222, 221, 218));
		table.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 14));

	    table.setModel(new DefaultTableModel(
	    	new Object[][] {
	    	},
	    	new String[] {
	    		"Product ID", "Product Name", "Category", "Price"
	    	}
	    ) {
	    	boolean[] columnEditables = new boolean[] {
	    		false, false, false, false
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
	    
	    lblCart = new JLabel("Cart :");
	    lblCart.setFont(new Font("DejaVu Sans Condensed", Font.BOLD, 19));
	    lblCart.setBounds(399, 356, 70, 15);
	    panel.add(lblCart);
	    
	    btnAddWishlist = new JButton("Add Wishlist");
	    btnAddWishlist.setIcon(new ImageIcon(getClass().getResource("/com/logilite/img/filled-wishlist.png")));
	    btnAddWishlist.setBounds(203, 239, 150, 25);
	    panel.add(btnAddWishlist);
	    
	    btnRemove = new JButton("Remove");
	    btnRemove.setIcon(new ImageIcon(Purchase.class.getResource("/com/logilite/img/remove.png")));
	    btnRemove.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 14));
	    btnRemove.setBounds(217, 438, 136, 37);
	    panel.add(btnRemove);
	    
	    JLabel label = new JLabel("");
	    label.setIcon(new ImageIcon(Purchase.class.getResource("/com/logilite/img/discount.gif")));
	    label.setBounds(858, 0, 56, 56);
	    panel.add(label);
	    
	    JLabel lblDiscount = new JLabel("Discount :");
	    lblDiscount.setIcon(new ImageIcon(Purchase.class.getResource("/com/logilite/img/direction.png")));
	    lblDiscount.setFont(new Font("Noto Sans CJK SC", Font.BOLD, 26));
	    lblDiscount.setBounds(639, 10, 181, 35);
	    panel.add(lblDiscount);
	    
	    lblDiscountValue = new JLabel("30");
	    lblDiscountValue.setFont(new Font("Noto Sans CJK SC", Font.BOLD, 26));
	    lblDiscountValue.setBounds(825, 10, 33, 35);
	    panel.add(lblDiscountValue);
	    
	    lblTotalAmount = new JLabel("Total");
	    lblTotalAmount.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 14));
	    lblTotalAmount.setBounds(94, 272, 70, 15);
	    panel.add(lblTotalAmount);
	    
	    txtTotalAmount = new JTextField();
	    txtTotalAmount.setEditable(false);
	    txtTotalAmount.setText("0.0");
	    txtTotalAmount.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 14));
	    txtTotalAmount.setColumns(10);
	    txtTotalAmount.setBounds(94, 294, 126, 30);
	    panel.add(txtTotalAmount);
	    
	    lblTotalDiscount = new JLabel("Total Discount");
	    lblTotalDiscount.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 14));
	    lblTotalDiscount.setBounds(227, 272, 111, 15);
	    panel.add(lblTotalDiscount);
	    
	    txtDiscountAmount = new JTextField();
	    txtDiscountAmount.setEditable(false);
	    txtDiscountAmount.setText("0.0");
	    txtDiscountAmount.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 14));
	    txtDiscountAmount.setColumns(10);
	    txtDiscountAmount.setBounds(227, 294, 126, 30);
	    panel.add(txtDiscountAmount);
	    
	    lblGrandTotal = new JLabel("Grand Total");
	    lblGrandTotal.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 14));
	    lblGrandTotal.setBounds(94, 333, 103, 15);
	    panel.add(lblGrandTotal);
	    
	    txtGrandTotal = new JTextField();
	    txtGrandTotal.setEditable(false);
	    txtGrandTotal.setText("0.0");
	    txtGrandTotal.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 14));
	    txtGrandTotal.setColumns(10);
	    txtGrandTotal.setBounds(94, 355, 259, 30);
	    panel.add(txtGrandTotal);
	    
	    comboBox = new JComboBox();
	    comboBox.setModel(new DefaultComboBoxModel(new String[] {"Using Razorpay", "Using Stripe"}));
	    comboBox.setBounds(94, 512, 259, 24);
	    panel.add(comboBox);
	    
	    JLabel lblPaymentMethod = new JLabel("Payment Method :");
	    lblPaymentMethod.setFont(new Font("DejaVu Sans Condensed", Font.BOLD, 14));
	    lblPaymentMethod.setBounds(94, 487, 136, 26);
	    panel.add(lblPaymentMethod);
	}
}
