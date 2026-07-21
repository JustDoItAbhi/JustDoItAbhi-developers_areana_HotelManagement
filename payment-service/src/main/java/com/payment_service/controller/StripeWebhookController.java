package com.payment_service.controller;


import com.payment_service.paymentmapper.PaymentHelper;
import com.payment_service.paymentgateway.PaymentStatusService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.net.Webhook;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/webhooks/stripe")
@Slf4j
public class StripeWebhookController {

    @Autowired
    private PaymentStatusService paymentStatusService;

    @Value("${stripe.webhook.secret}")
    private String webhookSecret;

    @Autowired
    private PaymentHelper paymentHelper;

    @PostMapping
    public ResponseEntity<String> handleWebhook(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String sigHeader) throws SignatureVerificationException {
        System.out.println("==== WEBHOOK RECEIVED ====");
        Event event = Webhook.constructEvent(payload, sigHeader, webhookSecret);
        System.out.println("EVENT TYPE = " + event.getType());
        log.info("PAYLOAD LOGS "+payload);
        switch (event.getType()) {
            case "checkout.session.completed":
                paymentHelper.handleCheckoutSessionCompleted(event);
                log.info("Preparing PAYMENT_SUCCESS event...: {}".toUpperCase(),event.getType());
                break;
            case "checkout.session.expired":
               paymentHelper.handleCheckoutExpired(event);
               log.error("PAYMENT FAILER : {}",event.getType());
                break;
            default:
                log.info("Unhandled event {}", event.getType());
        }
        return ResponseEntity.ok("received");
    }
}