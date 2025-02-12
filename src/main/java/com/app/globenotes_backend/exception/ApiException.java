package com.app.globenotes_backend.exception;

public class ApiException extends RuntimeException {
    public ApiException(String message) {
        super(message);
    }
}
