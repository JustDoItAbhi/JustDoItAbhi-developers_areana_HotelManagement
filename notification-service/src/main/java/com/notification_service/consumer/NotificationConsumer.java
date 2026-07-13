
package com.notification_service.consumer;

import com.commonlibrary.common_library.common.enums.KafkaTopics;
import com.commonlibrary.common_library.common.event.BookingCreatedEvent;
import com.commonlibrary.common_library.common.event.NotificationEvent;
import com.commonlibrary.common_library.common.event.PaymentCompletedEvent;
import com.commonlibrary.common_library.common.event.UserRegisteredEvent;

import com.notification_service.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class NotificationConsumer {
@Autowired
    private  NotificationService notificationService;

    @KafkaListener(topics = KafkaTopics.BOOKING_CANCELLED, groupId = "notification-group")
    public void handleBookingCreated(BookingCreatedEvent event) {
        log.info("Received booking created event for notification: {}", event.getBookingId());
        notificationService.sendBookingConfirmation(event);
    }

    @KafkaListener(topics = "payment-success", groupId = "notification-group")
    public void handlePaymentSuccess(PaymentCompletedEvent event) {
        log.info("Received payment success event for notification: {}", event.getBookingId());
        notificationService.sendPaymentSuccess(event);
    }

    @KafkaListener(topics = "notification-email", groupId = "notification-group")
    public void handleEmailNotification(NotificationEvent event) {
        log.info("Received email notification for: {}", event.getRecipient());
        notificationService.sendEmail(event);
    }

    @KafkaListener(topics = "user-registered", groupId = "notification-group")
    public void handleUserRegistered(UserRegisteredEvent event) {
        log.info("Received user registered event for: {}", event.getEmail());
        notificationService.sendWelcomeEmail(event);
    }
}