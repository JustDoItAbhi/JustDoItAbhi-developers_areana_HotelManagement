package com.booking_service.feignclient;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.UUID;

@FeignClient(name = "payment-service",url = "http://localhost:9000/pay")
public interface PaymentFeignClinet {
    @PostMapping
    public String pay(@PathVariable("roomId") UUID roomId, @PathVariable("amount")long amount);
}
