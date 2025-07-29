package com.f1rstdigital.catalogodosabio.service;

import com.f1rstdigital.catalogodosabio.domain.genre.Genre;
import com.f1rstdigital.catalogodosabio.dto.genre.CreateGenreRequestDto;
import com.f1rstdigital.catalogodosabio.dto.genre.GetSimpleGenreResponseDto;
import com.f1rstdigital.catalogodosabio.dto.genre.UpdateGenreRequestDto;
import com.f1rstdigital.catalogodosabio.dto.page.PageDto;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface GenreService {
    PageDto<GetSimpleGenreResponseDto> findAllSimpleGenre(Pageable pageable);

    GetSimpleGenreResponseDto createGenre(CreateGenreRequestDto createDto);

    GetSimpleGenreResponseDto updateGenre(UUID id, UpdateGenreRequestDto updateDto);

    void deleteGenre(UUID id);

    Genre getGenreOrThrow(UUID uuid);
}
