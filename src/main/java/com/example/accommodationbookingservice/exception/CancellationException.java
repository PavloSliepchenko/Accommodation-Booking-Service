package com.example.accommodationbookingservice.exception;

public class CancellationException extends RuntimeException {
    public CancellationException(String message) {
        super(message);
    }
}
