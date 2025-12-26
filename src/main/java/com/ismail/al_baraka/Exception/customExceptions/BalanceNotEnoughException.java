package com.ismail.al_baraka.Exception.customExceptions;

import org.springframework.http.HttpStatus;

import com.ismail.al_baraka.Exception.AppException;

public class BalanceNotEnoughException extends AppException{

    public BalanceNotEnoughException() {
        super("you havn't enough money in your account", HttpStatus.BAD_REQUEST);
    }
}
