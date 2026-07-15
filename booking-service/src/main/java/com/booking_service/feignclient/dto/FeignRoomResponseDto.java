package com.booking_service.feignclient.dto;

import com.commonlibrary.common_library.common.enums.RoomType;
import lombok.Data;

import java.util.UUID;
@Data
public class FeignRoomResponseDto {
    private UUID roomId;
    private RoomType roomType;
    private double roomPrice;
    private int numberOfPeopleAllowed;
    private UUID hotelId;
    private String roomStatus;
}
