package com.commonlibrary.common_library.common.event;

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
public  class BookingConfirmedEvent {
    private UUID bookingId;
    private UUID userId;
    private UUID hotelId;
    private UUID roomId;
    private String hotelName;
    private String roomType;
    private Instant checkInDate;
    private Instant checkOutDate;
    private double totalAmount;
    private String userEmail;
    private String userName;
    @Builder.Default
    private Instant confirmedAt = Instant.now();
}
