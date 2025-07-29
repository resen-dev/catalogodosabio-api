package com.f1rstdigital.catalogodosabio.repository;

import com.f1rstdigital.catalogodosabio.domain.author.Author;
import com.f1rstdigital.catalogodosabio.dto.author.GetSimpleAuthorResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AuthorRepository extends JpaRepository<Author, UUID> {

    @Query("""
                SELECT new com.f1rstdigital.catalogodosabio.dto.author.GetSimpleAuthorResponseDto(
                    a.id,
                    a.name
                )
                FROM Author a
            """)
    Page<GetSimpleAuthorResponseDto> findAllSimpleAuthor(Pageable pageable);
}
