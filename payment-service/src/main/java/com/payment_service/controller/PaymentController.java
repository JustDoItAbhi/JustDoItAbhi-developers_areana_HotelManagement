package com.payment_service.controller;

import com.commonlibrary.common_library.common.ratelimit.RateLimit;
import com.payment_service.dto.PaymentRequestDto;
import com.payment_service.event.PaymentEventPublisher;
import com.payment_service.paymentgateway.PaymentGateway;
import com.stripe.exception.StripeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/pay")
@RequiredArgsConstructor
@Slf4j
public class PaymentController {

    private final PaymentGateway paymentGateway;
    private final PaymentEventPublisher paymentEventPublisher;

    @PostMapping
    @RateLimit(value = 50,duration = 60000)
    public ResponseEntity<String> pay(@PathVariable("roomId")UUID roomId, @PathVariable ("amount")long amount) throws StripeException {
        try {
            String paymentUrl = paymentGateway.pay(roomId, amount);
            return ResponseEntity.ok(paymentUrl);
        } catch (Exception e) {
            log.error("Payment failed for booking: {}", roomId, e);

            throw e;
        }
    }
}