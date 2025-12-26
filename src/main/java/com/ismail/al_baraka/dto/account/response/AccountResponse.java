package com.ismail.al_baraka.dto.account.response;

import java.util.List;

import com.ismail.al_baraka.dto.operation.response.OperationResponse;
import com.ismail.al_baraka.model.enums.Role;

public record AccountResponse (
    String fullName,
    Role role,
    Boolean active,
    String accountNumber,
    Float balance,
    List<OperationResponse> operationsSource,
    List<OperationResponse> operationsDestination
) {}
