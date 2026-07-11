package com.notification_service.service;

import com.commonlibrary.common_library.common.event.BookingCreatedEvent;
import com.commonlibrary.common_library.common.event.NotificationEvent;
import com.commonlibrary.common_library.common.event.PaymentCompletedEvent;
import com.commonlibrary.common_library.common.event.UserRegisteredEvent;

import com.notification_service.email.SendEmailNotification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificationService {
@Autowired
    private  KafkaTemplate<String, Object> kafkaTemplate;
    @Autowired
    private SendEmailNotification emailNotifiation;

    @Async
    public void sendBookingConfirmation(BookingCreatedEvent event) {
        String message = String.format(
                "Dear Customer,\n\n" +
                        "Your booking has been confirmed!\n" +
                        "Booking ID: %s\n" +
                        "Hotel ID: %s\n" +
                        "Room ID: %s\n" +
                        "Check-in: %s\n" +
                        "Check-out: %s\n" +
                        "Total Amount: $%.2f\n\n" +
                        "Thank you for choosing our service!",
                event.getBookingId(),
                event.getHotelId(),
                event.getRoomId(),
                event.getCheckInDate(),
                event.getCheckOutDate(),
                event.getTotalAmount()
        );

        // Send email notification
        NotificationEvent emailEvent = NotificationEvent.builder()
                .recipient(event.getUserEmail()) // Get from user service
                .subject("Booking Confirmation - " + event.getBookingId())
                .message(message)
                .email(event.getUserEmail())
                .build();

        kafkaTemplate.send("notification-email", emailEvent);
        String subject ="ROOM RESERVED PLEASE PAY ";
        emailNotifiation.sendEmail(event.getUserEmail(),subject,message);
        log.info("Booking confirmation notification queued for: {}", event.getBookingId());
    }

    @Async
    public void sendPaymentSuccess(PaymentCompletedEvent event) {
        String message = String.format(
                "Dear Customer,\n\n" +
                        "Your payment of $%.2f has been successfully processed.\n" +
                        "Payment ID: %s\n" +
                        "Transaction ID: %s\n\n" +
                        "Thank you for your payment!",
                event.getAmount(),
                event.getPaymentId(),
                event.getTransactionId()
        );

        NotificationEvent emailEvent = NotificationEvent.builder()
                .recipient("customer@example.com")
                .subject("Payment Successful - " + event.getBookingId())
                .message(message)
                .email("EMAIL")
                .build();

        kafkaTemplate.send("notification-email", emailEvent);
        log.info("Payment success notification queued for: {}", event.getBookingId());
    }

    @Async
    public void sendWelcomeEmail(UserRegisteredEvent event) {
        String message = String.format(
                "Welcome %s!\n\n" +
                        "Thank you for registering with us.\n" +
                        "Your username is: %s\n" +
                        "Your email is: %s\n\n" +
                        "We're excited to have you on board!",
                event.getFullName(),
                event.getUsername(),
                event.getEmail()

        );

        NotificationEvent emailEvent = NotificationEvent.builder()
                .recipient(event.getEmail())
                .subject("Welcome to Our Hotel Booking Platform")
                .message(message)
                .email("EMAIL")
                .build();

        kafkaTemplate.send("notification-email", emailEvent);
        log.info("Welcome email queued for: {}", event.getEmail());
    }

    @Async
    public void sendEmail(NotificationEvent event) {
        log.info("Sending email to: {}", event.getRecipient());
        log.info("Subject: {}", event.getSubject());
        log.info("Message: {}", event.getMessage());
        // In production, integrate with actual email service (SendGrid, AWS SES, etc.)
    }
}
