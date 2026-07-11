package com.commonlibrary.common_library.common.saga;

import com.commonlibrary.common_library.common.event.BookingCreatedEvent;
import com.commonlibrary.common_library.common.event.PaymentCompletedEvent;
import com.commonlibrary.common_library.common.event.PaymentFailedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
@Slf4j
public class SagaOrchestrator {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ConcurrentHashMap<UUID, SagaState> sagaStates = new ConcurrentHashMap<>();

    public void startBookingSaga(BookingCreatedEvent event) {
        UUID bookingId = event.getBookingId();
        SagaState state = new SagaState(bookingId);
        state.setCurrentStep(SagaStep.INVENTORY_RESERVATION);
        sagaStates.put(bookingId, state);

        log.info("Starting booking saga for booking: {}", bookingId);
        kafkaTemplate.send("inventory-reserved", bookingId.toString(), event);
    }

    public void handleInventoryReserved(UUID bookingId) {
        SagaState state = sagaStates.get(bookingId);
        if (state == null) {
            log.warn("No saga found for booking: {}", bookingId);
            return;
        }

        state.setCurrentStep(SagaStep.PAYMENT);
        log.info("Inventory reserved for booking: {}, proceeding to payment", bookingId);
        kafkaTemplate.send("payment-request", bookingId.toString(),
                Map.of("bookingId", bookingId, "amount", state.getAmount()));
    }

    public void handlePaymentCompleted(PaymentCompletedEvent event) {
        UUID bookingId = event.getBookingId();
        SagaState state = sagaStates.get(bookingId);
        if (state == null) {
            log.warn("No saga found for booking: {}", bookingId);
            return;
        }

        state.setCurrentStep(SagaStep.BOOKING_CONFIRMATION);
        state.setCompleted(true);
        log.info("Payment completed for booking: {}, saga complete", bookingId);
        sagaStates.remove(bookingId);
    }

    public void handlePaymentFailed(PaymentFailedEvent event) {
        UUID bookingId = event.getBookingId();
        SagaState state = sagaStates.get(bookingId);
        if (state == null) {
            log.warn("No saga found for booking: {}", bookingId);
            return;
        }

        // Compensating transaction
        log.info("Payment failed for booking: {}, starting compensation", bookingId);
        state.setCurrentStep(SagaStep.COMPENSATION);

        kafkaTemplate.send("inventory-released", bookingId.toString(),
                Map.of("bookingId", bookingId));

        sagaStates.remove(bookingId);
    }

    public enum SagaStep {
        INVENTORY_RESERVATION,
        PAYMENT,
        BOOKING_CONFIRMATION,
        COMPENSATION
    }

    @lombok.Data
    public static class SagaState {
        private final UUID bookingId;
        private SagaStep currentStep;
        private boolean completed = false;
        private Double amount;

        public SagaState(UUID bookingId) {
            this.bookingId = bookingId;
        }
    }
}