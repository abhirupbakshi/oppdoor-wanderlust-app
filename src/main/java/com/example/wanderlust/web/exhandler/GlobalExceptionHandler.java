package com.example.wanderlust.web.exhandler;

import com.example.wanderlust.exception.HttpMethodNotImplementedException;
import com.example.wanderlust.exception.IllegalRequestException;
import com.example.wanderlust.exception.ResourceAbsentException;
import com.example.wanderlust.exception.ResourceExistException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.URI;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ResourceAbsentException.class)
    protected ProblemDetail handleResourceAbsentException(ResourceAbsentException e) {

        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        problemDetail.setType(URI.create("resource-not-found"));
        problemDetail.setTitle(e.getMessage());

        log.error(e.getMessage());
        return problemDetail;
    }

    @ExceptionHandler(ResourceExistException.class)
    protected ProblemDetail handleResourceExistException(ResourceExistException e) {

        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.CONFLICT);
        problemDetail.setType(URI.create("resource-already-exists"));
        problemDetail.setTitle(e.getMessage());

        log.error(e.getMessage());
        return problemDetail;
    }

    @ExceptionHandler(IllegalRequestException.class)
    protected ProblemDetail handleIllegalRequestException(IllegalRequestException e) {

        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setType(URI.create("invalid-request"));
        problemDetail.setTitle(e.getMessage());

        log.error(e.getMessage());
        return problemDetail;
    }

    @ExceptionHandler(HttpMethodNotImplementedException.class)
    @ResponseStatus(HttpStatus.NOT_IMPLEMENTED)
    protected void handleHttpMethodNotImplementedException(HttpMethodNotImplementedException e) {
        log.error(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected void handleException(Exception e) {
        log.error(e.getMessage(), e);
    }
}
