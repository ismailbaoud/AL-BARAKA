package com.ismail.al_baraka.dto.document.response;

import java.time.LocalDateTime;


public record DocumentResponse(
    Long id,
    String fileName,
    String fileType,
    String storagePath,
    LocalDateTime createdAt,
    Long operationId
) {}
