package org.payd.services.implementations;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.Map;

@Service
public class FlutterwaveService {

    private final WebClient webClient;
    private final String secretKey = "[YOUR_FLUTTERWAVE_SECRET_KEY]";

    public FlutterwaveService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://api.flutterwave.com")
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + secretKey)
                .build();
    }

    public Mono<String> initiatePayout(String bankCode, String accountNumber, BigDecimal amountNgn, String reference) {
        Map<String, Object> requestBody = Map.of(
                "account_bank", bankCode,
                "account_number", accountNumber,
                "amount", amountNgn,
                "currency", "NGN",
                "reference", reference
        );

        // Make the POST request to the Flutterwave Payouts API
        return webClient.post()
                .uri("transfers")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class);
    }
}
