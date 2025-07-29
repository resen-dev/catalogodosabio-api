package com.f1rstdigital.catalogodosabio.service.impl;

import com.f1rstdigital.catalogodosabio.domain.author.Author;
import com.f1rstdigital.catalogodosabio.dto.author.CreateAuthorRequestDto;
import com.f1rstdigital.catalogodosabio.dto.author.GetSimpleAuthorResponseDto;
import com.f1rstdigital.catalogodosabio.dto.author.UpdateAuthorRequestDto;
import com.f1rstdigital.catalogodosabio.dto.page.PageDto;
import com.f1rstdigital.catalogodosabio.handler.exceptions.RecordNotFoundException;
import com.f1rstdigital.catalogodosabio.repository.AuthorRepository;
import com.f1rstdigital.catalogodosabio.service.AuthorService;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@CacheConfig(cacheNames = "authors")
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    public AuthorServiceImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    @Cacheable(key = "'all-authors-page-' + #pageable.pageNumber + '-' + #pageable.pageSize + '-' + #pageable.sort.toString()")
    public PageDto<GetSimpleAuthorResponseDto> findAllSimpleAuthor(Pageable pageable) {
        return new PageDto<>(authorRepository.findAllSimpleAuthor(pageable));
    }

    @Override
    @CacheEvict(allEntries = true)
    public GetSimpleAuthorResponseDto createAuthor(CreateAuthorRequestDto createDto) {
        Author author = new Author(UUID.randomUUID(), createDto.name());
        author = authorRepository.save(author);
        return new GetSimpleAuthorResponseDto(author.getId(), author.getName());
    }

    @Override
    @CacheEvict(allEntries = true)
    public GetSimpleAuthorResponseDto updateAuthor(UUID id, UpdateAuthorRequestDto updateDto) {
        Author author = getAuthorOrThrow(id);

        author.setName(updateDto.name());
        author = authorRepository.save(author);
        return new GetSimpleAuthorResponseDto(author.getId(), author.getName());
    }

    @Override
    @CacheEvict(allEntries = true)
    public void deleteAuthor(UUID id) {
        if (!authorRepository.existsById(id)) {
            throw new RecordNotFoundException(Author.class, id);
        }
        authorRepository.deleteById(id);
    }

    @Override
    public Author getAuthorOrThrow(UUID id) {
        return authorRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(Author.class, id));
    }
}
