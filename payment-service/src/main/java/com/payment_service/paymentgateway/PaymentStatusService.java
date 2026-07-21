package com.payment_service.paymentgateway;

import com.commonlibrary.common_library.common.enums.KafkaTopics;
import com.commonlibrary.common_library.common.event.PaymentCompletedEvent;
import com.commonlibrary.common_library.common.exception.excepitons.BookingDetailsNotFoundException;
import com.commonlibrary.common_library.common.kafka.EventProducer;
import com.payment_service.clinet.BookingDetails;
import com.payment_service.clinet.BookingInfoClinet;
import com.payment_service.model.Payment;
import com.payment_service.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentStatusService {

    private final PaymentRepository paymentRepository;
    private final EventProducer eventProducer;
    private final BookingInfoClinet bookingInfoClient;


    @Transactional
    public void updatePaymentStatus(UUID bookingId, String status, String transactionId) {
        try {
            log.info("Updating payment status for booking {} to {}".toUpperCase(), bookingId, status);

            Optional<Payment> paymentOpt = paymentRepository.findByBookingId(bookingId);
            Payment payment = paymentOpt.orElseThrow(() ->
                    new RuntimeException("Payment not found for booking: ".toUpperCase() + bookingId));

            payment.setStatus(status);
            payment.setTransactionId(transactionId);
            payment.setUpdatedAt(LocalDateTime.now());

            if ("SUCCESS".equals(status)) {
                System.out.println("PAYMENT SUCCCESSFULLY ADDED ");
                payment.setCreatedAt(LocalDateTime.now());
            }

            paymentRepository.save(payment);
            System.out.println("PAYMENT SAVED");
            if ("SUCCESS".equals(status)) {
                handlePaymentSuccess(bookingId, transactionId);
            } else if ("FAILED".equals(status)) {
                handlePaymentFailure(bookingId, transactionId);
            }

        } catch (Exception e) {
            log.error("Failed to update payment status for booking {}: {}", bookingId, e.getMessage(), e);
            throw e;
        }
    }

    private void handlePaymentSuccess(UUID bookingId, String transactionId) {
        System.out.println("INSIDE SUCCESS");
        try {
            BookingDetails bookingDetails = bookingInfoClient.getFinalbooking (bookingId);
            if(bookingDetails==null){
                throw new BookingDetailsNotFoundException("INVLAID  BOOKING DETAILS "+bookingId);
            }
            System.out.println("BOOKING DETAILS IS CALLLED BY BOOKING ID  "+bookingDetails.getBookingId());
            log.info("BookingDetails = {}".toUpperCase(), bookingDetails);
            PaymentCompletedEvent event = new PaymentCompletedEvent();
            event.setBookingId(bookingId);
            event.setUserId(bookingDetails.getUserId());
            event.setUserEmail(bookingDetails.getUserEmail());
            event.setUserName(bookingDetails.getUserName());
            event.setAmount(bookingDetails.getAmount());
            event.setCurrency("USD");
            event.setTransactionId(transactionId);
            event.setPaymentDate(LocalDateTime.now());
            event.setPaymentMethod("CARD");
            event.setRoomId(bookingDetails.getRoomId());
            event.setHotelName(bookingDetails.getHotelName());
            event.setRoomType(bookingDetails.getRoomType());
            event.setCheckInDate(bookingDetails.getCheckInDate());
            event.setCheckOutDate(bookingDetails.getCheckOutDate());
            event.setNumberOfGuests(bookingDetails.getNumberOfGuests());
            event.setIsSuccessful(true);
            System.out.println("SENDING TO KAFKA");
            eventProducer.sendEvent(KafkaTopics.PAYMENT_SUCCESS, event);
            log.info("Payment success event sent for booking: {}", bookingId);

        } catch (Exception e) {
            log.error("Error handling payment success for booking {}: {}", bookingId, e.getMessage(), e);
        }
    }

    private void handlePaymentFailure(UUID bookingId, String transactionId) {
        try {
            BookingDetails bookingDetails = bookingInfoClient.getFinalbooking(bookingId);
            if(bookingDetails==null){
                throw new BookingDetailsNotFoundException ("UNABLE TO FETCH BOOKING DETAILS FROM FEIGN CLIENT ");
            }
            System.out.println("BOOKING DETAILS RESPONSE  "+bookingDetails.getCurrency());
            PaymentCompletedEvent event = new PaymentCompletedEvent();
            event.setBookingId(bookingId);
            event.setUserEmail(bookingDetails.getUserEmail());
            event.setUserName(bookingDetails.getUserName());
            event.setIsSuccessful(false);
            event.setFailureReason("Payment failed. Please try again.");

            eventProducer.sendEvent(KafkaTopics.PAYMENT_FAILED, event);
            log.info("Payment failure event sent for booking: {}", bookingId);

        } catch (Exception e) {
            log.error("Error handling payment failure for booking {}: {}", bookingId, e.getMessage(), e);
        }
    }
}