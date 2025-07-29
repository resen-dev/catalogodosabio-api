package com.f1rstdigital.catalogodosabio.service;

import com.f1rstdigital.catalogodosabio.domain.author.Author;
import com.f1rstdigital.catalogodosabio.dto.author.CreateAuthorRequestDto;
import com.f1rstdigital.catalogodosabio.dto.author.GetSimpleAuthorResponseDto;
import com.f1rstdigital.catalogodosabio.dto.author.UpdateAuthorRequestDto;
import com.f1rstdigital.catalogodosabio.dto.page.PageDto;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface AuthorService {
    PageDto<GetSimpleAuthorResponseDto> findAllSimpleAuthor(Pageable pageable);

    GetSimpleAuthorResponseDto createAuthor(CreateAuthorRequestDto createDto);

    GetSimpleAuthorResponseDto updateAuthor(UUID id, UpdateAuthorRequestDto updateDto);

    void deleteAuthor(UUID id);

    Author getAuthorOrThrow(UUID uuid);
}
