package com.logilite.admin;

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

public class Transaction extends JPanel
{

	private static final long serialVersionUID = 1L;
	public static JPanel panel;
	public static JTable table;
	public static JTextField searchField;
	public static JLabel lblX;
	public static JScrollPane scrollPane;
	public static JLabel lblSearch;
	public static JDialog dialog;
	int xx, xy;
	DefaultTableModel model;
	PurchaseDao purchase = new PurchaseDao();

	public Transaction()
	{
		initComponent();
		init();
	}
	
	private void init() {
		transactionTable();
		
		searchField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				table.setModel(new DefaultTableModel(null, new Object[] {"Payment ID", "Order ID", "User ID", "User Name", "User Phone", "Total", "Payment Date", "Payment Status"}));
				table.getColumnModel().getColumn(0).setPreferredWidth(185);
				table.getColumnModel().getColumn(1).setPreferredWidth(185);
				table.getColumnModel().getColumn(2).setPreferredWidth(50);
				table.getColumnModel().getColumn(3).setPreferredWidth(100);
				table.getColumnModel().getColumn(4).setPreferredWidth(93);
				table.getColumnModel().getColumn(5).setPreferredWidth(100);
				table.getColumnModel().getColumn(6).setPreferredWidth(90);
				table.getColumnModel().getColumn(7).setPreferredWidth(110);
				DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		        
		        for (int i = 0; i < table.getColumnCount(); i++) {
		            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
		        }
				table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
				table.getTableHeader().setReorderingAllowed(false);
				purchase.transaction(table, searchField.getText());
			}
		});
		
		table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
            	model = (DefaultTableModel) table.getModel();
                int rowIndex = table.getSelectedRow();
                openTransactionDetailsPopup(rowIndex);
            }
        });
	}
	
	private void transactionTable() {
		purchase.transaction(table, "");
		model = (DefaultTableModel) table.getModel();
		table.setRowHeight(30);
		table.setShowGrid(true);
		table.setGridColor(Color.black);
		table.setBackground(Color.white);
		table.setSelectionBackground(Color.LIGHT_GRAY);
	}
	
	private void openTransactionDetailsPopup(int rowIndex) {
        model = (DefaultTableModel) table.getModel();

        String paymentId = model.getValueAt(rowIndex, 0).toString();
        String orderId = model.getValueAt(rowIndex, 1).toString();
        String userId = model.getValueAt(rowIndex, 2).toString();
        String userName = model.getValueAt(rowIndex, 3).toString();
        String userPhone = model.getValueAt(rowIndex, 4).toString();
        String total = model.getValueAt(rowIndex, 5).toString();
        String paymentDate = model.getValueAt(rowIndex, 6).toString();
        String paymentStatus = model.getValueAt(rowIndex, 7).toString();

        dialog = new JDialog();
        dialog.setTitle("Transaction Details");
        dialog.setSize(400, 400);
        dialog.setLayout(new BoxLayout(dialog.getContentPane(), BoxLayout.Y_AXIS));

        addLabelWithSpacing(dialog, "Payment ID:", paymentId);
        addLabelWithSpacing(dialog, "Order ID:", orderId);
        addLabelWithSpacing(dialog, "User ID:", userId);
        addLabelWithSpacing(dialog, "User Name:", userName);
        addLabelWithSpacing(dialog, "User Phone:", userPhone);
        addLabelWithSpacing(dialog, "Total:", total);
        addLabelWithSpacing(dialog, "Payment Date:", paymentDate);
        addLabelWithSpacing(dialog, "Payment Status:", paymentStatus);

        JPanel buttonPanel = new JPanel();
        JButton okButton = new JButton("OK");

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
		panel.setBounds(0, 0, 962, 576);
		add(panel);
		panel.setLayout(null);
		
		table = new JTable();
		table.setBackground(new Color(222, 221, 218));
		table.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 14));
		table.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Payment ID", "Order ID", "User ID", "User Name", "User Phone", "Total", "Payment Date", "Payment Status"
			}
		) {
			boolean[] columnEditables = new boolean[] {
				false, false, false, false, false, false, false, false
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		table.getColumnModel().getColumn(0).setPreferredWidth(185);
		table.getColumnModel().getColumn(1).setPreferredWidth(185);
		table.getColumnModel().getColumn(2).setPreferredWidth(50);
		table.getColumnModel().getColumn(3).setPreferredWidth(100);
		table.getColumnModel().getColumn(4).setPreferredWidth(93);
		table.getColumnModel().getColumn(5).setPreferredWidth(100);
		table.getColumnModel().getColumn(6).setPreferredWidth(90);
		table.getColumnModel().getColumn(7).setPreferredWidth(110);
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.getTableHeader().setReorderingAllowed(false);
		scrollPane = new JScrollPane(table);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setBounds(23, 130, 931, 417);
		panel.add(scrollPane);
		
		searchField = new JTextField();
		searchField.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 14));
		searchField.setBounds(561, 88, 278, 30);
		panel.add(searchField);
		searchField.setColumns(10);
		
		lblSearch = new JLabel("Search");
		lblSearch.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 14));
		lblSearch.setBounds(500, 88, 59, 30);
		panel.add(lblSearch);
	}

}
