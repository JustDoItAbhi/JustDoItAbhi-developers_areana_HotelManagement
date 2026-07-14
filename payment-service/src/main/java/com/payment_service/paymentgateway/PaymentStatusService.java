package com.payment_service.paymentgateway;

import com.commonlibrary.common_library.common.event.BookingPaymentEvent;
import com.commonlibrary.common_library.common.kafka.EventProducer;
import com.payment_service.model.Payment;
import com.payment_service.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentStatusService {

    private final PaymentRepository paymentRepository;
    private final EventProducer eventProducer;

    public void updatePaymentStatus(UUID bookingId, String status, String transactionId) {
        // Update payment in database
        Optional<Payment> paymentOpt = paymentRepository.findByBookingId(bookingId);
        Payment payment;
        if (paymentOpt.isPresent()) {
            payment = paymentOpt.get();
            payment.setStatus(status);
            payment.setTransactionId(transactionId);
        } else {
            payment = new Payment();
            payment.setBookingId(bookingId);
            payment.setStatus(status);
            payment.setTransactionId(transactionId);
        }
        paymentRepository.save(payment);

        // Send event to notify other services
        BookingPaymentEvent event = new BookingPaymentEvent();
        event.setBookingId(bookingId);
        event.setStatus(status);
        event.setTransactionId(transactionId);

        if ("SUCCESS".equals(status)) {
            event.setStatus("SUCCESS");
            eventProducer.sendEvent("payment-success", event);
            log.info("Payment success event sent for booking: {}", bookingId);
        } else if ("FAILED".equals(status)) {
            event.setStatus("FAILED");
            eventProducer.sendEvent("payment-failed", event);
            log.info("Payment failed event sent for booking: {}", bookingId);
        }
    }
}
