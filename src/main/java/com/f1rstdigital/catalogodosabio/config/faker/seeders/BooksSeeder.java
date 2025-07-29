package com.f1rstdigital.catalogodosabio.config.faker.seeders;

import com.f1rstdigital.catalogodosabio.domain.author.Author;
import com.f1rstdigital.catalogodosabio.domain.book.Book;
import com.f1rstdigital.catalogodosabio.domain.genre.Genre;
import com.f1rstdigital.catalogodosabio.repository.AuthorRepository;
import com.f1rstdigital.catalogodosabio.repository.BookRepository;
import com.f1rstdigital.catalogodosabio.repository.GenreRepository;
import jakarta.annotation.PostConstruct;
import net.datafaker.Faker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Profile("local")
public class BooksSeeder {

    private static final Logger logger = LoggerFactory.getLogger(BooksSeeder.class);

    private final AuthorRepository authorRepository;
    private final GenreRepository genreRepository;
    private final BookRepository bookRepository;
    private final Faker faker;
    private final RedisTemplate<String, Object> redisTemplate;

    @Value("${dataseeder.generate:true}")
    private boolean generate;

    @Value("${dataseeder.authors:10}")
    private int authorsCount;

    @Value("${dataseeder.genres:5}")
    private int genresCount;

    @Value("${dataseeder.books:20}")
    private int booksCount;

    public BooksSeeder(
            AuthorRepository authorRepository,
            GenreRepository genreRepository,
            BookRepository bookRepository,
            Faker faker,
            RedisTemplate<String, Object> redisTemplate
    ) {
        this.authorRepository = authorRepository;
        this.genreRepository = genreRepository;
        this.bookRepository = bookRepository;
        this.faker = faker;
        this.redisTemplate = redisTemplate;
    }

    @PostConstruct
    public void seed() {

        if (generate) {

            clearRedisCache();

            bookRepository.deleteAll();
            authorRepository.deleteAll();
            genreRepository.deleteAll();

            List<Author> authors = createAuthors(authorsCount);
            authorRepository.saveAll(authors);

            List<Genre> genres = createGenres(genresCount);
            genreRepository.saveAll(genres);

            List<Book> books = createBooks(booksCount, authors, genres);
            bookRepository.saveAll(books);

            logger.info("Database has been reset and seeded with data.");

        } else {
            logger.info("Seeder skipped.");
        }
    }

    private void clearRedisCache() {
        try {
            Set<String> keys = redisTemplate.keys("*");
            if (!keys.isEmpty()) {
                redisTemplate.delete(keys);
                logger.info("Redis cache cleared.");
            }
        } catch (Exception e) {
            logger.warn("Could not clear Redis cache: {}", e.getMessage());
        }
    }

    private List<Author> createAuthors(int count) {
        Set<String> usedNames = new HashSet<>();
        List<Author> authors = new ArrayList<>();
        while (authors.size() < count) {
            String name = faker.book().author();
            if (usedNames.add(name)) {
                authors.add(new Author(UUID.randomUUID(), name));
            }
        }
        return authors;
    }

    private List<Genre> createGenres(int count) {
        Set<String> usedGenres = new HashSet<>();
        List<Genre> genres = new ArrayList<>();
        while (genres.size() < count) {
            String name = faker.book().genre();
            if (usedGenres.add(name)) {
                genres.add(new Genre(UUID.randomUUID(), name));
            }
        }
        return genres;
    }

    private List<Book> createBooks(int count, List<Author> authors, List<Genre> genres) {
        List<Book> books = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Book book = new Book(
                    UUID.randomUUID(),
                    faker.book().title(),
                    faker.lorem().paragraph(),
                    getRandom(authors),
                    getRandom(genres)
            );
            books.add(book);
        }
        return books;
    }

    private <T> T getRandom(List<T> list) {
        return list.get(faker.random().nextInt(list.size()));
    }
}
