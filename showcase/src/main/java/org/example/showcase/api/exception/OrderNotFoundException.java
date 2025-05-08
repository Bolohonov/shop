package org.example.showcase.api.exception;

public class OrderNotFoundException extends RuntimeException {


    public OrderNotFoundException(String message) {
        super(message);
    }
}
