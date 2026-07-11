package com.booking_service.event;

import com.commonlibrary.common_library.common.enums.RoomType;
import com.commonlibrary.common_library.common.event.BookingCreatedEvent;
import com.commonlibrary.common_library.common.kafka.KafkaTopics;
import com.commonlibrary.common_library.common.model.OutboxEvent;
import com.commonlibrary.common_library.common.outbox.repo.OutboxRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;
@Service
@RequiredArgsConstructor
@Slf4j
public class BookingEvenPublisher {
    private final OutboxRepository outboxRepository;
    private final ObjectMapper objectMapper;


    @Transactional
    public void publishBookingCreated(UUID bookingId, String userEmail, UUID hotelId, UUID roomId,
                                    RoomType roomType, Instant checkInDate,Instant checkoutDate,double totalPrice) {
        try {

            BookingCreatedEvent event=new BookingCreatedEvent();
            event.setTotalAmount(totalPrice);
            event.setRoomId(roomId);
            event.setBookingId(bookingId);
            event.setRoomType(roomType);
            event.setHotelId(hotelId);
            event.setCreatedAt(Instant.now());
            event.setCheckInDate(Instant.now());
           event.setUserEmail(userEmail);

            String payload = objectMapper.writeValueAsString(event);

            OutboxEvent outboxEvent = OutboxEvent.builder()
                    .aggregateId(hotelId.toString())
                    .aggregateType("BOOKING")
                    .eventType("BOOKING_CREATED")
                    .payload(payload)
                    .topic(KafkaTopics.BOOKING_CREATED)
                    .status("PENDING")
                    .build();

            outboxRepository.save(outboxEvent);
            log.info("Booking created event saved to outbox for booking: {}", bookingId);

        } catch (Exception e) {
            log.error("Failed to save booking created event to outbox", e);
            throw new RuntimeException("Failed to publish booking created event", e);
        }
    }
}
