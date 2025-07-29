package com.f1rstdigital.catalogodosabio.dto.book;

import java.io.Serializable;
import java.util.UUID;

public record SimpleBookDataResponseDto(
        UUID id,
        String title,
        String author,
        String genre
) implements Serializable {
}
