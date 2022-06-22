package com.k0s.onlineshop.exceptions;

public class SaveProductFailureException extends RuntimeException {
    public SaveProductFailureException(String msg) {
        super(msg);
    }
}
