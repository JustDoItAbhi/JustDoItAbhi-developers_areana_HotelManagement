package com.booking_service.dto;

import com.commonlibrary.common_library.common.enums.RoomType;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;
@Data
public class BookingResponseDto {
    private UUID bookngId;
    private String userEmail;
    private UUID hotelId;
    private UUID roomId;
    private RoomType roomType;
    private Instant checkInDate;
    private Instant checkOutDate;
    private double totalAmount;
    private String paymentUrl;
    private String status;

}
