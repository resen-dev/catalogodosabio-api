package com.f1rstdigital.catalogodosabio.mapper;

import com.f1rstdigital.catalogodosabio.domain.book.Book;
import com.f1rstdigital.catalogodosabio.dto.book.DetailedBookDataResponseDto;
import com.f1rstdigital.catalogodosabio.dto.book.SimpleBookDataResponseDto;
import org.springframework.stereotype.Component;

@Component
public class BookMapper {

    public SimpleBookDataResponseDto toSimpleBookDto(Book book) {
        return new SimpleBookDataResponseDto(
                book.getId(),
                book.getTitle(),
                book.getAuthor().getName(),
                book.getGenre().getName()
        );
    }

    public DetailedBookDataResponseDto toDetailedBookDto(Book book) {
        return new DetailedBookDataResponseDto(
                book.getId(),
                book.getTitle(),
                book.getDescription(),
                book.getAuthor().getName(),
                book.getGenre().getName()
        );
    }
}
