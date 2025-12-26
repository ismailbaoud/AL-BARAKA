package com.ismail.al_baraka.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.ismail.al_baraka.dto.account.response.AccountResponse;
import com.ismail.al_baraka.model.Account;

@Mapper(componentModel = "spring")
public interface AccountMapper{
    @Mapping(source = "owner.fullName", target = "fullName")
    @Mapping(source = "owner.role", target = "role")
    @Mapping(source = "owner.active", target = "active")
    AccountResponse toDto(Account acc);
    @Mapping(target = "owner", ignore = true)
    @Mapping(target = "operationsSource", ignore = true)
    @Mapping(target = "operationsDestination", ignore = true)
    Account toEntity(AccountResponse accRes);
}
