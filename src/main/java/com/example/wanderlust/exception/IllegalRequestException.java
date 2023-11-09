package com.example.wanderlust.exception;

public class IllegalRequestException extends RuntimeException {

    public IllegalRequestException() {
        super();
    }

    public IllegalRequestException(String message) {
        super(message);
    }

    public IllegalRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalRequestException(Throwable cause) {
        super(cause);
    }

    protected IllegalRequestException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
