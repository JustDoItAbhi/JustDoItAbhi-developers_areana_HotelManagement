package com.payment_service.controller;

import com.payment_service.paymentgateway.PaymentService;
import com.stripe.exception.StripeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/pay")
@RequiredArgsConstructor
@Slf4j
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/create")
    public String createPayment(@RequestBody PaymentRequest request) throws StripeException {
        log.info("Creating payment for booking: {}, room: {}, amount: {}",
                request.getBookingId(), request.getRoomId(), request.getAmount());

        return paymentService.pay(
                request.getRoomId(),
                request.getAmount(),
                request.getBookingId(),
                request.getEmail()
        );
    }
}