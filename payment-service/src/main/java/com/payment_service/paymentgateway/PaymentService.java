package com.payment_service.paymentgateway;

import com.commonlibrary.common_library.common.enums.KafkaTopics;
import com.commonlibrary.common_library.common.event.BookingPaymentEvent;
import com.commonlibrary.common_library.common.kafka.EventProducer;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentLink;
import com.stripe.model.Price;
import com.stripe.model.Product;
import com.stripe.param.PaymentLinkCreateParams;
import com.stripe.param.PriceCreateParams;
import com.stripe.param.ProductCreateParams;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService implements PaymentGateway {

    @Value("${spring.strip.privateKey}")
    private String apiKey;

    @Value("${spring.after.payment.url}")
    private String redirectUrl;

    private final EventProducer eventProducer;

    @Override
    public String pay(UUID roomId, long amount, UUID bookingId, String userEmail) throws StripeException {
        Stripe.apiKey = apiKey;

        ProductCreateParams productCreateParams = ProductCreateParams.builder()
                .setName(roomId.toString())
                .build();
        Product product = Product.create(productCreateParams);

        PriceCreateParams priceCreateParams = PriceCreateParams.builder()
                .setCurrency("usd")
                .setUnitAmount(amount)
                .setProduct(product.getId())
                .build();
        Price price = Price.create(priceCreateParams);

        PaymentLinkCreateParams linkParams = PaymentLinkCreateParams.builder()
                .addLineItem(
                        PaymentLinkCreateParams.LineItem.builder()
                                .setPrice(price.getId())
                                .setQuantity(1L)
                                .build()
                )
                .addPaymentMethodType(PaymentLinkCreateParams.PaymentMethodType.CARD)
                .addPaymentMethodType(PaymentLinkCreateParams.PaymentMethodType.LINK)
                .setAfterCompletion(
                        PaymentLinkCreateParams.AfterCompletion.builder()
                                .setType(PaymentLinkCreateParams.AfterCompletion.Type.REDIRECT)
                                .setRedirect(
                                        PaymentLinkCreateParams.AfterCompletion.Redirect.builder()
                                                .setUrl(redirectUrl + "?bookingId=" + bookingId)
                                                .build()
                                )
                                .build()
                )
                .build();
        PaymentLink paymentLink = PaymentLink.create(linkParams);

        String paymentUrl = paymentLink.getUrl();
        log.info("Payment link created: {}", paymentUrl);

        // Send payment event to Kafka
        BookingPaymentEvent event = new BookingPaymentEvent();
        event.setBookingId(bookingId);
        event.setUserEmail(userEmail);
        event.setAmount((double) amount / 100);
        event.setStatus("PENDING");
        event.setPaymentLink(paymentUrl);
        event.setTransactionId(paymentLink.getId());

        eventProducer.sendEvent(KafkaTopics.BOOKING_CREATED, event);
        log.info("Payment event sent for booking: {}", bookingId);

        return paymentUrl;
    }
}