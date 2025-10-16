package com.timex.api.exception;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
public class ValidationErrorResponse extends ErrorDetails {
    private Map<String, String> errors;

    public ValidationErrorResponse(LocalDateTime timestamp, int status, String message, String details,
            Map<String, String> errors) {
        super(timestamp, status, message, details);
        this.errors = errors;
    }
}