package com.unsia.edu.exceptions;

import lombok.Getter;

@Getter
public class BadRequestException extends RuntimeException {
    private final Object errors;

    public BadRequestException(Object errors) {
        this.errors = errors;
    }
}
