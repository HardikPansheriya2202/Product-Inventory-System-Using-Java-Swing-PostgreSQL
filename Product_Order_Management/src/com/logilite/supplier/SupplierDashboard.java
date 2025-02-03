package com.logilite.supplier;

import java.awt.Color;
import java.awt.Cursor;
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

import com.logilite.dao.Statistics;
import com.logilite.dao.SupplierDao;
import com.logilite.panelloader.JpanelLoader;
import com.logilite.user.Login;

public class SupplierDashboard extends JFrame
{

	private static final long	serialVersionUID	= 1L;
	public static JPanel				contentPane;
	public static JPanel panel;
	public static JLabel lblonlineproductinventory;
	public static JLabel lblLogout;
	public static JLabel lblSupplieremail;
	public static JPanel panel_1;
	public static JPanel panel_6;
	int xx, xy;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	public static JPanel panel_5;
	public static JLabel lblMyStatistics;
	public static JLabel lblStatisticsDeliveries;
	public static JLabel lblDeliveriesValue;
	public static JButton dashboardBtn;
	public static JButton btnDelivery;
	public static JButton btnMyDeliveries;
	public static JButton btnMyAccount;
	public static JButton btnDeliveryDetail;
	JpanelLoader loader = new JpanelLoader();
	Statistics statistics = new Statistics();
	SupplierDao supplier = new SupplierDao();

	public SupplierDashboard()
	{
		initComponent();
		init();
	}
	
