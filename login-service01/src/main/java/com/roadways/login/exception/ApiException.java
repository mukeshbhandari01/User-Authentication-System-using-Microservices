package com.roadways.login.exception;

public class ApiException extends RuntimeException {
    public ApiException(String message) {
        super(message);
    }
}
