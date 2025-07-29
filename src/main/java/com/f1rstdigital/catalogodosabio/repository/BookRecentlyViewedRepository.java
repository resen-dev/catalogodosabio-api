package com.f1rstdigital.catalogodosabio.repository;

import com.f1rstdigital.catalogodosabio.domain.book.recentlyviewed.BookRecentlyViewed;
import com.f1rstdigital.catalogodosabio.domain.book.recentlyviewed.BookRecentlyViewedId;
import com.f1rstdigital.catalogodosabio.dto.book.SimpleBookDataResponseDto;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BookRecentlyViewedRepository extends JpaRepository<BookRecentlyViewed, BookRecentlyViewedId> {

    @Query(value = """
                SELECT * FROM books_recently_viewed
                WHERE user_id = :userId
                ORDER BY last_access DESC
                OFFSET :limit
            """, nativeQuery = true)
    List<BookRecentlyViewed> findExceedingRecordsByUserId(@Param("userId") UUID userId, @Param("limit") int limit);

    @Query("""
                SELECT new com.f1rstdigital.catalogodosabio.dto.book.SimpleBookDataResponseDto(
                    b.id,
                    b.title,
                    a.name,
                    g.name
                )
                FROM BookRecentlyViewed brv
                JOIN Book b ON brv.id.bookId = b.id
                JOIN b.author a
                JOIN b.genre g
                WHERE brv.id.userId = :userId
                ORDER BY brv.lastAccess DESC
            """)
    List<SimpleBookDataResponseDto> findRecentBooksByUser(UUID userId);

}
