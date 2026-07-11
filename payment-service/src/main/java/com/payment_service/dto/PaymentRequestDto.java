package com.payment_service.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class PaymentRequestDto {
    private UUID paymentId;
    private UUID userId;
    private UUID bookingId;
    private String transactionId;
    private UUID roomId;
    private long amount;

}
