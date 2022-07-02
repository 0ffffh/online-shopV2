package com.k0s.onlineshop.exceptions;

public class ProductCartNotFoundException extends RuntimeException {
    public ProductCartNotFoundException(String s) {
        super(s);
    }
}
