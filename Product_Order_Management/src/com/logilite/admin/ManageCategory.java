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
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.logilite.dao.CategoryDao;
import com.logilite.dao.Statistics;

public class ManageCategory extends JPanel
{

	private static final long serialVersionUID = 1L;
	public static JPanel panel;
	public static JTable table;
	public static JTextField searchField;
	public static JTextField IDField;
	public static JTextField nameField;
	public static JTextField descriptionField;
	public static JButton btnSave;
	public static JButton btnUpdate;
	public static JButton btnDelete;
	public static JButton btnClear;
	public static JLabel lblSearch;
	public static JLabel lblCategoryId;
	public static JLabel lblCategoryName;
	public static JLabel lblDescription;
	int xx, xy;
	CategoryDao category = new CategoryDao();
	Color noEdit = new Color(204, 204, 204);
	DefaultTableModel model;
	int rowIndex;
	Statistics statistics = new Statistics();

	public ManageCategory()
	{
		initComponent();
		init();
		categoryTable();
	}
	
	private void init() {
		IDField.setBackground(noEdit);
		IDField.setText(String.valueOf(category.getMaxRow()));
		
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				model = (DefaultTableModel) table.getModel();
				rowIndex = table.getSelectedRow();
				IDField.setText(model.getValueAt(rowIndex, 0).toString());
				nameField.setText(model.getValueAt(rowIndex, 1).toString());
				descriptionField.setText(model.getValueAt(rowIndex, 2).toString());
			}
		});
		
		searchField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				table.setModel(new DefaultTableModel(null, new Object[] {"Category ID", "Category Name", "Description"}));

				DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		        
		        for (int i = 0; i < table.getColumnCount(); i++) {
		            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
		        }
		        
				category.getCategoryData(table, searchField.getText());
			}
		});
		
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (isEmpty()) {
					int id = Integer.parseInt(IDField.getText());
					String cname = nameField.getText();
					String desc = descriptionField.getText();
					if (!category.isIDExists(id)) {
						if (!category.isCategoryNameExists(cname)) {
							category.insert(id, cname, desc);
							table.setModel(new DefaultTableModel(null, new Object[] {"Category ID", "Category Name", "Description"}));
							
							DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
							centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
							
							for (int i = 0; i < table.getColumnCount(); i++) {
								table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
							}
							
							category.getCategoryData(table, "");
							clear();
						}else {
							JOptionPane.showMessageDialog(ManageCategory.this, "Category name already exists!", "Warning", 2);
						}
					}else {
						JOptionPane.showMessageDialog(ManageCategory.this, "Category id already exists!", "Warning", 2);
					}
				}
			}
		});
		
		btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (isEmpty()) {
					int id = Integer.parseInt(IDField.getText());
					if (category.isIDExists(id)) {
						if (!check()) {
							String cname = nameField.getText();
							String desc = descriptionField.getText();
							category.update(id, cname, desc);
							table.setModel(new DefaultTableModel(null, new Object[] {"Category ID", "Category Name", "Description"}));
							
							DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
							centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
							
							for (int i = 0; i < table.getColumnCount(); i++) {
								table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
							}
							
							category.getCategoryData(table, "");
							clear();
						}						
					}else {
						JOptionPane.showMessageDialog(ManageCategory.this, "Category id doesn't exists!", "Warning", 2);
					}
				}
			}
		});
		
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (IDField.getText().isEmpty()) {
					JOptionPane.showMessageDialog(ManageCategory.this, "Please select a category");
				}else {
					int id = Integer.parseInt(IDField.getText());
					if (category.isIDExists(id)) {
						category.delete(id);
						table.setModel(new DefaultTableModel(null, new Object[] {"Category ID", "Category Name", "Description"}));
						
						DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
						centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
						
						for (int i = 0; i < table.getColumnCount(); i++) {
							table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
						}
						
						category.getCategoryData(table, "");
						clear();
					}else {
						JOptionPane.showMessageDialog(ManageCategory.this, "Category id doesn't exists!", "Warning", 2);
					}
				}
			}
		});
		
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clear();
			}
		});
	}
	
	private void categoryTable() {
		category.getCategoryData(table, "");
		model = (DefaultTableModel) table.getModel();
		table.setRowHeight(30);
		table.setShowGrid(true);
		table.setGridColor(Color.black);
		table.setBackground(Color.white);
		table.setSelectionBackground(Color.LIGHT_GRAY);
	}
	
	private void clear() {
		searchField.setText("");
		IDField.setText(String.valueOf(category.getMaxRow()));
		nameField.setText("");
		descriptionField.setText("");
		table.clearSelection();
		statistics.admin();
	}
	
	private boolean isEmpty() {
		if (nameField.getText().isEmpty()) {
			JOptionPane.showMessageDialog(ManageCategory.this, "Category name is required", "Warning", 2);
			return false;
		}
		if (descriptionField.getText().isEmpty()) {
			JOptionPane.showMessageDialog(ManageCategory.this, "Description is required", "Warning", 2);
			return false;
		}
		return true;
	}
	
	private boolean check() {
		String newCategory = nameField.getText();
		String oldCategory = model.getValueAt(rowIndex, 1).toString();
		
		if (newCategory.equals(oldCategory)) {
			return false;
		}else {
			if (!newCategory.equals(oldCategory)) {
				boolean x = category.isCategoryNameExists(newCategory);
				if (x) {
					JOptionPane.showMessageDialog(ManageCategory.this, "This category already exists", "Warning", 2);
				}
				return x;
			}
		}
		return false;
	}
	
	@SuppressWarnings("serial")
	private void initComponent() {
		setLayout(null);
		
		panel = new JPanel();
		panel.setBounds(0, 0, 1029, 579);
		add(panel);
		panel.setLayout(null);

		table = new JTable();
		table.setBackground(new Color(222, 221, 218));
		table.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 14));
		table.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Category ID", "Category Name", "Description"
			}
		) {
			boolean[] columnEditables = new boolean[] {
				false, false, true
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
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBounds(412, 130, 523, 410);
		panel.add(scrollPane);
		
		searchField = new JTextField();
		searchField.setFont(new Font("DejaVu Sans Condensed", Font.BOLD, 14));
		searchField.setBounds(690, 90, 245, 30);
		panel.add(searchField);
		searchField.setColumns(10);
		
		IDField = new JTextField();
		IDField.setEditable(false);
		IDField.setFont(new Font("DejaVu Sans Condensed", Font.BOLD, 14));
		IDField.setBounds(107, 150, 254, 30);
		panel.add(IDField);
		IDField.setColumns(10);
		
		nameField = new JTextField();
		nameField.setFont(new Font("DejaVu Sans Condensed", Font.BOLD, 14));
		nameField.setBounds(107, 210, 254, 30);
		panel.add(nameField);
		nameField.setColumns(10);
		
		descriptionField = new JTextField();
		descriptionField.setFont(new Font("DejaVu Sans Condensed", Font.BOLD, 14));
		descriptionField.setBounds(107, 268, 254, 30);
		panel.add(descriptionField);
		descriptionField.setColumns(10);
		
		lblCategoryId = new JLabel("Category ID");
		lblCategoryId.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 14));
		lblCategoryId.setBounds(107, 130, 156, 15);
		panel.add(lblCategoryId);
		
		lblCategoryName = new JLabel("Category Name");
		lblCategoryName.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 14));
		lblCategoryName.setBounds(107, 189, 156, 15);
		panel.add(lblCategoryName);
		
		lblDescription = new JLabel("Description");
		lblDescription.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 14));
		lblDescription.setBounds(107, 248, 156, 15);
		panel.add(lblDescription);
		
		btnSave = new JButton("Save");
		btnSave.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnSave.setIcon(new ImageIcon(getClass().getResource("/com/logilite/img/save.png")));
		btnSave.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 16));
		btnSave.setBounds(107, 344, 117, 37);
		btnSave.setFocusPainted(false);
		panel.add(btnSave);
		
		btnUpdate = new JButton("Update");
		btnUpdate.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnUpdate.setIcon(new ImageIcon(getClass().getResource("/com/logilite/img/update.png")));
		btnUpdate.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 16));
		btnUpdate.setBounds(236, 344, 125, 37);
		btnUpdate.setFocusPainted(false);
		panel.add(btnUpdate);
		
		btnDelete = new JButton("Delete");
		btnDelete.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnDelete.setIcon(new ImageIcon(getClass().getResource("/com/logilite/img/remove.png")));
		btnDelete.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 16));
		btnDelete.setBounds(107, 405, 117, 37);
		btnDelete.setFocusPainted(false);
		panel.add(btnDelete);
		
		btnClear = new JButton("Clear");
		btnClear.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnClear.setIcon(new ImageIcon(getClass().getResource("/com/logilite/img/clear.png")));
		btnClear.setFont(new Font("DejaVu Serif Condensed", Font.BOLD, 16));
		btnClear.setBounds(236, 405, 125, 37);
		btnClear.setFocusPainted(false);
		panel.add(btnClear);
		
		lblSearch = new JLabel("Search");
		lblSearch.setFont(new Font("DejaVu Serif", Font.BOLD, 16));
		lblSearch.setBounds(602, 88, 70, 30);
		panel.add(lblSearch);
	}

}
