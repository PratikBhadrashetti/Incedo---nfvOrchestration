package com.nfv.mail;

import java.util.Properties;

import javax.annotation.Resource;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class SendMail {
	private String username;

	private String password;
	
	@Resource
    private Environment environment;

	@Async
	public void sendEmail(String toEmail, String fromEmail, String password, String subject, String body) throws Exception {
		username = fromEmail;
		this.password = password;
        Message message = new MimeMessage(getSession());
        message.setFrom(new InternetAddress(username));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
		message.setSubject(subject);
		message.setContent(body, "text/html; charset=utf-8");
        Transport.send(message);
	}
	
	private Session getSession() {
		Properties props = new Properties();
		props.put("mail.smtp.host", environment.getProperty("mail.smtp.host"));
		props.put("mail.smtp.auth", environment.getProperty("mail.smtp.auth"));
		props.put("mail.smtp.port", environment.getProperty("mail.smtp.port"));
		if (environment.getProperty("mail.server").equalsIgnoreCase("Incedo")) {
			props.put("mail.smtp.user", username);
			//props.put("mail.debug", "true");
			props.put("mail.smtp.starttls.enable", "true");
			props.put("mail.smtp.EnableSSL.enable", "true");
		} else {
		    props.put("mail.smtp.socketFactory.port", environment.getProperty("mail.smtp.socketFactory.port"));
			props.put("mail.smtp.socketFactory.class", environment.getProperty("mail.smtp.socketFactory.class"));
		}
		return Session.getInstance(props, new javax.mail.Authenticator() {
	        protected PasswordAuthentication getPasswordAuthentication() {
	            return new PasswordAuthentication(username, password);
	        }
	    });
	}
}