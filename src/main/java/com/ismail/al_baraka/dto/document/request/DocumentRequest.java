package com.ismail.al_baraka.dto.document.request;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class DocumentRequest {
    private MultipartFile file;
}
