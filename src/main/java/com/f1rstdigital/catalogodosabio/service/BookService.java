package com.f1rstdigital.catalogodosabio.service;

import com.f1rstdigital.catalogodosabio.dto.book.CreateBookRequestDto;
import com.f1rstdigital.catalogodosabio.dto.book.DetailedBookDataResponseDto;
import com.f1rstdigital.catalogodosabio.dto.book.SimpleBookDataResponseDto;
import com.f1rstdigital.catalogodosabio.dto.book.UpdateBookRequestDto;
import com.f1rstdigital.catalogodosabio.dto.page.PageDto;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface BookService {
    PageDto<SimpleBookDataResponseDto> getAllSimpleBooks(Pageable pageable);

    PageDto<SimpleBookDataResponseDto> getAllSimpleBooksByAuthorId(UUID authorId, Pageable pageable);

    PageDto<SimpleBookDataResponseDto> getAllSimpleBooksByGenreId(UUID genreId, Pageable pageable);

    DetailedBookDataResponseDto getDetailedBookById(UUID id);

    DetailedBookDataResponseDto trackAndGetDetailedBook(UUID id);

    List<SimpleBookDataResponseDto> getBooksRecentlyViewed();

    DetailedBookDataResponseDto createBook(CreateBookRequestDto createDto);

    DetailedBookDataResponseDto updateBook(UUID id, UpdateBookRequestDto updateDto);

    void deleteBook(UUID id);
}
