package org.example.shop;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

@WebFluxTest(ItemControllerTest.class)
class ItemControllerTest extends TestContainerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void getItemById_shouldReturnHtmlWithItem() {
        webTestClient.get()
                .uri("/items/2")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentTypeCompatibleWith(MediaType.TEXT_HTML);
    }

    @Test
    void addToCart_shouldReturnRedirect() {
        webTestClient.post()
                .uri("/items/2")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .bodyValue("action=plus")
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueMatches("Location", "/.*");
    }
}