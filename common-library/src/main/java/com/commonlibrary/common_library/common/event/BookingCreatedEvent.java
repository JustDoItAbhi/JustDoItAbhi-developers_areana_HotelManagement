package com.commonlibrary.common_library.common.event;

import com.commonlibrary.common_library.common.enums.RoomType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingCreatedEvent {
    private UUID bookingId;
    private String userEmail;
    private UUID hotelId;
    private UUID roomId;
    private RoomType roomType;
    private Instant checkInDate;
    private Instant checkOutDate;
    private double totalAmount;
    @Builder.Default
    private Instant createdAt = Instant.now();


}
