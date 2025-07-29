package com.f1rstdigital.catalogodosabio.dto.handler;

import java.time.LocalDateTime;

public record ErrorResponseDto(
        int status,
        String error,
        String message,
        String path,
        LocalDateTime timestamp
) {
}
