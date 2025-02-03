package com.logilite.admin;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.ui.RectangleEdge;
import org.jfree.util.Rotation;

import com.logilite.dao.ProductDao;
import com.logilite.dao.Statistics;
import com.logilite.dao.SupplierDao;
import com.logilite.panelloader.JpanelLoader;
import com.logilite.user.Login;

public class AdminDashboard extends JFrame
{

	private static final long	serialVersionUID	= 1L;
	private JPanel				contentPane;
	private JPanel panel;
	public static JLabel lblonlineproductinventory;
	public static JTable table_1;
	public static JLabel lblLogout;
	public static JLabel lblAdminemail;
	public static JScrollPane scrollPane1;
	public static JPanel panel_1;
	public static int xx, xy;
	public static JPanel panel_6;
	public static JButton btnManageCategories;
	public static JButton btnManageProducts;
	public static JButton btnManageUsers;
	public static JButton btnAddSupplier;
	public static JButton btnManageSupplier;
	public static JButton btnSelectSupplier;
	public static JButton btnTransactions;
	public static JPanel panel_5;
	public static JLabel lblStatistics;
	public static JLabel lblTotalCategories;
	public static JLabel lblTotalProducts;
	public static JLabel lblTotalUsers;
	public static JLabel lblCategoryValue;
	public static JLabel lblProductValue;
	public static JLabel lblUsersValue;
	public static JLabel lblTodaySales;
	public static JLabel lblTotalSuppliers;
	public static JLabel lblSupplierValue;
	public static JLabel lblTotalSales_1;
	public static JLabel lblSalesValue;
	public static JLabel lblTotalSalesValue;
	public static JButton btnAdd;
	public static JDialog dialog;
	public static JPanel buttonPanel;
	public static JButton confirmButton;
	public static JButton cancelButton;
	JpanelLoader loader = new JpanelLoader();
	private JButton dashboardBtn;
	private JLabel lblAddDiscount;
	private JTextField txtDiscount;
	private JLabel label;
	private DefaultPieDataset dataset;
	ProductDao product = new ProductDao();
	Statistics statistics = new Statistics();
	SupplierDao supplier = new SupplierDao();
	private JLabel lblSuppliersRequests;
	DefaultTableModel model;
	int rowIndex;

	public AdminDashboard()
	{
		initComponent();
		init();
	}
	
