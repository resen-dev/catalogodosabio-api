package com.f1rstdigital.catalogodosabio.service.impl;

import com.f1rstdigital.catalogodosabio.domain.book.recentlyviewed.BookRecentlyViewed;
import com.f1rstdigital.catalogodosabio.domain.book.recentlyviewed.BookRecentlyViewedId;
import com.f1rstdigital.catalogodosabio.dto.book.SimpleBookDataResponseDto;
import com.f1rstdigital.catalogodosabio.repository.BookRecentlyViewedRepository;
import com.f1rstdigital.catalogodosabio.service.BookRecentlyViewedService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class BookRecentlyViewedServiceImpl implements BookRecentlyViewedService {

    private final BookRecentlyViewedRepository repository;

    @Value("${app.recently-viewed.limit:10}")
    private int recentlyViewedBooksLimit;

    public BookRecentlyViewedServiceImpl(BookRecentlyViewedRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<SimpleBookDataResponseDto> getRecentlyViewedByUser(UUID userId) {
        return repository.findRecentBooksByUser(userId);
    }

    @Async
    @Override
    @Transactional
    public void updateRecentlyViewed(UUID userId, UUID bookId) {

        BookRecentlyViewedId id = new BookRecentlyViewedId(userId, bookId);

        BookRecentlyViewed viewed = repository
                .findById(id)
                .orElse(new BookRecentlyViewed(id))
                .updateLastAccess();

        repository.save(viewed);

        List<BookRecentlyViewed> excess = repository.findExceedingRecordsByUserId(userId, recentlyViewedBooksLimit);

        if (!excess.isEmpty()) {
            repository.deleteAll(excess);
        }
    }
}
