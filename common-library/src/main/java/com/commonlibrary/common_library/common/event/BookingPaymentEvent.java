package com.commonlibrary.common_library.common.event;

import lombok.Data;

import java.util.UUID;

@Data
public class BookingPaymentEvent {
    private UUID bookingId;
    private String userEmail;
    private Double amount;
    private String status; // SUCCESS, FAILED
    private String paymentLink;
    private String transactionId;
}
