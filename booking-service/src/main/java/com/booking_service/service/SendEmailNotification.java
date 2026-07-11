package com.booking_service.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class SendEmailNotification {

  @Autowired private JavaMailSender mailSender;

  @Value("${spring.mail.username}")
  private String adminEmail;


    public void sendEmail(String sendEmail,String subject, String message ) {
        SimpleMailMessage email = new SimpleMailMessage();
        email.setFrom(adminEmail);
        email.setTo(sendEmail);
        email.setSubject(subject);
        email.setText(message);

        mailSender.send(email);
        System.out.println("email send to "+ Arrays.toString(email.getTo()));
    }
}
