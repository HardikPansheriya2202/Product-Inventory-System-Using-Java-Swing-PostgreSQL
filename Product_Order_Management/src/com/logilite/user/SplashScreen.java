package com.logilite.user;

import javax.swing.*;
import java.awt.*;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.border.SoftBevelBorder;
import javax.swing.border.BevelBorder;

@SuppressWarnings("serial")
public class SplashScreen extends JFrame {
    public static JPanel panel;
    public static JProgressBar progressBar;
    public static JLabel lblPreparing;
    

    public SplashScreen() {
        initComponent();
        
        new Thread()
        {
        	@Override
        	public void run()
        	{
        		try
				{
					Thread.sleep(1000);
					Task task = new Task();
					task.execute();
				}
				catch (Exception e)
				{
					Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, e);
				}
        	}
        }.start();
    }
    
    public void initComponent() {
    	setSize(623, 455); 
    	setUndecorated(true);
        setLocationRelativeTo(null); 
        getContentPane().setLayout(null);
        
        panel = new JPanel();
        panel.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
        panel.setBounds(0, 0, 623, 455);
        panel.setBackground(Color.LIGHT_GRAY); 
        panel.setLayout(null);
        
        getContentPane().add(panel);
        
        JLabel label_1 = new JLabel("");
        label_1.setIcon(new ImageIcon(SplashScreen.class.getResource("/com/logilite/img/login_page_logo.png")));
        label_1.setBounds(187, 49, 219, 207);
        panel.add(label_1);
        
        JLabel lblWelcomeToProduct = new JLabel("Welcome To Product Inventory System");
        lblWelcomeToProduct.setFont(new Font("Dialog", Font.BOLD, 26));
        lblWelcomeToProduct.setHorizontalAlignment(SwingConstants.CENTER);
        lblWelcomeToProduct.setBounds(0, 268, 623, 33);
        panel.add(lblWelcomeToProduct);
        
        JLabel lblLogiliteTechnologies = new JLabel("LOGILITE TECHNOLOGIES");
        lblLogiliteTechnologies.setFont(new Font("Dialog", Font.BOLD, 12));
        lblLogiliteTechnologies.setBounds(27, 335, 181, 15);
        panel.add(lblLogiliteTechnologies);
        
        JLabel lblCopyrightc = new JLabel("Copyright (C) 2013");
        lblCopyrightc.setBounds(27, 362, 137, 15);
        panel.add(lblCopyrightc);
        
        progressBar = new JProgressBar();
        progressBar.setBorderPainted(false);
        progressBar.setFocusable(false);
        progressBar.setBounds(27, 418, 569, 14);
        panel.add(progressBar);
        
        lblPreparing = new JLabel("Preparing....");
        lblPreparing.setBounds(27, 391, 240, 15);
        panel.add(lblPreparing);
        
        JPanel panel_1 = new JPanel();
        panel_1.setBackground(new Color(154, 153, 150));
        panel_1.setBounds(0, 0, 623, 10);
        panel.add(panel_1);
        panel_1.setLayout(null);
        
        JPanel panel_2 = new JPanel();
        panel_2.setBackground(new Color(154, 153, 150));
        panel_2.setBounds(613, 0, 10, 455);
        panel.add(panel_2);
        panel_2.setLayout(null);
        
        JPanel panel_3 = new JPanel();
        panel_3.setBackground(new Color(154, 153, 150));
        panel_3.setBounds(0, 445, 623, 10);
        panel.add(panel_3);
        panel_3.setLayout(null);
        
        JPanel panel_4 = new JPanel();
        panel_4.setBackground(new Color(154, 153, 150));
        panel_4.setBounds(0, 0, 10, 454);
        panel.add(panel_4);
        setAlwaysOnTop(true);
    }
    
    class Task extends SwingWorker<Void, Void>
    {
    	@Override
    	public Void doInBackground()
    	{
    		Random ran = new Random();
    		int progress = 0;
    		setProgress(0);
    		
    		while(progress <= 100) {
    			try
				{
					Thread.sleep(ran.nextInt(1000));
				}
				catch (Exception e)
				{
					Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, e);
				}
    			progress += ran.nextInt(10);
    			setProgress(Math.min(progress, 100));
    			progressBar.setValue(progress);
    			
    			if (progress <= 20)
    			{
    				lblPreparing.setText("Preparing system "+ progress +"%");
    			}
    			else if (progress <= 50) {
					lblPreparing.setText("Preparing resources " + progress + "%");
				}
    			else if (progress <= 90) {
    				lblPreparing.setText("Preparing database " + progress + "%");
				}
    			else if (progress >= 100) {
    				lblPreparing.setText("Done....");
    				try
					{
						Thread.sleep(1000);
						setVisible(false);
					}
					catch (Exception e)
					{
						Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, e);
					}
				}
    		}
    		return null;
    	}
    }

    public void showSplash() {
        setVisible(true); 
        repaint(); 
    }

}
