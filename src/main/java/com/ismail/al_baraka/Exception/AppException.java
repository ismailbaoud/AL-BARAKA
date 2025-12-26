package com.ismail.al_baraka.Exception;

import org.springframework.http.HttpStatus;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class AppException extends RuntimeException {
    private final HttpStatus status;

    public AppException(String message , HttpStatus status) {
        super(message);
        this.status = status;
    }
}