package com.booking_service.dto.request;

import lombok.Data;

import java.util.UUID;

@Data
public class BookingRequestDto {
private String userEmail;
private UUID roomId;
private UUID hotelId;
}
