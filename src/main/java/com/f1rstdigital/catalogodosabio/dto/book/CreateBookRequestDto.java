package com.f1rstdigital.catalogodosabio.dto.book;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateBookRequestDto(
        @NotBlank String title,
        @NotBlank String description,
        @NotNull UUID authorId,
        @NotNull UUID genreId
) {
}