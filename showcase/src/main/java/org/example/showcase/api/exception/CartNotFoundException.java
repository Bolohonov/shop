package org.example.showcase.api.exception;

public class CartNotFoundException extends RuntimeException {

    public CartNotFoundException(String message) {
        super(message);
    }
}
