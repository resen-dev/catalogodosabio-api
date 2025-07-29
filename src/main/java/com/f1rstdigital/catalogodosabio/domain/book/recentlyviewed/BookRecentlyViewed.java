package com.f1rstdigital.catalogodosabio.domain.book.recentlyviewed;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "books_recently_viewed")
public class BookRecentlyViewed {

    @EmbeddedId
    private BookRecentlyViewedId id;

    @Column(name = "last_access", nullable = false)
    private LocalDateTime lastAccess;

    public BookRecentlyViewed() {
    }

    public BookRecentlyViewed(BookRecentlyViewedId id) {
        this.id = id;
    }

    public BookRecentlyViewedId getId() {
        return id;
    }

    public void setId(BookRecentlyViewedId id) {
        this.id = id;
    }

    public LocalDateTime getLastAccess() {
        return lastAccess;
    }

    public void setLastAccess(LocalDateTime lastAccess) {
        this.lastAccess = lastAccess;
    }

    public BookRecentlyViewed updateLastAccess() {
        this.lastAccess = LocalDateTime.now();
        return this;
    }

    public UUID getUserId() {
        return id.getUserId();
    }

    public UUID getBookId() {
        return id.getBookId();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BookRecentlyViewed that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
