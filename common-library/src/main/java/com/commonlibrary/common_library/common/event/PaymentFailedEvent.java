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
public class PaymentFailedEvent {
    private UUID bookingId;
    private String userEmail;
    private UUID roomId;
    private double amount;
    private String errorMessage;
    @Builder.Default
    private LocalDateTime failedAt = LocalDateTime.now();
}
