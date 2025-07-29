package com.f1rstdigital.catalogodosabio.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequestDto(
        @Email
        @NotBlank
        String email,

        @Size(min = 8, max = 30)
        @NotBlank
        String password
) {
}