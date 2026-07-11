package com.booking_service.feignclient;

import lombok.Data;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
@Data
public class FeignHotelResponseDto {
    private UUID hotelId;
    private String uniqueCode;
    private String hotelName;
    private String description;
    private int numberOfRooms;
    private boolean breakFast;
    private Instant checkInTime;
    private Instant CheckoutTime;
    private List<FeignAddressResponseDto> addressDtoList;
    private FeignRatingResponseDto rating;
    private List<FeignRoomResponseDto>roomList;
    private long phoneNumber;
    private String email;
    private int numberOfBranches;
}
