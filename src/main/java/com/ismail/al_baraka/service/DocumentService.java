package com.ismail.al_baraka.service;

import com.ismail.al_baraka.dto.document.request.DocumentRequest;
import com.ismail.al_baraka.dto.document.response.DocumentResponse;

public interface DocumentService {

    public DocumentResponse uploadDocument(DocumentRequest request, Long operationId);
    
}
