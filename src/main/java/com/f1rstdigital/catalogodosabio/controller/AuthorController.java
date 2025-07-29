package com.f1rstdigital.catalogodosabio.controller;

import com.f1rstdigital.catalogodosabio.dto.author.CreateAuthorRequestDto;
import com.f1rstdigital.catalogodosabio.dto.author.GetSimpleAuthorResponseDto;
import com.f1rstdigital.catalogodosabio.dto.author.UpdateAuthorRequestDto;
import com.f1rstdigital.catalogodosabio.dto.page.PageDto;
import com.f1rstdigital.catalogodosabio.service.AuthorService;
import com.f1rstdigital.catalogodosabio.service.impl.AuthorServiceImpl;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/authors")
public class AuthorController {

    private final AuthorService authorService;

    public AuthorController(AuthorServiceImpl authorService) {
        this.authorService = authorService;
    }

    @GetMapping
    public ResponseEntity<PageDto<GetSimpleAuthorResponseDto>> findAllSimpleAuthor(@PageableDefault(sort = "name", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok(authorService.findAllSimpleAuthor(pageable));
    }

    @PostMapping
    public ResponseEntity<GetSimpleAuthorResponseDto> createAuthor(@Valid @RequestBody CreateAuthorRequestDto createDto) {
        GetSimpleAuthorResponseDto createdAuthor = authorService.createAuthor(createDto);
        URI location = URI.create("/authors/" + createdAuthor.id());
        return ResponseEntity.created(location).body(createdAuthor);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GetSimpleAuthorResponseDto> updateAuthor(@PathVariable UUID id, @Valid @RequestBody UpdateAuthorRequestDto updateDto) {
        return ResponseEntity.ok(authorService.updateAuthor(id, updateDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuthor(@PathVariable UUID id) {
        authorService.deleteAuthor(id);
        return ResponseEntity.noContent().build();
    }
}
