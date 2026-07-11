package com.booking_service.event;

import com.commonlibrary.common_library.common.event.BookingCancelledEvent;
import com.commonlibrary.common_library.common.event.BookingCreatedEvent;
import com.commonlibrary.common_library.common.model.OutboxEvent;
import com.commonlibrary.common_library.common.outbox.repo.OutboxRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingEventPublisher {

  @Autowired
  private  OutboxRepository outboxRepository;
    @Autowired
    private  ObjectMapper objectMapper;

    @Transactional
    public void publishBookingCreated(UUID bookingId, String userEmail, UUID hotelId, UUID roomId,
                                      Instant checkInDate, Instant checkOutDate, double amount) {
        try {
            BookingCreatedEvent event = BookingCreatedEvent.builder()
                    .bookingId(bookingId)
                    .userEmail(userEmail)
                    .hotelId(hotelId)
                    .roomId(roomId)
                    .checkInDate(checkInDate)
                    .checkOutDate(checkOutDate)
                    .totalAmount(amount)
                    .build();

            String payload = objectMapper.writeValueAsString(event);

            OutboxEvent outboxEvent = OutboxEvent.builder()
                    .aggregateId(bookingId.toString())
                    .aggregateType("BOOKING")
                    .eventType("BOOKING_CREATED")
                    .payload(payload)
                    .topic("booking-created")
                    .status("PENDING")
                    .build();

            outboxRepository.save(outboxEvent);
            log.info("Booking created event saved to outbox for booking: {}", bookingId);

        } catch (Exception e) {
            log.error("Failed to save booking created event to outbox", e);
            throw new RuntimeException("Failed to publish booking created event", e);
        }
    }

    @Transactional
    public void publishBookingCancelled(UUID bookingId, UUID userId, UUID roomId, double refundAmount) {
        try {
            BookingCancelledEvent event = BookingCancelledEvent.builder()
                    .bookingId(bookingId)
                    .userId(userId)
                    .roomId(roomId)
                    .refundAmount(refundAmount)
                    .build();

            String payload = objectMapper.writeValueAsString(event);

            OutboxEvent outboxEvent = OutboxEvent.builder()
                    .aggregateId(bookingId.toString())
                    .aggregateType("BOOKING")
                    .eventType("BOOKING_CANCELLED")
                    .payload(payload)
                    .topic("booking-cancelled")
                    .status("PENDING")
                    .build();

            outboxRepository.save(outboxEvent);
            log.info("Booking cancelled event saved to outbox for booking: {}", bookingId);

        } catch (Exception e) {
            log.error("Failed to save booking cancelled event to outbox", e);
            throw new RuntimeException("Failed to publish booking cancelled event", e);
        }
    }
}