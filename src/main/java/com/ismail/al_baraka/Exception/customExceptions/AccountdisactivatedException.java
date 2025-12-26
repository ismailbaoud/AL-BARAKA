package com.ismail.al_baraka.Exception.customExceptions;

import org.springframework.http.HttpStatus;

import com.ismail.al_baraka.Exception.AppException;

public class AccountdisactivatedException extends AppException {
    public AccountdisactivatedException() {
        super("this account is disactivated", HttpStatus.UNAUTHORIZED);
    }
}
