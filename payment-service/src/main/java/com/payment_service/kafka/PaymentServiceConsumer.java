package com.payment_service.kafka;

import com.commonlibrary.common_library.common.event.BookingCreatedEvent;
import com.payment_service.paymentgateway.PaymentService;
import com.stripe.exception.StripeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceConsumer {

    private final PaymentService paymentService;

    @KafkaListener(topics = "booking-created", groupId = "payment-group")
    public void handleBookingCreated(BookingCreatedEvent event) {
        try {
            log.info("Received booking created event: {}", event);

            // Convert amount to cents for Stripe
            long amountInCents = (long) (event.getTotalAmount() * 100);

            // Generate payment link
            String paymentUrl = paymentService.pay(
                    UUID.fromString(event.getRoomId().toString()), // Convert roomId to UUID
                    amountInCents,
                    event.getBookingId(),
                    event.getUserEmail()
            );

            log.info("Payment link generated for booking {}: {}",
                    event.getBookingId(), paymentUrl);

            // Here you would send email/SMS to user with payment link

        } catch (StripeException e) {
            log.error("Stripe payment failed for booking {}: {}",
                    event.getBookingId(), e.getMessage());

            // Send failure event
            sendPaymentFailureEvent(event.getBookingId(), e.getMessage());
        } catch (Exception e) {
            log.error("Error processing payment for booking {}: {}",
                    event.getBookingId(), e.getMessage());
        }
    }

    private void sendPaymentFailureEvent(UUID bookingId, String errorMessage) {
        // You can send a failure event to notify booking service
        log.info("Payment failed for booking: {}, reason: {}", bookingId, errorMessage);
        // eventProducer.sendEvent("payment-failed", failureEvent);
    }
}