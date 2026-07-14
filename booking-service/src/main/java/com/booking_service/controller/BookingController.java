package com.booking_service.controller;

import com.booking_service.dto.BookingResponseDto;
import com.booking_service.dto.request.BookingRequestDto;
import com.booking_service.feignclient.FeignHotelResponseDto;
import com.booking_service.feignclient.FeignRoomResponseDto;
import com.booking_service.requestdto.FeignSearchHotelRequestDto;
import com.booking_service.responsedto.FeignSearchResponseDto;
import com.booking_service.service.BookingService;
import com.commonlibrary.common_library.common.annotation.Tracking;
import com.commonlibrary.common_library.common.ratelimit.RateLimit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/booking")
public class BookingController {
    @Autowired
    private BookingService bookingService;
    @PostMapping("/selectHotel")
    @RateLimit(value = 50,duration = 60000)
    public ResponseEntity<List<FeignSearchResponseDto>>selectHotel(@RequestParam(defaultValue = "0")int pageNumber,

                                                                   @RequestParam(defaultValue = "5")int pageSize,
                                                                   @RequestBody FeignSearchHotelRequestDto dto){
        return ResponseEntity.ok(bookingService.selectHotel(pageNumber,pageSize,dto));
    }

    @GetMapping("/{hotelId}")
    @Tracking
    @RateLimit(value = 50,duration = 60000)
    public ResponseEntity<FeignHotelResponseDto>getByHotelId(@PathVariable("hotelId") UUID hotelId) {
        return ResponseEntity.ok(bookingService.getHotelBYid(hotelId));
    }
    @GetMapping("getRoomsByHotelId/{hotelId}")
    @Tracking
    @RateLimit(value = 50,duration = 60000)
    public ResponseEntity<List<FeignRoomResponseDto>>getAllRoomByHotelId(@PathVariable("hotelId") UUID hotelId) {
        return ResponseEntity.ok(bookingService.getAllRoomsOfHotel(hotelId));
    }
    @GetMapping("/getRoomById/{roomId}")
    @Tracking
    @RateLimit(value = 50,duration = 60000)
    public ResponseEntity<FeignRoomResponseDto>selectRoomByroomId(@PathVariable("roomId") UUID roomId) {
        return ResponseEntity.ok(bookingService.selectRoomByID(roomId));
    }
    @PutMapping("/reserve/{userEmail}/{roomId}")
    @Tracking
    @RateLimit(value = 50,duration = 60000)
    public ResponseEntity<String>reserveRoomByroomId(@PathVariable("userEmail") String userEmail,@PathVariable("roomId") UUID roomId) {
        return ResponseEntity.ok(bookingService.reserveRoom(userEmail,roomId));
    }
    @PostMapping("/book")
    @Tracking
    @RateLimit(value = 50,duration = 60000)
    public ResponseEntity<BookingResponseDto>booking(@RequestBody BookingRequestDto dto) {
        return ResponseEntity.ok(bookingService.createBooking(dto));
    }
}
