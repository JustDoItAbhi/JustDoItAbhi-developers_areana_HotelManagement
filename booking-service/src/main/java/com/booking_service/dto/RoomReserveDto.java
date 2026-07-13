package com.booking_service.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class RoomReserveDto {
    private UUID roomId;
    private BigDecimal price;
    private String hotelName;
    private UUID hotelId;
    private String userEmail;
    private UUID bookingId;
}