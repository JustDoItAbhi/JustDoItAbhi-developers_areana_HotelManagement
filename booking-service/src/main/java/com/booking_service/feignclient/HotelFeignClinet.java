package com.booking_service.feignclient;

import com.booking_service.dto.RoomReserveDto;
import com.booking_service.feignclient.dto.FeignHotelResponseDto;
import com.booking_service.feignclient.dto.FeignRoomResponseDto;
import com.booking_service.requestdto.FeignSearchHotelRequestDto;
import com.booking_service.responsedto.FeignSearchResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "HOTEL-SERVICE")
public interface HotelFeignClinet {

    @PostMapping("/api/hotel/search")
    public List<FeignSearchResponseDto> getAllHotelsByFilters(
            @RequestParam(defaultValue = "0")int pageNumber,
            @RequestParam(defaultValue = "5")int pageSize
            ,@RequestBody FeignSearchHotelRequestDto filers);


    @GetMapping("/api/hotel/{hotelId}")
    public FeignHotelResponseDto getByHotelId(@PathVariable("hotelId") UUID hotelId);


    @GetMapping("/api/hotel/getRoomsByHotelId/{hotelId}")
    public List<FeignRoomResponseDto>getRoomsByHotelId (@PathVariable("hotelId")UUID hotelId);

    @GetMapping("/api/hotel/getRoomsByRoomId/{roomId}")
    public FeignRoomResponseDto selectRoom(@PathVariable("roomId")UUID roomId);

    @PutMapping("/api/hotel/reserveRoom/{roomId}")
    public RoomReserveDto resereveRoom(@PathVariable("roomId")UUID roomId);

}
