package com.booking_service.event;

import com.booking_service.model.Booking;
import com.commonlibrary.common_library.common.enums.KafkaTopics;
import com.commonlibrary.common_library.common.event.BookingCreatedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class BookingEventListener {
    @Autowired
    private KafkaTemplate<String, Object> bookingKafkaTemplate;
    @Value("${spring.kafka.consumer.group-id}")
    private String groupName;

    @KafkaListener(topics = KafkaTopics.BOOKING_CREATED, groupId = "booking-group")
    public void handleRoomReserved(BookingCreatedEvent event) {
        // 1. Create booking record (status: PENDING)
        // 2. Call Payment Service via Feign to get payment link
        // 3. If successful, publish event
        Booking paymentEvent = new BookingInitiatedEvent(event.getReservationId());
        bookingKafkaTemplate.send("payment-initiate", paymentEvent);
    }
}
