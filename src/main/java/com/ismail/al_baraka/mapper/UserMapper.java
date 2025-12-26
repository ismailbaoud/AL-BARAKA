package com.ismail.al_baraka.mapper;

import org.mapstruct.Mapper;

import com.ismail.al_baraka.dto.user.request.UserRequest;
import com.ismail.al_baraka.dto.user.response.UserResponse;
import com.ismail.al_baraka.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponse toDto(User user);
    User toEntity(UserRequest request);
}