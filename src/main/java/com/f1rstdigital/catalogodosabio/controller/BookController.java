package com.f1rstdigital.catalogodosabio.controller;

import com.f1rstdigital.catalogodosabio.dto.book.CreateBookRequestDto;
import com.f1rstdigital.catalogodosabio.dto.book.DetailedBookDataResponseDto;
import com.f1rstdigital.catalogodosabio.dto.book.SimpleBookDataResponseDto;
import com.f1rstdigital.catalogodosabio.dto.book.UpdateBookRequestDto;
import com.f1rstdigital.catalogodosabio.dto.page.PageDto;
import com.f1rstdigital.catalogodosabio.service.BookService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public ResponseEntity<PageDto<SimpleBookDataResponseDto>> findAll(@PageableDefault(sort = "title", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok(bookService.getAllSimpleBooks(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DetailedBookDataResponseDto> findDetailedBookById(@PathVariable UUID id) {
        return ResponseEntity.ok(bookService.trackAndGetDetailedBook(id));
    }

    @GetMapping("/author/{id}")
    public ResponseEntity<PageDto<SimpleBookDataResponseDto>> findAllByAuthor(@PathVariable UUID id, @PageableDefault(sort = "title", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok(bookService.getAllSimpleBooksByAuthorId(id, pageable));
    }

    @GetMapping("/genre/{id}")
    public ResponseEntity<PageDto<SimpleBookDataResponseDto>> findAllByGenre(@PathVariable UUID id, @PageableDefault(sort = "title", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok(bookService.getAllSimpleBooksByGenreId(id, pageable));
    }

    @GetMapping("/recently-viewed")
    public ResponseEntity<List<SimpleBookDataResponseDto>> findBooksRecentlyViewed() {
        return ResponseEntity.ok(bookService.getBooksRecentlyViewed());
    }

    @PostMapping
    public ResponseEntity<DetailedBookDataResponseDto> createBook(@Valid @RequestBody CreateBookRequestDto createDto) {
        DetailedBookDataResponseDto createdBook = bookService.createBook(createDto);
        URI location = URI.create("/books/" + createdBook.id());
        return ResponseEntity.created(location).body(createdBook);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DetailedBookDataResponseDto> updateBook(@PathVariable UUID id, @Valid @RequestBody UpdateBookRequestDto updateDto) {
        return ResponseEntity.ok(bookService.updateBook(id, updateDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable UUID id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }
}
