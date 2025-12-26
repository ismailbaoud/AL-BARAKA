package com.ismail.al_baraka.mapper;

import org.mapstruct.Mapper;

import com.ismail.al_baraka.dto.operation.response.OperationResponse;
import com.ismail.al_baraka.model.Operation;

@Mapper(componentModel = "spring")
public interface OperationMapper {
    OperationResponse toDto(Operation op);
    Operation toEntity(OperationResponse opRes);
}
