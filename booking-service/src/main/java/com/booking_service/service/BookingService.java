package com.booking_service.service;

import com.booking_service.dto.BookingDetail;
import com.booking_service.dto.BookingResponseDto;
import com.booking_service.dto.request.BookingRequestDto;
import com.booking_service.feignclient.dto.FeignHotelResponseDto;
import com.booking_service.feignclient.dto.FeignRoomResponseDto;
import com.booking_service.requestdto.FeignSearchHotelRequestDto;
import com.booking_service.responsedto.FeignSearchResponseDto;

import java.util.List;
import java.util.UUID;

public interface BookingService {
    List<FeignSearchResponseDto> selectHotel(int pageNumber, int pageSize, FeignSearchHotelRequestDto dto);
    FeignHotelResponseDto getHotelBYid(UUID hotelId);
    List<FeignRoomResponseDto>getAllRoomsOfHotel(UUID hotelId);
    FeignRoomResponseDto selectRoomByID(UUID roomId);
//    String reserveRoom(String userEmail,UUID roomId);
    BookingResponseDto createBooking(BookingRequestDto dto);
    BookingDetail bookingResult(UUID bookingId);
}
