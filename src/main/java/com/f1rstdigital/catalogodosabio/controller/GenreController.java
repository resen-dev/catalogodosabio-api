package com.f1rstdigital.catalogodosabio.controller;

import com.f1rstdigital.catalogodosabio.dto.genre.CreateGenreRequestDto;
import com.f1rstdigital.catalogodosabio.dto.genre.GetSimpleGenreResponseDto;
import com.f1rstdigital.catalogodosabio.dto.genre.UpdateGenreRequestDto;
import com.f1rstdigital.catalogodosabio.dto.page.PageDto;
import com.f1rstdigital.catalogodosabio.service.GenreService;
import com.f1rstdigital.catalogodosabio.service.impl.GenreServiceImpl;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/genres")
public class GenreController {

    private final GenreService genreService;

    public GenreController(GenreServiceImpl genreService) {
        this.genreService = genreService;
    }

    @GetMapping
    public ResponseEntity<PageDto<GetSimpleGenreResponseDto>> findAllSimpleGenre(@PageableDefault(sort = "name", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok(genreService.findAllSimpleGenre(pageable));
    }

    @PostMapping
    public ResponseEntity<GetSimpleGenreResponseDto> createGenre(@Valid @RequestBody CreateGenreRequestDto createDto) {
        GetSimpleGenreResponseDto createdGenre = genreService.createGenre(createDto);

        URI location = URI.create("/genres/" + createdGenre.id());

        return ResponseEntity.created(location).body(createdGenre);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GetSimpleGenreResponseDto> updateGenre(@PathVariable UUID id, @Valid @RequestBody UpdateGenreRequestDto updateDto) {
        return ResponseEntity.ok(genreService.updateGenre(id, updateDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGenre(@PathVariable UUID id) {
        genreService.deleteGenre(id);
        return ResponseEntity.noContent().build();
    }
}
