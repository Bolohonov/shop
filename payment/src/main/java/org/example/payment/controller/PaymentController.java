package org.example.payment.controller;

import jakarta.validation.Valid;
import org.example.payment.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.example.payment.api.PaymentApi;
import org.example.payment.model.BalanceDto;
import org.example.payment.model.PaymentRequestDto;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/payment")
public class PaymentController implements PaymentApi {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @Override
    @GetMapping
    public Mono<ResponseEntity<BalanceDto>> getBalance(
            @RequestParam("accountId") @Valid Long accountId,
            ServerWebExchange exchange
    ) {
        return paymentService.getBalance(accountId)
                .map(ResponseEntity::ok)
                .onErrorResume(e -> Mono.just(ResponseEntity.notFound().build()));
    }

    @Override
    @PostMapping
    public Mono<ResponseEntity<Void>> makePayment(
            @RequestParam("accountId") @Valid Long accountId,
            Mono<PaymentRequestDto> paymentRequestDto,
            ServerWebExchange exchange
    ) {
        return paymentRequestDto
                .flatMap(dto -> paymentService.makePayment(dto, accountId))
                .thenReturn(ResponseEntity.ok().<Void>build());
    }
}
