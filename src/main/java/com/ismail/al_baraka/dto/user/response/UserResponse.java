package com.ismail.al_baraka.dto.user.response;

import java.time.LocalDateTime;

import com.ismail.al_baraka.model.enums.Role;

public record UserResponse (
    String fullName,
    Role role,
    Boolean active,
    LocalDateTime createdAt
) {}