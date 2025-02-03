package com.logilite.user;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

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

import com.logilite.dao.Statistics;
import com.logilite.panelloader.JpanelLoader;

public class UserDashboard extends JFrame
{

	private static final long	serialVersionUID	= 1L;
	public static JPanel				contentPane;
	public static JPanel panel;
	public static JLabel lblonlineproductinventory;
	public static JLabel lblLogout;
	public static JLabel lblUseremail;
	public static JPanel panel_1;
	public static JPanel panel_6;
	public static JPanel panel_5;
	public static JLabel lblMyStatistics;
	public static JLabel lblTotalCategories;
	public static JLabel lblTotalProducts;
	public static JLabel lblTotalPurchase;
	public static JLabel lblcategoryValue;
	public static JLabel lblProductValue;
	public static JLabel lblPurchaseValue;
	public static JButton dashboardBtn;
	public static JButton btnPrintPurchase;
	int xx, xy;
	public static JButton btnPurchase;
	public static JButton btnPurchaseDetails;
	public static JButton btnMyAccount;
	private DefaultPieDataset dataset;
	public static final ButtonGroup buttonGroup = new ButtonGroup();
	JpanelLoader loader = new JpanelLoader();
	private JButton btnWishlist;
	Statistics statistics = new Statistics();

	public UserDashboard()
	{
		initComponent();
		init();
	}
	
