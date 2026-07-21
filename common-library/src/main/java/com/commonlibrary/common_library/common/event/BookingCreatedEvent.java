package com.commonlibrary.common_library.common.event;

import com.commonlibrary.common_library.common.enums.RoomType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
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
    private LocalDateTime checkInDate;
    private LocalDateTime checkOutDate;
    private double totalAmount;
    private String paymentLink;
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();


}
