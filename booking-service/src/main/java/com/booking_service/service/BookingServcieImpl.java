package com.booking_service.service;

import com.booking_service.dto.RoomReserveDto;
import com.booking_service.dto.UserDto;
import com.booking_service.feignclient.*;
import com.booking_service.model.Booking;
import com.booking_service.repo.BookingRepository;
import com.booking_service.requestdto.FeignSearchHotelRequestDto;
import com.booking_service.responsedto.FeignSearchResponseDto;
import com.commonlibrary.common_library.common.enums.KafkaTopics;
import com.commonlibrary.common_library.common.event.BookingCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class BookingServcieImpl implements BookingService{

    @Autowired private  HotelFeignClinet hotelFeignClinet;
    @Autowired private UserClient userClient;
    @Autowired private SendEmailNotification sendEmailNotification;
    @Autowired private BookingRepository bookingRepository;
    @Autowired private KafkaTemplate<String, Object>kafkaTemplate;

@Autowired private PaymentFeignClinet paymentFeignClinet;

    @Override
    public List<FeignSearchResponseDto> selectHotel(int pageNumber, int pageSize, FeignSearchHotelRequestDto dto) {
        return hotelFeignClinet.getAllHotelsByFilters(pageNumber,pageSize,dto);
    }

    @Override
    public FeignHotelResponseDto getHotelBYid(UUID hotelId) {
        return hotelFeignClinet.getByHotelId(hotelId);
    }

    @Override
    public List<FeignRoomResponseDto> getAllRoomsOfHotel(UUID hotelId) {
        return hotelFeignClinet.getRoomsByHotelId(hotelId);
    }

    @Override
    public FeignRoomResponseDto selectRoomByID(UUID roomId) {
        return hotelFeignClinet.selectRoom(roomId);
    }

    @Override
    public String reserveRoom(String userEmail,UUID roomId) {
        UserDto userDto=userClient.getUser(userEmail);
        if(userDto==null){
            throw new RuntimeException("INVALID USER "+userEmail);
        }
        Optional<Booking>bookingOptional=bookingRepository.findByRoomId(roomId);
        if(bookingOptional.isPresent()){
            return "this is already reserved please choose another room"+roomId;
        }
        FeignRoomResponseDto dto=hotelFeignClinet.selectRoom(roomId);
        Booking booking=new Booking();
        booking.setUserEmail(userEmail);
        booking.setRoomType(dto.getRoomType());
        booking.setHotelId(dto.getHotelId());
        booking.setRoomId(dto.getRoomId());
        booking.setTotalAmount(780);
        bookingRepository.save(booking);
        BookingCreatedEvent event=new BookingCreatedEvent();
        event.setTotalAmount(780);
        event.setRoomId(roomId);
        event.setUserEmail(userDto.getEmail());
        event.setBookingId(booking.getId());
        event.setRoomType(dto.getRoomType());
        event.setHotelId(dto.getHotelId());
        RoomReserveDto reserveDto=hotelFeignClinet.resereveRoom(roomId);
        reserveDto.setBookingId(booking.getId());
        reserveDto.setUserEmail(booking.getUserEmail());
        if(reserveDto==null){
            throw new RuntimeException("ROOM CANNOT BE RESERVED PLEASE CONTACT ADMINISTRTOR");
        }
        String paymentLink=paymentFeignClinet.pay(booking.getRoomId(), (long) booking.getTotalAmount());
        kafkaTemplate.send(KafkaTopics.BOOKING_CREATED,event);
        String message ="ROOM RESERVED PLEASE PAY NOW ";
        sendEmailNotification.sendEmail(event.getUserEmail(),message,paymentLink);
        log.info("EMAIL SENT TO USER FOR PAYENT ");
        return paymentFeignClinet.pay(event.getRoomId(), (long) event.getTotalAmount());
    }
}
