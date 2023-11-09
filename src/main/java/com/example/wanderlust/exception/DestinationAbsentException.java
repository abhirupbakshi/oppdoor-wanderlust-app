package com.example.wanderlust.exception;

public class DestinationAbsentException extends ResourceAbsentException {

    public DestinationAbsentException() {
        super();
    }

    public DestinationAbsentException(String message) {
        super(message);
    }

    public DestinationAbsentException(String message, Throwable cause) {
        super(message, cause);
    }

    public DestinationAbsentException(Throwable cause) {
        super(cause);
    }

    protected DestinationAbsentException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