	private void initComponent() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent e) {
				for (double i = 0.1; i <= 1.0; i += 0.1) {
					String s = "" + i;
					float f = Float.parseFloat(s);
					SupplierDashboard.this.setOpacity(f);
					try
					{
						Thread.sleep(20);
					}
					catch (InterruptedException e1)
					{
						Logger.getLogger(SupplierDashboard.class.getName()).log(Level.SEVERE, null, e1);
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
		
		lblSupplieremail = new JLabel("supplier@gmail.com");
		lblSupplieremail.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lblSupplieremail.setIcon(new ImageIcon(getClass().getResource("/com/logilite/img/user.png")));
		lblSupplieremail.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 18));
		lblSupplieremail.setBounds(840, 19, 325, 30);
		panel.add(lblSupplieremail);
		
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
		
		btnDelivery = new JButton("Delivery");
		btnDelivery.setHorizontalAlignment(SwingConstants.LEFT);
		btnDelivery.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnDelivery.setIcon(new ImageIcon(getClass().getResource("/com/logilite/img/delivery.png")));
		buttonGroup.add(btnDelivery);
		btnDelivery.setBounds(38, 129, 179, 32);
		btnDelivery.setFocusPainted(false);
		panel_1.add(btnDelivery);
		
		btnMyDeliveries = new JButton("My Deliveries");
		btnMyDeliveries.setHorizontalAlignment(SwingConstants.LEFT);
		btnMyDeliveries.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnMyDeliveries.setIcon(new ImageIcon(getClass().getResource("/com/logilite/img/my_delivery.png")));
		buttonGroup.add(btnMyDeliveries);
		btnMyDeliveries.setBounds(38, 186, 179, 32);
		btnMyDeliveries.setFocusPainted(false);
		panel_1.add(btnMyDeliveries);
		
		btnMyAccount = new JButton("My Account");
		btnMyAccount.setHorizontalAlignment(SwingConstants.LEFT);
		btnMyAccount.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnMyAccount.setIcon(new ImageIcon(getClass().getResource("/com/logilite/img/my_account.png")));
		buttonGroup.add(btnMyAccount);
		btnMyAccount.setBounds(38, 291, 179, 32);
		btnMyAccount.setFocusPainted(false);
		panel_1.add(btnMyAccount);
		
		dashboardBtn = new JButton("Dashboard");
		dashboardBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		dashboardBtn.setIcon(new ImageIcon(getClass().getResource("/com/logilite/img/dashboard.png")));
		dashboardBtn.setForeground(Color.BLACK);
		dashboardBtn.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 20));
		dashboardBtn.setFocusPainted(false);
		dashboardBtn.setBorder(BorderFactory.createEmptyBorder());
		dashboardBtn.setBackground(new Color(204, 225, 238));
		dashboardBtn.setBounds(0, 39, 255, 44);
		panel_1.add(dashboardBtn);
		
		btnDeliveryDetail = new JButton("Delivery Report");
		btnDeliveryDetail.setIcon(new ImageIcon(SupplierDashboard.class.getResource("/com/logilite/img/print.png")));
		btnDeliveryDetail.setHorizontalAlignment(SwingConstants.LEFT);
		btnDeliveryDetail.setFocusPainted(false);
		btnDeliveryDetail.setBounds(38, 240, 179, 32);
		panel_1.add(btnDeliveryDetail);
		
		panel_6 = new JPanel();
		panel_6.setBounds(254, 70, 1036, 650);
		panel_6.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		contentPane.add(panel_6);
		panel_6.setLayout(null);
		
		panel_5 = new JPanel();
		panel_5.setLayout(null);
		panel_5.setBackground(Color.WHITE);
		panel_5.setBounds(81, 33, 561, 127);
		panel_6.add(panel_5);
		
		lblMyStatistics = new JLabel("My Statistics");
		lblMyStatistics.setHorizontalAlignment(SwingConstants.CENTER);
		lblMyStatistics.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 20));
		lblMyStatistics.setBounds(0, 0, 340, 27);
		panel_5.add(lblMyStatistics);
		
		lblStatisticsDeliveries = new JLabel("My Deliveries :");
		lblStatisticsDeliveries.setForeground(new Color(119, 118, 123));
		lblStatisticsDeliveries.setFont(new Font("DejaVu Sans Condensed", Font.BOLD, 16));
		lblStatisticsDeliveries.setBounds(10, 40, 155, 27);
		panel_5.add(lblStatisticsDeliveries);
		
		lblDeliveriesValue = new JLabel("0");
		lblDeliveriesValue.setForeground(new Color(119, 118, 123));
		lblDeliveriesValue.setFont(new Font("DejaVu Sans Condensed", Font.BOLD, 16));
		lblDeliveriesValue.setBounds(141, 46, 70, 15);
		panel_5.add(lblDeliveriesValue);
		
	}
	
	private void init() {
		panel.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				xx = e.getX();
				xy = e.getY();
			}
		});
		
		panel.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				int x = e.getXOnScreen();
				int y = e.getYOnScreen();
				SupplierDashboard.this.setLocation(x - xx, y - xy);
			}
		});
		
		lblLogout.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int a = JOptionPane.showConfirmDialog(SupplierDashboard.this, "Do you want to logout now?", "Logout", JOptionPane.YES_NO_OPTION);
				if (a == 0) {
					new Login().setVisible(true);
					dispose();
				}
			}
		});
		
		lblSupplieremail.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				SupplierAccount supplierAccount = new SupplierAccount();
				loader.jPanelLoader(panel_6, supplierAccount);
			}
		});
		
		btnDelivery.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				com.logilite.supplier.Delivery delivery = new com.logilite.supplier.Delivery();
				loader.jPanelLoader(panel_6, delivery);
			}
		});
		
		btnMyDeliveries.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MyDeliveries myDeliveries = new MyDeliveries();
				loader.jPanelLoader(panel_6, myDeliveries);
			}
		});
		
		btnMyAccount.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SupplierAccount supplierAccount = new SupplierAccount();
				loader.jPanelLoader(panel_6, supplierAccount);
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
		
		btnDeliveryDetail.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				PrintSupplierReport printSupplierReport = new PrintSupplierReport();
				loader.jPanelLoader(panel_6, printSupplierReport);
			}
		});
		
	}
}
