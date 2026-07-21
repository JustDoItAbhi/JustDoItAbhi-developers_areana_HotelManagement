package com.payment_service.clinet;

import com.commonlibrary.common_library.common.enums.RoomType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@JsonIgnoreProperties
public class BookingDetails {
    private UUID bookingId;
    private UUID userId;
    private String userEmail;
    private String userName;
    private Double amount;
    private String currency;
    private String transactionId;
    private LocalDateTime paymentDate;
    private String paymentMethod;
    private UUID roomId;
    private RoomType roomType;
    private LocalDateTime checkInDate;
    private LocalDateTime checkOutDate;
    private Integer numberOfGuests;
    private Boolean isSuccessful;
    private String failureReason;
    private String hotelName;

}
