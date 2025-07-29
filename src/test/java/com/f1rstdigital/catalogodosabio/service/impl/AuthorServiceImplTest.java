package com.f1rstdigital.catalogodosabio.service.impl;

import com.f1rstdigital.catalogodosabio.domain.author.Author;
import com.f1rstdigital.catalogodosabio.dto.author.CreateAuthorRequestDto;
import com.f1rstdigital.catalogodosabio.dto.author.GetSimpleAuthorResponseDto;
import com.f1rstdigital.catalogodosabio.dto.author.UpdateAuthorRequestDto;
import com.f1rstdigital.catalogodosabio.dto.page.PageDto;
import com.f1rstdigital.catalogodosabio.handler.exceptions.RecordNotFoundException;
import com.f1rstdigital.catalogodosabio.repository.AuthorRepository;
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

class AuthorServiceImplTest {

    @Mock
    private AuthorRepository authorRepository;

    @InjectMocks
    private AuthorServiceImpl authorService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAllSimpleAuthor_withPageable() {
        Pageable pageable = PageRequest.of(0, 5);
        List<GetSimpleAuthorResponseDto> authors = List.of(
                new GetSimpleAuthorResponseDto(UUID.randomUUID(), "Autor A"),
                new GetSimpleAuthorResponseDto(UUID.randomUUID(), "Autor B")
        );
        Page<GetSimpleAuthorResponseDto> page = new PageImpl<>(authors, pageable, authors.size());

        when(authorRepository.findAllSimpleAuthor(pageable)).thenReturn(page);

        PageDto<GetSimpleAuthorResponseDto> result = authorService.findAllSimpleAuthor(pageable);

        assertEquals(2, result.getContent().size());
        assertEquals("Autor A", result.getContent().get(0).name());
        verify(authorRepository).findAllSimpleAuthor(pageable);
    }

    @Test
    void testCreateAuthor_success() {
        CreateAuthorRequestDto dto = new CreateAuthorRequestDto("Novo Autor");

        when(authorRepository.save(any(Author.class))).thenAnswer(invocation -> {
            Author a = invocation.getArgument(0);
            return new Author(a.getId(), a.getName());
        });

        GetSimpleAuthorResponseDto result = authorService.createAuthor(dto);

        assertNotNull(result.id());
        assertEquals("Novo Autor", result.name());
        verify(authorRepository).save(any(Author.class));
    }

    @Test
    void testUpdateAuthor_successAndNotFound() {
        UUID id = UUID.randomUUID();
        UpdateAuthorRequestDto dto = new UpdateAuthorRequestDto("Atualizado");

        Author existing = new Author(id, "Antigo");
        when(authorRepository.findById(id)).thenReturn(Optional.of(existing));
        when(authorRepository.save(any(Author.class))).thenAnswer(i -> i.getArgument(0));

        GetSimpleAuthorResponseDto result = authorService.updateAuthor(id, dto);
        assertEquals("Atualizado", result.name());

        UUID notFoundId = UUID.randomUUID();
        when(authorRepository.findById(notFoundId)).thenReturn(Optional.empty());

        assertThrows(RecordNotFoundException.class, () -> authorService.updateAuthor(notFoundId, dto));
    }

    @Test
    void testDeleteAuthor_successAndNotFound() {
        UUID existingId = UUID.randomUUID();
        UUID notFoundId = UUID.randomUUID();

        when(authorRepository.existsById(existingId)).thenReturn(true);
        authorService.deleteAuthor(existingId);
        verify(authorRepository).deleteById(existingId);

        when(authorRepository.existsById(notFoundId)).thenReturn(false);
        assertThrows(RecordNotFoundException.class, () -> authorService.deleteAuthor(notFoundId));
    }
}
