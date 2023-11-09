package com.example.wanderlust.exception;

public class UserAbsentException extends ResourceAbsentException {

    public UserAbsentException() {
        super();
    }

    public UserAbsentException(String message) {
        super(message);
    }

    public UserAbsentException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserAbsentException(Throwable cause) {
        super(cause);
    }

    protected UserAbsentException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
