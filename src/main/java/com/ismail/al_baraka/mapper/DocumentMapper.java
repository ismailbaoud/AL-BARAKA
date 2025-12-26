package com.ismail.al_baraka.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.ismail.al_baraka.dto.document.response.DocumentResponse;
import com.ismail.al_baraka.model.Document;

@Mapper(componentModel = "spring")
public interface DocumentMapper {
    @Mapping(source = "operation.id", target = "operationId")
    DocumentResponse toResponse(Document document);
}
