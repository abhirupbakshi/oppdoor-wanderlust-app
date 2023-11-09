package com.example.wanderlust.exception;

public class ItineraryAbsentException extends ResourceAbsentException {

    public ItineraryAbsentException() {
        super();
    }

    public ItineraryAbsentException(String message) {
        super(message);
    }

    public ItineraryAbsentException(String message, Throwable cause) {
        super(message, cause);
    }

    public ItineraryAbsentException(Throwable cause) {
        super(cause);
    }

    protected ItineraryAbsentException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
