package com.f1rstdigital.catalogodosabio.dto.genre;

import jakarta.validation.constraints.NotBlank;

public record UpdateGenreRequestDto(
        @NotBlank
        String name
) {
}
