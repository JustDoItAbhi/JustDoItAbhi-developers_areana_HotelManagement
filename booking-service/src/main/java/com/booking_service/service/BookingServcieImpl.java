package com.booking_service.service;

import com.booking_service.dto.BookingResponseDto;
import com.booking_service.dto.RoomReserveDto;
import com.booking_service.dto.UserDto;
import com.booking_service.dto.request.BookingRequestDto;
import com.booking_service.dto.request.PaymentRequestDto;
import com.booking_service.feignclient.*;
import com.booking_service.model.Booking;
import com.booking_service.repo.BookingRepository;
import com.booking_service.requestdto.FeignSearchHotelRequestDto;
import com.booking_service.responsedto.FeignSearchResponseDto;
import com.commonlibrary.common_library.common.enums.KafkaTopics;
import com.commonlibrary.common_library.common.event.BookingCreatedEvent;
import com.commonlibrary.common_library.common.kafka.EventProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
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
   @Autowired private PaymentFeignClient paymentFeignClinet;
    private final EventProducer eventProducer;

    public BookingServcieImpl(EventProducer eventProducer) {
        this.eventProducer = eventProducer;
    }
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
//        String paymentLink=paymentFeignClinet.pay(booking.getRoomId(), (long) booking.getTotalAmount());
//        kafkaTemplate.send(KafkaTopics.BOOKING_CREATED,event);
        String message ="ROOM RESERVED PLEASE PAY NOW ";
//        sendEmailNotification.sendEmail(event.getUserEmail(),message,paymentLink);
        log.info("EMAIL SENT TO USER FOR PAYENT ");
//        return paymentFeignClinet.pay(event.getRoomId(), (long) event.getTotalAmount());
    return "";
    }

    @Override
    public BookingResponseDto createBooking(BookingRequestDto dto) {
        // Get user details
        UserDto userDto = userClient.getUser(dto.getUserEmail());
        if (userDto == null) {
            throw new RuntimeException("INVALID USER");
        }

        // Check if room is available
        Optional<Booking> bookingOptional = bookingRepository.findByRoomId(dto.getRoomId());
        if (bookingOptional.isPresent()) {
            throw new RuntimeException("This room is already reserved. Please choose another room: " + dto.getRoomId());
        }

        // Get room details from Hotel Service
        FeignRoomResponseDto feignRoomResponseDto = hotelFeignClinet.selectRoom(dto.getRoomId());

        // Create booking
        Booking booking = new Booking();
        booking.setUserEmail(userDto.getEmail());
        booking.setRoomType(feignRoomResponseDto.getRoomType());
        booking.setHotelId(feignRoomResponseDto.getHotelId());
        booking.setRoomId(feignRoomResponseDto.getRoomId());
        booking.setTotalAmount(feignRoomResponseDto.getPrice());
//        booking.setCheckInDate(dto.getCheckInDate());
//        booking.setCheckOutDate(dto.getCheckOutDate());
        booking.setStatus("PENDING");
        bookingRepository.save(booking);

        // Create payment request
        PaymentRequestDto paymentRequestDto = new PaymentRequestDto();
        paymentRequestDto.setBookingId(booking.getId());
        paymentRequestDto.setRoomId(booking.getRoomId());
        paymentRequestDto.setAmount((long) (booking.getTotalAmount() * 100)); // Convert to cents
        paymentRequestDto.setEmail(booking.getUserEmail());

        // Call Payment Service
        String paymentUrl = null;
        try {
            paymentUrl = paymentFeignClinet.pay(paymentRequestDto);
            log.info("Payment URL received: {}", paymentUrl);
            booking.setUrl(paymentUrl);
            bookingRepository.save(booking);
        } catch (Exception e) {
            log.error("Failed to create payment: {}", e.getMessage());
            // Continue without payment link
        }

        // Create and send BookingCreatedEvent
        BookingCreatedEvent event = new BookingCreatedEvent();
        event.setBookingId(booking.getId());
        event.setUserEmail(booking.getUserEmail());
        event.setHotelId(booking.getHotelId());
        event.setRoomId(booking.getRoomId());
        event.setRoomType(booking.getRoomType());
        event.setTotalAmount(booking.getTotalAmount());
        event.setCheckInDate(booking.getCheckInDate());
        event.setCheckOutDate(booking.getCheckOutDate());
        event.setPaymentLink(paymentUrl);
        event.setCreatedAt(Instant.now());

        eventProducer.sendEvent(KafkaTopics.BOOKING_CREATED, event);
        log.info("Booking created event sent for booking: {}", booking.getId());

        return fromBookingEntity(booking);
    }

    private BookingResponseDto fromBookingEntity(Booking booking) {
        BookingResponseDto dto = new BookingResponseDto();
        dto.setBookngId(booking.getId());
        dto.setUserEmail(booking.getUserEmail());
        dto.setHotelId(booking.getHotelId());
        dto.setRoomId(booking.getRoomId());
        dto.setRoomType(booking.getRoomType());
        dto.setCheckInDate(booking.getCheckInDate());
        dto.setCheckOutDate(booking.getCheckOutDate());
        dto.setTotalAmount(booking.getTotalAmount());
        dto.setPaymentUrl(booking.getUrl()); // Add this field
        dto.setStatus(booking.getStatus());
        return dto;
    }
}
