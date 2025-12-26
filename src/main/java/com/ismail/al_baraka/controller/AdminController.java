package com.ismail.al_baraka.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ismail.al_baraka.dto.account.response.AccountResponse;
import com.ismail.al_baraka.dto.user.request.UserRequest;
import com.ismail.al_baraka.service.impliment.UserServiceImpl;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
public class AdminController {
    
    private final UserServiceImpl userService;


    @GetMapping("/{id}")
    public ResponseEntity<AccountResponse> getUser(@PathVariable("id") Long clientId) {
        return ResponseEntity.ok().body(userService.getUser(clientId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AccountResponse> updateUser(@PathVariable("id") Long clientId , @RequestBody UserRequest request) {
        return ResponseEntity.ok().body(userService.updateUser(clientId, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") Long clientId) {
        return ResponseEntity.ok().body(userService.deleteUser(clientId));
    }
} 
