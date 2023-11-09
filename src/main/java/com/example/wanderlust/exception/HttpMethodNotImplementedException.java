package com.example.wanderlust.exception;

public class HttpMethodNotImplementedException extends RuntimeException {

    public HttpMethodNotImplementedException() {
        super();
    }

    public HttpMethodNotImplementedException(String message) {
        super(message);
    }

    public HttpMethodNotImplementedException(String message, Throwable cause) {
        super(message, cause);
    }

    public HttpMethodNotImplementedException(Throwable cause) {
        super(cause);
    }

    protected HttpMethodNotImplementedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
