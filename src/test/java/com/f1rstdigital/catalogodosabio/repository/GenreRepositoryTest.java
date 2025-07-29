package com.f1rstdigital.catalogodosabio.repository;

import com.f1rstdigital.catalogodosabio.domain.genre.Genre;
import com.f1rstdigital.catalogodosabio.dto.genre.GetSimpleGenreResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class GenreRepositoryTest {

    @Autowired
    private GenreRepository genreRepository;

    @BeforeEach
    void setUp() {
        Genre genre1 = new Genre();
        genre1.setId(UUID.randomUUID());
        genre1.setName("Action");

        Genre genre2 = new Genre();
        genre2.setId(UUID.randomUUID());
        genre2.setName("Comedy");

        genreRepository.save(genre1);
        genreRepository.save(genre2);
    }

    @Test
    void findAllSimpleGenre() {
        Pageable pageable = PageRequest.of(0, 10);

        Page<GetSimpleGenreResponseDto> page = genreRepository.findAllSimpleGenre(pageable);

        assertNotNull(page);
        assertEquals(2, page.getTotalElements());

        List<GetSimpleGenreResponseDto> genres = page.getContent();
        assertNotNull(genres);
        assertEquals(2, genres.size());

        assertTrue(genres.stream().anyMatch(g -> g.name().equals("Action")));
        assertTrue(genres.stream().anyMatch(g -> g.name().equals("Comedy")));
        assertTrue(genres.stream().noneMatch(g -> g.name().equals("Other")));
    }
}
