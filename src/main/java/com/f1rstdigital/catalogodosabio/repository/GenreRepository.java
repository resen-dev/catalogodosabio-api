package com.f1rstdigital.catalogodosabio.repository;

import com.f1rstdigital.catalogodosabio.domain.genre.Genre;
import com.f1rstdigital.catalogodosabio.dto.genre.GetSimpleGenreResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface GenreRepository extends JpaRepository<Genre, UUID> {
    @Query("""
                SELECT new com.f1rstdigital.catalogodosabio.dto.genre.GetSimpleGenreResponseDto(
                    g.id,
                    g.name
                )
                FROM Genre g
            """)
    Page<GetSimpleGenreResponseDto> findAllSimpleGenre(Pageable pageable);
}
