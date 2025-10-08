package com.mpt.journal.controller;

import org.springframework.dao.PermissionDeniedDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.nio.file.AccessDeniedException;

@ControllerAdvice
public class ErrorHandlerController {
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        return new ResponseEntity<>("Invalid argument: " + ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<String> handleInternalException(MethodArgumentTypeMismatchException ex) {
        return new ResponseEntity<>("Bad request: could not convert parameter " + ex.getPropertyName() + " of type " + ex.getValue().getClass().getName() + " to required " + ex.getRequiredType().getName(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> handleGlobalException(AccessDeniedException ex) {
        return new ResponseEntity<>("Access denied", HttpStatus.CONFLICT);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<String> handleGlobalException(NoResourceFoundException ex) {
        return new ResponseEntity<>("Not found", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleGlobalException(RuntimeException ex) {
        return new ResponseEntity<>("Server error: " + ex.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
