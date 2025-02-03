package com.logilite.email;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;  

public class Email
{
	 public static void send(String to,String sub,String msg, File attachment) throws IOException{
		 Properties props = new Properties();
		 props.put("mail.smtp.host", "smtp.gmail.com");
		 props.put("mail.smtp.socketFactory.port", "465");
		 props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		 props.put("mail.smtp.auth", "true");
		 props.put("mail.smtp.port", "465");
		 
		 Session session = Session.getDefaultInstance(props,
				 new javax.mail.Authenticator() {
			 protected PasswordAuthentication getPasswordAuthentication() {
				 return new PasswordAuthentication("Your email", "Your email app password");
			 }
		 });
		 
		 try
		{
			MimeMessage message = new MimeMessage(session);
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			message.setSubject(sub);
			
			MimeBodyPart attachmentPart = new MimeBodyPart();
	        attachmentPart.attachFile(attachment);
	        
	        Multipart multipart = new MimeMultipart();
	        multipart.addBodyPart(attachmentPart);
	        
	        message.setContent(multipart);
            
			Transport.send(message);
		}
		catch (MessagingException e)
		{
			throw new RuntimeException(e);
		}
	 }
	 
	 public static boolean send(String to,String sub,String msg){
		 Properties props = new Properties();
		 props.put("mail.smtp.host", "smtp.gmail.com");
		 props.put("mail.smtp.socketFactory.port", "465");
		 props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		 props.put("mail.smtp.auth", "true");
		 props.put("mail.smtp.port", "465");
		 
		 Session session = Session.getDefaultInstance(props,
				 new javax.mail.Authenticator() {
			 protected PasswordAuthentication getPasswordAuthentication() {
				 return new PasswordAuthentication("Your email", "Your email app password");
			 }
		 });
		 
		 try
		{
			MimeMessage message = new MimeMessage(session);
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			message.setSubject(sub);
			
			message.setContent(msg, "text/html; charset=UTF-8");
						
			Transport.send(message);
			return true;
		}
		catch (MessagingException e)
		{
			return false;
		}
	 }
}
