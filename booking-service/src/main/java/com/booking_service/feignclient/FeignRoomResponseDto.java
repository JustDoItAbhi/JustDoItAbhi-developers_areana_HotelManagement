package com.booking_service.feignclient;

import com.commonlibrary.common_library.common.enums.RoomType;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;
@Data
public class FeignRoomResponseDto {
    private UUID roomId;
    private RoomType roomType;
    private double price;
    private int numberOfPeopleAllowed;
    private UUID hotelId;
    private String roomStatus;
}
