package com.booking_service.feignclient;


import com.booking_service.dto.request.PaymentRequestDto;
import com.payment_service.controller.PaymentRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@FeignClient(name = "PAYMENT-SERVICE")
public interface PaymentFeignClient {
    @PostMapping("/api/pay/create")
    String pay(@RequestBody PaymentRequestDto request);
}
