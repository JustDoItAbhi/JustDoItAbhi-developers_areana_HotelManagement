package com.inventory_service.service;


import com.commonlibrary.common_library.common.enums.KafkaTopics;
import com.commonlibrary.common_library.common.event.*;
import com.commonlibrary.common_library.common.kafka.EventProducer;
import com.inventory_service.model.UserInventry;
import com.inventory_service.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    @Autowired
    private EventProducer eventProducer;

    @KafkaListener(topics = KafkaTopics.PAYMENT_SUCCESS)
    public void inventryreserve(PaymentCompletedEvent event){
        try {
            UserInventry inventory = inventoryRepository.findByRoomId(event.getRoomId());

            if (inventory.getAvailableQuantity() < 1) {
                throw new RuntimeException("Not enough inventory for room: " + event.getRoomId());
            }
            inventory.setRoomId(event.getRoomId());
            inventory.setAmount(event.getAmount());
            inventory.setUserEmail(event.getUserEmail());
            inventory.setHotelName(event.getHotelName());
            inventory.setAvailableQuantity(inventory.getAvailableQuantity() - 1);
            inventory.setReservedQuantity(inventory.getReservedQuantity() + 1);
            inventoryRepository.save(inventory);

            InventoryReservedEvent reservedEvent = InventoryReservedEvent.builder()
                    .bookingId(event.getBookingId())
                    .roomId(event.getRoomId())
                    .hotelName(inventory.getHotelName())
                    .userEmail(event.getUserEmail())
                    .quantity(1)
                    .amount(event.getAmount())
                    .build();
            System.out.println("ADDED INVENTIRY SERVICES INVENTIRY RESERVED EVENT AND SEND TO KAFKA ");
            eventProducer.sendEvent(KafkaTopics.INVENTORY_RESERVED,reservedEvent);
            log.info("Inventory reserved for booking: {}", event.getBookingId());

        } catch (Exception e) {
            log.error("Failed to reserve inventory for booking: {}", event.getBookingId(), e);
            throw e;
        }
    }


    @KafkaListener(topics = KafkaTopics.PAYMENT_FAILED)
    public void releaseInventory(PaymentCompletedEvent event) {
        try {
            UserInventry inventory = inventoryRepository.findByRoomId(event.getRoomId());
            inventory.setHotelName(event.getHotelName());
            inventory.setAmount(event.getAmount());
            inventory.setRoomId(event.getRoomId());
            inventory.setUserEmail(event.getUserEmail());
            inventory.setBookingId(event.getBookingId());
            inventory.setReason(event.getFailureReason());
            inventory.setAvailableQuantity(inventory.getAvailableQuantity() + 1);
            inventory.setReservedQuantity(inventory.getReservedQuantity() - 1);
            inventoryRepository.save(inventory);

            InventoryReleasedEvent releasedEvent = InventoryReleasedEvent.builder()
                    .bookingId(event.getBookingId())
                    .roomId(event.getRoomId())
                    .userId(event.getUserId())
                    .quantity(1)
                    .reason("Booking cancelled")
                    .build();
            System.out.println("INVENTRY FAILED EVENT PASSED ");
            eventProducer.sendEvent(KafkaTopics.INVENTORY_RELEASED,releasedEvent);
            log.info("Inventory released for booking: {}", event.getBookingId());

        } catch (Exception e) {
            log.error("Failed to release inventory for booking: {}", event.getBookingId(), e);
            throw e;
        }
    }
}
