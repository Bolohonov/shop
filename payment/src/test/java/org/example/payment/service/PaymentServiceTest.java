package org.example.payment.service;

import org.example.payment.CreateTestAccountService;
import org.example.payment.config.TestcontainersConfiguration;
import org.example.payment.model.Account;
import org.example.payment.model.BalanceDto;
import org.example.payment.model.PaymentRequestDto;
import org.example.payment.repo.AccountRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class PaymentServiceTest extends TestcontainersConfiguration {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PaymentService paymentService;

    @Test
    @DisplayName("Успешное получение баланса")
    void testGetBalance() {
        Account account = accountRepository.save(CreateTestAccountService.createDefaultAccount()).block();

        Mono<BalanceDto> balanceMono = paymentService.getBalance(account.getId());

        StepVerifier.create(balanceMono)
                .assertNext(balanceDto -> assertEquals(5000.0, balanceDto.getBalance()))
                .verifyComplete();
    }

    @Test
    @DisplayName("Успешная оплата")
    void testMakePaymentSuccess() {
        Account account = accountRepository.save(CreateTestAccountService.createDefaultAccount()).block();

        PaymentRequestDto request = new PaymentRequestDto(2000.0);

        StepVerifier.create(paymentService.makePayment(request, account.getId()))
                .verifyComplete();

        StepVerifier.create(accountRepository.findById(account.getId()))
                .assertNext(updated -> assertEquals(3000.0, updated.getBalance()))
                .verifyComplete();
    }

    @Test
    @DisplayName("Ошибка при оплате: аккаунт не найден")
    void testMakePaymentAccountNotFound() {
        PaymentRequestDto request = new PaymentRequestDto(100.0);

        StepVerifier.create(paymentService.makePayment(request, 1000L))
                .expectErrorMatches(e -> e instanceof RuntimeException &&
                        e.getMessage().equals("Account not found"))
                .verify();
    }

    @Test
    @DisplayName("Ошибка при оплате: недостаточно средств")
    void testMakePaymentInsufficientFunds() {
        Account account = accountRepository.save(CreateTestAccountService.createDefaultAccount()).block();

        PaymentRequestDto request = new PaymentRequestDto(10000.0);

        StepVerifier.create(paymentService.makePayment(request, account.getId()))
                .expectErrorMatches(e -> e instanceof IllegalStateException &&
                        e.getMessage().equals("Insufficient funds"))
                .verify();
    }

    @Test
    @DisplayName("Ошибка при оплате: некорректная сумма")
    void testMakePaymentInvalidAmount() {
        Account account = accountRepository.save(CreateTestAccountService.createDefaultAccount()).block();

        PaymentRequestDto request = new PaymentRequestDto(-200.0);

        StepVerifier.create(paymentService.makePayment(request, account.getId()))
                .expectErrorMatches(e -> e instanceof IllegalArgumentException &&
                        e.getMessage().equals("Invalid payment amount"))
                .verify();
    }
}
