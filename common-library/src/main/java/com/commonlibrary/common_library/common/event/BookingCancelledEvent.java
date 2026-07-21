package com.commonlibrary.common_library.common.event;

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
public class BookingCancelledEvent {
    private UUID bookingId;
    private UUID userId;
    private UUID roomId;
    private double refundAmount;
    @Builder.Default
    private LocalDateTime cancelledAt = LocalDateTime.now();
}
