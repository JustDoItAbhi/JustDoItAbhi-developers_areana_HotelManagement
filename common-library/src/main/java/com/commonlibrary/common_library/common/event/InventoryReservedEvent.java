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
public class InventoryReservedEvent {
    private UUID bookingId;
    private UUID roomId;
    private UUID userId;
    private int quantity;
    private double amount;
    @Builder.Default
    private Instant reservedAt = Instant.now();
}