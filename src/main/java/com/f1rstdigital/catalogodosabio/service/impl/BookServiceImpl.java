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
import com.f1rstdigital.catalogodosabio.repository.specs.BookSpecifications;
import com.f1rstdigital.catalogodosabio.service.AuthenticatedUserService;
import com.f1rstdigital.catalogodosabio.service.AuthorService;
import com.f1rstdigital.catalogodosabio.service.BookRecentlyViewedService;
import com.f1rstdigital.catalogodosabio.service.BookService;
import com.f1rstdigital.catalogodosabio.service.GenreService;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@CacheConfig(cacheNames = "books")
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final BookRecentlyViewedService bookRecentlyViewedService;
    private final AuthorService authorService;
    private final GenreService genreService;
    private final AuthenticatedUserService authenticatedUserService;
    private final BookMapper bookMapper;

    public BookServiceImpl(
            BookRepository bookRepository,
            BookRecentlyViewedService bookRecentlyViewedService,
            AuthorService authorService,
            GenreService genreService,
            AuthenticatedUserService authenticatedUserService,
            BookMapper bookMapper
    ) {
        this.bookRepository = bookRepository;
        this.bookRecentlyViewedService = bookRecentlyViewedService;
        this.authorService = authorService;
        this.genreService = genreService;
        this.authenticatedUserService = authenticatedUserService;
        this.bookMapper = bookMapper;
    }

    @Override
    @Cacheable(key = "'all-books-page-' + #pageable.pageNumber + '-' + #pageable.pageSize + '-' + #pageable.sort.toString()")
    public PageDto<SimpleBookDataResponseDto> getAllSimpleBooks(Pageable pageable) {
        Page<SimpleBookDataResponseDto> books = bookRepository.findAll(pageable)
                .map(bookMapper::toSimpleBookDto);
        return new PageDto<>(books);
    }

    @Override
    @Cacheable(key = "'books-by-author-' + #authorId + '-' + #pageable.pageNumber + '-' + #pageable.pageSize + '-' + #pageable.sort.toString()")
    public PageDto<SimpleBookDataResponseDto> getAllSimpleBooksByAuthorId(UUID authorId, Pageable pageable) {
        Specification<Book> spec = BookSpecifications.hasAuthorId(authorId);
        Page<SimpleBookDataResponseDto> books = bookRepository.findAll(spec, pageable)
                .map(bookMapper::toSimpleBookDto);
        return new PageDto<>(books);
    }

    @Override
    @Cacheable(key = "'books-by-genre-' + #genreId + '-' + #pageable.pageNumber + '-' + #pageable.pageSize + '-' + #pageable.sort.toString()")
    public PageDto<SimpleBookDataResponseDto> getAllSimpleBooksByGenreId(UUID genreId, Pageable pageable) {
        Specification<Book> spec = BookSpecifications.hasGenreId(genreId);
        Page<SimpleBookDataResponseDto> books = bookRepository.findAll(spec, pageable)
                .map(bookMapper::toSimpleBookDto);
        return new PageDto<>(books);
    }

    @Override
    @Cacheable(key = "'by-id-' + #id", unless = "#result == null")
    public DetailedBookDataResponseDto getDetailedBookById(UUID id) {
        return bookRepository.findById(id)
                .map(bookMapper::toDetailedBookDto)
                .orElse(null);
    }

    @Override
    public DetailedBookDataResponseDto trackAndGetDetailedBook(UUID id) {
        DetailedBookDataResponseDto book = getDetailedOrThrow(id);

        if (!authenticatedUserService.isAnonymous()) {
            UUID userId = authenticatedUserService.getAuthenticatedUserId();
            bookRecentlyViewedService.updateRecentlyViewed(userId, book.id());
        }

        return book;
    }

    @Override
    public List<SimpleBookDataResponseDto> getBooksRecentlyViewed() {
        UUID userId = authenticatedUserService.getAuthenticatedUserId();
        return bookRecentlyViewedService.getRecentlyViewedByUser(userId);
    }

    @Override
    @CacheEvict(allEntries = true)
    public DetailedBookDataResponseDto createBook(CreateBookRequestDto createDto) {
        Author author = authorService.getAuthorOrThrow(createDto.authorId());
        Genre genre = genreService.getGenreOrThrow(createDto.genreId());

        Book book = buildBook(createDto, author, genre);
        book = bookRepository.save(book);

        return bookMapper.toDetailedBookDto(book);
    }

    @Override
    @CacheEvict(allEntries = true)
    public DetailedBookDataResponseDto updateBook(UUID id, UpdateBookRequestDto updateDto) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(Book.class, id));

        UUID currentAuthorId = book.getAuthor().getId();
        UUID currentGenreId = book.getGenre().getId();

        if (!updateDto.authorId().equals(currentAuthorId)) {
            book.setAuthor(authorService.getAuthorOrThrow(updateDto.authorId()));
        }

        if (!updateDto.genreId().equals(currentGenreId)) {
            book.setGenre(genreService.getGenreOrThrow(updateDto.genreId()));
        }

        book.setTitle(updateDto.title());
        book.setDescription(updateDto.description());

        book = bookRepository.save(book);

        return bookMapper.toDetailedBookDto(book);
    }

    @Override
    @CacheEvict(allEntries = true)
    public void deleteBook(UUID id) {
        if (!bookRepository.existsById(id)) {
            throw new RecordNotFoundException(Book.class, id);
        }
        bookRepository.deleteById(id);
    }

    private Book buildBook(CreateBookRequestDto dto, Author author, Genre genre) {
        return new Book(UUID.randomUUID(), dto.title(), dto.description(), author, genre);
    }

    private DetailedBookDataResponseDto getDetailedOrThrow(UUID id) {
        return Optional.ofNullable(getDetailedBookById(id))
                .orElseThrow(() -> new RecordNotFoundException(Book.class, id));
    }
}
