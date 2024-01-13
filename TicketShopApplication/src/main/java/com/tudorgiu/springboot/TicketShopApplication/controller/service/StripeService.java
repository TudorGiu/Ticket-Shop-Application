package com.tudorgiu.springboot.TicketShopApplication.controller.service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.tudorgiu.springboot.TicketShopApplication.model.entity.ChargeRequest;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class StripeService {

    // Injecting the Stripe secret key from application properties
    @Value("${stripe.secret.key}")
    private String secretKey;

    // Initializing Stripe API key after the bean is constructed
    @PostConstruct
    public void init() {
        Stripe.apiKey = secretKey;
    }

    /**
     * Process the charge using the Stripe API.
     *
     * @param chargeRequest The charge request containing necessary information.
     * @return The Stripe Charge object representing the result of the charge.
     * @throws StripeException If there is an issue with the Stripe API.
     */
    public Charge charge(ChargeRequest chargeRequest) throws StripeException {

        // Create a map to hold the parameters for the charge
        Map<String, Object> chargeParams = new HashMap<>();
        chargeParams.put("currency", chargeRequest.getCurrency());
        chargeParams.put("description", chargeRequest.getDescription());
        chargeParams.put("source", chargeRequest.getStripeToken());
        chargeParams.put("amount", chargeRequest.getAmount());

        // Call the Stripe API to create a charge using the provided parameters
        return Charge.create(chargeParams);
    }
}