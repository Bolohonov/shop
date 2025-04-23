package org.example.shop;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

@WebFluxTest(OrderControllerTest.class)
public class OrderControllerTest extends TestContainerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void getOrderTest() {
        webTestClient.get()
                .uri("/orders/10")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentTypeCompatibleWith(MediaType.TEXT_HTML);
    }
}
