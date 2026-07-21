package com.booking_service.consumer;

import com.booking_service.model.Booking;
import com.booking_service.repo.BookingRepository;
import com.commonlibrary.common_library.common.enums.KafkaTopics;
import com.commonlibrary.common_library.common.event.InventoryReleasedEvent;
import com.commonlibrary.common_library.common.event.InventoryReservedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Slf4j
@Service
public class BookingConumer {
    @Autowired
    private BookingRepository bookingRepository;

    @KafkaListener(topics = KafkaTopics.INVENTORY_RESERVED)
    public void reserveRoom(InventoryReservedEvent event){
        Optional<Booking> booking=bookingRepository.findById(event.getBookingId());
        if(booking.isEmpty()){
            throw new RuntimeException("INVALID BOOKING ID "+event.getBookingId());
        }
        Booking bookingstatus=booking.get();
        bookingstatus.setStatus("CONFIRMED".toUpperCase());
        bookingRepository.save(bookingstatus);
        log.error("BOOKING SUCCESSFULL TO BOOK A ROOM : {}",event.getAction());
    }

    @KafkaListener(topics = KafkaTopics.INVENTORY_RELEASED)
    public void releaseEvent(InventoryReleasedEvent event){
        Optional<Booking> booking=bookingRepository.findById(event.getBookingId());
        if(booking.isEmpty()){
            throw new RuntimeException("INVALID BOOKING ID "+event.getBookingId());
        }
        Booking bookingstatus=booking.get();
        bookingstatus.setStatus("FAIL".toUpperCase());
        bookingRepository.save(bookingstatus);
        log.error("BOOKING FAIL TO BOOK A ROOM : {}",event.getReason());
    }
}
