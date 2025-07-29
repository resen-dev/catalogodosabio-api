package com.f1rstdigital.catalogodosabio.dto.book;

import java.io.Serializable;
import java.util.UUID;

public record DetailedBookDataResponseDto(
        UUID id,
        String title,
        String description,
        String author,
        String genre
) implements Serializable {
}
