package com.commonlibrary.common_library.common.mail;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

public class JavaMailCreator {
     @Autowired
    private final JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String adminMail;


    public JavaMailCreator(JavaMailSender javaMailSender) {
        this.javaMailSender=javaMailSender;
    }

    public void send(String to, String title, String message) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(adminMail);
            mailMessage.setTo(to);
            mailMessage.setSubject(title);
            mailMessage.setText(message);

            javaMailSender.send(mailMessage);
            System.out.println("Email sent successfully to ".toUpperCase()+ to);

        } catch (Exception e) {
            System.out.println("Failed to send email to "+to+" ERROR "+e.getMessage() );
            throw new RuntimeException("Failed to send email", e);
        }
}





}
