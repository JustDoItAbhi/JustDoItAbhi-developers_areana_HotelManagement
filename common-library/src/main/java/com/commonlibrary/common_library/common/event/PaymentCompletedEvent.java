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
public class PaymentCompletedEvent {
    private UUID paymentId;
    private UUID bookingId;
    private UUID userId;
    private double amount;
    private String transactionId;
    @Builder.Default
    private Instant completedAt = Instant.now();
}
