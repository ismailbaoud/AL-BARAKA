package com.ismail.al_baraka.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ismail.al_baraka.dto.operation.response.OperationResponse;
import com.ismail.al_baraka.service.impliment.OperationServiceImpl;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/agent/operations")
@RequiredArgsConstructor
public class AgentController {
    
    private final OperationServiceImpl operationService;

    @GetMapping("/pending")
    public ResponseEntity<List<OperationResponse>> getPandingOperationsList() {
        return ResponseEntity.ok().body(operationService.getPandingOperationsList());
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<OperationResponse> approveOperations(@PathVariable("id") Long opId) {
        return ResponseEntity.ok().body(operationService.approveOperation(opId));
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<OperationResponse> rejectOperation(@PathVariable("id") Long opId) {
        return ResponseEntity.ok().body(operationService.rejectOperation(opId));
    }
}
