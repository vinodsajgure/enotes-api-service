package com.demo.enotes_api.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import com.demo.enotes_api.dto.EmailRequest;

import jakarta.mail.internet.MimeMessage;

@Component
public class EmailService {

	@Autowired
	private JavaMailSender mailsender;
	
	@Value("${spring.mail.username}")
	private String mailFrom;
	
	public void sendEmail(EmailRequest emailRequest) throws Exception {
		
		MimeMessage mimeMessage = mailsender.createMimeMessage();
		MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
		
		mimeMessageHelper.setFrom(mailFrom,emailRequest.getTitle());
		mimeMessageHelper.setTo(emailRequest.getTo());
		mimeMessageHelper.setSubject(emailRequest.getSubject());
		mimeMessageHelper.setText(emailRequest.getMessage(),true);
		
		mailsender.send(mimeMessage);
		
	}
	
}
