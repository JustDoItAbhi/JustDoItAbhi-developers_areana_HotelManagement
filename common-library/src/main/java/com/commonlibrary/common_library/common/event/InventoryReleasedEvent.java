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
public class InventoryReleasedEvent {
    private UUID bookingId;
    private UUID roomId;
    private UUID userId;
    private int quantity;
    private String reason;
    @Builder.Default
    private Instant releasedAt = Instant.now();
}
