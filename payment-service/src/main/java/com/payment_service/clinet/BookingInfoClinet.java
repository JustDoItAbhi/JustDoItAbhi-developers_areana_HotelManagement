package com.payment_service.clinet;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "BOOKING-SERVICE")
public interface BookingInfoClinet {
    @GetMapping("/api/bookings/bookingResult/{bookingId}")
   public BookingDetails getFinalbooking(@PathVariable("bookingId") UUID bookingId);

}
