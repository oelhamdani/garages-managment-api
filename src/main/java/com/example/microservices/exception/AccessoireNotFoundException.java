package com.example.microservices.exception;

public class AccessoireNotFoundException extends RuntimeException {
    public AccessoireNotFoundException(String message) {
        super(message);
    }
}