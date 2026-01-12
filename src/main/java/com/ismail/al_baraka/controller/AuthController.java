package com.ismail.al_baraka.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ismail.al_baraka.dto.account.response.AccountResponse;
import com.ismail.al_baraka.dto.auth.request.LoginRequest;
import com.ismail.al_baraka.dto.auth.response.LoginResponse;
import com.ismail.al_baraka.dto.user.request.UserRequest;
import com.ismail.al_baraka.service.impliment.AuthServiceImpl;
import com.ismail.al_baraka.service.impliment.UserServiceImpl;

import lombok.RequiredArgsConstructor;
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserServiceImpl userService;
    private final AuthServiceImpl authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authentication(@RequestBody LoginRequest req) {

        try{   
            return ResponseEntity.ok().body(authService.authenticate(req));
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    @PostMapping("/register")
    public ResponseEntity<AccountResponse> createUser(@RequestBody UserRequest request) {
                return ResponseEntity.ok().body(userService.createUser(request));
    }
}