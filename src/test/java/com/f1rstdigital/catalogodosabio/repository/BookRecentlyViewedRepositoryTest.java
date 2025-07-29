package com.f1rstdigital.catalogodosabio.repository;

import com.f1rstdigital.catalogodosabio.domain.author.Author;
import com.f1rstdigital.catalogodosabio.domain.book.Book;
import com.f1rstdigital.catalogodosabio.domain.book.recentlyviewed.BookRecentlyViewed;
import com.f1rstdigital.catalogodosabio.domain.book.recentlyviewed.BookRecentlyViewedId;
import com.f1rstdigital.catalogodosabio.domain.genre.Genre;
import com.f1rstdigital.catalogodosabio.dto.book.SimpleBookDataResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BookRecentlyViewedRepositoryTest {

    @Autowired
    private BookRecentlyViewedRepository bookRecentlyViewedRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private GenreRepository genreRepository;

    private UUID userId;

    @BeforeEach
    void setUp() {

        Author author = new Author();
        author.setId(UUID.randomUUID());
        author.setName("Author Test");
        authorRepository.save(author);

        Genre genre = new Genre();
        genre.setId(UUID.randomUUID());
        genre.setName("Genre Test");
        genreRepository.save(genre);

        Book book1 = new Book(UUID.randomUUID(), "Book One", "Description One", author, genre);
        Book book2 = new Book(UUID.randomUUID(), "Book Two", "Description Two", author, genre);

        bookRepository.save(book1);
        bookRepository.save(book2);

        userId = UUID.randomUUID();

        BookRecentlyViewed brv1 = new BookRecentlyViewed();
        brv1.setId(new BookRecentlyViewedId(userId, book1.getId()));
        brv1.setLastAccess(LocalDateTime.now());
        bookRecentlyViewedRepository.save(brv1);

        BookRecentlyViewed brv2 = new BookRecentlyViewed();
        brv2.setId(new BookRecentlyViewedId(userId, book2.getId()));
        brv2.setLastAccess(LocalDateTime.now().minusHours(1));
        bookRecentlyViewedRepository.save(brv2);
    }

    @Test
    void findExceedingRecordsByUserId() {

        List<BookRecentlyViewed> exceeding = bookRecentlyViewedRepository.findExceedingRecordsByUserId(userId, 1);
        assertNotNull(exceeding);
        assertEquals(1, exceeding.size());
    }

    @Test
    void findRecentBooksByUser() {
        List<SimpleBookDataResponseDto> recentBooks = bookRecentlyViewedRepository.findRecentBooksByUser(userId);
        assertNotNull(recentBooks);
        assertEquals(2, recentBooks.size());

        assertEquals("Book One", recentBooks.get(0).title());
        assertEquals("Book Two", recentBooks.get(1).title());

        assertEquals("Author Test", recentBooks.get(0).author());
        assertEquals("Genre Test", recentBooks.get(0).genre());
    }
}
