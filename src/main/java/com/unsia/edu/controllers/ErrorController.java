package com.unsia.edu.controllers;


import com.unsia.edu.exceptions.BadRequestException;
import com.unsia.edu.models.common.CommonResponse;
import com.unsia.edu.utils.ValidationUtil;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestControllerAdvice
@Slf4j
public class ErrorController {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> constraintViolationExceptionHandler(ConstraintViolationException exception) {
        Map<String, Object> errors = ValidationUtil.mapConstraintViolationException(exception.getConstraintViolations());
        CommonResponse<?> commonResponse = CommonResponse.builder()
                .errors(errors)
                .build();
        log.error("an error occurred: {}", exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(commonResponse);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<?> responseStatusExceptionHandler(ResponseStatusException exception) {
        CommonResponse<?> commonResponse = CommonResponse.builder()
                .errors(exception.getReason())
                .build();
        log.error("an error occurred: {}", exception.getMessage());
        return ResponseEntity.status(exception.getStatusCode())
                .body(commonResponse);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<?> badRequestExceptionHandler(BadRequestException exception) {
        CommonResponse<?> commonResponse = CommonResponse.builder()
                .errors(exception.getErrors())
                .build();
        log.error("an error occurred: {}", exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(commonResponse);
    }

}
