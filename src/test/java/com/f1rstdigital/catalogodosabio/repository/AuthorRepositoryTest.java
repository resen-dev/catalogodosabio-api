package com.f1rstdigital.catalogodosabio.repository;

import com.f1rstdigital.catalogodosabio.domain.author.Author;
import com.f1rstdigital.catalogodosabio.dto.author.GetSimpleAuthorResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AuthorRepositoryTest {

    @Autowired
    private AuthorRepository authorRepository;

    @BeforeEach
    void setUp() {
        Author author1 = new Author();
        author1.setId(UUID.randomUUID());
        author1.setName("J. K. Rowling");

        Author author2 = new Author();
        author2.setId(UUID.randomUUID());
        author2.setName("George R. R. Martin");

        authorRepository.save(author1);
        authorRepository.save(author2);
    }

    @Test
    void findAllSimpleAuthor() {
        Pageable pageable = PageRequest.of(0, 10);

        Page<GetSimpleAuthorResponseDto> page = authorRepository.findAllSimpleAuthor(pageable);

        assertNotNull(page);
        assertEquals(2, page.getTotalElements());

        var authors = page.getContent();
        assertNotNull(authors);
        assertEquals(2, authors.size());

        assertTrue(authors.stream().anyMatch(a -> a.name().equals("J. K. Rowling")));
        assertTrue(authors.stream().anyMatch(a -> a.name().equals("George R. R. Martin")));
        assertTrue(authors.stream().noneMatch(a -> a.name().equals("Unknown Author")));
    }
}