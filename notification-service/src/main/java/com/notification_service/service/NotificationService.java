package com.notification_service.service;

import com.commonlibrary.common_library.common.enums.KafkaTopics;
import com.commonlibrary.common_library.common.event.*;

import com.commonlibrary.common_library.common.kafka.EventProducer;
import com.commonlibrary.common_library.common.mail.JavaMailCreator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificationService {

    @Value("${spring.mail.username}")
    private String adminEmail;

    @Autowired
    private EventProducer eventProducer;
    @Autowired
    private JavaMailCreator javaMailCreator;
@KafkaListener(topics = KafkaTopics.BOOKING_CREATED)
   public void bookingCreated (BookingCreatedEvent event){
    System.out.println("REQUEST REACHED TO NOTIFICATION SERVICE ");
    javaMailCreator.send(event.getUserEmail(),"BOOKING CREATED PLEASE PAY ",event.getPaymentLink());
    log.info("INVENTORY BOOKING LINK HAS BEEN PUBLISH {}  ",event.getUserEmail());

   }

   @KafkaListener(topics = KafkaTopics.PAYMENT_SUCCESS)
    public void paymentStatus(PaymentCompletedEvent event){
       System.out.println("PAYMENT STATUS UPDATED ");
    String message ="PAYMENT CONGRATULATIONS "+event.getUserName()+" FOR USING OUT SERVICE AS YOUR ROOM TYPE  "+event.getRoomType()+" BOOKED BY "+event.getUserEmail();
    javaMailCreator.send(event.getUserEmail(),"PAYMENT CONFIRMED "+event.getIsSuccessful(),message);
    log.info("EMAIL SEND TO USER WITH PAYMENT CONFIMED  :{}",event.getUserEmail());
}
    @KafkaListener(topics = KafkaTopics.PAYMENT_FAILED)
    public void paymentFail(PaymentCompletedEvent event){
        System.out.println("PAYMENT FAIL STATUS UPDATED ");
        String message ="PAYMENT DETAILS "+event.getIsSuccessful()+" "+event.getRoomType()+" "+event.getUserEmail();
        javaMailCreator.send(event.getUserEmail(),"PAYMENT STATUS "+event.getIsSuccessful(),message);
        log.info("EMAIL SEND TO USER WITH :{}",event.getUserEmail());
    }

@KafkaListener(topics = KafkaTopics.USER_REGISTERED)
    public void userRegistered(UserRegisteredEvent event){
    System.out.println("USER REGISTERED EVENT ");
    String messge ="PLEASE LOGIN "+" http://localhost:9000/api/users/login";
    javaMailCreator.send(event.getEmail(),"SIGN UP SUCCESSFULL ",messge);
    System.out.println("EMAIL SENT BY USER SERVICE ");
    log.info("User registered: {}", event.getEmail());
}

@KafkaListener(topics = KafkaTopics.USER_LOGIN)
    public void loginNotification(UserLoginEvent event){
    String messge ="welcome back "+event.getUserName();
    javaMailCreator.send(event.getUserEmail(),"LOGIN SUCCESSFULL ",messge);
    System.out.println("EMAIL SENT BY NOTIFICATION SERVICE ");
}

    @KafkaListener(topics = KafkaTopics.HOTEL_CREATED)
    public void hotelCreated(HotelCreatedEvent event){
        String messge ="CONGRATULATIONS FOR NEW HOTEL ENTRY  "+event.getHotelName();
        javaMailCreator.send(adminEmail,"HOTEL ADDED SUCCESSFULL ",messge);
        System.out.println("EMAIL SENT BY NOTIFICATION SERVICE FOR NEW HOTEL ");
    }
}
