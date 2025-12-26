package com.ismail.al_baraka.dto.operation.request;

import com.ismail.al_baraka.model.enums.OperationType;

import jakarta.annotation.Nullable;
import lombok.Data;

@Data
public class OperationRequest {
    private OperationType operationType;
    private Double amount; 
    @Nullable
    private Long accountDestination;
}
