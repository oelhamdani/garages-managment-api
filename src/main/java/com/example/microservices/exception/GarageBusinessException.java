package com.example.microservices.exception;

public class GarageBusinessException extends RuntimeException {
    public GarageBusinessException(String message) {
        super(message);
    }
}