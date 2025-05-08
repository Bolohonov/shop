package org.example.showcase.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.example.showcase.payment.api.PaymentApi;
import org.example.showcase.payment.invoker.ApiClient;

@Configuration
public class PaymentApiConfiguration {

    @Bean
    public PaymentApi paymentApi(WebClient.Builder webClientBuilder) {
        WebClient webClient = webClientBuilder.baseUrl("http://payment:8081")
                .build();

        return new PaymentApi(new ApiClient(webClient));
    }
}
