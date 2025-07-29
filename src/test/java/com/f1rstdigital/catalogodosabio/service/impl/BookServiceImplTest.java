package com.f1rstdigital.catalogodosabio.service.impl;

import com.f1rstdigital.catalogodosabio.domain.author.Author;
import com.f1rstdigital.catalogodosabio.domain.book.Book;
import com.f1rstdigital.catalogodosabio.domain.genre.Genre;
import com.f1rstdigital.catalogodosabio.dto.book.CreateBookRequestDto;
import com.f1rstdigital.catalogodosabio.dto.book.DetailedBookDataResponseDto;
import com.f1rstdigital.catalogodosabio.dto.book.SimpleBookDataResponseDto;
import com.f1rstdigital.catalogodosabio.dto.book.UpdateBookRequestDto;
import com.f1rstdigital.catalogodosabio.dto.page.PageDto;
import com.f1rstdigital.catalogodosabio.handler.exceptions.RecordNotFoundException;
import com.f1rstdigital.catalogodosabio.mapper.BookMapper;
import com.f1rstdigital.catalogodosabio.repository.BookRepository;
import com.f1rstdigital.catalogodosabio.service.AuthenticatedUserService;
import com.f1rstdigital.catalogodosabio.service.AuthorService;
import com.f1rstdigital.catalogodosabio.service.BookRecentlyViewedService;
import com.f1rstdigital.catalogodosabio.service.GenreService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {

    @Mock
    private BookRepository bookRepository;
    @Mock
    private BookRecentlyViewedService bookRecentlyViewedService;
    @Mock
    private AuthorService authorService;
    @Mock
    private GenreService genreService;
    @Mock
    private AuthenticatedUserService authenticatedUserService;
    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    void getAllSimpleBooks() {
        Pageable pageable = PageRequest.of(0, 10);
        Book book = new Book();
        Page<Book> bookPage = new PageImpl<>(List.of(book));
        SimpleBookDataResponseDto dto = new SimpleBookDataResponseDto(UUID.randomUUID(), "Book Title", "Author", "Genre");

        when(bookRepository.findAll(pageable)).thenReturn(bookPage);
        when(bookMapper.toSimpleBookDto(book)).thenReturn(dto);

        PageDto<SimpleBookDataResponseDto> result = bookService.getAllSimpleBooks(pageable);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
    }

    @Test
    void getAllSimpleBooksByAuthorId() {
        UUID authorId = UUID.randomUUID();
        Pageable pageable = PageRequest.of(0, 10);
        Book book = new Book();
        SimpleBookDataResponseDto dto = new SimpleBookDataResponseDto(UUID.randomUUID(), "Book Title", "Author", "Genre");

        when(bookRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(new PageImpl<>(List.of(book)));
        when(bookMapper.toSimpleBookDto(book)).thenReturn(dto);

        PageDto<SimpleBookDataResponseDto> result = bookService.getAllSimpleBooksByAuthorId(authorId, pageable);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
    }

    @Test
    void getAllSimpleBooksByGenreId() {
        UUID genreId = UUID.randomUUID();
        Pageable pageable = PageRequest.of(0, 10);
        Book book = new Book();
        SimpleBookDataResponseDto dto = new SimpleBookDataResponseDto(UUID.randomUUID(), "Book Title", "Author", "Genre");

        when(bookRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(new PageImpl<>(List.of(book)));
        when(bookMapper.toSimpleBookDto(book)).thenReturn(dto);

        PageDto<SimpleBookDataResponseDto> result = bookService.getAllSimpleBooksByGenreId(genreId, pageable);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
    }

    @Test
    void getDetailedBookById() {
        UUID id = UUID.randomUUID();
        Book book = new Book();
        DetailedBookDataResponseDto dto = new DetailedBookDataResponseDto(id, "Title", "Desc", "Author", "Genre");

        when(bookRepository.findById(id)).thenReturn(Optional.of(book));
        when(bookMapper.toDetailedBookDto(book)).thenReturn(dto);

        DetailedBookDataResponseDto result = bookService.getDetailedBookById(id);

        assertNotNull(result);
        assertEquals("Title", result.title());
    }

    @Test
    void trackAndGetDetailedBook() {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        DetailedBookDataResponseDto dto = new DetailedBookDataResponseDto(id, "Title", "Desc", "Author", "Genre");

        when(bookRepository.findById(id)).thenReturn(Optional.of(new Book()));
        when(bookMapper.toDetailedBookDto(any())).thenReturn(dto);
        when(authenticatedUserService.isAnonymous()).thenReturn(false);
        when(authenticatedUserService.getAuthenticatedUserId()).thenReturn(userId);

        DetailedBookDataResponseDto result = bookService.trackAndGetDetailedBook(id);

        assertNotNull(result);
        verify(bookRecentlyViewedService).updateRecentlyViewed(userId, id);
    }

    @Test
    void getBooksRecentlyViewed() {
        UUID userId = UUID.randomUUID();
        List<SimpleBookDataResponseDto> dtoList = List.of(
                new SimpleBookDataResponseDto(UUID.randomUUID(), "Book1", "Author", "Genre")
        );

        when(authenticatedUserService.getAuthenticatedUserId()).thenReturn(userId);
        when(bookRecentlyViewedService.getRecentlyViewedByUser(userId)).thenReturn(dtoList);

        List<SimpleBookDataResponseDto> result = bookService.getBooksRecentlyViewed();

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void createBook() {
        UUID authorId = UUID.randomUUID();
        UUID genreId = UUID.randomUUID();
        CreateBookRequestDto dto = new CreateBookRequestDto("Title", "Desc", authorId, genreId);

        Author author = new Author(authorId, "Author");
        Genre genre = new Genre(genreId, "Genre");
        Book book = new Book(UUID.randomUUID(), "Title", "Desc", author, genre);
        DetailedBookDataResponseDto response = new DetailedBookDataResponseDto(book.getId(), "Title", "Desc", "Author", "Genre");

        when(authorService.getAuthorOrThrow(authorId)).thenReturn(author);
        when(genreService.getGenreOrThrow(genreId)).thenReturn(genre);
        when(bookRepository.save(any())).thenReturn(book);
        when(bookMapper.toDetailedBookDto(book)).thenReturn(response);

        DetailedBookDataResponseDto result = bookService.createBook(dto);

        assertNotNull(result);
        assertEquals("Title", result.title());
    }

    @Test
    void updateBook() {
        UUID id = UUID.randomUUID();
        UUID newAuthorId = UUID.randomUUID();
        UUID newGenreId = UUID.randomUUID();
        UpdateBookRequestDto dto = new UpdateBookRequestDto("Updated Title", "Updated Desc", newAuthorId, newGenreId);

        Author oldAuthor = new Author(UUID.randomUUID(), "Old Author");
        Genre oldGenre = new Genre(UUID.randomUUID(), "Old Genre");
        Book book = new Book(id, "Old Title", "Old Desc", oldAuthor, oldGenre);

        Author newAuthor = new Author(newAuthorId, "New Author");
        Genre newGenre = new Genre(newGenreId, "New Genre");

        when(bookRepository.findById(id)).thenReturn(Optional.of(book));
        when(authorService.getAuthorOrThrow(newAuthorId)).thenReturn(newAuthor);
        when(genreService.getGenreOrThrow(newGenreId)).thenReturn(newGenre);
        when(bookRepository.save(any())).thenReturn(book);
        when(bookMapper.toDetailedBookDto(book)).thenReturn(
                new DetailedBookDataResponseDto(id, "Updated Title", "Updated Desc", "New Author", "New Genre")
        );

        DetailedBookDataResponseDto result = bookService.updateBook(id, dto);

        assertNotNull(result);
        assertEquals("Updated Title", result.title());
    }

    @Test
    void deleteBook() {
        UUID id = UUID.randomUUID();
        when(bookRepository.existsById(id)).thenReturn(true);
        doNothing().when(bookRepository).deleteById(id);

        bookService.deleteBook(id);

        verify(bookRepository).deleteById(id);
    }

    @Test
    void deleteBook_NotFound_ShouldThrow() {
        UUID id = UUID.randomUUID();
        when(bookRepository.existsById(id)).thenReturn(false);

        assertThrows(RecordNotFoundException.class, () -> bookService.deleteBook(id));
    }
}
