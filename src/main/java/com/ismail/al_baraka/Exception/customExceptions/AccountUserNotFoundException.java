package com.ismail.al_baraka.Exception.customExceptions;

import org.springframework.http.HttpStatus;

import com.ismail.al_baraka.Exception.AppException;

public class AccountUserNotFoundException extends AppException {
    public AccountUserNotFoundException() {
        super("this Account not found", HttpStatus.NOT_FOUND);
    }
    public AccountUserNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
