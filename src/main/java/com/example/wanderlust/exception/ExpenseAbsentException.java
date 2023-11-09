package com.example.wanderlust.exception;

public class ExpenseAbsentException extends ResourceAbsentException {

    public ExpenseAbsentException() {
        super();
    }

    public ExpenseAbsentException(String message) {
        super(message);
    }

    public ExpenseAbsentException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExpenseAbsentException(Throwable cause) {
        super(cause);
    }

    protected ExpenseAbsentException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
