package com.booking_service.event;

import com.commonlibrary.common_library.common.event.PaymentCompletedEvent;
import com.commonlibrary.common_library.common.event.PaymentFailedEvent;
import com.commonlibrary.common_library.common.saga.SagaOrchestrator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentEventConsumer {

    private final SagaOrchestrator sagaOrchestrator;

    @KafkaListener(topics = "payment-success", groupId = "booking-group")
    public void handlePaymentSuccess(PaymentCompletedEvent event) {
        log.info("Received payment success event for booking: {}", event.getBookingId());
        sagaOrchestrator.handlePaymentCompleted(event);
    }

    @KafkaListener(topics = "payment-failed", groupId = "booking-group")
    public void handlePaymentFailed(PaymentFailedEvent event) {
        log.info("Received payment failed event for booking: {}", event.getBookingId());
        sagaOrchestrator.handlePaymentFailed(event);
    }
}
