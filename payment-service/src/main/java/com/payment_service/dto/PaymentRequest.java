package com.payment_service.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class PaymentRequest {
     private UUID roomId;
     private long amount;
     private UUID bookingId;
     private String email;
}
