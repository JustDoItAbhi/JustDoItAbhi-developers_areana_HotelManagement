package com.inventory_service.clinet;

import com.commonlibrary.common_library.common.annotation.Tracking;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.UUID;

@FeignClient(name = "booking-service",url = "http://localhost:8087/api/booking")
public interface BookingClient {

    @PutMapping("/reserve/{roomId}")
    public String reserveRoomByroomId(@PathVariable("roomId") UUID roomId);
}
