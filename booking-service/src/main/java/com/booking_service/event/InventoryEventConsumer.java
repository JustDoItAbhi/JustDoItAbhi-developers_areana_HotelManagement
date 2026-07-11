package com.booking_service.event;


import com.commonlibrary.common_library.common.event.InventoryReleasedEvent;
import com.commonlibrary.common_library.common.event.InventoryReservedEvent;
import com.commonlibrary.common_library.common.saga.SagaOrchestrator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class InventoryEventConsumer {

    private final SagaOrchestrator sagaOrchestrator;

    @KafkaListener(topics = "inventory-reserved", groupId = "booking-group")
    public void handleInventoryReserved(InventoryReservedEvent event) {
        log.info("Received inventory reserved event for booking: {}", event.getBookingId());
        sagaOrchestrator.handleInventoryReserved(event.getBookingId());
    }

    @KafkaListener(topics = "inventory-released", groupId = "booking-group")
    public void handleInventoryReleased(InventoryReleasedEvent event) {
        log.info("Received inventory released event for booking: {}", event.getBookingId());
        // Update booking status to cancelled
    }
}
