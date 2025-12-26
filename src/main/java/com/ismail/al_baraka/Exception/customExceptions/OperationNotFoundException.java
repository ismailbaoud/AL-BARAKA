package com.ismail.al_baraka.Exception.customExceptions;

import org.springframework.http.HttpStatus;

import com.ismail.al_baraka.Exception.AppException;

public class OperationNotFoundException extends AppException{
    public OperationNotFoundException() {
        super("this operation not found", HttpStatus.NOT_FOUND);
    }
}