	@SuppressWarnings("serial")
	private void initComponent() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent e) {
				for (double i = 0.1; i <= 1.0; i += 0.1) {
					String s = "" + i;
					float f = Float.parseFloat(s);
					AdminDashboard.this.setOpacity(f);
					try
					{
						Thread.sleep(20);
					}
					catch (InterruptedException e1)
					{
						Logger.getLogger(AdminDashboard.class.getName()).log(Level.SEVERE, null, e1);
					}
				}
			}
		});
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1290, 720);
		setUndecorated(true);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setForeground(new Color(255, 255, 255));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		panel = new JPanel() {
			private static final long serialVersionUID = 1L;

			@Override
			protected void paintComponent(Graphics g)
			{
				super.paintComponent(g);
				Graphics2D g2 = (Graphics2D) g;
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				GradientPaint gp = new GradientPaint(getHeight(), 0, Color.decode("#bdc3c7"), 0, getHeight(), Color.decode("#2c3e50"));
				g2.setPaint(gp);
				g2.fillRoundRect(0, 0, getWidth(), getHeight(), 0, 0);
			}
		};
		panel.setBounds(0, 0, 1290, 71);
		panel.setBackground(new Color(192, 191, 188));
		contentPane.add(panel);
		panel.setLayout(null);
		
		lblonlineproductinventory = new JLabel("ONLINE PRODUCT INVENTORY");
		lblonlineproductinventory.setIcon(new ImageIcon(getClass().getResource("/com/logilite/img/dashboard_logo.png")));
		lblonlineproductinventory.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 30));
		lblonlineproductinventory.setBounds(0, 0, 600, 71);
		panel.add(lblonlineproductinventory);
			
		lblLogout = new JLabel("Logout");
		lblLogout.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lblLogout.setHorizontalAlignment(SwingConstants.RIGHT);
		lblLogout.setIcon(new ImageIcon(getClass().getResource("/com/logilite/img/logout.png")));
		lblLogout.setFont(new Font("DejaVu Sans Condensed", Font.BOLD, 18));
		lblLogout.setBounds(1177, 16, 101, 35);
		panel.add(lblLogout);
		
		lblAdminemail = new JLabel("admin@gmail.com");
		lblAdminemail.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		lblAdminemail.setIcon(new ImageIcon(getClass().getResource("/com/logilite/img/user.png")));
		lblAdminemail.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 18));
		lblAdminemail.setBounds(844, 19, 321, 30);
		panel.add(lblAdminemail);
		
		panel_1 = new JPanel() {
			private static final long serialVersionUID = 1L;

			@Override
			protected void paintComponent(Graphics g)
			{
				super.paintComponent(g);
				Graphics2D g2 = (Graphics2D) g;
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				GradientPaint gp = new GradientPaint(getHeight(), 0, Color.decode("#2c3e50"), 0, getWidth(), Color.decode("#bdc3c7"));
				g2.setPaint(gp);
				g2.fillRoundRect(0, 0, getWidth(), getHeight(), 0, 0);
			}
		};
		panel_1.setBounds(0, 70, 255, 650);
		panel_1.setBackground(new Color(192, 191, 188));
		contentPane.add(panel_1);
		panel_1.setLayout(null);
		
		btnManageCategories = new JButton("Manage Categories");
		btnManageCategories.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnManageCategories.setHorizontalAlignment(SwingConstants.LEFT);
		btnManageCategories.setIcon(new ImageIcon(getClass().getResource("/com/logilite/img/manage_categories.png")));
		btnManageCategories.setBounds(29, 126, 201, 31);
		btnManageCategories.setFocusPainted(false);
		panel_1.add(btnManageCategories);
		
		btnManageProducts = new JButton("Manage Products");
		btnManageProducts.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnManageProducts.setHorizontalAlignment(SwingConstants.LEFT);
		btnManageProducts.setIcon(new ImageIcon(getClass().getResource("/com/logilite/img/manage_products.png")));
		btnManageProducts.setBounds(29, 179, 201, 31);
		btnManageProducts.setFocusPainted(false);
		panel_1.add(btnManageProducts);
		
		btnManageUsers = new JButton("Manage Users");
		btnManageUsers.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnManageUsers.setHorizontalAlignment(SwingConstants.LEFT);
		btnManageUsers.setIcon(new ImageIcon(getClass().getResource("/com/logilite/img/manage_users.png")));
		btnManageUsers.setBounds(29, 232, 201, 31);
		btnManageUsers.setFocusPainted(false);
		panel_1.add(btnManageUsers);
		
		btnAddSupplier = new JButton("Add Supplier");
		btnAddSupplier.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnAddSupplier.setHorizontalAlignment(SwingConstants.LEFT);
		btnAddSupplier.setIcon(new ImageIcon(getClass().getResource("/com/logilite/img/add_supplier.png")));
		btnAddSupplier.setBounds(29, 283, 201, 31);
		btnAddSupplier.setFocusPainted(false);
		panel_1.add(btnAddSupplier);
		
		btnManageSupplier = new JButton("Manage Supplier");
		btnManageSupplier.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnManageSupplier.setHorizontalAlignment(SwingConstants.LEFT);
		btnManageSupplier.setIcon(new ImageIcon(getClass().getResource("/com/logilite/img/manage_suppliers.png")));
		btnManageSupplier.setBounds(29, 334, 201, 31);
		btnManageSupplier.setFocusPainted(false);
		panel_1.add(btnManageSupplier);
		
		btnSelectSupplier = new JButton("Select Supplier");
		btnSelectSupplier.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnSelectSupplier.setHorizontalAlignment(SwingConstants.LEFT);
		btnSelectSupplier.setIcon(new ImageIcon(getClass().getResource("/com/logilite/img/select_supplier.png")));
		btnSelectSupplier.setBounds(29, 386, 201, 31);
		btnSelectSupplier.setFocusPainted(false);
		panel_1.add(btnSelectSupplier);
		
		btnTransactions = new JButton("Transactions");
		btnTransactions.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnTransactions.setHorizontalAlignment(SwingConstants.LEFT);
		btnTransactions.setIcon(new ImageIcon(getClass().getResource("/com/logilite/img/transactions.png")));
		btnTransactions.setBounds(29, 440, 201, 31);
		btnTransactions.setFocusPainted(false);
		panel_1.add(btnTransactions);
		
		dashboardBtn = new JButton("Dashboard");
		dashboardBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		dashboardBtn.setIcon(new ImageIcon(getClass().getResource("/com/logilite/img/dashboard.png")));
		dashboardBtn.setForeground(Color.BLACK);
		dashboardBtn.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 20));
		dashboardBtn.setFocusPainted(false);
		dashboardBtn.setBorder(BorderFactory.createEmptyBorder());
		dashboardBtn.setBackground(new Color(204, 225, 238));
		dashboardBtn.setBounds(0, 45, 255, 44);
		panel_1.add(dashboardBtn);
		
		panel_6 = new JPanel();
		panel_6.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel_6.setBounds(253, 70, 1037, 650);
		contentPane.add(panel_6);
		panel_6.setLayout(null);
		
		panel_5 = new JPanel();
		panel_5.setLayout(null);
		panel_5.setBorder(new EtchedBorder(EtchedBorder.RAISED, null, null));
		panel_5.setBackground(Color.WHITE);
		panel_5.setBounds(12, 22, 997, 628);
		panel_6.add(panel_5);
		
		lblStatistics = new JLabel("Statistics");
		lblStatistics.setHorizontalAlignment(SwingConstants.CENTER);
		lblStatistics.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 20));
		lblStatistics.setBounds(0, 0, 997, 27);
		panel_5.add(lblStatistics);
		
		lblTotalCategories = new JLabel("Total Categories :");
		lblTotalCategories.setForeground(new Color(119, 118, 123));
		lblTotalCategories.setFont(new Font("DejaVu Sans Condensed", Font.BOLD, 16));
		lblTotalCategories.setBounds(10, 40, 155, 27);
		panel_5.add(lblTotalCategories);
		
		lblTotalProducts = new JLabel("Total Products :");
		lblTotalProducts.setForeground(new Color(119, 118, 123));
		lblTotalProducts.setFont(new Font("DejaVu Sans Condensed", Font.BOLD, 16));
		lblTotalProducts.setBounds(12, 73, 130, 27);
		panel_5.add(lblTotalProducts);
		
		lblTotalUsers = new JLabel("Total Users :");
		lblTotalUsers.setForeground(new Color(119, 118, 123));
		lblTotalUsers.setFont(new Font("DejaVu Sans Condensed", Font.BOLD, 16));
		lblTotalUsers.setBounds(378, 39, 102, 27);
		panel_5.add(lblTotalUsers);
		
		lblCategoryValue = new JLabel("0");
		lblCategoryValue.setForeground(new Color(119, 118, 123));
		lblCategoryValue.setFont(new Font("DejaVu Sans Condensed", Font.BOLD, 16));
		lblCategoryValue.setBounds(165, 47, 137, 15);
		panel_5.add(lblCategoryValue);
		
		lblProductValue = new JLabel("0");
		lblProductValue.setForeground(new Color(119, 118, 123));
		lblProductValue.setFont(new Font("DejaVu Sans Condensed", Font.BOLD, 16));
		lblProductValue.setBounds(154, 79, 155, 15);
		panel_5.add(lblProductValue);
		
		lblUsersValue = new JLabel("0");
		lblUsersValue.setForeground(new Color(119, 118, 123));
		lblUsersValue.setFont(new Font("DejaVu Sans Condensed", Font.BOLD, 16));
		lblUsersValue.setBounds(491, 46, 155, 15);
		panel_5.add(lblUsersValue);
		
		lblTodaySales = new JLabel("Today Sales :");
		lblTodaySales.setForeground(new Color(119, 118, 123));
		lblTodaySales.setFont(new Font("DejaVu Sans Condensed", Font.BOLD, 16));
		lblTodaySales.setBounds(694, 39, 137, 27);
		panel_5.add(lblTodaySales);
		
		lblTotalSuppliers = new JLabel("Total Suppliers :");
		lblTotalSuppliers.setForeground(new Color(119, 118, 123));
		lblTotalSuppliers.setFont(new Font("DejaVu Sans Condensed", Font.BOLD, 16));
		lblTotalSuppliers.setBounds(378, 73, 130, 27);
		panel_5.add(lblTotalSuppliers);
		
		lblSupplierValue = new JLabel("0");
		lblSupplierValue.setForeground(new Color(119, 118, 123));
		lblSupplierValue.setFont(new Font("DejaVu Sans Condensed", Font.BOLD, 16));
		lblSupplierValue.setBounds(511, 80, 155, 15);
		panel_5.add(lblSupplierValue);
		
		lblTotalSales_1 = new JLabel("Total Sales :");
		lblTotalSales_1.setForeground(new Color(119, 118, 123));
		lblTotalSales_1.setFont(new Font("DejaVu Sans Condensed", Font.BOLD, 16));
		lblTotalSales_1.setBounds(694, 73, 137, 27);
		panel_5.add(lblTotalSales_1);
		
		lblSalesValue = new JLabel("0.0");
		lblSalesValue.setForeground(new Color(119, 118, 123));
		lblSalesValue.setFont(new Font("DejaVu Sans Condensed", Font.BOLD, 16));
		lblSalesValue.setBounds(802, 47, 155, 15);
		panel_5.add(lblSalesValue);
		
		lblTotalSalesValue = new JLabel("0.0");
		lblTotalSalesValue.setForeground(new Color(119, 118, 123));
		lblTotalSalesValue.setFont(new Font("DejaVu Sans Condensed", Font.BOLD, 16));
		lblTotalSalesValue.setBounds(802, 79, 155, 15);
		panel_5.add(lblTotalSalesValue);
		
		lblAddDiscount = new JLabel("Add Discount :");
		lblAddDiscount.setFont(new Font("DejaVu Sans Condensed", Font.BOLD, 18));
		lblAddDiscount.setBounds(613, 201, 137, 25);
		panel_5.add(lblAddDiscount);
		
		txtDiscount = new JTextField();
		txtDiscount.setFont(new Font("DejaVu Sans Condensed", Font.BOLD, 18));
		txtDiscount.setBounds(756, 200, 75, 25);
		panel_5.add(txtDiscount);
		txtDiscount.setColumns(10);
		
		btnAdd = new JButton("Add");
		btnAdd.setBounds(756, 231, 75, 25);
		panel_5.add(btnAdd);
		
		label = new JLabel("%");
		label.setFont(new Font("DejaVu Sans Condensed", Font.BOLD, 18));
		label.setBounds(837, 200, 21, 25);
		panel_5.add(label);
		
		createPieChart();
		
		table_1 = new JTable();
		table_1.setBackground(new Color(222, 221, 218));
		table_1.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 14));
		table_1.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"First Name", "Last Name", "Email", "Mobile no.", "Address1", "Address2"
			}
		) {
			boolean[] columnEditables = new boolean[] {
				false, false, false, false, false, false
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		table_1.getColumnModel().getColumn(0).setPreferredWidth(100);
		table_1.getColumnModel().getColumn(1).setPreferredWidth(100);
		table_1.getColumnModel().getColumn(2).setPreferredWidth(150);
		table_1.getColumnModel().getColumn(3).setPreferredWidth(100);
		table_1.getColumnModel().getColumn(4).setPreferredWidth(150);
		table_1.getColumnModel().getColumn(5).setPreferredWidth(150);
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        
        for (int i = 0; i < table_1.getColumnCount(); i++) {
            table_1.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
		table_1.getTableHeader().setReorderingAllowed(false);
		scrollPane1 = new JScrollPane(table_1);
		scrollPane1.setBounds(29, 449, 802, 167);
	    panel_5.add(scrollPane1);
	    
	    lblSuppliersRequests = new JLabel("Supplier's Requests");
	    lblSuppliersRequests.setHorizontalAlignment(SwingConstants.CENTER);
	    lblSuppliersRequests.setFont(new Font("Dialog", Font.BOLD, 18));
	    lblSuppliersRequests.setBounds(29, 414, 802, 25);
	    panel_5.add(lblSuppliersRequests);
	}
	
	 private void createPieChart() {
		 	PieDataset dataset = createDataset();

	        JFreeChart chart = createChart(dataset);

	        ChartPanel chartPanel = new ChartPanel(chart);
	        chartPanel.setPreferredSize(new Dimension(600, 400));
	        chartPanel.setBounds(29, 120, 489, 276);
	        panel_5.add(chartPanel);
	        panel_5.revalidate();
	        panel_5.repaint();
	    }

	    @SuppressWarnings("static-access")
		private PieDataset createDataset() {
	        dataset = new DefaultPieDataset();
	        dataset.setValue("Categories", statistics.total("category"));
	        dataset.setValue("Products", statistics.total("product"));
	        dataset.setValue("Users", statistics.total("users"));
	        dataset.setValue("Suppliers", statistics.total("supplier"));
	        return dataset;
	    }

	    private JFreeChart createChart(PieDataset dataset) {
	        JFreeChart chart = ChartFactory.createPieChart3D(
	                "Statistics Overview", 
	                dataset,               
	                true,                  
	                true,                  
	                false                  
	                );

	        PiePlot3D plot = (PiePlot3D) chart.getPlot();

	        plot.setLabelGenerator(new StandardPieSectionLabelGenerator(
	                "{0} = {1} ({2})")); 
	        
	        plot.setDepthFactor(0.30); 
	        
	        plot.setExplodePercent("Categories", 0.1);
	        
	        plot.setShadowXOffset(5);
	        plot.setShadowYOffset(5);
	        plot.setShadowPaint(Color.GRAY);
	        
	        chart.setTitle(new TextTitle("Statistics Overview", new Font("Serif", Font.BOLD, 20)));
	        LegendTitle legend = chart.getLegend();
	        legend.setPosition(RectangleEdge.BOTTOM); 
	        legend.setItemFont(new Font("Arial", Font.PLAIN, 12));
	        legend.setBackgroundPaint(new Color(255, 255, 255, 200)); 
	        legend.setVisible(true);
	        
	        plot.setBackgroundPaint(new GradientPaint(0, 0, Color.WHITE, 0, 0, Color.LIGHT_GRAY));
	        
	        if (plot instanceof PiePlot3D) {
	            PiePlot3D plot3D = (PiePlot3D) plot;
	            plot3D.setDepthFactor(0.30); 
	            plot3D.setStartAngle(60);    
	            plot3D.setDirection(Rotation.CLOCKWISE);
	        }
	        
	        return chart;
	    }
	    
	    private void supplierRequestTable()
	    {
	    	supplier.getSupplierRequestData(table_1);
	    	model = (DefaultTableModel) table_1.getModel();
			table_1.setRowHeight(30);
			table_1.setShowGrid(true);
			table_1.setGridColor(Color.black);
			table_1.setBackground(Color.white);
			table_1.setSelectionBackground(Color.LIGHT_GRAY);
	    }
	    
	    private void openSupplierRequestPopup(int rowindex) {
	        model = (DefaultTableModel) table_1.getModel();

	        String firstName = model.getValueAt(rowindex, 0).toString();
	        String lastName = model.getValueAt(rowindex, 1).toString();
	        String email = model.getValueAt(rowindex, 2).toString();
	        String mobileNo = model.getValueAt(rowindex, 3).toString();
	        String address1 = model.getValueAt(rowindex, 4).toString();
	        String address2 = model.getValueAt(rowindex, 5).toString();

	        dialog = new JDialog();
	        dialog.setTitle("Supplier Request Details");
	        dialog.setSize(400, 300);
	        dialog.setLayout(new BoxLayout(dialog.getContentPane(), BoxLayout.Y_AXIS));

	        addLabelWithSpacing(dialog, "First Name :", firstName);
	        addLabelWithSpacing(dialog, "Last Name :", lastName);
	        addLabelWithSpacing(dialog, "Email :", email);
	        addLabelWithSpacing(dialog, "Mobile No. :", mobileNo);
	        addLabelWithSpacing(dialog, "Address :", address1+ ", " + address2);

	        buttonPanel = new JPanel();
		    confirmButton = new JButton("Confirm");
		    cancelButton = new JButton("Cancel");

		    confirmButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					dialog.dispose();
					model = (DefaultTableModel) table_1.getModel();
					rowIndex = table_1.getSelectedRow();
					AddSupplier addSupplier = new AddSupplier();
					loader.jPanelLoader(panel_6, addSupplier);
					
					AddSupplier.nameField.setText(model.getValueAt(rowIndex, 0).toString().toLowerCase() + "" + model.getValueAt(rowIndex, 1).toString().toLowerCase());
					AddSupplier.emailField.setText(model.getValueAt(rowIndex, 2).toString());
					AddSupplier.mobileField.setText(model.getValueAt(rowIndex, 3).toString());
					AddSupplier.address1Field.setText(model.getValueAt(rowIndex, 4).toString());
					AddSupplier.address2Field.setText(model.getValueAt(rowIndex, 5).toString());
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

	@SuppressWarnings("static-access")
	private void init() {
		supplierRequestTable();
		txtDiscount.setText(String.valueOf(product.getDiscount()));
		
		table_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				model = (DefaultTableModel) table_1.getModel();
				rowIndex = table_1.getSelectedRow();
				openSupplierRequestPopup(rowIndex);
			}
		});
		
		panel.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				int x = e.getXOnScreen();
				int y = e.getYOnScreen();
				AdminDashboard.this.setLocation(x - xx, y - xy);
			}
		});
		
		panel.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				xx = e.getX();
				xy = e.getY();
			}
		});
		
		lblLogout.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int a = JOptionPane.showConfirmDialog(AdminDashboard.this, "Do you want to logout now?", "Logout", JOptionPane.YES_NO_OPTION);
				if (a == 0) {
					new Login().setVisible(true);
					dispose();
				}
			}
		});
		
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int discount = Integer.parseInt(txtDiscount.getText());
				product.setDiscount(discount);
			}
		});
		
		
		btnManageCategories.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				com.logilite.admin.ManageCategory manageCategory = new com.logilite.admin.ManageCategory();
				loader.jPanelLoader(panel_6, manageCategory);
			}
		});
		
		btnManageProducts.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ManageProduct manageProduct = new ManageProduct();
				loader.jPanelLoader(panel_6, manageProduct);
			}
		});
		
		btnManageUsers.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ManageUsers manageUsers = new ManageUsers();
				loader.jPanelLoader(panel_6, manageUsers);
			}
		});
		
		btnAddSupplier.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AddSupplier addSupplier = new AddSupplier();
				loader.jPanelLoader(panel_6, addSupplier);
			}
		});
		
		btnManageSupplier.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ManageSuppliers manageSuppliers = new ManageSuppliers();
				loader.jPanelLoader(panel_6, manageSuppliers);
			}
		});
		
		btnSelectSupplier.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				SelectSupplier selectSupplier = new SelectSupplier();
				loader.jPanelLoader(panel_6, selectSupplier);
			}
		});
		
		btnTransactions.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Transaction transaction = new Transaction();
				loader.jPanelLoader(panel_6, transaction);
			}
		});
		
		dashboardBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				dashboardBtn.setBackground(new Color(189, 195, 199));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				dashboardBtn.setBackground(new Color(142, 158, 173));
			}
			@Override
			public void mouseClicked(MouseEvent e) {
				loader.jPanelLoader(panel_6, panel_5);
				model = (DefaultTableModel) table_1.getModel();
				table_1.setModel(new DefaultTableModel(null, new Object[] {"First Name", "Last Name", "Email", "Mobile no.", "Address1", "Address2"}));
				DefaultTableCellRenderer centerRenderer2 = new DefaultTableCellRenderer();
	    		centerRenderer2.setHorizontalAlignment(SwingConstants.CENTER);
	    		
	    		for (int i = 0; i < table_1.getColumnCount(); i++) {
	    			table_1.getColumnModel().getColumn(i).setCellRenderer(centerRenderer2);
	    		}
				supplierRequestTable();
			}
		});	
	}
}
