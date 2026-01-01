package com.ismail.al_baraka.Exception;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.ismail.al_baraka.Exception.customExceptions.AccountdisactivatedException;
import com.ismail.al_baraka.Exception.customExceptions.BalanceNotEnoughException;
import com.ismail.al_baraka.Exception.customExceptions.OperationNotFoundException;
import com.ismail.al_baraka.Exception.customExceptions.AccountUserNotFoundException;
import com.ismail.al_baraka.dto.ApiErrorResponse;

import jakarta.servlet.http.HttpServletRequest;

import java.time.LocalDateTime;

import javax.naming.AuthenticationException;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler {

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiErrorResponse> hanldeAuthenticationException(AuthenticationException ex, HttpServletRequest request) {
        ex.printStackTrace();
        return buildError("authentication error", request, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BalanceNotEnoughException.class)
    public ResponseEntity<ApiErrorResponse> handleBalanceNotEnouphException(BalanceNotEnoughException ex, HttpServletRequest request) {
        ex.printStackTrace();
        return buildError("balance not enough", request, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(OperationNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleOperationNotFoundException(OperationNotFoundException ex, HttpServletRequest request) {
        ex.printStackTrace();
        return buildError("operation not found", request, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AccountUserNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleUserNotFoundException(AccountUserNotFoundException ex, HttpServletRequest request) {
        ex.printStackTrace();
        return buildError("user not found", request, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AccountdisactivatedException.class) 
    public ResponseEntity<ApiErrorResponse> handleAccountdisactivatedException(AccountdisactivatedException ex, HttpServletRequest request) {
        ex.printStackTrace();
        return buildError("disactivated account", request, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handelGeneral(Exception ex, HttpServletRequest request) {
        ex.printStackTrace();
        return buildError("Internal server error", request, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiErrorResponse> handleHttpMethodNotSeported(HttpRequestMethodNotSupportedException ex, HttpServletRequest request) {
        ex.printStackTrace();
        return buildError("the method '"+ ex.getMethod() + "'' not validate in this endpoint", request, HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<ApiErrorResponse> buildError(String message , HttpServletRequest req, HttpStatus status) {
        ApiErrorResponse error = ApiErrorResponse.builder()
        .status(status.value())
        .message(message)
        .path(req.getRequestURI())
        .timestamp(LocalDateTime.now())
        .build();
        return ResponseEntity.ok().body(error);
    }
}