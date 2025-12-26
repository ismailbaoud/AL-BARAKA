package com.ismail.al_baraka.Exception.customExceptions;

import org.springframework.http.HttpStatus;

import com.ismail.al_baraka.Exception.AppException;

public class AuthenticationException extends AppException{
    public AuthenticationException() {
        super("Your email or password isn't correct", HttpStatus.BAD_REQUEST);
    }
}
