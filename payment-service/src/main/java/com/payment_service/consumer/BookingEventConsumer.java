package com.payment_service.consumer;

import com.commonlibrary.common_library.common.event.BookingCreatedEvent;
import com.commonlibrary.common_library.common.event.PaymentCompletedEvent;
import com.commonlibrary.common_library.common.event.PaymentFailedEvent;
import com.payment_service.event.PaymentEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class BookingEventConsumer {

    private final PaymentEventPublisher paymentEventPublisher;

    @KafkaListener(topics = "booking-created", groupId = "payment-group")
    public void handleBookingCreated(BookingCreatedEvent event) {
        log.info("Received booking created event for payment: {}", event.getBookingId());
        // For now, simulate payment success (in real scenario, we'd call payment gateway here)
        try {
            UUID paymentId = UUID.randomUUID();
            String transactionId = UUID.randomUUID().toString();
//            paymentEventPublisher.publishPaymentSuccess(
//                    paymentId,
//                    event.getBookingId(),
////                    event.getUserId(),
//                    event.getTotalAmount(),
//                    transactionId
//            );
        } catch (Exception e) {
            log.error("Payment processing failed for booking: {}", event.getBookingId(), e);
//            paymentEventPublisher.publishPaymentFailed(
//                    event.getBookingId(),
//                    event.getUserId(),
//                    event.getTotalAmount(),
//                    e.getMessage()
//            );
        }
    }
}
