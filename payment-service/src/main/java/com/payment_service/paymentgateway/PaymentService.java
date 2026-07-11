package com.payment_service.paymentgateway;

import com.payment_service.event.PaymentEventPublisher;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentLink;
import com.stripe.model.Price;
import com.stripe.model.Product;
import com.stripe.param.PaymentLinkCreateParams;
import com.stripe.param.PriceCreateParams;
import com.stripe.param.ProductCreateParams;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PaymentService implements  PaymentGateway{

    @Value("${spring.strip.privateKey}")
    private String apiKey;


    @Value("${spring.after.payment.url}")
    private String redirectUrl;

    @Override
    public String pay( UUID roomId, long amount) throws StripeException {
        Stripe.apiKey=apiKey;

        ProductCreateParams productCreateParams=ProductCreateParams.builder()
                .setName(roomId.toString())
                .build();
        Product product=Product.create(productCreateParams);

        PriceCreateParams priceCreateParams=PriceCreateParams.builder()
                .setCurrency("usd")
                .setUnitAmount(amount)
                .setProduct(product.getId())
                .build();
        Price price=Price.create(priceCreateParams);

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
                                                .setUrl(redirectUrl)
                                                .build()
                                )
                                .build()
                )
                .build();
        PaymentLink paymentLink=PaymentLink.create(linkParams);

        return paymentLink.getUrl().toString();
    }
}
