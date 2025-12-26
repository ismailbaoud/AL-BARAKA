package com.ismail.al_baraka.dto;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;


@Builder
public record ApiErrorResponse(
    int status,
    String message,
    String path,
    LocalDateTime timestamp,
    List<String> details
) {}