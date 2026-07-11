package com.booking_service.responsedto;

import lombok.Data;

import java.time.Instant;
import java.util.UUID;
@Data
public class FeignSearchResponseDto {
    private UUID hotelId;
    private Instant createdAt;
    private String uniqueCode;
    private String hotelName;
    private String description;
    private boolean breakFast;
    private Instant checkInTime;
    private Instant CheckoutTime;
    private int numberOfTotalRooms;
    private int rating;
}
