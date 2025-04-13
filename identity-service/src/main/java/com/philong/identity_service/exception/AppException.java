package com.philong.identity_service.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Getter
public class AppException extends RuntimeException {
    Error error;
    public AppException(Error error) {
        super(error.getMessage());
        this.error = error;
    }
}
