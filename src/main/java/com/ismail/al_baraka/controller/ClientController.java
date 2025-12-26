package com.ismail.al_baraka.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ismail.al_baraka.dto.document.request.DocumentRequest;
import com.ismail.al_baraka.dto.document.response.DocumentResponse;
import com.ismail.al_baraka.dto.operation.request.OperationRequest;
import com.ismail.al_baraka.dto.operation.response.OperationResponse;
import com.ismail.al_baraka.model.enums.Status;
import com.ismail.al_baraka.service.impliment.DocumentServiceImpl;
import com.ismail.al_baraka.service.impliment.OperationServiceImpl;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/client/operations")
@RequiredArgsConstructor
public class ClientController {
    private final OperationServiceImpl operationService;
    private final DocumentServiceImpl documentService;
    @PostMapping
    public ResponseEntity<OperationResponse> createOperation(@RequestBody OperationRequest request) {
        OperationResponse operationResponse = operationService.createOperation(request);
        if(operationResponse.getStatus().equals(Status.PANDING)) {
            operationResponse.setMessage("your operation created with status panding , you should upload some justifications(PDF/JPG/PNG max 5MB)");
        }
        return ResponseEntity.ok().body(operationResponse);
    }


    @PostMapping(value = "/{id}/document", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<DocumentResponse> uploadDocument(
            @ModelAttribute @RequestBody DocumentRequest request,
            @PathVariable("id") Long operationId) {
        if (request.getFile() == null || request.getFile().isEmpty()) {
            throw new ValidationException("File is required");
        }

        DocumentResponse response = documentService.uploadDocument(request, operationId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<OperationResponse>> getOperationsList() {
        return ResponseEntity.ok().body(operationService.getOperationList());
    }
}
