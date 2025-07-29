package com.f1rstdigital.catalogodosabio.service;

import com.f1rstdigital.catalogodosabio.dto.book.SimpleBookDataResponseDto;

import java.util.List;
import java.util.UUID;

public interface BookRecentlyViewedService {

    List<SimpleBookDataResponseDto> getRecentlyViewedByUser(UUID userId);

    void updateRecentlyViewed(UUID userId, UUID bookId);
}
