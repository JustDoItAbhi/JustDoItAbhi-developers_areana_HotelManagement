//package com.payment_service.event;
//
//import com.commonlibrary.common_library.common.enums.KafkaTopics;
//import com.commonlibrary.common_library.common.event.PaymentCompletedEvent;
//import com.commonlibrary.common_library.common.event.PaymentFailedEvent;
//
//import com.commonlibrary.common_library.common.model.OutboxEvent;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.AllArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//
//import java.util.UUID;
//
//@Service
//@Slf4j
//@AllArgsConstructor
//public class PaymentEventPublisher {
//
//    private final OutboxRepository outboxRepository;
//    private final ObjectMapper objectMapper;
//
//
//    @Transactional
//    public void publishPaymentSuccess(UUID paymentId, UUID bookingId, UUID userId,
//                                      double amount, String transactionId) {
//        try {
//            PaymentCompletedEvent event = PaymentCompletedEvent.builder()
//                    .paymentId(paymentId)
//                    .bookingId(bookingId)
//                    .userId(userId)
//                    .amount(amount)
//                    .transactionId(transactionId)
//                    .build();
//
//            String payload = objectMapper.writeValueAsString(event);
//
//            OutboxEvent outboxEvent = OutboxEvent.builder()
//                    .aggregateId(bookingId.toString())
//                    .aggregateType("PAYMENT")
//                    .eventType("PAYMENT_COMPLETED")
//                    .payload(payload)
//                    .topic(KafkaTopics.PAYMENT_SUCCESS)
//                    .status("PENDING")
//                    .build();
//
//            outboxRepository.save(outboxEvent);
//            log.info("Payment success event saved to outbox for booking: {}", bookingId);
//
//        } catch (Exception e) {
//            log.error("Failed to save payment success event to outbox", e);
//            throw new RuntimeException("Failed to publish payment success event", e);
//        }
//    }
//
//    @Transactional
//    public void publishPaymentFailed(UUID bookingId, UUID userId, double amount, String errorMessage) {
//        try {
//            PaymentFailedEvent event = PaymentFailedEvent.builder()
//                    .bookingId(bookingId)
//                    .userId(userId)
//                    .amount(amount)
//                    .errorMessage(errorMessage)
//                    .build();
//
//            String payload = objectMapper.writeValueAsString(event);
//
//            OutboxEvent outboxEvent = OutboxEvent.builder()
//                    .aggregateId(bookingId.toString())
//                    .aggregateType("PAYMENT")
//                    .eventType("PAYMENT_FAILED")
//                    .payload(payload)
//                    .topic("payment-failed")
//                    .status("PENDING")
//                    .build();
//
//            outboxRepository.save(outboxEvent);
//            log.info("Payment failed event saved to outbox for booking: {}", bookingId);
//
//        } catch (Exception e) {
//            log.error("Failed to save payment failed event to outbox", e);
//            throw new RuntimeException("Failed to publish payment failed event", e);
//        }
//    }
//}
