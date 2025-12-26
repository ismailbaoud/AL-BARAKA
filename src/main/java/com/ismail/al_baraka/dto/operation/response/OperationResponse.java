package com.ismail.al_baraka.dto.operation.response;

import java.time.LocalDateTime;
import java.util.List;

import com.ismail.al_baraka.dto.document.response.DocumentResponse;
import com.ismail.al_baraka.model.enums.OperationType;
import com.ismail.al_baraka.model.enums.Status;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OperationResponse {
    private OperationType operationType;
    private double amount;
    private Status status;
    private LocalDateTime createdAt;
    private LocalDateTime validatedAt;
    private LocalDateTime executedAt;
    // private AccountResponse accountsource;
    // private AccountResponse accountDestination;
    private List<DocumentResponse> documents;
    private String message;
}
