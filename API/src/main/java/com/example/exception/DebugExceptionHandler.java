package com.example.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class DebugExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        e.printStackTrace();
        Throwable root = e;
        while (root.getCause() != null)
            root = root.getCause();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(root.getClass().getSimpleName() + ": " + root.getMessage());
    }
}
