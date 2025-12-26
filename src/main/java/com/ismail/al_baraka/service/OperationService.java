package com.ismail.al_baraka.service;

import java.util.List;

import com.ismail.al_baraka.dto.operation.request.OperationRequest;
import com.ismail.al_baraka.dto.operation.response.OperationResponse;

public interface OperationService {
    
    public OperationResponse createOperation(OperationRequest request);

    public List<OperationResponse> getOperationList();

    public List<OperationResponse> getPandingOperationsList();

    public OperationResponse approveOperation(Long opId);

    public OperationResponse rejectOperation(Long opId);
}
