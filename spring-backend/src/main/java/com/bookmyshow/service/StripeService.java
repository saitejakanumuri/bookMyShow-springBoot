package com.bookmyshow.service;

import com.bookmyshow.config.AppProperties;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.param.PaymentIntentCreateParams;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class StripeService {

    private final AppProperties appProperties;

    @PostConstruct
    void init() {
        if (appProperties.getStripeKey() != null && !appProperties.getStripeKey().isBlank()) {
            Stripe.apiKey = appProperties.getStripeKey();
        }
    }

    public String createPaymentIntent(String paymentMethodId, long amountRupees) throws StripeException {
        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(amountRupees)
                .setCurrency("inr")
                .setPaymentMethod(paymentMethodId)
                .setConfirm(true)
                .setDescription("payment for movie ticket.")
                .setAutomaticPaymentMethods(
                    PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                        .setEnabled(true)
                        .setAllowRedirects(PaymentIntentCreateParams.AutomaticPaymentMethods.AllowRedirects.NEVER)
                        .build()
                )
                .build();
        return com.stripe.model.PaymentIntent.create(params).getId();
    }
}
