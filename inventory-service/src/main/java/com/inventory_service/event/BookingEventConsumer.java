package com.inventory_service.event;


import com.commonlibrary.common_library.common.event.BookingCancelledEvent;
import com.commonlibrary.common_library.common.event.BookingCreatedEvent;
import com.inventory_service.service.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class BookingEventConsumer {

    private final InventoryService inventoryService;

    @KafkaListener(topics = "booking-created", groupId = "inventory-group")
    public void handleBookingCreated(BookingCreatedEvent event) {
        log.info("Received booking created event for inventory: {}", event.getBookingId());
        inventoryService.reserveInventory(event);
    }

    @KafkaListener(topics = "booking-cancelled", groupId = "inventory-group")
    public void handleBookingCancelled(BookingCancelledEvent event) {
        log.info("Received booking cancelled event for inventory: {}", event.getBookingId());
        inventoryService.releaseInventory(event);
    }
}