	private void init() {
		lblLogout.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int a = JOptionPane.showConfirmDialog(UserDashboard.this, "Do you want to logout now?", "Logout", JOptionPane.YES_NO_OPTION);
				if (a == 0) {
					new Login().setVisible(true);
					dispose();
				}
			}
		});
		
		lblUseremail.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				UserAccount userAccount = new UserAccount();
				loader.jPanelLoader(panel_6, userAccount);
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
			}
		});
		
		btnPurchase.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Purchase purchase = new Purchase();
				loader.jPanelLoader(panel_6, purchase);
			}
		});
		
		btnPurchaseDetails.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PurchaseDetails purchaseDetails = new PurchaseDetails();
				loader.jPanelLoader(panel_6, purchaseDetails);
			}
		});
		
		btnMyAccount.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				UserAccount userAccount = new UserAccount();
				loader.jPanelLoader(panel_6, userAccount);
			}
		});
		
		btnPrintPurchase.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				PrintPurchase printPurchase = new PrintPurchase();
				loader.jPanelLoader(panel_6, printPurchase);
			}
		});
	}
	
	private void initComponent() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent e) {
				for (double i = 0.1; i <= 1.0; i += 0.1) {
					String s = "" + i;
					float f = Float.parseFloat(s);
					UserDashboard.this.setOpacity(f);
					try
					{
						Thread.sleep(20);
					}
					catch (InterruptedException e1)
					{
						Logger.getLogger(UserDashboard.class.getName()).log(Level.SEVERE, null, e1);
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
		
		lblUseremail = new JLabel("user@gmail.com");
		lblUseremail.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lblUseremail.setIcon(new ImageIcon(getClass().getResource("/com/logilite/img/user.png")));
		lblUseremail.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 18));
		lblUseremail.setBounds(802, 19, 363, 30);
		panel.add(lblUseremail);
		
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
		contentPane.add(panel_1);
		panel_1.setLayout(null);
		
		dashboardBtn = new JButton("Dashboard");
		dashboardBtn.setIcon(new ImageIcon(getClass().getResource("/com/logilite/img/dashboard.png")));
		dashboardBtn.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 20));
		dashboardBtn.setBackground(new Color(204, 225, 238));
		dashboardBtn.setForeground(new Color(0, 0, 0));
		dashboardBtn.setBorder(BorderFactory.createEmptyBorder());
		dashboardBtn.setFocusPainted(false);
		dashboardBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		dashboardBtn.setBounds(0, 33, 255, 44);
		
		panel_1.add(dashboardBtn);
		
		btnPurchase = new JButton("Purchase");
		btnPurchase.setMargin(new Insets(2, 5, 2, 14));
		btnPurchase.setHorizontalAlignment(SwingConstants.LEFT);
		btnPurchase.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnPurchase.setIcon(new ImageIcon(getClass().getResource("/com/logilite/img/purchase.png")));
		buttonGroup.add(btnPurchase);
		btnPurchase.setBounds(24, 125, 210, 34);
		btnPurchase.setFocusPainted(false);
		panel_1.add(btnPurchase);
		
		btnPurchaseDetails = new JButton("Purchase Details");
		btnPurchaseDetails.setMargin(new Insets(2, 5, 2, 14));
		btnPurchaseDetails.setHorizontalAlignment(SwingConstants.LEFT);
		btnPurchaseDetails.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnPurchaseDetails.setIcon(new ImageIcon(getClass().getResource("/com/logilite/img/purchase_details.png")));
		buttonGroup.add(btnPurchaseDetails);
		btnPurchaseDetails.setBounds(24, 182, 210, 34);
		btnPurchaseDetails.setFocusPainted(false);
		panel_1.add(btnPurchaseDetails);
		
		btnMyAccount = new JButton("My Account");
		btnMyAccount.setMargin(new Insets(2, 5, 2, 14));
		btnMyAccount.setHorizontalAlignment(SwingConstants.LEFT);
		btnMyAccount.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnMyAccount.setIcon(new ImageIcon(getClass().getResource("/com/logilite/img/my_account.png")));
		buttonGroup.add(btnMyAccount);
		btnMyAccount.setBounds(24, 342, 210, 34);
		btnMyAccount.setFocusPainted(false);
		panel_1.add(btnMyAccount);
		
		btnPrintPurchase = new JButton("Print Purchase Order");
		btnPrintPurchase.setMargin(new Insets(2, 5, 2, 14));
		btnPrintPurchase.setIcon(new ImageIcon(getClass().getResource("/com/logilite/img/print.png")));
		btnPrintPurchase.setHorizontalAlignment(SwingConstants.LEFT);
		buttonGroup.add(btnPrintPurchase);
		btnPrintPurchase.setBounds(24, 237, 210, 34);
		btnPrintPurchase.setFocusPainted(false);
		panel_1.add(btnPrintPurchase);
		
		btnWishlist = new JButton("Wishlist");
		btnWishlist.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				WishList wishList = new WishList();
				loader.jPanelLoader(panel_6, wishList);
			}
		});
		btnWishlist.setMargin(new Insets(2, 5, 2, 14));
		btnWishlist.setIcon(new ImageIcon(UserDashboard.class.getResource("/com/logilite/img/filled-wishlist.png")));
		btnWishlist.setHorizontalAlignment(SwingConstants.LEFT);
		btnWishlist.setFocusPainted(false);
		btnWishlist.setBounds(24, 290, 210, 34);
		panel_1.add(btnWishlist);
		
		panel_6 = new JPanel() ;
		panel_6.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panel_6.setBounds(254, 70, 1036, 650);
		contentPane.add(panel_6);
		panel_6.setLayout(null);
		
		panel_5 = new JPanel();
		panel_5.setBounds(38, 31, 692, 460);
		panel_5.setLayout(null);
		panel_5.setBackground(Color.WHITE);
		panel_6.add(panel_5);
		
		lblMyStatistics = new JLabel("My Statistics");
		lblMyStatistics.setHorizontalAlignment(SwingConstants.CENTER);
		lblMyStatistics.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 20));
		lblMyStatistics.setBounds(0, 0, 749, 27);
		panel_5.add(lblMyStatistics);
		
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
		
		lblTotalPurchase = new JLabel("Total Purchase :");
		lblTotalPurchase.setForeground(new Color(119, 118, 123));
		lblTotalPurchase.setFont(new Font("DejaVu Sans Condensed", Font.BOLD, 16));
		lblTotalPurchase.setBounds(378, 39, 137, 27);
		panel_5.add(lblTotalPurchase);
		
		lblcategoryValue = new JLabel("0");
		lblcategoryValue.setForeground(new Color(119, 118, 123));
		lblcategoryValue.setFont(new Font("DejaVu Sans Condensed", Font.BOLD, 16));
		lblcategoryValue.setBounds(165, 47, 137, 15);
		panel_5.add(lblcategoryValue);
		
		lblProductValue = new JLabel("0");
		lblProductValue.setForeground(new Color(119, 118, 123));
		lblProductValue.setFont(new Font("DejaVu Sans Condensed", Font.BOLD, 16));
		lblProductValue.setBounds(154, 79, 166, 15);
		panel_5.add(lblProductValue);
		
		lblPurchaseValue = new JLabel("0.0");
		lblPurchaseValue.setForeground(new Color(119, 118, 123));
		lblPurchaseValue.setFont(new Font("DejaVu Sans Condensed", Font.BOLD, 16));
		lblPurchaseValue.setBounds(516, 47, 166, 15);
		panel_5.add(lblPurchaseValue);
		
		createPieChart();
	}
	
	private void createPieChart() {
	 	PieDataset dataset = createDataset();

        JFreeChart chart = createChart(dataset);

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(600, 400));
        chartPanel.setBounds(31, 172, 489, 276);
        panel_5.add(chartPanel);
        panel_5.revalidate();
        panel_5.repaint();
    }

    @SuppressWarnings("static-access")
	private PieDataset createDataset() {
        dataset = new DefaultPieDataset();
        dataset.setValue("Categories", statistics.total("category"));
        dataset.setValue("Products", statistics.total("product"));
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
}
