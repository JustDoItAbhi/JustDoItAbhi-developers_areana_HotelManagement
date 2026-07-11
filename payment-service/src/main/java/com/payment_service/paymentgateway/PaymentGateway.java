package com.payment_service.paymentgateway;

import com.stripe.exception.StripeException;

import java.util.UUID;

public interface PaymentGateway {

    String pay(UUID roomId, long amount) throws StripeException;
}
