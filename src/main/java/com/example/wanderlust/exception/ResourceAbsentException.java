package com.example.wanderlust.exception;

public class ResourceAbsentException extends RuntimeException {

    public ResourceAbsentException() {
        super();
    }

    public ResourceAbsentException(String message) {
        super(message);
    }

    public ResourceAbsentException(String message, Throwable cause) {
        super(message, cause);
    }

    public ResourceAbsentException(Throwable cause) {
        super(cause);
    }

    protected ResourceAbsentException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
