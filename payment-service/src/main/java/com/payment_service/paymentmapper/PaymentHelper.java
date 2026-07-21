package com.payment_service.paymentmapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.payment_service.paymentgateway.PaymentStatusService;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.model.checkout.Session;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;
@Slf4j
@Component
public class PaymentHelper {
    @Autowired
    private PaymentStatusService paymentStatusService;

    public  void handleCheckoutSessionCompleted(Event event) {
        System.out.println("INSIDE handleCheckoutSessionCompleted".toUpperCase());
        try {
            log.info("Event Type: {}", event.getType());

            var deserializer = event.getDataObjectDeserializer();

            log.info("Object present: {}", deserializer.getObject().isPresent());

            log.info("Raw object: {}", event.getData().getObject().toJson());
//            ObjectMapper mapper = new ObjectMapper();

            Session session = (Session) event
                    .getDataObjectDeserializer()
                    .getObject()
                    .orElseThrow(() ->
                            new RuntimeException("Unable to deserialize Checkout Session".toUpperCase()));

            String paymentIntentId = session.getPaymentIntent();
            PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentIntentId);
            String bookingId = paymentIntent.getMetadata().get("bookingId");
            String roomId = paymentIntent.getMetadata().get("roomId");
            String userEmail = paymentIntent.getMetadata().get("userEmail");
            log.info("Payment completed for booking {}".toUpperCase(), bookingId);
            paymentStatusService.updatePaymentStatus(
                    UUID.fromString(bookingId),
                    "SUCCESS",
                    paymentIntentId
            );

        } catch (Exception e) {
            log.error("Error handling checkout.session.completed", e);
        }
    }

    public  void handleCheckoutExpired(Event event) {
        try {
            Session session = (Session) event.getDataObjectDeserializer()
                    .getObject()
                    .orElseThrow(() -> new RuntimeException("Unable to deserialize Checkout Session"));
            String paymentIntentId = session.getPaymentIntent();
            PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentIntentId);
            String bookingId = paymentIntent.getMetadata().get("bookingId");
            String roomId = paymentIntent.getMetadata().get("roomId");
            String userEmail = paymentIntent.getMetadata().get("userEmail");
            paymentStatusService.updatePaymentStatus(
                    UUID.fromString(bookingId),
                    "FAILED",
                    paymentIntentId
            );
        } catch (Exception e) {
            log.error("Error handling checkout.session.expired", e);
        }
    }
}
