package org.example.shop;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

@WebFluxTest(ShopControllerTest.class)
class ShopControllerTest extends TestContainerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void indexTest() {
        webTestClient.get()
                .uri("/")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentTypeCompatibleWith(MediaType.TEXT_HTML);
    }

    @Test
    void addToCartMainTest() {
        webTestClient.post()
                .uri("/main/items/2")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .bodyValue("action=plus")
                .exchange()
                .expectStatus().is3xxRedirection();
    }
}