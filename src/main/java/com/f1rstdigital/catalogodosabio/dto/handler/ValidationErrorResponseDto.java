package com.f1rstdigital.catalogodosabio.dto.handler;

import java.time.LocalDateTime;
import java.util.Map;

public record ValidationErrorResponseDto(
        int status,
        String error,
        Map<String, String> fieldErrors,
        String path,
        LocalDateTime timestamp
) {
}
