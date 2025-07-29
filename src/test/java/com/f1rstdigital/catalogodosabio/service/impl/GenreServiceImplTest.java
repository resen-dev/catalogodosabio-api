package com.f1rstdigital.catalogodosabio.service.impl;

import com.f1rstdigital.catalogodosabio.domain.genre.Genre;
import com.f1rstdigital.catalogodosabio.dto.genre.CreateGenreRequestDto;
import com.f1rstdigital.catalogodosabio.dto.genre.GetSimpleGenreResponseDto;
import com.f1rstdigital.catalogodosabio.dto.genre.UpdateGenreRequestDto;
import com.f1rstdigital.catalogodosabio.dto.page.PageDto;
import com.f1rstdigital.catalogodosabio.handler.exceptions.RecordNotFoundException;
import com.f1rstdigital.catalogodosabio.repository.GenreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class GenreServiceImplTest {

    @Mock
    private GenreRepository genreRepository;

    @InjectMocks
    private GenreServiceImpl genreService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAllSimpleGenre_withPageable() {
        Pageable pageable = PageRequest.of(0, 5);
        List<GetSimpleGenreResponseDto> genres = List.of(
                new GetSimpleGenreResponseDto(UUID.randomUUID(), "Terror"),
                new GetSimpleGenreResponseDto(UUID.randomUUID(), "Comédia")
        );
        Page<GetSimpleGenreResponseDto> page = new PageImpl<>(genres, pageable, genres.size());

        when(genreRepository.findAllSimpleGenre(pageable)).thenReturn(page);

        PageDto<GetSimpleGenreResponseDto> result = genreService.findAllSimpleGenre(pageable);

        assertEquals(2, result.getContent().size());
        assertEquals("Terror", result.getContent().get(0).name());
        verify(genreRepository).findAllSimpleGenre(pageable);
    }

    @Test
    void testCreateGenre_success() {
        CreateGenreRequestDto dto = new CreateGenreRequestDto("Ação");

        when(genreRepository.save(any(Genre.class))).thenAnswer(invocation -> {
            Genre g = invocation.getArgument(0);
            return new Genre(g.getId(), g.getName());
        });

        GetSimpleGenreResponseDto result = genreService.createGenre(dto);

        assertNotNull(result.id());
        assertEquals("Ação", result.name());
        verify(genreRepository).save(any(Genre.class));
    }

    @Test
    void testUpdateGenre_successAndNotFound() {
        UUID id = UUID.randomUUID();
        UpdateGenreRequestDto dto = new UpdateGenreRequestDto("Romance");

        Genre existing = new Genre(id, "Drama");
        when(genreRepository.findById(id)).thenReturn(Optional.of(existing));
        when(genreRepository.save(any(Genre.class))).thenAnswer(inv -> inv.getArgument(0));

        GetSimpleGenreResponseDto result = genreService.updateGenre(id, dto);
        assertEquals("Romance", result.name());

        UUID notFoundId = UUID.randomUUID();
        when(genreRepository.findById(notFoundId)).thenReturn(Optional.empty());

        assertThrows(RecordNotFoundException.class, () -> genreService.updateGenre(notFoundId, dto));
    }

    @Test
    void testDeleteGenre_successAndNotFound() {
        UUID existingId = UUID.randomUUID();
        UUID notFoundId = UUID.randomUUID();

        when(genreRepository.existsById(existingId)).thenReturn(true);

        genreService.deleteGenre(existingId);
        verify(genreRepository).deleteById(existingId);

        when(genreRepository.existsById(notFoundId)).thenReturn(false);

        assertThrows(RecordNotFoundException.class, () -> genreService.deleteGenre(notFoundId));
    }
}
