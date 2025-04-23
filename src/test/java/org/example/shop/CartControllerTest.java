package org.example.shop;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

@WebFluxTest(CartControllerTest.class)
public class CartControllerTest extends TestContainerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void getCartTest() {
        webTestClient.get()
                .uri("/cart/items")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentTypeCompatibleWith(MediaType.TEXT_HTML);
    }

    @Test
    void addToCartTest() {
        webTestClient.post()
                .uri("/cart/items/2")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .bodyValue("action=plus")
                .exchange()
                .expectStatus().is3xxRedirection();
    }
}
