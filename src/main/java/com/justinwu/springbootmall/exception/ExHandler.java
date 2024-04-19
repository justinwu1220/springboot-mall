package com.justinwu.springbootmall.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

@ControllerAdvice
public class ExHandler {

    @ExceptionHandler(ResponseStatusException.class)
    private ResponseEntity<String> handle(ResponseStatusException e){
        return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
    }
}
