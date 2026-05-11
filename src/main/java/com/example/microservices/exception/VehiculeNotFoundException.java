package com.example.microservices.exception;

public class VehiculeNotFoundException extends RuntimeException {
    public VehiculeNotFoundException(String message) {
        super(message);
    }
}