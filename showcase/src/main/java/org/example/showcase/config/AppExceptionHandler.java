package org.example.showcase.config;

import lombok.extern.slf4j.Slf4j;
import org.example.showcase.api.exception.CartNotFoundException;
import org.example.showcase.api.exception.IllegalActionException;
import org.example.showcase.api.exception.OrderNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

@Slf4j
@RestControllerAdvice
public class AppExceptionHandler {

    @ExceptionHandler(CartNotFoundException.class)
    public Mono<ResponseEntity<String>> cartNotFoundException(CartNotFoundException ex) {
        log.error("Корзина не найдена{}", ex.toString());
        return Mono.just(ResponseEntity.badRequest().body(ex.getMessage()));
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public Mono<ResponseEntity<String>> orderNotFoundException(OrderNotFoundException ex) {
        log.error("Заказ не найден{}", ex.toString());
        return Mono.just(ResponseEntity.badRequest().body(ex.getMessage()));
    }

    @ExceptionHandler(IllegalActionException.class)
    public Mono<ResponseEntity<String>> illegalActionException(IllegalActionException ex) {
        log.error("Произведена попытка некорректной операции{}", ex.toString());
        return Mono.just(ResponseEntity.badRequest().body(ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<String>> handleUnexpectedException(Exception ex) {
        log.error("Произошла внутренняя ошибка{}", ex.toString());
        return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Произошла внутренняя ошибка. Пожалуйста, попробуйте позже."));
    }
}
