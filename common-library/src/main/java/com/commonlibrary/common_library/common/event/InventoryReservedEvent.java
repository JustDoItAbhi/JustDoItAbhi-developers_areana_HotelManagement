package com.commonlibrary.common_library.common.event;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryReservedEvent {
    private UUID bookingId;
    private UUID roomId;
    private String userEmail;
    private LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;
    private int quantity;
    private double amount;
    private String hotelName;
    private String action;
    @Builder.Default
    private LocalDateTime reservedAt = LocalDateTime.now();
}