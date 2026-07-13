package com.inventory_service.service;


import com.commonlibrary.common_library.common.event.BookingCancelledEvent;
import com.commonlibrary.common_library.common.event.BookingCreatedEvent;
import com.commonlibrary.common_library.common.event.InventoryReleasedEvent;
import com.commonlibrary.common_library.common.event.InventoryReservedEvent;
import com.inventory_service.model.UserInventry;
import com.inventory_service.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Transactional
    public void reserveInventory(BookingCreatedEvent event) {
        try {
            UserInventry inventory = inventoryRepository.findByRoomId(event.getRoomId());
            if(inventory==null){
                throw new RuntimeException("Inventory not found for room: " + event.getRoomId());
            }

            if (inventory.getAvailableQuantity() < 1) {
                throw new RuntimeException("Not enough inventory for room: " + event.getRoomId());
            }
            inventory.setAvailableQuantity(inventory.getAvailableQuantity() - 1);
            inventory.setReservedQuantity(inventory.getReservedQuantity() + 1);
            inventoryRepository.save(inventory);

            // Publish inventory reserved event
            InventoryReservedEvent reservedEvent = InventoryReservedEvent.builder()
                    .bookingId(event.getBookingId())
                    .roomId(event.getRoomId())
                    .userEmail(event.getUserEmail())
                    .quantity(1)
                    .amount(event.getTotalAmount())
                    .build();

            kafkaTemplate.send("inventory-reserved", event.getBookingId().toString(), reservedEvent);
            log.info("Inventory reserved for booking: {}", event.getBookingId());

        } catch (Exception e) {
            log.error("Failed to reserve inventory for booking: {}", event.getBookingId(), e);
            throw e;
        }
    }

    @Transactional
    public void releaseInventory(BookingCancelledEvent event) {
        try {
            UserInventry inventory = inventoryRepository.findByRoomId(event.getRoomId());
            if(inventory==null){
                throw new RuntimeException("Inventory not found for room: " + event.getRoomId());
            }

            inventory.setAvailableQuantity(inventory.getAvailableQuantity() + 1);
            inventory.setReservedQuantity(inventory.getReservedQuantity() - 1);
            inventoryRepository.save(inventory);

            // Publish inventory released event
            InventoryReleasedEvent releasedEvent = InventoryReleasedEvent.builder()
                    .bookingId(event.getBookingId())
                    .roomId(event.getRoomId())
                    .userId(event.getUserId())
                    .quantity(1)
                    .reason("Booking cancelled")
                    .build();

            kafkaTemplate.send("inventory-released", event.getBookingId().toString(), releasedEvent);
            log.info("Inventory released for booking: {}", event.getBookingId());

        } catch (Exception e) {
            log.error("Failed to release inventory for booking: {}", event.getBookingId(), e);
            throw e;
        }
    }
}
