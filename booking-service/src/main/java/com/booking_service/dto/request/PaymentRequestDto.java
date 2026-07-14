package com.booking_service.dto.request;

import lombok.Data;

import java.util.UUID;

@Data
public class PaymentRequestDto {
     private UUID roomId;
     private long amount;
     private UUID bookingId;
     private String email;


}
