package com.f1rstdigital.catalogodosabio.service.impl;

import com.f1rstdigital.catalogodosabio.domain.genre.Genre;
import com.f1rstdigital.catalogodosabio.dto.genre.CreateGenreRequestDto;
import com.f1rstdigital.catalogodosabio.dto.genre.GetSimpleGenreResponseDto;
import com.f1rstdigital.catalogodosabio.dto.genre.UpdateGenreRequestDto;
import com.f1rstdigital.catalogodosabio.dto.page.PageDto;
import com.f1rstdigital.catalogodosabio.handler.exceptions.RecordNotFoundException;
import com.f1rstdigital.catalogodosabio.repository.GenreRepository;
import com.f1rstdigital.catalogodosabio.service.GenreService;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@CacheConfig(cacheNames = "genres")
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;

    public GenreServiceImpl(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    @Override
    @Cacheable(key = "'all-genres-page-' + #pageable.pageNumber + '-' + #pageable.pageSize + '-' + #pageable.sort.toString()")
    public PageDto<GetSimpleGenreResponseDto> findAllSimpleGenre(Pageable pageable) {
        return new PageDto<>(genreRepository.findAllSimpleGenre(pageable));
    }

    @Override
    @CacheEvict(allEntries = true)
    public GetSimpleGenreResponseDto createGenre(CreateGenreRequestDto createDto) {

        Genre genre = new Genre(UUID.randomUUID(), createDto.name());

        genre = genreRepository.save(genre);

        return new GetSimpleGenreResponseDto(genre.getId(), genre.getName());
    }

    @Override
    @CacheEvict(allEntries = true)
    public GetSimpleGenreResponseDto updateGenre(UUID id, UpdateGenreRequestDto updateDto) {

        Genre genre = getGenreOrThrow(id);

        genre.setName(updateDto.name());

        genre = genreRepository.save(genre);

        return new GetSimpleGenreResponseDto(genre.getId(), genre.getName());
    }

    @Override
    @CacheEvict(allEntries = true)
    public void deleteGenre(UUID id) {
        if (!genreRepository.existsById(id)) {
            throw new RecordNotFoundException(Genre.class, id);
        }
        genreRepository.deleteById(id);
    }

    @Override
    public Genre getGenreOrThrow(UUID id) {
        return genreRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(Genre.class, id));
    }
}
