package com.booking_service.responsedto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;
@Data
public class FeignSearchResponseDto {
    private UUID hotelId;
    private LocalDateTime createdAt;
    private String uniqueCode;
    private String hotelName;
    private String description;
    private boolean breakFast;
    private LocalDateTime checkInTime;
    private LocalDateTime CheckoutTime;
    private int numberOfTotalRooms;
    private int rating;
}
