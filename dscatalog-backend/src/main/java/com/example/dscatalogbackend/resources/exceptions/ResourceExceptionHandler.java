package com.example.dscatalogbackend.resources.exceptions;

import com.example.dscatalogbackend.services.exceptions.DatabaseException;
import com.example.dscatalogbackend.services.exceptions.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;

@ControllerAdvice
public class ResourceExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<StandardError> entityNotFound(ResourceNotFoundException resourceNotFoundException, HttpServletRequest request) {
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        StandardError error = new StandardError();
        error.setTimestamp(Instant.now());
        error.setStatus(httpStatus.value());
        error.setError("Resource not found.");
        error.setMessage(resourceNotFoundException.getMessage());
        error.setPath(request.getRequestURI());
        return ResponseEntity.status(httpStatus).body(error);
    }

    @ExceptionHandler(DatabaseException.class)
    public ResponseEntity<StandardError> database(DatabaseException databaseException, HttpServletRequest request) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        StandardError error = new StandardError();
        error.setTimestamp(Instant.now());
        error.setStatus(httpStatus.value());
        error.setError("Database exception.");
        error.setMessage(databaseException.getMessage());
        error.setPath(request.getRequestURI());
        return ResponseEntity.status(httpStatus).body(error);
    }
}