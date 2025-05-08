package org.example.shop.api.exception;

public class OrderNotFoundException extends RuntimeException {


    public OrderNotFoundException(String message) {
        super(message);
    }
}
