package com.timex.api.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ResourceNotFoundException extends RuntimeException {

    private final String resourceName;
    private final String fieldName;
    private final Object fieldValue;

    @Override
    public String getMessage() {
        return String.format("%s not found with %s : '%s'", resourceName, fieldName, fieldValue);
    }
}